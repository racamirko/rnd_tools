#include "CUtils.h"

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;

CUtils::CUtils()
{

}

void CUtils::dispHist( MatND &pHist, String wndName ){
  // 10px for each step and bin
  int stepX = 10,
      histHeight = 300;
  int bins = 32;
  Mat histImg = Mat::zeros(histHeight, bins*stepX, CV_8UC3);
  double maxVal=0;
  minMaxLoc(pHist, 0, &maxVal, 0, 0);

  for( int binIdx = 0; binIdx < bins; ++binIdx )
  {
    float binVal = pHist.at<float>(binIdx);
    int intensity = cvRound(255*binIdx/(float)bins);
    int endHeight = (1-binVal/maxVal)*histHeight;
    rectangle( histImg, Point(binIdx*stepX, endHeight),
               Point( (binIdx+1)*stepX, histHeight),
               Scalar::all(intensity),
               CV_FILLED );
    // draw contra-sided rectangle
    rectangle( histImg, Point(binIdx*stepX, 0),
               Point( (binIdx+1)*stepX, endHeight),
               Scalar::all(255-intensity),
               CV_FILLED );
  }

  imshow( wndName, histImg );
}

void CUtils::normHistBasedOnPatchSize(MatND &pHist, Mat& pPatch){
  float maxDiff = pPatch.cols*pPatch.rows*255.0f;
  pHist = pHist*(1/maxDiff);
}

void CUtils::genGrayscaleHist( Mat &pImg, MatND &pHist ){
  Mat grayImg;
  const int channels[] = {0};
  float range_chan1[] = {0, 256};
  const float* ranges[] = { range_chan1 };
  const int histBinSize[] = { 32 };
  cvtColor(pImg, grayImg, CV_BGR2GRAY);

  calcHist( &grayImg, 1, channels, Mat(), pHist, 1, histBinSize, ranges );
}
