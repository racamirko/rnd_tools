#include <QtGui/QApplication>

#include "globalInclude.h"

#include "mainwindow.h"

int main(int argc, char *argv[])
{
    google::InitGoogleLogging(argv[0]);
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    
    return a.exec();
}
