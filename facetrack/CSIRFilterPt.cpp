#include "CSIRFilterPt.h"

#include <algorithm>

using namespace std;
using namespace cv;

CSIRFilterPt::CSIRFilterPt(cv::Point2i pInitPt, cv::Mat &pInitImg, cv::Point2i pAreaSize)
  : mArea(pAreaSize)
{
  int shiftX = pAreaSize.x/2,
      shiftY = pAreaSize.y/2;

  mRefData = pInitImg(Rect(pInitPt.x-shiftX, pInitPt.y-shiftY, shiftX, shiftY));
  initRng();

  mDistX = 10.0f;
  mDistY = 10.0f;

  unsigned int initPts[] = {0};
  mVecPts.push_back(pInitPt);
  generatePoints( initPts, 1, 1000 );
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
  int* vecProb = new int[mVecPts.size()];
  vector<Point2i>::iterator iter;
  for( iter = mVecPts.begin(); iter != mVecPts.end(); ++iter ){

  }
  // fill probability array
  // random draw of the points from the array -> generate new points, fill new points vector
  // cleanup
    delete [] vecProb;
}

cv::Point2i CSIRFilterPt::getPosition(){

}

float CSIRFilterPt::getSimilarity(){

}
