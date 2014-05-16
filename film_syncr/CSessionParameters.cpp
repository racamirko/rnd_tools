#include "CSessionParameters.h"

#include <globalInclude.h>

#include "tinyxml2.h"
#include <stdio.h>
#include <cstdio>

using namespace tinyxml2;
using namespace std;

CSessionParameters::CSessionParameters()
    : pTimeMarks(NULL),
      pPersons(NULL)
{
    DLOG(INFO) << "SessionParameters created";
}


void CSessionParameters::load(std::string _filename){
    LOG(INFO) << "Loading parameters from file " << _filename;
    XMLDocument doc;
    doc.LoadFile(_filename.c_str());
    char buffer[50];

    XMLElement* root = doc.FirstChildElement();
    // filename parts

    filename.clear();
    zeroOffset.clear();
    for( int idx = 1; idx <= MAX_NUM_OF_VIDEOS; ++idx ){
        sprintf(buffer, "filename%d", idx);
        if(root->FirstChildElement(buffer)) // filenames
            filename[idx] = string(root->FirstChildElement(buffer)->FirstChild()->Value());

        // offsets
        sprintf(buffer, "offset%d", idx);
        int tmpInt = -1;
        if(root->FirstChildElement(buffer)){ // offset
            root->FirstChildElement(buffer)->QueryIntText(&tmpInt);
            zeroOffset[idx] = tmpInt;
        }
    }

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
    char buffer[300], bufferLabel[100];


    XMLElement* root = doc.NewElement("configuration");
    doc.InsertEndChild(root);
    XMLElement* tmpEle = NULL;

    for( int idx = 1; idx < MAX_NUM_OF_VIDEOS; ++idx ){
        if( filename.count(idx) == 0 || zeroOffset.count(idx) == 0 ){
            continue;
        }
        // filename
        sprintf(bufferLabel, "filename%d", idx);
        XMLElement* tmpEle = doc.NewElement(bufferLabel);
        tmpEle->InsertEndChild( doc.NewText(filename.at(idx).c_str()) );
        root->InsertEndChild(tmpEle);
        // offset
        sprintf(bufferLabel, "offset%d", idx);
        tmpEle = doc.NewElement(bufferLabel);
        sprintf(buffer, "%d", zeroOffset[idx]);
        tmpEle->InsertEndChild( doc.NewText(buffer) );
        root->InsertEndChild(tmpEle);
    }

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

int CSessionParameters::getZeroOffset(int _camIdx){
    if( zeroOffset.count(_camIdx) == 0 ){
        return -1;
    }
    return zeroOffset[_camIdx];
}

int CSessionParameters::setZeroOffset(int _camIdx, int _value){
    LOG(INFO) << "ZeroOffset for cam #" << _camIdx << " : " << _value;
    zeroOffset[_camIdx] = _value;
}

std::string CSessionParameters::getFilename(int _camIdx){
    if( filename.count(_camIdx) == 0 ){
        return "";
    }
    return filename[_camIdx];
}

void CSessionParameters::setFilename(int _camIdx, std::string _filename){
    filename[_camIdx] = _filename;
}


