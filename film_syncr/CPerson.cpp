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

void CPerson::setCameraHeadPosition(int _camId, long _time, CImageRegion _point){
    if( headPosition.count(_camId) == 0){
        headPosition.insert(std::pair<int, tPointsInTime>(_camId, tPointsInTime()));
    }
    headPosition[_camId].insert( std::pair<int, CImageRegion>(_time, _point));
}

bool CPerson::hasHeadInCamera(int _camIdx){
    if( headPosition.count(_camIdx) > 0 )
        return true;
    return false;
}

tPointsInTime CPerson::getCameraHeadPos(int _camIdx){
    if( !hasHeadInCamera(_camIdx) )
        return tPointsInTime();
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
        for( std::map<int, tPointsInTime>::iterator iter = headPosition.begin();
             iter != headPosition.end(); ++iter )
        {
            for( tPointsInTime::iterator iterTime = iter->second.begin();
                 iterTime != iter->second.end(); ++iterTime )
            {
                XMLElement* tmpSubEle = _doc->NewElement("headPos");
                tmpSubEle->SetAttribute("camIdx", iter->first);
                tmpSubEle->SetAttribute("time", (int)iterTime->first);
                tmpSubEle->SetAttribute("x1", iterTime->second.x1);
                tmpSubEle->SetAttribute("y1", iterTime->second.y1);
                tmpSubEle->SetAttribute("x2", iterTime->second.x2);
                tmpSubEle->SetAttribute("y2", iterTime->second.y2);

                tmpEle->InsertEndChild(tmpSubEle);
            }
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
            CImageRegion point;
            int camIdx = atoi(headNode->Attribute("camIdx"));
            long time = atoi(headNode->Attribute("time"));
            point.x1 = atoi(headNode->Attribute("x1"));
            point.y1 = atoi(headNode->Attribute("y1"));
            point.x2 = atoi(headNode->Attribute("x2"));
            point.y2 = atoi(headNode->Attribute("y2"));

            setCameraHeadPosition(camIdx, time, point);
            if( headNode == lastHeadPos )
                headNode = NULL;
            else
                headNode = headNode->NextSiblingElement();
        }

    }
}

