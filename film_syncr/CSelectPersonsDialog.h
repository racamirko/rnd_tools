#ifndef CSELECTPERSONSDIALOG_H
#define CSELECTPERSONSDIALOG_H

#include <map>
#include <string>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <QtCore>

#include "CPerson.h"

enum enumDialogMode { DM_BODY_REGIONS, DM_SELECT_HEAD };

class CSelectPersonsDialog
{
protected:
    enumDialogMode mode;
    CvPoint p1, p2;
    cv::Mat frameData, tmpImage;
    int camIdx;
    int* currentPersonId;
    std::map<int, CPerson*> *mapPersons;
    bool drawingRect;

    void drawPersons();
    void addPersonRect(int _personId, CImageRegion _region);
    void addPeronsHead(int _personId, cv::Point _point);
public:
    CSelectPersonsDialog(int* _personIndex);

    void getAreas(std::map<int, CPerson*> *_mapPersons, int _camIdx, std::string _filename, int _timeOffset);
    // opencv event handlers
    friend void onMouse( int event, int x, int y, int, void* param );
};

#endif // CSELECTPERSONSDIALOG_H
