#include "mainwindow.h"
#include "ui_mainwindow.h"

#include "CUtil.h"

#include <phonon/VideoPlayer>
#include <QFileDialog>
#include <string>
#include <stdio.h>

#include "globalInclude.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    startPos1(0), startPos2(0), startPos3(0)
{
    LOG(INFO) << "construct";
    ui->setupUi(this);
    tickTimer = new QTimer(this);

    setupHooks();
    setupAdditionalUI();
}

MainWindow::~MainWindow()
{
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
    connect(ui->action_Open, SIGNAL(triggered()), this, SLOT(slot_openSession())); // check for playing
    connect(ui->actionGo_cam1, SIGNAL(triggered()), this, SLOT(slot_checkPlay1()));
    connect(ui->actionGo_cam2, SIGNAL(triggered()), this, SLOT(slot_checkPlay2()));
    connect(ui->actionGo_cam3, SIGNAL(triggered()), this, SLOT(slot_checkPlay3()));
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
            if( startPos1 != 0 ){
                ui->videoPlayer1->seek(startPos1);
                startPos1 = 0;
            }
        }
    }
    if( ui->chkPlay2->isChecked() ){
        if(ui->videoPlayer2->isPlaying())
            ui->videoPlayer2->pause();
        else{
            ui->videoPlayer2->play();
            if( startPos1 != 0 ){
                ui->videoPlayer1->seek(startPos1);
                startPos1 = 0;
            }
        }
    }
    if( ui->chkPlay3->isChecked()){
        if(ui->videoPlayer3->isPlaying())
            ui->videoPlayer3->pause();
        else{
            ui->videoPlayer3->play();
            if( startPos1 != 0 ){
                ui->videoPlayer1->seek(startPos1);
                startPos1 = 0;
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
    startPos1 = ui->videoPlayer1->currentTime();
    startPos2 = ui->videoPlayer2->currentTime();
    startPos3 = ui->videoPlayer3->currentTime();
    slot_play();
    QTimer::singleShot(1000, this, SLOT(slot_endPeriodPlaying()));
}

void MainWindow::slot_pause(){
    LOG(INFO) << "slot_pause";
    ui->videoPlayer1->pause();
    ui->videoPlayer2->pause();
    ui->videoPlayer3->pause();
    slot_updateTimeLabels();
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
    char buffer[20];
    QString path = QFileDialog::getOpenFileName(this, tr("Choose session filename to save"), QString("/home/raca/data/video_material/12.09.13 - talk2 - bc410/"), QString::Null());
    sessParams.load(path.toStdString());
    // filenames
    if( !sessParams.filename1.empty() ){
        ui->editFilename1->setPlainText(QString::fromStdString(sessParams.filename1));
        ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
    }
    if( !sessParams.filename2.empty() ){
        ui->editFilename1->setPlainText(QString::fromStdString(sessParams.filename1));
        ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
    }
    if( !sessParams.filename3.empty() ){
        ui->editFilename1->setPlainText(QString::fromStdString(sessParams.filename1));
        ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
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

void MainWindow::slot_endPeriodPlaying(){
    LOG(INFO) << "slot_endPeriodPlaying";
    ui->videoPlayer1->seek(startPos1);
    ui->videoPlayer2->seek(startPos2);
    ui->videoPlayer3->seek(startPos3);
    //slot_pause();
//    slot_updateTimeLabels();
}
