#include "CPerson.h"

#include "globalInclude.h"

CPerson::CPerson()
    : id(-1)
    , desc("")
{

}

CPerson::CPerson(int _id)
    : desc("")
{
    id = _id;
}

void CPerson::setCameraRegion(int _camId, CImageRegion _reg){
    cameraRegions.insert( std::pair<int, CImageRegion>(_camId, _reg) );
}


void CPerson::toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    // to do later
}

void CPerson::fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    // to do later
}

