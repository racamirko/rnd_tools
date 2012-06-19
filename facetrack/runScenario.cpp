#include "runScenario.h"

#include "CSIRFilterPt.h"
#include "initData.h"
#include <CFileUtils.h>
#include <opencv2/highgui/highgui.hpp>

#include <iostream>


using namespace std;
using namespace cv;

void run_sirFilter(){
  vector<Point2i> vecInitPts;
  vector<String> vecFilenames;
  initStartCoord(vecInitPts);
  Mat nextImg;

  CFileUtils::getDirFiles("/cvlabdata1/home/raca/data/classroom/canon_digital/imgs", vecFilenames, true );

  Mat firstImg = imread(vecFilenames[301]);
  CSIRFilterPt testPt(vecInitPts[0], firstImg, Point2i(100,100));
  testPt.showAllPoints(firstImg);
  int cntr = 0;
  for(vector<String>::iterator iter = vecFilenames.begin();
      iter != vecFilenames.end(); ++iter )
  {
    ++cntr;
    if(cntr < 302)
      continue;
    nextImg = imread(*iter);
    testPt.predictNextPos(nextImg);
    testPt.showAllPoints(nextImg);
  }
}

void run_testOpenCVSubtract(){
  Mat m1 = Mat(2,2,CV_32FC3);
  m1.at<Vec3f>(0,0)[0] = 1.0;
  m1.at<Vec3f>(0,0)[1] = 1.0;
  m1.at<Vec3f>(0,0)[2] = 1.0;
  m1.at<Vec3f>(0,1)[0] = 2.0;
  m1.at<Vec3f>(0,1)[1] = 2.0;
  m1.at<Vec3f>(0,1)[2] = 2.0;
  m1.at<Vec3f>(1,0)[0] = 3.0;
  m1.at<Vec3f>(1,0)[1] = 3.0;
  m1.at<Vec3f>(1,0)[2] = 3.0;
  m1.at<Vec3f>(1,1)[0] = 4.0;
  m1.at<Vec3f>(1,1)[1] = 4.0;
  m1.at<Vec3f>(1,1)[2] = 4.0;

  //
  Mat m2 = Mat(2,2,CV_32FC3);
  m2.at<Vec3f>(0,0)[0] = 1.0;
  m2.at<Vec3f>(0,0)[1] = 1.0;
  m2.at<Vec3f>(0,0)[2] = 2.0;
  m2.at<Vec3f>(0,1)[0] = 2.0;
  m2.at<Vec3f>(0,1)[1] = 2.0;
  m2.at<Vec3f>(0,1)[2] = 2.0;
  m2.at<Vec3f>(1,0)[0] = 3.0;
  m2.at<Vec3f>(1,0)[1] = 3.0;
  m2.at<Vec3f>(1,0)[2] = 3.0;
  m2.at<Vec3f>(1,1)[0] = 4.0;
  m2.at<Vec3f>(1,1)[1] = 4.0;
  m2.at<Vec3f>(1,1)[2] = 4.0;

  Mat m3 = m1-m2;
  Mat m4;
  absdiff(m1, m2, m4);
  cout << "First matrix: " << endl;
  cout << "| " << m1.at<float>(0,0) << ", " << m1.at<float>(0,1) << " |" << endl;
  cout << "| " << m1.at<float>(1,0) << ", " << m1.at<float>(1,1) << " |" << endl;
  cout << endl;
  cout << "Second matrix: " << endl;
  cout << "| " << m2.at<float>(0,0) << ", " << m2.at<float>(0,1) << " |" << endl;
  cout << "| " << m2.at<float>(1,0) << ", " << m2.at<float>(1,1) << " |" << endl;
  cout << endl;
  cout << "End matrix: " << endl;
  cout << "| " << m3.at<float>(0,0) << ", " << m3.at<float>(0,1) << " |" << endl;
  cout << "| " << m3.at<float>(1,0) << ", " << m3.at<float>(1,1) << " |" << endl;
  cout << endl;
  cout << "m4 matrix: " << endl;
  cout << "| " << m4.at<float>(0,0) << ", " << m4.at<float>(0,1) << " |" << endl;
  cout << "| " << m4.at<float>(1,0) << ", " << m4.at<float>(1,1) << " |" << endl;

  Scalar suma = sum(m3);
  cout << "m3 sum: " << suma[0] << ", " << suma[1] << ", " << suma[2] << ", " << suma[3] << endl;


  suma = sum(m4);
  cout << "m4 sum: " << suma[0] << ", " << suma[1] << ", " << suma[2] << ", " << suma[3] << endl;
}
