#include "CImageRegion.h"

#include "globalInclude.h"
#include <stdio.h>

using namespace tinyxml2;

CImageRegion::CImageRegion()
     : x1(0)
     , y1(0)
     , x2(0)
     , y2(0)
{
    DLOG(INFO) << "CImageRegion created(default) at [0, 0, 0, 0]";
}

CImageRegion::CImageRegion(int _x1, int _y1, int _x2, int _y2)
    : x1(_x1)
    , y1(_y1)
    , x2(_x2)
    , y2(_y2)
{
    DLOG(INFO) << "CImageRegion created at " << "[" << x1 << ", " << y1 << ", "<< x2 << ", "<< y2 << "]";
}

std::string CImageRegion::toString(){
    char buf[20];
    sprintf(buf, "[%d, %d, %d, %d]", x1, y1, x2, y2);
    return std::string(buf);
}
