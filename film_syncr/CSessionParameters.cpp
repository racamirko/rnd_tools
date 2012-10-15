#include "CSessionParameters.h"

#include <globalInclude.h>

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
      zeroOffset3(-1),
      pTimeMarks(NULL),
      pPersons(NULL)
{
    DLOG(INFO) << "SessionParameters created";
}


void CSessionParameters::load(std::string _filename){
    LOG(INFO) << "Loading parameters from file " << _filename;
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

    // persons
    if( pPersons ){
        if(root->FirstChildElement("persons") && !root->FirstChildElement("persons")->NoChildren()){
            XMLElement* personNode = root->FirstChildElement("persons")->FirstChildElement("person");
            XMLElement* lastChild = root->FirstChildElement("persons")->LastChildElement("person");

            while(1) {
                CPerson* person = new CPerson(&doc, personNode);
                pPersons->insert(std::pair<int, CPerson*>(person->getId(), person));
                if( personNode == lastChild )
                    break;
                else
                    personNode = personNode->NextSiblingElement();
            }
        }
    } else
        LOG(WARNING) << "Person rectangles not loaded because the person map pointer wasnt provided";

    // timeline
    if( pTimeMarks ) {
        if(root->FirstChildElement("timeline") && !root->FirstChildElement("timeline")->NoChildren()){
            XMLElement* timeEvent = root->FirstChildElement("timeline")->FirstChildElement("mark");
            XMLElement* lastChild = root->FirstChildElement("timeline")->LastChildElement("mark");

            while(1) {
                CTimeMark mark(&doc, timeEvent);
                pTimeMarks->push_back(mark);
                if( timeEvent == lastChild )
                    break;
                else
                    timeEvent = timeEvent->NextSiblingElement();
            }
        }
    } else
        LOG(WARNING) << "Time marks not loaded because the timeline pointer wasnt provided";
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

    // persons
    if( pPersons ){
        tmpEle = doc.NewElement("persons");
        root->InsertEndChild(tmpEle);
        for( std::map<int, CPerson*>::iterator iter = pPersons->begin();
             iter != pPersons->end(); ++iter )
        {
            CPerson* tmpPerson = iter->second;
            tmpPerson->toXml(&doc, tmpEle);
        }
    }

    // time marks
    if( pTimeMarks ){
        tmpEle = doc.NewElement("timeline");
        root->InsertEndChild(tmpEle);
        for( vector<CTimeMark>::iterator iter = pTimeMarks->begin();
             iter != pTimeMarks->end(); ++iter)
        {
            iter->toXml(&doc, tmpEle);
        }
    }

    doc.SaveFile(_filename.c_str());
}

