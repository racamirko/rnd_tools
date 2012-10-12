#ifndef CSELECTPERSONSDIALOG_H
#define CSELECTPERSONSDIALOG_H

#include <vector>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>

#include "CImageRegion.h"

class CSelectPersonsDialog
{
protected:
    CvPoint p1, p2;
    cv::Mat frameData, tmpImage;
    int currentPersonId;
    std::vector<CImageRegion> vecRegions;
    bool drawingRect;
public:
    CSelectPersonsDialog();

    void getAreas(std::vector<CImageRegion>& _regionList, std::string _filename, int _timeOffset = 0);
    friend void onMouse( int event, int x, int y, int, void* param );
};

#endif // CSELECTPERSONSDIALOG_H
