#ifndef CIMAGEREGION_H
#define CIMAGEREGION_H

#include <string>
#include "tinyxml2.h"
#include <opencv2/core/core.hpp>

class CImageRegion
{
protected:
    // we'll protect something, someday...
public:
    int x1, y1, x2, y2;

    CImageRegion();
    CImageRegion(int _x1, int _y1, int _x2, int _y2);
    CImageRegion( cv::Rect _rect );

    int getWidth() { return x2-x1; }
    int getHeight() { return y2-y1; }

    std::string toString();

    cv::Rect toRect(){ return cv::Rect(x1, y1, x2-x1, y2-y1); }

    tinyxml2::XMLElement* toXml(tinyxml2::XMLDocument* _doc, tinyxml2::XMLElement* _parent, std::string elementName = "region");
    void fromXml(tinyxml2::XMLDocument* _doc, tinyxml2::XMLElement* _parent);
};

#endif // CIMAGEREGION_H
