#include "CTimeMark.h"

#include <stdio.h>
#include "globalInclude.h"

using namespace std;
using namespace tinyxml2;

CTimeMark::CTimeMark( ETimeMarkType _type, qint64 _time, string _notes)
    : type(_type)
    , time(_time)
    , notes(_notes)
{
    DLOG(INFO) << "Created a time mark";
}


/**
  *  Input:
  *     xmlelement of type <timeline>, which is the parent
  *  Structure
  *     <mark time="2424242" type="ChangeSlide">
  *         This is an optional note.
  *     </mark>
  */
void CTimeMark::toXml(XMLDocument* _doc, XMLElement* _parent){
    char buffer[300];
    DLOG(INFO) << "Output to xml [" << markTypeToText(type) << "@ " << time << "]" ;

    XMLElement *tmpEle = _doc->NewElement("mark");
    sprintf(buffer, "%d", time);
    tmpEle->SetAttribute("time", buffer);
    tmpEle->SetAttribute("type", markTypeToText(type).c_str());
    if( notes.length() > 0 ){
        tmpEle->InsertEndChild( _doc->NewText(notes.c_str()));
    }
    _parent->InsertEndChild(tmpEle);
}

void CTimeMark::fromXml(XMLDocument* _doc, XMLElement* _parent){
    DLOG(INFO) << "Loading from XML";
    // do loading stuff here
    DLOG(INFO) << "Got from xml [" << markTypeToText(type) << "@ " << time << "]" ;
}

std::string CTimeMark::markTypeToText(ETimeMarkType _markType){
    switch(_markType){
        case TMT_CHANGE_SLIDE:
            return "ChangeSlide";
        case TMT_BEGIN_QUESTION:
            return "BeginQuestion";
        case TMT_END_QUESTION:
            return "EndQuestion";
        case TMT_BEGIN_ANSWER:
            return "BeginAnswer";
        case TMT_END_ANSWER:
            return "EndAnswer";
    }
}
