#ifndef CSESSIONPARAMETERS_H
#define CSESSIONPARAMETERS_H

#include <string>
#include <vector>
#include "CTimeMark.h"

class CSessionParameters
{
public:
    std::string filename1, filename2, filename3;
    int zeroOffset1, zeroOffset2, zeroOffset3;
    std::vector<CTimeMark>* pTimeMarks;

    CSessionParameters();

    void load(std::string _filename);
    void save(std::string _filename);
};

#endif // CSESSIONPARAMETERS_H
