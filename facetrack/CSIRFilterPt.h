#ifndef CSIRFILTERPT_H
#define CSIRFILTERPT_H

#include <vector>
#include <opencv2/core/core.hpp>
#include <gsl/gsl_rng.h>

class CSIRFilterPt
{
protected:
  cv::Point2i mArea,
              mCurrentPosition;
  cv::Mat mRefData;
  cv::MatND mRefHist;
  std::vector<cv::Point2i> mVecPts;
  gsl_rng* mRngR;
  float mDistX, mDistY, mCurrentSimilarity;
  unsigned int mNumOfParticles;

  void generatePoints( unsigned int* pProbs, unsigned int pNumOfPoints, unsigned int pNumOfPtsToGenerate );
  float calcScore(cv::Mat& pImgData, cv::Point2i pt);
  cv::Rect pt2rect(cv::Point2i pPt, cv::Point2i pAreaSize);
  void getSegment(cv::Mat& pImgData, cv::Rect pRegion, cv::Mat& pSegment);
  void initRng();

public:
  CSIRFilterPt(cv::Point2i pInitPt, cv::Mat &pInitImg, cv::Point2i pAreaSize);
  ~CSIRFilterPt();

  void predictNextPos(cv::Mat &pImgData);
  cv::Point2i getPosition();
  float getSimilarity();

  void showAllPoints(cv::Mat& pImg, bool pDisplay = true);
  void showAllPointsAreas(cv::Mat& pImg);
  void showFewPoints(cv::Mat& pImg, bool pDisplay = true);

  // while testing
  static float histSimilarity(cv::MatND& hist1, cv::MatND& hist2);
  static float histSimilarity2(cv::MatND& hist1, cv::MatND& hist2);
  static float histSimilarity3(cv::MatND& hist1, cv::MatND& hist2);
  static float histSimilarity4(cv::MatND& hist1, cv::MatND& hist2);
};

#endif // CSIRFILTERPT_H
