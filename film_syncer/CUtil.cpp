#include "CUtil.h"

#include <stdio.h>

using namespace std;

CUtil::CUtil()
{
}


std::string CUtil::formatRect(QRect& _rect){
    char buffer[200];
    sprintf(buffer, "[%d, %d, %d, %d]", _rect.x(), _rect.y(), _rect.width(), _rect.height());
    return string(buffer);
}
