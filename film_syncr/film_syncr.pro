#-------------------------------------------------
#
# Project created by QtCreator 2012-09-24T09:56:32
#
#-------------------------------------------------

QT       += core gui phonon

TARGET = film_syncr
TEMPLATE = app

INCLUDEPATH += /usr/include
LIBS += -lglog -L/usr/lib -lopencv_core -lopencv_highgui -lopencv_video -lopencv_imgproc

SOURCES += main.cpp\
        mainwindow.cpp \
    CUtil.cpp \
    tinyxml2.cpp \
    CSessionParameters.cpp \
    CMarkDialog.cpp \
    CTimeMark.cpp \
    CSelectPersonsDialog.cpp \
    CImageRegion.cpp \
    CPerson.cpp

HEADERS  += mainwindow.h \
    globalInclude.h \
    CUtil.h \
    tinyxml2.h \
    CSessionParameters.h \
    CMarkDialog.h \
    CTimeMark.h \
    CSelectPersonsDialog.h \
    CImageRegion.h \
    CPerson.h

FORMS    += mainwindow.ui \
    CMarkDialog.ui

RESOURCES +=
