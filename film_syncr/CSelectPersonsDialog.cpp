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

void CSelectPersonsDialog::getAreas(vector<CImageRegion>& _regionList, string _filename, int _timeOffset){
    char buffer[50];
    CvCapture* videoSrc = cvCaptureFromFile(_filename.c_str());
    cvSetCaptureProperty(videoSrc, CV_CAP_PROP_POS_MSEC, double(_timeOffset) );
    IplImage* tmpImg = cvQueryFrame(videoSrc);
    vecRegions = _regionList;
    frameData = Mat(tmpImg);
    currentPersonId = 0;

    sprintf(buffer, "Current person id: %d", currentPersonId);
    putText(frameData, string(buffer), cvPoint(30,12), FONT_HERSHEY_SIMPLEX, 0.4, cvScalar(0,0,255));

    tmpImage = frameData.clone();
    drawingRect = false;

    cvNamedWindow("testWnd");
    setMouseCallback( "testWnd", onMouse, this );
    imshow("testWnd", tmpImage);
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
            d->vecRegions.push_back(CImageRegion(d->p1.x, d->p1.y, d->p2.x, d->p2.y, d->currentPersonId++));
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
