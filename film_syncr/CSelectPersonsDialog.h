#ifndef CSELECTPERSONSDIALOG_H
#define CSELECTPERSONSDIALOG_H

#include <map>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>

#include "CPerson.h"

class CSelectPersonsDialog
{
protected:
    CvPoint p1, p2;
    cv::Mat frameData, tmpImage;
    int currentPersonId, camIdx;
    std::map<int, CPerson*> *mapPersons;
    bool drawingRect;
public:
    CSelectPersonsDialog();

    void getAreas(std::map<int, CPerson*> *_mapPersons, int _camIdx, std::string _filename, int _timeOffset = 0);
    friend void onMouse( int event, int x, int y, int, void* param );
    void addPersonRect(int _personId, CImageRegion _region);
};

#endif // CSELECTPERSONSDIALOG_H
