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

bool CPerson::hasRegionForCamera(int _camIdx){
    if( cameraRegions.count(_camIdx) > 0 )
        return true;
    return false;
}

CImageRegion CPerson::getCameraRegion(int _camIdx){
    if( !hasRegionForCamera(_camIdx) )
        return CImageRegion();
    return cameraRegions[_camIdx];
}

void CPerson::setCameraHeadPosition(int _camId, cv::Point _point){
    headPosition.insert( std::pair<int, cv::Point>(_camId, _point));
}

bool CPerson::hasHeadInCamera(int _camIdx){
    if( headPosition.count(_camIdx) > 0 )
        return true;
    return false;
}

cv::Point CPerson::getCameraHeadPos(int _camIdx){
    if( !hasHeadInCamera(_camIdx) )
        return cv::Point();
    return headPosition[_camIdx];
}


void CPerson::toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    DLOG(INFO) << "Output to xml Person[" << id << "]" ;
    XMLElement *tmpEle = _doc->NewElement("person");
    tmpEle->SetAttribute("id",id);

    XMLElement *tmpSubEle = NULL;
    if( !desc.empty() ){
        tmpSubEle = _doc->NewElement("desc");
        tmpSubEle->InsertEndChild(_doc->NewText(desc.c_str()));
        tmpEle->InsertEndChild(tmpSubEle);
    }
    // regions
    if( !cameraRegions.empty() ){
        for( std::map<int, CImageRegion>::iterator iter = cameraRegions.begin();
             iter != cameraRegions.end(); ++iter )
        {
            XMLElement* tmpSubEle = _doc->NewElement("region");

            tmpSubEle->SetAttribute("camIdx", iter->first);
            tmpSubEle->SetAttribute("x1", iter->second.x1);
            tmpSubEle->SetAttribute("y1", iter->second.y1);
            tmpSubEle->SetAttribute("x2", iter->second.x2);
            tmpSubEle->SetAttribute("y2", iter->second.y2);

            tmpEle->InsertEndChild(tmpSubEle);
        }
    }
    // head positions
    if( !headPosition.empty() ){
        for( std::map<int, cv::Point>::iterator iter = headPosition.begin();
             iter != headPosition.end(); ++iter )
        {
            XMLElement* tmpSubEle = _doc->NewElement("headPos");

            tmpSubEle->SetAttribute("camIdx", iter->first);
            tmpSubEle->SetAttribute("x", iter->second.x);
            tmpSubEle->SetAttribute("y", iter->second.y);

            tmpEle->InsertEndChild(tmpSubEle);
        }
    }
    // finalize
    _parent->InsertEndChild(tmpEle);
}

void CPerson::fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent){
    DLOG(INFO) << "Loading from XML";
    const char* attributeValue = _parent->Attribute("id");
    id = atoi(attributeValue);
    // load all regions
    if(!_parent->NoChildren()){
        // regions
        XMLElement* regionNode = _parent->FirstChildElement("region");
        XMLElement* lastChild = _parent->LastChildElement("region");
        while(regionNode) {
            CImageRegion region = CImageRegion();
            int camIdx = atoi(regionNode->Attribute("camIdx"));
            region.x1 = atoi(regionNode->Attribute("x1"));
            region.y1 = atoi(regionNode->Attribute("y1"));
            region.x2 = atoi(regionNode->Attribute("x2"));
            region.y2 = atoi(regionNode->Attribute("y2"));

            cameraRegions[camIdx] = region;
            if( regionNode == lastChild )
                regionNode = NULL;
            else
                regionNode = regionNode->NextSiblingElement();
        }
        // head positions
        XMLElement* headNode = _parent->FirstChildElement("headPos");
        XMLElement* lastHeadPos = _parent->LastChildElement("headPos");
        while(headNode) {
            cv::Point point;
            int camIdx = atoi(headNode->Attribute("camIdx"));
            point.x = atoi(headNode->Attribute("x"));
            point.y = atoi(headNode->Attribute("y"));

            headPosition[camIdx] = point;
            if( headNode == lastHeadPos )
                headNode = NULL;
            else
                headNode = headNode->NextSiblingElement();
        }

    }
}

