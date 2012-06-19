#include "runScenario.h"

#include "CSIRFilterPt.h"
#include "initData.h"
#include <CFileUtils.h>
#include <opencv2/highgui/highgui.hpp>


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
