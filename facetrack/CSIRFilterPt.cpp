#include "CSIRFilterPt.h"

#include <algorithm>
#include <opencv2/highgui/highgui.hpp>
#include <gsl/gsl_randist.h>
#include <opencv2/imgproc/imgproc.hpp>
#include <algorithm>

#include "CUtils.h"

using namespace std;
using namespace cv;

//#define _DEBUG

#ifdef _DEBUG
#include <iostream>
#endif

gsl_rng* CSIRFilterPt::sRngR = NULL;

CSIRFilterPt::CSIRFilterPt(cv::Point2i pInitPt, cv::Mat &pInitImg, cv::Point2i pAreaSize)
  : mArea(pAreaSize)
  , mCurrentPosition(pInitPt)
  , mCurrentSimilarity(0.9) // need to set it because of the gauss variance which is connected
{
  mRefData = pInitImg(pt2rect(pInitPt, pAreaSize));
  CUtils::genGrayscaleHist(mRefData, mRefHist);
  CUtils::normHistBasedOnPatchSize(mRefHist, mRefData);

  #ifdef _DEBUG
    imshow("ctrwnd - Referent data", mRefData);
    waitKey();
  #endif

  initRng();
  mDistX = 50.0f;
  mDistY = 50.0f;
  mNumOfParticles = 10000;

  unsigned int initPts[] = {0};
  mVecPts.push_back(pInitPt);
  generatePoints( initPts, 1, mNumOfParticles );
}

cv::Rect CSIRFilterPt::pt2rect(cv::Point2i pPt, cv::Point2i pAreaSize){
  int shiftX = pAreaSize.x/2,
      shiftY = pAreaSize.y/2;

  return Rect(pPt.x-shiftX, pPt.y-shiftY, pAreaSize.x, pAreaSize.y);
}

void CSIRFilterPt::getSegment(cv::Mat& pImgData, cv::Rect pRegion, cv::Mat& pSegment){
//  #ifdef _DEBUG
//  Rect origRect = pRegion;
//  #endif

  pRegion.x = min(max(pRegion.x,0), pImgData.cols-1);
  pRegion.y = min(max(pRegion.y,0), pImgData.rows-1);
  pRegion.width = max(min(pRegion.width, pImgData.cols - pRegion.x),1);
  pRegion.height = max(min(pRegion.height, pImgData.rows - pRegion.y),1);
//  #ifdef _DEBUG
//  if( origRect.x != pRegion.x || origRect.y != pRegion.y ||
//      origRect.width != pRegion.width || origRect.height != pRegion.height )
//  {
//    cout << "Original region " << origRect.x << ", " << origRect.y << ", " << origRect.width << ", " << origRect.height << endl;
//    cout << "Region size changed: " << pRegion.x << ", " << pRegion.y << ", " << pRegion.width << ", " << pRegion.height << endl;
//  }
//  #endif

  pSegment = pImgData(pRegion);
}

CSIRFilterPt::~CSIRFilterPt(){
}

void CSIRFilterPt::initRng(){
  if( sRngR != NULL )
    return;

  const gsl_rng_type * T;
  gsl_rng_env_setup();

  T = gsl_rng_default;
  sRngR = gsl_rng_alloc (T);
}

void CSIRFilterPt::deinitRng(){
  gsl_rng_free(sRngR);
}

void CSIRFilterPt::generatePoints( unsigned int *pProbs, unsigned int pNumOfPoints, unsigned int pNumOfPtsToGenerate ){
  vector<Point2i> vecNewPts;

//#ifdef _DEBUG
//  cout << "Original point array" << endl;
//  for( vector<Point2i>::iterator iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
//    cout << "[" << iter->x << ", " << iter->y << "] ";
//  }
//  cout << endl;
//#endif

  int ptIdx = 0;
  for(int i = 0; i < pNumOfPtsToGenerate; ++i){
    ptIdx = (int) (pNumOfPoints-1)*gsl_rng_uniform(sRngR);
    float stepX = gsl_ran_gaussian(sRngR, 1-mCurrentSimilarity)*mDistX,
          stepY = gsl_ran_gaussian(sRngR, 1-mCurrentSimilarity)*mDistY;
    unsigned int ptNum = pProbs[ptIdx];
//#ifdef _DEBUG
//    cout << "using point: " << ptNum << endl;
//#endif
    Point2i startPt = mVecPts[ptNum];
    int newX = startPt.x+stepX,
        newY = startPt.y+stepY;
    vecNewPts.push_back(Point2i(newX, newY));
  }
  // replace the points
  mVecPts.clear();
  copy( vecNewPts.begin(), vecNewPts.end(), back_inserter(mVecPts) );
//#ifdef _DEBUG
//  for( vector<Point2i>::iterator iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
//    cout << "[" << iter->x << ", " << iter->y << "] ";
//  }
//  cout << "Check point array" << endl;
//#endif
 }

void CSIRFilterPt::predictNextPos(cv::Mat &pImgData){
  // evalutate current set of points
  float* vecScores = new float[mVecPts.size()];
  const unsigned int PROB_SIZE = 10000;
  unsigned int* vecProbabs = new unsigned int[PROB_SIZE];
  // init
  memset(vecProbabs, 0, sizeof(unsigned int)*PROB_SIZE);
  vector<Point2i>::iterator iter;
  unsigned int idx = 0,
               bestIdx = -1;
  float totalScore = 0,
        bestScore = -1;
  mCurrentSimilarity = 0;
  for( iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
    vecScores[idx] = calcScore(pImgData, *iter);
    #ifdef _DEBUG
      cout << "Point #" << idx << " score: " << vecScores[idx] << endl;
    #endif
    totalScore += vecScores[idx];
    if( vecScores[idx] > bestScore ){
      bestScore = vecScores[idx];
      bestIdx = idx;
    }
    ++idx;
  }
#ifdef _DEBUG
  cout << "Best score: " << bestScore << endl;
#endif

  // set the best point
  mCurrentPosition = mVecPts[bestIdx];
  mCurrentSimilarity = bestScore;
  // normalize scores
  unsigned int fillIndex = 0;
  for( idx = 0; idx < mVecPts.size(); ++idx ){
    if(fillIndex>=mVecPts.size())
      break; // to account for the possibility of rounding-up too much
    vecScores[idx] /= totalScore;
    unsigned int endIdx = fillIndex + round(vecScores[idx]*((float)PROB_SIZE));
    for( fillIndex; fillIndex < endIdx; ++fillIndex )
      vecProbabs[fillIndex] = idx;
  }
  // random draw of the points from the array -> generate new points, fill new points vector
  generatePoints(vecProbabs, PROB_SIZE, mNumOfParticles);
  // cleanup
  delete [] vecScores;
  delete [] vecProbabs;
}

/**
    returns Probability of similarity.
  */
float CSIRFilterPt::calcScore(Mat& pImgData, Point2i pt){
  Mat segment;
  MatND histNewPart;
  getSegment(pImgData, pt2rect(pt, mArea), segment);

  CUtils::genGrayscaleHist(segment, histNewPart);
  CUtils::normHistBasedOnPatchSize(histNewPart, segment);

  return histSimilarity4(mRefHist, histNewPart);
}

float CSIRFilterPt::histSimilarity(MatND& hist1, MatND& hist2){
//  Score same: 1
//  Score diff: 0.948292
//  Worst diff: 0.692497

  float histScore = compareHist(hist1, hist2, CV_COMP_CORREL);
  return std::max(0.0f, histScore);
}

float CSIRFilterPt::histSimilarity2(MatND& hist1, MatND& hist2){
//  Score same: 1
//  Score diff: 0.989226
//  Worst diff: 0.833977

  float histScore = compareHist(hist1, hist2, CV_COMP_CHISQR);
  // sum of hists
  Scalar s1 = sum(hist1),
         s2 = sum(hist2);
  Scalar totalSum = s1+s2;
  return (1.0f-histScore/(totalSum[0]+totalSum[1]+totalSum[2]));
}

float CSIRFilterPt::histSimilarity4(MatND& hist1, MatND& hist2){
//  Score same:
//  Score diff:
//  Worst diff:

  float histScore = compareHist(hist1, hist2, CV_COMP_CHISQR);
  // sum of hists
  Scalar s1 = sum(hist1),
         s2 = sum(hist2);
  Scalar totalSum = s1+s2;
  return pow((1.0f-histScore/(totalSum[0]+totalSum[1]+totalSum[2])),6);
}

float CSIRFilterPt::histSimilarity3(MatND& hist1, MatND& hist2){
//  Score same: 1
//  Score diff: 0.925835
//  Worst diff: 0.676101

  float histScore = compareHist(hist1, hist2, CV_COMP_BHATTACHARYYA);
  return max(0.0f, 1.0f-histScore);
}


Point2i CSIRFilterPt::getPosition(){
  return mCurrentPosition;
}

float CSIRFilterPt::getSimilarity(){
  return mCurrentSimilarity;
}

void CSIRFilterPt::showAllPoints(Mat& pImg, bool pDisplay){
  vector<Point2i>::iterator iter;
  Scalar ptClr(255, 0, 0, 255);
  Scalar ptMainClr(0, 0, 255, 255);
  for( iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
    circle(pImg, *iter, 2, ptClr);
  }
  circle(pImg, mCurrentPosition, 4, ptMainClr);
  if( pDisplay ){
    imshow("Ctrl window", pImg);
    waitKey();
  }
}

void CSIRFilterPt::showFewPoints(Mat& pImg, bool pDisplay){
  vector<Point2i>::iterator iter;
  Scalar ptClr(255, 0, 0, 255);
  Scalar ptMainClr(0, 0, 255, 255);
  for( iter = mVecPts.begin(); iter != mVecPts.end(); iter += 100 ){
    circle(pImg, *iter, 2, ptClr);
  }
  circle(pImg, mCurrentPosition, 4, ptMainClr);
  if( pDisplay ){
    imshow("Ctrl window", pImg);
    waitKey();
  }
}

void CSIRFilterPt::showAllPointsAreas(Mat& pImg){

}
