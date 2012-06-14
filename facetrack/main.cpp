#include "opencv2/video/tracking.hpp"
#include "opencv2/highgui/highgui.hpp"

#include <stdio.h>

using namespace cv;

static inline Point calcPoint(Point2f center, double R, double angle)
{
    return center + Point2f((float)cos(angle), (float)-sin(angle))*(float)R;
}

void help()
{
  printf( "\nExamle of c calls to OpenCV's Kalman filter.\n"
"   Tracking of rotating point.\n"
"   Rotation speed is constant.\n"
"   Both state and measurements vectors are 1D (a point angle),\n"
"   Measurement is the real point angle + gaussian noise.\n"
"   The real and the estimated points are connected with yellow line segment,\n"
"   the real and the measured points are connected with red line segment.\n"
"   (if Kalman filter works correctly,\n"
"    the yellow segment should be shorter than the red one).\n"
      "\n"
"   Pressing any key (except ESC) will reset the tracking with a different speed.\n"
"   Pressing ESC will stop the program.\n"
      );
}

int main(int, char**)
{
    help();
    Mat img(500, 500, CV_8UC3);
    KalmanFilter KF(2, 1, 0);
    Mat state(2, 1, CV_32F); /* (phi, delta_phi) */
    Mat processNoise(2, 1, CV_32F);
    Mat measurement = Mat::zeros(1, 1, CV_32F);
    char code = (char)-1;

    for(;;)
    {
        randn( state, Scalar::all(0), Scalar::all(0.1) );
        KF.transitionMatrix = *(Mat_<float>(2, 2) << 1, 1, 0, 1);

        setIdentity(KF.measurementMatrix);
        setIdentity(KF.processNoiseCov, Scalar::all(1e-5));
        setIdentity(KF.measurementNoiseCov, Scalar::all(1e-1));
        setIdentity(KF.errorCovPost, Scalar::all(1));

        randn(KF.statePost, Scalar::all(0), Scalar::all(0.1));

        for(;;)
        {
            Point2f center(img.cols*0.5f, img.rows*0.5f);
            float R = img.cols/3.f;
            double stateAngle = state.at<float>(0);
            Point statePt = calcPoint(center, R, stateAngle);

            Mat prediction = KF.predict();
            double predictAngle = prediction.at<float>(0);
            Point predictPt = calcPoint(center, R, predictAngle);

            randn( measurement, Scalar::all(0), Scalar::all(KF.measurementNoiseCov.at<float>(0)));

            // generate measurement
            measurement += KF.measurementMatrix*state;

            double measAngle = measurement.at<float>(0);
            Point measPt = calcPoint(center, R, measAngle);

            // plot points
            #define drawCross( center, color, d )                                 \
                line( img, Point( center.x - d, center.y - d ),                \
                             Point( center.x + d, center.y + d ), color, 1, CV_AA, 0); \
                line( img, Point( center.x + d, center.y - d ),                \
                             Point( center.x - d, center.y + d ), color, 1, CV_AA, 0 )

            img = Scalar::all(0);
            drawCross( statePt, Scalar(255,255,255), 3 );
            drawCross( measPt, Scalar(0,0,255), 3 );
            drawCross( predictPt, Scalar(0,255,0), 3 );
            line( img, statePt, measPt, Scalar(0,0,255), 3, CV_AA, 0 );
            line( img, statePt, predictPt, Scalar(0,255,255), 3, CV_AA, 0 );

            KF.correct(measurement);

            randn( processNoise, Scalar(0), Scalar::all(sqrt(KF.processNoiseCov.at<float>(0, 0))));
            state = KF.transitionMatrix*state + processNoise; // this is where they update the world state

            imshow( "Kalman", img );
            code = (char)waitKey(100);

            if( code > 0 )
                break;
        }
        if( code == 27 || code == 'q' || code == 'Q' )
            break;
    }

    return 0;
}


void initStartCoord( vector<Point2i> pPersonPoints ){
  pPersonPoints.clear();
  pPersonPoints.push_back(Point2i(215, 788));
  pPersonPoints.push_back(Point2i(232, 729));
  pPersonPoints.push_back(Point2i(334, 727));
  pPersonPoints.push_back(Point2i(425, 805));
  pPersonPoints.push_back(Point2i(417, 719));
  pPersonPoints.push_back(Point2i(512, 723));
  pPersonPoints.push_back(Point2i(548, 690));
  pPersonPoints.push_back(Point2i(582, 732));
  pPersonPoints.push_back(Point2i(633, 693));
  pPersonPoints.push_back(Point2i(682, 685));
  pPersonPoints.push_back(Point2i(736, 633));
  pPersonPoints.push_back(Point2i(791, 652));
  pPersonPoints.push_back(Point2i(766, 710));
  pPersonPoints.push_back(Point2i(935, 762));
  pPersonPoints.push_back(Point2i(981, 677));
  pPersonPoints.push_back(Point2i(988, 590));
  pPersonPoints.push_back(Point2i(1029, 568));
  pPersonPoints.push_back(Point2i(1103, 556));
  pPersonPoints.push_back(Point2i(1166, 548));
  pPersonPoints.push_back(Point2i(1158, 610));
  pPersonPoints.push_back(Point2i(1276, 518));
  pPersonPoints.push_back(Point2i(1310, 497));
  pPersonPoints.push_back(Point2i(1542, 566));
  pPersonPoints.push_back(Point2i(1509, 519));
  pPersonPoints.push_back(Point2i(1521, 471));
  pPersonPoints.push_back(Point2i(1628, 452));
  pPersonPoints.push_back(Point2i(1785, 551));
  pPersonPoints.push_back(Point2i(1867, 426));
}
