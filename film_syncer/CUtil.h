#ifndef CUTIL_H
#define CUTIL_H

#include <QtCore/QRect>
#include <string>

class CUtil
{
public:
    CUtil();

    static std::string formatRect(QRect& _rect);
};

#endif // CUTIL_H
