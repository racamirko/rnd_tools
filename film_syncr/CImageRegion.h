#ifndef CIMAGEREGION_H
#define CIMAGEREGION_H

#include <string>
#include "tinyxml2.h"

class CImageRegion
{
protected:
    // we'll protect something, someday...
public:
    int x1, y1, x2, y2;

    CImageRegion();
    CImageRegion(int _x1, int _y1, int _x2, int _y2);

    int getWidth() { return x2-x1; }
    int getHeight() { return y2-y1; }

    std::string toString();
};

#endif // CIMAGEREGION_H
