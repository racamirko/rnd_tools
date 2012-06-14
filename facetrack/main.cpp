#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <string>
#include <vector>

using namespace cv;
using namespace std;

void initStartCoord( vector<Point2i> pPersonPoints );

int main(int argc, char *argv[])
{
  vector<Point2i> vecStartPnts;
  initStartCoord(vecStartPnts);

  // start filter(s)


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
