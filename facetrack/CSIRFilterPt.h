#ifndef CSIRFILTERPT_H
#define CSIRFILTERPT_H

#include <vector>
#include <opencv2/core/core.hpp>
#include <gsl/gsl_rng.h>

class CSIRFilterPt
{
protected:
  cv::Point2i mArea;
  cv::Mat mRefData;
  std::vector<cv::Point2i> mVecPts;
  gsl_rng* mRngR;
  float mDistX, mDistY;

  void generatePoints( unsigned int* pProbs, unsigned int pNumOfPoints, unsigned int pNumOfPtsToGenerate );
  void initRng();
public:
    CSIRFilterPt(cv::Point2i pInitPt, cv::Mat &pInitImg, cv::Point2i pAreaSize);
    ~CSIRFilterPt();

    void predictNextPos(cv::Mat &pImgData);
    cv::Point2i getPosition();
    float getSimilarity();
};

#endif // CSIRFILTERPT_H
