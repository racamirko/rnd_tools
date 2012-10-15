#ifndef CPERSON_H
#define CPERSON_H

#include <map>
#include <string>
#include "CImageRegion.h"

class CPerson
{
protected:
    int id;
    std::string desc;
    std::map<int, CImageRegion> cameraRegions;
public:
    CPerson();
    CPerson(int _id);

    int getId(){ return id; }
    void setId(int _id) { id = _id; }
    void setDesc(std::string _desc) { desc = _desc; }
    std::string getDesc(){ return desc; }
    void setCameraRegion(int _camId, CImageRegion _reg);
    std::map<int, CImageRegion>::iterator beginRegions(){ return cameraRegions.begin(); }
    std::map<int, CImageRegion>::iterator endRegions() { return cameraRegions.end(); }

    void toXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
    void fromXml(tinyxml2::XMLDocument* _doc,tinyxml2::XMLElement* _parent);
};

#endif // CPERSON_H
