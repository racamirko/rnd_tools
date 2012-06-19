#-------------------------------------------------
#
# Project created by QtCreator 2012-05-07T16:01:40
#
#-------------------------------------------------

QT       += core

QT       -= gui

TARGET = facetrack
CONFIG   += console
CONFIG   -= app_bundle

TEMPLATE = app

INCLUDEPATH = /home/raca/include ../../../../repo/cvlab-code/trunk/raca/utilsLib /sw/include/
LIBS += -L/home/raca/lib -L/sw/lib -L../../../../repo/cvlab-code/trunk/raca/utilsLib-build-desktop -lopencv_core -lopencv_imgproc -lopencv_highgui -lopencv_video -lgsl -lgslcblas -lutilsLib

SOURCES += main.cpp \
    CSIRFilterPt.cpp \
    test_OpenCv_Kalman.cpp \
    initData.cpp \
    runScenario.cpp

HEADERS += \
    CSIRFilterPt.h \
    initData.h \
    runScenario.h







