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

INCLUDEPATH = /home/raca/include ../../../utilsLib /sw/include/
LIBS += -L/home/raca/lib -L/sw/lib -L../../../utilsLib-build-desktop -lopencv_core -lopencv_imgproc -lopencv_highgui -lopencv_video

SOURCES += main.cpp
