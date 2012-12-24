#include "CImageRegion.h"

#include "globalInclude.h"
#include <stdio.h>

using namespace tinyxml2;

CImageRegion::CImageRegion( cv::Rect _rect )
    : x1( _rect.x )
    , y1( _rect.y )
    , x2( _rect.x+_rect.width )
    , y2( _rect.y+_rect.height )
{
    DLOG(INFO) << "CImageRegion created from cv::Rect at " << "[" << x1 << ", " << y1 << ", "<< x2 << ", "<< y2 << "]";
}

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

XMLElement* CImageRegion::toXml(tinyxml2::XMLDocument* _doc, tinyxml2::XMLElement* _parent, std::string elementName){
    XMLElement* tmpSubEle = _doc->NewElement(elementName.c_str());
    tmpSubEle->SetAttribute("x1", x1);
    tmpSubEle->SetAttribute("y1", y1);
    tmpSubEle->SetAttribute("x2", x2);
    tmpSubEle->SetAttribute("y2", y2);
    _parent->InsertEndChild(tmpSubEle);

    return tmpSubEle;
}

void CImageRegion::fromXml(tinyxml2::XMLDocument* _doc, tinyxml2::XMLElement* _parent){
    // not really that needed for now
}
