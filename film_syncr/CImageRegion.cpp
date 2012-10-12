#include "CImageRegion.h"

#include "globalInclude.h"

CImageRegion::CImageRegion()
     : x1(0)
     , y1(0)
     , x2(0)
     , y2(0)
{
    DLOG(INFO) << "CImageRegion created(default) at -1@[0, 0, 0, 0]";
}

CImageRegion::CImageRegion(int _x1, int _y1, int _x2, int _y2, int _personId)
    : x1(_x1)
    , y1(_y1)
    , x2(_x2)
    , y2(_y2)
    , personId(_personId)
{
    DLOG(INFO) << "CImageRegion created at " << personId << "@[" << x1 << ", " << y1 << ", "<< x2 << ", "<< y2 << "]";
}

void CImageRegion::toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    // to be done later
}

void CImageRegion::fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    // to be done later
}
