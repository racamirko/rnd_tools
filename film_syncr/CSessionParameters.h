#ifndef CSESSIONPARAMETERS_H
#define CSESSIONPARAMETERS_H

#include <string>
#include <vector>
#include <map>
#include "CTimeMark.h"
#include "CPerson.h"

#define MAX_NUM_OF_VIDEOS 20

class CSessionParameters
{
protected:
    std::map<int, int> zeroOffset; // camIdx -> offset in miliseconds
public:
    std::map<int,std::string> filename; // camIdx -> filename
    std::vector<CTimeMark>* pTimeMarks;
    std::map<int, CPerson*>* pPersons;

    CSessionParameters();

    void load(std::string _filename);
    void save(std::string _filename);

    int getZeroOffset(int _camIdx);
    int setZeroOffset(int _camIdx, int _value);

    std::string getFilename(int _camIdx);
    void setFilename(int _camIdx, std::string _filename);
};

#endif // CSESSIONPARAMETERS_H
