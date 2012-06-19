#include "CSIRFilterPt.h"

#include <algorithm>
#include <opencv2/highgui/highgui.hpp>

using namespace std;
using namespace cv;

#define DEBUG

CSIRFilterPt::CSIRFilterPt(cv::Point2i pInitPt, cv::Mat &pInitImg, cv::Point2i pAreaSize)
  : mArea(pAreaSize)
  , mCurrentPosition(pInitPt)
  , mCurrentSimilarity(0)
{
  int shiftX = pAreaSize.x/2,
      shiftY = pAreaSize.y/2;

  mRefData = pInitImg(Rect(pInitPt.x-shiftX, pInitPt.y-shiftY, shiftX, shiftY));
  #ifdef DEBUG
    imshow("ctrwnd - Referent data", mRefData);
    waitKey();
  #endif

  initRng();
  mDistX = 50.0f;
  mDistY = 50.0f;
  mNumOfParticles = 1000;

  unsigned int initPts[] = {0};
  mVecPts.push_back(pInitPt);
  generatePoints( initPts, 1, mNumOfParticles );
}

cv::Rect CSIRFilterPt::pt2rect(cv::Point2i pPt, cv::Point2i pAreaSize){
  int shiftX = pAreaSize.x/2,
      shiftY = pAreaSize.y/2;

  return Rect(pPt.x-shiftX, pPt.y-shiftY, shiftX, shiftY);
}

void CSIRFilterPt::getSegment(cv::Mat& pImgData, cv::Rect pRegion, cv::Mat& pSegment){
  // TODO: edge avoidance here
  pSegment = pImgData(pRegion);
}

CSIRFilterPt::~CSIRFilterPt(){
  gsl_rng_free(mRngR);
}

void CSIRFilterPt::initRng(){
  const gsl_rng_type * T;
  gsl_rng_env_setup();

  T = gsl_rng_default;
  mRngR = gsl_rng_alloc (T);
}

void CSIRFilterPt::generatePoints( unsigned int *pProbs, unsigned int pNumOfPoints, unsigned int pNumOfPtsToGenerate ){
  vector<Point2i> vecNewPts;

  int ptIdx = 0;
  for(int i = 0; i < pNumOfPtsToGenerate; ++i){
    ptIdx = (int) (pNumOfPoints-1)*gsl_rng_uniform(mRngR);
    float stepX = (gsl_rng_uniform(mRngR) - 0.5)*mDistX,
          stepY = (gsl_rng_uniform(mRngR) - 0.5)*mDistY;
    Point2i startPt = mVecPts[ptIdx];
    int newX = startPt.x+stepX,
        newY = startPt.y+stepY;
    vecNewPts.push_back(Point2i(newX, newY));
  }
  // replace the points
  mVecPts.clear();
  copy( vecNewPts.begin(), vecNewPts.end(), back_inserter(mVecPts) );
}

void CSIRFilterPt::predictNextPos(cv::Mat &pImgData){
  // evalutate current set of points
  float* vecScores = new float[mVecPts.size()];
  const unsigned int PROB_SIZE = 10000;
  unsigned int* vecProbabs = new unsigned int[PROB_SIZE];
  vector<Point2i>::iterator iter;
  unsigned int idx = 0,
               bestIdx = -1;
  float totalScore = 0,
        bestScore = -1;
  mCurrentSimilarity = 0;
  for( iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
    vecScores[idx] = calcScore(pImgData, *iter);
    totalScore += vecScores[idx];
    if( vecScores[idx] > bestScore ){
      bestScore = vecScores[idx];
      bestIdx = idx;
    }
    ++idx;
  }
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
  getSegment(pImgData, pt2rect(pt, mArea), segment);
   // todo: if the segment is not the same size as the original image
  Mat diff;
  cv::absdiff(segment, mRefData, diff);
  Mat::MSize matSize = mRefData.size;
  float maxDiff = matSize[0]*matSize[1]*3*255;
  Scalar scalDiff = sum(diff);
  float curDiff = scalDiff[0]+scalDiff[1]+scalDiff[2];
  return 1.0f - curDiff/maxDiff;
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
  for( iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){
    circle(pImg, *iter, 2, ptClr);
  }
  if( pDisplay ){
    imshow("Ctrl window", pImg);
    waitKey();
  }
}

void CSIRFilterPt::showAllPointsAreas(Mat& pImg){

}
