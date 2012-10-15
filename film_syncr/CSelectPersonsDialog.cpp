#include "CSelectPersonsDialog.h"

#include <stdio.h>
#include "globalInclude.h"

using namespace std;
using namespace cv;

void onMouse( int event, int x, int y, int, void* param );

CSelectPersonsDialog::CSelectPersonsDialog()
    : drawingRect(false)
    , p1(cvPoint(0,0))
    , p2(cvPoint(0,0))
{

}

void CSelectPersonsDialog::getAreas(std::map<int, CPerson*> *_mapPersons, int _camIdx, std::string _filename, int _timeOffset){
    char buffer[50];
    // prepare image
    CvCapture* videoSrc = cvCaptureFromFile(_filename.c_str());
    cvSetCaptureProperty(videoSrc, CV_CAP_PROP_POS_MSEC, double(_timeOffset) );
    IplImage* tmpImg = cvQueryFrame(videoSrc);
    mapPersons = _mapPersons;
    frameData = Mat(tmpImg);
    currentPersonId = 0;
    camIdx = _camIdx;

    // prepare output information
    sprintf(buffer, "Current person id: %d", currentPersonId);
    putText(frameData, string(buffer), cvPoint(30,12), FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(0,0,255));

    // display pre-existing regions of persons
    for( std::map<int, CPerson*>::iterator iter = mapPersons->begin();
         iter != mapPersons->end(); ++iter )
    {
        CPerson* pers = iter->second;
        for( std::map<int, CImageRegion>::iterator iterInner = pers->beginRegions();
             iterInner != pers->endRegions(); ++iterInner )
        {
            if( iterInner->first != camIdx )
                continue;
            CImageRegion reg = iterInner->second;
            rectangle(frameData, Rect(reg.x1, reg.y1, reg.x2-reg.x1, reg.y2-reg.y1), cvScalar(255,0,0) );
            sprintf(buffer, "%d", pers->getId());
            putText(frameData, string(buffer), Point(reg.x1, reg.y1), FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(255,0,0));
        }
    }

    tmpImage = frameData.clone();
    drawingRect = false;

    cvNamedWindow("testWnd");
    setMouseCallback( "testWnd", onMouse, this );
    imshow("testWnd", tmpImage);
}

void CSelectPersonsDialog::addPersonRect(int _personId, CImageRegion _region){
    if( mapPersons->find(_personId) == mapPersons->end() ){
        mapPersons->insert(std::pair<int, CPerson*>(_personId, new CPerson(_personId)));
    }
    CPerson* curPerson = mapPersons->operator [](_personId);
    curPerson->setCameraRegion(camIdx, _region);
}

void onMouse( int event, int x, int y, int flags, void* param ){
    char buffer[50];
    CSelectPersonsDialog* d = static_cast<CSelectPersonsDialog*>(param);
    switch(event){
        case CV_EVENT_MOUSEMOVE:
            d->p2 = cvPoint(x,y);
            if(d->drawingRect){
                d->tmpImage.release();
                d->tmpImage = d->frameData.clone();
                rectangle(d->tmpImage, Rect(d->p1, d->p2), cvScalar(255,0,0) );
                imshow("testWnd", d->tmpImage);
            }
            break;
        case CV_EVENT_LBUTTONDOWN:
            DLOG(INFO) << "Mouse lbutton down";
            d->p1 = cvPoint(x,y);
            d->drawingRect = true;
            break;
        case CV_EVENT_LBUTTONUP:
            DLOG(INFO) << "Mouse lbutton up";
            d->frameData.release();
            sprintf(buffer, "%d", d->currentPersonId);
            putText(d->tmpImage, string(buffer), d->p1, FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(255,0,0));
            // save information
            d->addPersonRect(d->currentPersonId++,CImageRegion(d->p1.x, d->p1.y, d->p2.x, d->p2.y));
            // update video display
            d->drawingRect = false;
            sprintf(buffer, "Current person id: %d", d->currentPersonId);
            rectangle(d->tmpImage, Rect(30,0,200,15), cvScalar(0), CV_FILLED);
            putText(d->tmpImage, string(buffer), cvPoint(30,12), FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(0,0,255));
            d->frameData = d->tmpImage.clone();

            imshow("testWnd", d->tmpImage);
            break;
        case CV_EVENT_RBUTTONDOWN:
            {
                if( flags & CV_EVENT_FLAG_SHIFTKEY )
                    d->currentPersonId--;
                else
                    d->currentPersonId++;
                sprintf(buffer, "Current person id: %d", d->currentPersonId);
                rectangle(d->frameData, Rect(30,0,200,15), cvScalar(0), CV_FILLED);
                putText(d->frameData, string(buffer), cvPoint(30,12), FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(0,0,255));

                d->tmpImage = d->frameData.clone();
                imshow("testWnd", d->tmpImage);
            }

    }
}
