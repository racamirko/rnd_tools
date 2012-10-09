#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <phonon/VideoPlayer>
#include <QFileDialog>
#include <string>
#include <stdio.h>
#include <math.h>
#include <fstream>

#include "globalInclude.h"
#include "CUtil.h"

using namespace std;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    startPos1(0), startPos2(0), startPos3(0),
    markDialog(NULL)
{
    LOG(INFO) << "construct";
    ui->setupUi(this);
    tickTimer = new QTimer(this);

    setupHooks();
    setupAdditionalUI();
}

MainWindow::~MainWindow()
{
    if(markDialog != NULL)
        delete markDialog;
    delete ui;
}

void MainWindow::setupHooks(){
    LOG(INFO) << "setupHooks";
    connect(ui->action_Quit, SIGNAL(triggered()), this, SLOT(slot_quit()));
    connect(ui->actionPlay, SIGNAL(triggered()), this, SLOT(slot_play()));
    connect(ui->btnPlay, SIGNAL(clicked()), this, SLOT(slot_play()));
    connect(ui->btnSeekFront, SIGNAL(clicked()), this, SLOT(slot_seek_p10()));
    connect(ui->btnSeekBack, SIGNAL(clicked()), this, SLOT(slot_seek_m10()));
    connect(ui->actionPlay_10sec, SIGNAL(triggered()), this, SLOT(slot_play10sec()));
    connect(ui->btnPlay10, SIGNAL(clicked()), this, SLOT(slot_play10sec()));
    // open files routines
    connect(ui->btnLoad1, SIGNAL(clicked()), this, SLOT(slot_filename1()));
    connect(ui->btnLoad2, SIGNAL(clicked()), this, SLOT(slot_filename2()));
    connect(ui->btnLoad3, SIGNAL(clicked()), this, SLOT(slot_filename3()));
    connect(ui->action_Save, SIGNAL(triggered()), this, SLOT(slot_saveSession()));
    connect(ui->action_Open, SIGNAL(triggered()), this, SLOT(slot_openSession()));
    // menu
    connect(ui->actionGo_cam1, SIGNAL(triggered()), this, SLOT(slot_checkPlay1()));
    connect(ui->actionGo_cam2, SIGNAL(triggered()), this, SLOT(slot_checkPlay2()));
    connect(ui->actionGo_cam3, SIGNAL(triggered()), this, SLOT(slot_checkPlay3()));
    connect(ui->actionAdd_mark, SIGNAL(triggered()), this, SLOT(slot_addMark()));
    connect(ui->actionGoTo_Zero, SIGNAL(triggered()), this, SLOT(slot_gotoZero()));
    // timers
    connect(tickTimer, SIGNAL(timeout()), this, SLOT(slot_updateTimeLabels()));

    // just for quicker debugging
    ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
    ui->videoPlayer2->load(Phonon::MediaSource(ui->editFilename2->toPlainText()));
    ui->videoPlayer3->load(Phonon::MediaSource(ui->editFilename3->toPlainText()));
}

void MainWindow::setupAdditionalUI(){
    LOG(INFO) << "setupAdditionalUI";
}

void MainWindow::slot_play(){
    LOG(INFO) << "slot_play";

    if( ui->chkPlay1->isChecked() ){
        if(ui->videoPlayer1->isPlaying())
            ui->videoPlayer1->pause();
        else{
            ui->videoPlayer1->play();
            if( startPos1 != -1 ){
                DLOG(INFO) << "Rewinding camera #1 to position " << startPos1;
                ui->videoPlayer1->seek(startPos1);
                startPos1 = -1;
            }
        }
    }
    if( ui->chkPlay2->isChecked() ){
        if(ui->videoPlayer2->isPlaying())
            ui->videoPlayer2->pause();
        else{
            ui->videoPlayer2->play();
            if( startPos2 != -1 ){
                DLOG(INFO) << "Rewinding camera #2 to position " << startPos2;
                ui->videoPlayer2->seek(startPos2);
                startPos2 = -1;
            }
        }
    }
    if( ui->chkPlay3->isChecked()){
        if(ui->videoPlayer3->isPlaying())
            ui->videoPlayer3->pause();
        else{
            ui->videoPlayer3->play();
            if( startPos3 != -1 ){
                DLOG(INFO) << "Rewinding camera #3 to position " << startPos3;
                ui->videoPlayer3->seek(startPos3);
                startPos3 = -1;
            }
        }
    }

    if( ui->videoPlayer1->isPlaying() || ui->videoPlayer2->isPlaying() || ui->videoPlayer3->isPlaying() )
        tickTimer->start(500);
    else
        tickTimer->stop();

    slot_updateTimeLabels();
}

void MainWindow::slot_play10sec(){
    LOG(INFO) << "slot_play10sec";
    qint64 p1, p2, p3;
    if( startPos1 == 0 )
        p1 = ui->videoPlayer1->currentTime();
    else
        p1 = startPos1;
    if( startPos2 == 0 )
        p2 = ui->videoPlayer2->currentTime();
    else
        p2 = startPos2;
    if( startPos3 == 0 )
        p3 = ui->videoPlayer3->currentTime();
    else
        p3 = startPos3;
    slot_play();
    startPos1 = p1; startPos2 = p2; startPos3 = p3;
    QTimer::singleShot(2000, this, SLOT(slot_pause()));
}

void MainWindow::slot_pause(){
    LOG(INFO) << "slot_pause";
    ui->videoPlayer1->pause();
    ui->videoPlayer2->pause();
    ui->videoPlayer3->pause();
    tickTimer->stop();
    slot_updateTimeLabels();
}

void MainWindow::slot_gotoZero(){
    LOG(INFO) << "slot_gotoZero";
    if( ui->chkPlay1->isChecked() )
        startPos1 = ui->editOffset1->toPlainText().toInt();
    if( ui->chkPlay2->isChecked() )
        startPos2 = ui->editOffset2->toPlainText().toInt();
    if( ui->chkPlay3->isChecked() )
        startPos3 = ui->editOffset3->toPlainText().toInt();
    slot_pause();
    // update labels
    ui->editFrame1->setPlainText(ui->editOffset1->toPlainText());
    ui->editFrame2->setPlainText(ui->editOffset2->toPlainText());
    ui->editFrame3->setPlainText(ui->editOffset3->toPlainText());
}

void MainWindow::slot_seek_p10(){
    LOG(INFO) << "slot_seek_p10";
    if( ui->chkPlay1->isChecked() )
        ui->videoPlayer1->seek( ui->videoPlayer1->currentTime()+10000 );
    if( ui->chkPlay2->isChecked() )
        ui->videoPlayer2->seek( ui->videoPlayer2->currentTime()+10000 );
    if( ui->chkPlay3->isChecked() )
        ui->videoPlayer3->seek( ui->videoPlayer3->currentTime()+10000 );
    slot_updateTimeLabels();
}

void MainWindow::slot_seek_m10(){
    LOG(INFO) << "slot_seek_m10";
    if( ui->chkPlay1->isChecked() )
        ui->videoPlayer1->seek( ui->videoPlayer1->currentTime()-10000 );
    if( ui->chkPlay2->isChecked() )
        ui->videoPlayer2->seek( ui->videoPlayer2->currentTime()-10000 );
    if( ui->chkPlay3->isChecked() )
        ui->videoPlayer3->seek( ui->videoPlayer3->currentTime()-10000 );
    slot_updateTimeLabels();
}

void MainWindow::slot_rewind(){
    LOG(INFO) << "slot_rewind";
    ui->videoPlayer1->seek(0);
    ui->videoPlayer2->seek(0);
    ui->videoPlayer3->seek(0);
}


void MainWindow::slot_quit(){
    LOG(INFO) << "quitting...";
    close();
}

void MainWindow::slot_filename1(){
    getVideoFile(0);
}

void MainWindow::slot_filename2(){
    getVideoFile(1);
}

void MainWindow::slot_filename3(){
    getVideoFile(2);
}

void MainWindow::getVideoFile(int playerIndex){
    QString path = QFileDialog::getOpenFileName(this, tr("Choose video file to play"), QString("/home/raca/data/video_material/12.09.13 - talk2 - bc410/"), QString::Null());
    switch(playerIndex){
        case 0:
            ui->editFilename1->setPlainText(path);
            ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
            sessParams.filename1 = ui->editFilename1->toPlainText().toStdString();
            break;
        case 1:
            ui->editFilename2->setPlainText(path);
            ui->videoPlayer2->load(Phonon::MediaSource(ui->editFilename2->toPlainText()));
            sessParams.filename2 = ui->editFilename2->toPlainText().toStdString();
            break;
        case 2:
            ui->editFilename3->setPlainText(path);
            ui->videoPlayer3->load(Phonon::MediaSource(ui->editFilename3->toPlainText()));
            sessParams.filename3 = ui->editFilename3->toPlainText().toStdString();
            break;
    }
    slot_updateTimeLabels();
}

void MainWindow::slot_openSession(){
    char buffer[20]; ifstream testFile;
    LOG(INFO) << "slot_openSession";
    QString path = QFileDialog::getOpenFileName(this, tr("Choose session filename to save"), QString("/home/raca/data/video_material/12.09.13 - talk2 - bc410/"), QString::Null());
    sessParams.load(path.toStdString());
    // filenames
    if( !sessParams.filename1.empty() ){
        testFile.open(sessParams.filename1.c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename1->setPlainText(QString::fromStdString(sessParams.filename1));
            ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.filename1 << " not found";
    }
    if( !sessParams.filename2.empty() ){
        testFile.open(sessParams.filename2.c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename2->setPlainText(QString::fromStdString(sessParams.filename2));
            ui->videoPlayer2->load(Phonon::MediaSource(ui->editFilename2->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.filename2 << " not found";
    }
    if( !sessParams.filename3.empty() ){
        testFile.open(sessParams.filename3.c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename3->setPlainText(QString::fromStdString(sessParams.filename3));
            ui->videoPlayer3->load(Phonon::MediaSource(ui->editFilename3->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.filename3 << " not found";
    }

    if(sessParams.zeroOffset1 != -1){
        sprintf(buffer,"%d", sessParams.zeroOffset1);
        ui->editOffset1->setPlainText(QString::fromAscii(buffer));
        startPos1 = sessParams.zeroOffset1;
    }
    if(sessParams.zeroOffset1 != -1){
        sprintf(buffer,"%d", sessParams.zeroOffset2);
        ui->editOffset2->setPlainText(QString::fromAscii(buffer));
        startPos2 = sessParams.zeroOffset2;
    }
    if(sessParams.zeroOffset1 != -1){
        sprintf(buffer,"%d", sessParams.zeroOffset3);
        ui->editOffset3->setPlainText(QString::fromAscii(buffer));
        startPos3 = sessParams.zeroOffset3;
    }
}

void MainWindow::slot_saveSession(){
    QString path = QFileDialog::getSaveFileName(this, tr("Choose session filename to save"), QString("/home/raca/data/video_material/12.09.13 - talk2 - bc410/"), QString::Null());
    sessParams.pTimeMarks = &vecTimeMarks; // TODO: not cool, not cool at all
    sessParams.save(path.toStdString());
}

void MainWindow::slot_checkPlay1(){
    ui->chkPlay1->setChecked( !ui->chkPlay1->isChecked() );
}

void MainWindow::slot_checkPlay2(){
    ui->chkPlay2->setChecked( !ui->chkPlay2->isChecked() );
}

void MainWindow::slot_checkPlay3(){
    ui->chkPlay3->setChecked( !ui->chkPlay3->isChecked() );
}

void MainWindow::slot_updateTimeLabels(){
    char buffer[20];
    if( ui->chkPlay1->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer1->currentTime());
        ui->editFrame1->setPlainText(QString(buffer));
    }
    if( ui->chkPlay2->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer2->currentTime());
        ui->editFrame2->setPlainText(QString(buffer));
    }
    if( ui->chkPlay3->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer3->currentTime());
        ui->editFrame3->setPlainText(QString(buffer));
    }
}

qint64 MainWindow::getGlobalTime(){
    // find valid trackers and get a mean time to minimize error in missing ticks
    qint64 timeEst = 0, numOfCams = 0;
    if( ui->videoPlayer1->currentTime() < ui->videoPlayer1->totalTime() ){
        timeEst += (ui->videoPlayer1->currentTime() - sessParams.zeroOffset1);
        ++numOfCams;
    }
    if( ui->videoPlayer2->currentTime() < ui->videoPlayer2->totalTime() ){
        timeEst += (ui->videoPlayer2->currentTime() - sessParams.zeroOffset2);
        ++numOfCams;
    }
    if( ui->videoPlayer3->currentTime() < ui->videoPlayer3->totalTime() ){
        timeEst += (ui->videoPlayer3->currentTime() - sessParams.zeroOffset3);
        ++numOfCams;
    }
    DLOG(INFO) << "Global time is " << round( timeEst/numOfCams ) << " based on " << numOfCams << " cameras";
    return round( timeEst/numOfCams ); // TODO: check this
}

void MainWindow::slot_addMark(){
    LOG(INFO) << "slot_addMark";
    slot_pause();

    if( markDialog == NULL )
        markDialog = new CMarkDialog(this, getGlobalTime(), &vecTimeMarks);
    else
        markDialog->setTimeMark(getGlobalTime());

    markDialog->show();
}
