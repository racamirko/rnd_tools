#ifndef CUTILS_H
#define CUTILS_H

#include <opencv2/core/core.hpp>
#include <string>

class CUtils
{
public:
    CUtils();

    static void dispHist(cv::MatND &pHist, std::string wndName = "wnd1");
    static void genGrayscaleHist( cv::Mat &pImg, cv::MatND &pHist );
    static void normHistBasedOnPatchSize(cv::MatND &pHist, cv::Mat& pPatch);
};

#endif // CUTILS_H
