#include "CSessionParameters.h"

#include "tinyxml2.h"
#include <stdio.h>

using namespace tinyxml2;
using namespace std;

CSessionParameters::CSessionParameters()
    : filename1("noname"),
      filename2("noname"),
      filename3("noname"),
      zeroOffset1(-1),
      zeroOffset2(-1),
      zeroOffset3(-1)
{

}


void CSessionParameters::load(std::string _filename){
    XMLDocument doc;
    doc.LoadFile(_filename.c_str());

    XMLElement* root = doc.FirstChildElement();
    // filename parts
    if(root->FirstChildElement("filename1"))
        filename1 = string(root->FirstChildElement("filename1")->FirstChild()->Value());
    if(root->FirstChildElement("filename2"))
        filename2 = string(root->FirstChildElement("filename2")->FirstChild()->Value());
    if(root->FirstChildElement("filename3"))
        filename3 = string(root->FirstChildElement("filename3")->FirstChild()->Value());

    // offsets
    if(root->FirstChildElement("offset1"))
        root->FirstChildElement("offset1")->QueryIntText(&zeroOffset1);
    if(root->FirstChildElement("offset2"))
        root->FirstChildElement("offset2")->QueryIntText(&zeroOffset2);
    if(root->FirstChildElement("offset3"))
        root->FirstChildElement("offset3")->QueryIntText(&zeroOffset3);
}

void CSessionParameters::save(std::string _filename){
    XMLDocument doc;
    char buffer[300];


    XMLElement* root = doc.NewElement("configuration");
    doc.InsertEndChild(root);

    XMLElement* tmpEle = doc.NewElement("filename1");
    tmpEle->InsertEndChild( doc.NewText(filename1.c_str()) );
    root->InsertEndChild(tmpEle);

    tmpEle = doc.NewElement("filename2");
    tmpEle->InsertEndChild( doc.NewText(filename2.c_str()) );
    root->InsertEndChild(tmpEle);

    tmpEle = doc.NewElement("filename3");
    tmpEle->InsertEndChild( doc.NewText(filename3.c_str()) );
    root->InsertEndChild(tmpEle);

    tmpEle = doc.NewElement("offset1");
    sprintf(buffer, "%d", zeroOffset1);
    tmpEle->InsertEndChild( doc.NewText(buffer) );
    root->InsertEndChild(tmpEle);

    tmpEle = doc.NewElement("offset2");
    sprintf(buffer, "%d", zeroOffset2);
    tmpEle->InsertEndChild( doc.NewText(buffer) );
    root->InsertEndChild(tmpEle);

    tmpEle = doc.NewElement("offset3");
    sprintf(buffer, "%d", zeroOffset3);
    tmpEle->InsertEndChild( doc.NewText(buffer) );
    root->InsertEndChild(tmpEle);

    doc.SaveFile(_filename.c_str());
}

