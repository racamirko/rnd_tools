#-------------------------------------------------
#
# Project created by QtCreator 2012-09-24T09:56:32
#
#-------------------------------------------------

QT       += core gui phonon

TARGET = film_syncr
TEMPLATE = app

INCLUDEPATH += /usr/local/include
LIBS += -lglog -L/usr/local/lib

SOURCES += main.cpp\
        mainwindow.cpp \
    CUtil.cpp \
    tinyxml2.cpp \
    CSessionParameters.cpp

HEADERS  += mainwindow.h \
    globalInclude.h \
    CUtil.h \
    tinyxml2.h \
    CSessionParameters.h

FORMS    += mainwindow.ui
