#ifndef CPERSON_H
#define CPERSON_H

#include <map>
#include <string>
#include <opencv2/core/core.hpp>
#include "CImageRegion.h"

//typedef std::map<int, cv::Point> tPointsInTime;

class CPerson
{
protected:
    int id;
    std::string desc;
    std::map<int, CImageRegion> cameraRegions;
    std::map<int, cv::Point> headPosition; // cameraIdx -> position of head
public:
    CPerson();
    CPerson(int _id);
    CPerson(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);

    int getId(){ return id; }
    void setId(int _id) { id = _id; }
    void setDesc(std::string _desc) { desc = _desc; }
    std::string getDesc(){ return desc; }
    // image region methods
    void setCameraRegion(int _camId, CImageRegion _reg);
    std::map<int, CImageRegion>::iterator beginRegions(){ return cameraRegions.begin(); }
    std::map<int, CImageRegion>::iterator endRegions() { return cameraRegions.end(); }
    bool hasRegionForCamera(int _camIdx);
    CImageRegion getCameraRegion(int _camIdx);
    // head position methods
    void setCameraHeadPosition(int _camId, cv::Point _point);
    std::map<int, cv::Point>::iterator beginHeadPos(){ return headPosition.begin(); }
    std::map<int, cv::Point>::iterator endHeadPos() { return headPosition.end(); }
    bool hasHeadInCamera(int _camIdx);
    cv::Point getCameraHeadPos(int _camIdx);

    void toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
    void fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
};

#endif // CPERSON_H
