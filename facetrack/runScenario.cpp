#include "runScenario.h"

#include <opencv2/highgui/highgui.hpp>
#include <iostream>

#include <CFileUtils.h>

#include "test_OpenCv_Kalman.h"
#include "CSIRFilterPt.h"
#include "initData.h"
#include "CUtils.h"

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

void run_HistTest(){
  char* argv[] = { "execname" ,"/cvlabdata1/home/raca/data/classroom/canon_digital/imgs/frame000001.png" };
  main_func_hist(2, argv);
}

void run_testHistNorm(){
  Mat image = imread("/cvlabdata1/home/raca/data/classroom/canon_digital/imgs/frame000001.png");
  MatND hist, histNorm;
  Rect roi(175,738,100,100);
  Mat testSample = image(roi);
  CUtils::genGrayscaleHist(testSample, hist);
  histNorm = hist.clone();
  CUtils::normHistBasedOnPatchSize(histNorm, testSample);
  CUtils::dispHist(hist, "orig_hist");
  CUtils::dispHist(histNorm, "new_hist");
  waitKey();
}

void run_testHistCmp(){
  Mat image = imread("/cvlabdata1/home/raca/data/classroom/canon_digital/imgs/frame000001.png");
  Rect roi(175,738,50,50),
       roi2(185,748,50,50),
       roi3(385,748,50,50);
  Mat testSample = image(roi),
      cmpSample = image(roi2),
      totallyDifferentSample = image(roi3);

  MatND hist1, hist2, hist3;
  CUtils::genGrayscaleHist(testSample, hist1);
  CUtils::normHistBasedOnPatchSize(hist1, testSample);

  CUtils::genGrayscaleHist(cmpSample, hist2);
  CUtils::normHistBasedOnPatchSize(hist2, cmpSample);

  CUtils::genGrayscaleHist(totallyDifferentSample, hist3);
  CUtils::normHistBasedOnPatchSize(hist3, totallyDifferentSample);
  //  display
  CUtils::dispHist(hist1, "origHist");
  CUtils::dispHist(hist2, "shiftedHist");
  CUtils::dispHist(hist3, "differentHist");

  float scoreSame = CSIRFilterPt::histSimilarity3(hist1, hist1),
        scoreDiff = CSIRFilterPt::histSimilarity3(hist1, hist2),
        worstCaseDiff = CSIRFilterPt::histSimilarity3(hist1, hist3);

  cout << "Score same: " << scoreSame << endl;
  cout << "Score diff: " << scoreDiff << endl;
  cout << "Worst diff: " << worstCaseDiff << endl;

  waitKey();
}

void run_testGrayscaleHist(){
  Mat image = imread("/cvlabdata1/home/raca/data/classroom/canon_digital/imgs/frame000001.png");
  MatND hist;
  CUtils::genGrayscaleHist(image, hist);
  CUtils::dispHist(hist);
  waitKey();
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
