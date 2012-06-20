#include <opencv2/video/tracking.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

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

int main_func_kal(int argc, char** argv){
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

/**
 * OpenCV example for generating histograms
 * taken from
 *        http://opencv.willowgarage.com/documentation/cpp/imgproc_histograms.html?highlight=histogram
 */
int main_func_hist( int argc, char** argv )
{
    Mat src, hsv;
    if( argc != 2 || !(src=imread(argv[1], 1)).data )
        return -1;

    cvtColor(src, hsv, CV_BGR2HSV);

    // let's quantize the hue to 30 levels
    // and the saturation to 32 levels
    int hbins = 30, sbins = 32;
    int histSize[] = {hbins, sbins};
    // hue varies from 0 to 179, see cvtColor
    float hranges[] = { 0, 180 };
    // saturation varies from 0 (black-gray-white) to
    // 255 (pure spectrum color)
    float sranges[] = { 0, 256 };
    const float* ranges[] = { hranges, sranges };
    MatND hist;
    // we compute the histogram from the 0-th and 1-st channels
    int channels[] = {0, 1};

    calcHist( &hsv, 1, channels, Mat(), // do not use mask
             hist, 2, histSize, ranges,
             true, // the histogram is uniform
             false );
    double maxVal=0;
    minMaxLoc(hist, 0, &maxVal, 0, 0);

    int scale = 10;
    Mat histImg = Mat::zeros(sbins*scale, hbins*10, CV_8UC3);

    for( int h = 0; h < hbins; h++ )
        for( int s = 0; s < sbins; s++ )
        {
            float binVal = hist.at<float>(h, s);
            int intensity = cvRound(binVal*255/maxVal);
            rectangle( histImg, Point(h*scale, s*scale),
                        Point( (h+1)*scale - 1, (s+1)*scale - 1),
                        Scalar::all(intensity),
                        CV_FILLED );
        }

    namedWindow( "Source", 1 );
    imshow( "Source", src );

    namedWindow( "H-S Histogram", 1 );
    imshow( "H-S Histogram", histImg );
    waitKey();
}
