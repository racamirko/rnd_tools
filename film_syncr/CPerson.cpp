#include "CPerson.h"

#include "globalInclude.h"
#include <stdio.h>

using namespace tinyxml2;
using namespace std;

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

CPerson::CPerson(XMLDocument* _doc, XMLElement* _parent){
    DLOG(INFO) << "Creating Person from Xml";
    fromXml(_doc, _parent);
}

void CPerson::setCameraRegion(int _camId, CImageRegion _reg){
    cameraRegions.insert( std::pair<int, CImageRegion>(_camId, _reg) );
}


void CPerson::toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    char buffer[300];
    DLOG(INFO) << "Output to xml Person[" << id << "]" ;
    XMLElement *tmpEle = _doc->NewElement("person");
    tmpEle->SetAttribute("id",id);

    XMLElement *tmpSubEle = NULL;
    if( !desc.empty() ){
        tmpSubEle = _doc->NewElement("desc");
        tmpSubEle->InsertEndChild(_doc->NewText(desc.c_str()));
        tmpEle->InsertEndChild(tmpSubEle);
    }

    if( !cameraRegions.empty() ){
        for( std::map<int, CImageRegion>::iterator iter = cameraRegions.begin();
             iter != cameraRegions.end(); ++iter )
        {
            XMLElement* tmpSubEle = _doc->NewElement("region");

            sprintf(buffer,"%d", iter->first);
            tmpSubEle->SetAttribute("camIdx", buffer);

            sprintf(buffer,"%d", iter->second.x1);
            tmpSubEle->SetAttribute("x1", buffer);

            sprintf(buffer,"%d", iter->second.y1);
            tmpSubEle->SetAttribute("y1", buffer);

            sprintf(buffer,"%d", iter->second.x2);
            tmpSubEle->SetAttribute("x2", buffer);

            sprintf(buffer,"%d", iter->second.y2);
            tmpSubEle->SetAttribute("y2", buffer);

            tmpEle->InsertEndChild(tmpSubEle);
        }
    }

    _parent->InsertEndChild(tmpEle);
}

void CPerson::fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    DLOG(INFO) << "Loading from XML";
    const char* attributeValue = _parent->Attribute("id");
    id = atoi(attributeValue);
    // load all regions
    if(!_parent->NoChildren()){
        XMLElement* regionNode = _parent->FirstChildElement("region");
        XMLElement* lastChild = _parent->LastChildElement("region");

        while(1) {
            CImageRegion region = CImageRegion();
            int camIdx = atoi(regionNode->Attribute("camIdx"));
            region.x1 = atoi(regionNode->Attribute("x1"));
            region.y1 = atoi(regionNode->Attribute("y1"));
            region.x2 = atoi(regionNode->Attribute("x2"));
            region.y2 = atoi(regionNode->Attribute("y2"));

            cameraRegions[camIdx] = region;
            if( regionNode == lastChild )
                break;
            else
                regionNode = regionNode->NextSiblingElement();
        }
    }
}

