#include "CSessionParameters.h"

#include "tinyxml2.h"
#include <stdio.h>

using namespace tinyxml2;

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

