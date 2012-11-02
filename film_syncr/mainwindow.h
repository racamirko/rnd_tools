#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTimer>
#include <vector>
#include <map>
#include "CSessionParameters.h"
#include "CMarkDialog.h"
#include "CTimeMark.h"
#include "CImageRegion.h"
#include "CSelectPersonsDialog.h"
#include "CPerson.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT
    
public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();
    
private:
    CSessionParameters sessParams;
    qint64 startPos1, startPos2, startPos3;
    std::vector<CTimeMark> vecTimeMarks;
    std::map<int, CPerson*> mapPersons;

    Ui::MainWindow *ui;
    CMarkDialog *markDialog;
    CSelectPersonsDialog regionSelectDialog;
    QTimer* tickTimer;
    int currentPersonIndex;

    void setupHooks();
    void setupAdditionalUI();
    void getVideoFile(int playerIndex);
    qint64 getGlobalTime();
    void jumpVideo(qint64 offset);

    bool changingOffsetText1, changingOffsetText2, changingOffsetText3;

private slots:
    void slot_quit();

    void slot_play();
    void slot_play10sec();
    void slot_pause();
    void slot_seek_p10();
    void slot_seek_m10();
    void slot_seek_p5m();
    void slot_seek_m5m();
    void slot_rewind();

    void slot_filename1();
    void slot_filename2();
    void slot_filename3();

    void slot_checkPlay1();
    void slot_checkPlay2();
    void slot_checkPlay3();

    void slot_updateTimeLabels();
    void slot_saveSession();
    void slot_openSession();

    void slot_addMark();
    void slot_gotoZero();

    void slot_markRegionsCam1();
    void slot_markRegionsCam2();
    void slot_markRegionsCam3();

    void slot_changeOffset1Text();
    void slot_changeOffset2Text();
    void slot_changeOffset3Text();

    // helpers (to be removed)
    void slot_dumpPersonInfo();
};

#endif // MAINWINDOW_H
