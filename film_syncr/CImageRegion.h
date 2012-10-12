#ifndef CIMAGEREGION_H
#define CIMAGEREGION_H

#include <string>
#include "tinyxml2.h"

class CImageRegion
{
protected:
    // we'll protect something, someday...
public:
    int x1, y1, x2, y2, personId;
    std::string desc;

    CImageRegion();
    CImageRegion(int _x1, int _y1, int _x2, int _y2, int personId = -1);

    void toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
    void fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
};

#endif // CIMAGEREGION_H
