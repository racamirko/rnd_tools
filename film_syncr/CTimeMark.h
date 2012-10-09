#ifndef CTIMEMARK_H
#define CTIMEMARK_H

#include <qt4/QtCore/qglobal.h>
#include <string>
#include "tinyxml2.h"

enum ETimeMarkType { TMT_CHANGE_SLIDE = 0, TMT_BEGIN_QUESTION, TMT_END_QUESTION, TMT_BEGIN_ANSWER, TMT_END_ANSWER };

class CTimeMark
{
protected:
    qint64 time;
    ETimeMarkType type;
    std::string notes;

public:
    CTimeMark( ETimeMarkType _type, qint64 _time, std::string _notes = "");

    void toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
    void fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);

    static std::string markTypeToText(ETimeMarkType _markType);
};

#endif // CTIMEMARK_H
