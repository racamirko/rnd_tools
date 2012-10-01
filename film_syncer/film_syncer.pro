#-------------------------------------------------
#
# Project created by QtCreator 2012-09-24T09:56:32
#
#-------------------------------------------------

QT       += core gui phonon

TARGET = film_syncer
TEMPLATE = app

INCLUDEPATH += /usr/local/include
LIBS += -lglog -L/usr/local/lib

SOURCES += main.cpp\
        mainwindow.cpp \
    CUtil.cpp

HEADERS  += mainwindow.h \
    globalInclude.h \
    CUtil.h

FORMS    += mainwindow.ui
