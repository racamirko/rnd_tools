#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <string>

using namespace cv;
using namespace std;

int main(int argc, char *argv[])
{
  // usage format: exec camIdx sleepIntervalInSeconds outputFolder
  VideoCapture cap;
  string outFolder;
  int sleepPeriod = 2000, frameCnt = 0;
  char outFilenameBuf[1000];

  if( argc == 1 || (argc == 2 && strlen(argv[1]) == 1 && isdigit(argv[1][0])))
      cap.open(argc == 2 ? argv[1][0] - '0' : 0);
  else if( argc >= 2 )
      cap.open(atoi(argv[1]));
  if( argc >=3  )
    sleepPeriod = atoi(argv[2]); // seconds to miliseconds

  if( argc >=4 )
    outFolder = string(argv[3]);

  if( !cap.isOpened() )
  {
      cout << "Could not initialize capturing...\n";
      return 0;
  }

  Mat gray, prevGray, image;

  for(;;)
  {
      Mat frame;
      cap >> frame;
      if( frame.empty() ){
          sleep(sleepPeriod);
          continue;
      }

      flip(frame, image, 1);
//        frame.copyTo(image);
      cvtColor(image, gray, CV_BGR2GRAY);
//      imshow("camera",image);
      sprintf(outFilenameBuf, "%s/frame%05d.png", outFolder.c_str(), frameCnt);
      imwrite(string(outFilenameBuf), image);
      ++frameCnt;
      sleep(sleepPeriod);
  }

  return 0;
}

