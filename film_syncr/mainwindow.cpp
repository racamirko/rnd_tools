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
#include "CSelectPersonsDialog.h"

using namespace std;

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow),
    startPos1(0), startPos2(0), startPos3(0),
    markDialog(NULL),
    changingOffsetText1(false), changingOffsetText2(false), changingOffsetText3(false),
    currentPersonIndex(0),
    regionSelectDialog(&currentPersonIndex)
{
    LOG(INFO) << "construct";
    ui->setupUi(this);
    tickTimer = new QTimer(this);
    sessParams.pTimeMarks = &vecTimeMarks;
    sessParams.pPersons = &mapPersons;
    dummyBuffer = NULL;

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
    connect(ui->actionSeek_m5min, SIGNAL(triggered()), this, SLOT(slot_seek_m5m()));
    connect(ui->actionSeek_p5min, SIGNAL(triggered()), this, SLOT(slot_seek_p5m()));
    connect(ui->actionSeek_m10sec, SIGNAL(triggered()), this, SLOT(slot_seek_m10()));
    connect(ui->actionSeek_p10sec, SIGNAL(triggered()), this, SLOT(slot_seek_p10()));
    connect(ui->actionMark_region_c1, SIGNAL(triggered()), this, SLOT(slot_markRegionsCam1()));
    connect(ui->actionMark_region_c2, SIGNAL(triggered()), this, SLOT(slot_markRegionsCam2()));
    connect(ui->actionMark_region_c3, SIGNAL(triggered()), this, SLOT(slot_markRegionsCam3()));

    connect(ui->editOffset1, SIGNAL(textChanged()), this, SLOT(slot_changeOffset1Text()));
    connect(ui->editOffset2, SIGNAL(textChanged()), this, SLOT(slot_changeOffset2Text()));
    connect(ui->editOffset3, SIGNAL(textChanged()), this, SLOT(slot_changeOffset3Text()));

    connect(ui->comboCamSelect1, SIGNAL(currentIndexChanged(int)), this, SLOT(slot_changeSelectedCam1()));
    connect(ui->comboCamSelect2, SIGNAL(currentIndexChanged(int)), this, SLOT(slot_changeSelectedCam2()));
    connect(ui->comboCamSelect3, SIGNAL(currentIndexChanged(int)), this, SLOT(slot_changeSelectedCam3()));

    connect(ui->actionDump_info, SIGNAL(triggered()), this, SLOT(slot_dumpPersonInfo()));
    // timers
    connect(tickTimer, SIGNAL(timeout()), this, SLOT(slot_updateTimeLabels()));

    ui->videoPlayer1->load(dummyBuffer);
    ui->videoPlayer2->load(dummyBuffer);
    ui->videoPlayer3->load(dummyBuffer);

    // default values
    camIdxShown1 = 1;
    camIdxShown2 = 2;
    camIdxShown3 = 3;
}

void MainWindow::setupAdditionalUI(){
    LOG(INFO) << "setupAdditionalUI";

    fillCamSelections(ui->comboCamSelect1);
    ui->comboCamSelect1->setCurrentIndex(0);
    fillCamSelections(ui->comboCamSelect2);
    ui->comboCamSelect2->setCurrentIndex(1);
    fillCamSelections(ui->comboCamSelect3);
    ui->comboCamSelect3->setCurrentIndex(2);
}

void MainWindow::fillCamSelections(QComboBox *_pCam){
    char buff[20];
    for(int i = 1; i <= 20; i++){
        sprintf(buff, "%d", i);
        _pCam->addItem(QString(buff));
    }
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
        else {
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

void MainWindow::slot_changeOffset1Text(){
    LOG(INFO) << "slot_changeOffset1Text";
    if( changingOffsetText1 )
        return;
    changingOffsetText1 = true;
//    int cursorPos = ui->editOffset1->textCursor().position();
    QString tmpStr = ui->editOffset1->toPlainText();
    tmpStr = tmpStr.trimmed();
//    ui->editOffset1->setPlainText(tmpStr);
    if( tmpStr.length() == 0 )
        sessParams.setZeroOffset(camIdxShown1,0);
    int tmpInt = atoi(tmpStr.toAscii().constData());
    if( tmpInt == 0 ){
        if( strcmp("0", ui->editOffset1->toPlainText().toAscii().constData()) == 0 )
            sessParams.setZeroOffset(camIdxShown1, 0);
        else{
            char buffer[20];
            printf(buffer, "%li", sessParams.getZeroOffset(camIdxShown1));
            ui->editOffset1->setPlainText(QString(buffer));
        }
    }else
        sessParams.setZeroOffset(camIdxShown1,tmpInt);
//    cursorPos = min(cursorPos,tmpStr.length());
//    ui->editOffset1->textCursor().setPosition(cursorPos,QTextCursor::MoveAnchor);
    changingOffsetText1 = false;
}

void MainWindow::slot_changeOffset2Text(){
    LOG(INFO) << "slot_changeOffset2Text";
    if( changingOffsetText2 )
        return;
    changingOffsetText2 = true;
    QString tmpStr = ui->editOffset2->toPlainText();
    tmpStr = tmpStr.trimmed();
    if( tmpStr.length() == 0 )
        sessParams.setZeroOffset(camIdxShown2, 0);
    int tmpInt = atoi(tmpStr.toAscii().constData());
    if( tmpInt == 0 ){
        if( strcmp("0", ui->editOffset2->toPlainText().toAscii().constData()) == 0 )
            sessParams.setZeroOffset(camIdxShown2, 0);
        else{
            char buffer[20];
            printf(buffer, "%li", sessParams.getZeroOffset(camIdxShown2));
            ui->editOffset2->setPlainText(QString(buffer));
        }
    }else
        sessParams.setZeroOffset(camIdxShown2, tmpInt);
    changingOffsetText2 = false;
}

void MainWindow::slot_changeOffset3Text(){
    LOG(INFO) << "slot_changeOffset3Text";
    if( changingOffsetText3 )
        return;
    changingOffsetText3 = true;
    QString tmpStr = ui->editOffset3->toPlainText();
    tmpStr = tmpStr.trimmed();
    if( tmpStr.length() == 0 )
        sessParams.setZeroOffset(camIdxShown3, 0);
    int tmpInt = atoi(tmpStr.toAscii().constData());
    if( tmpInt == 0 ){
        if( strcmp("0", ui->editOffset3->toPlainText().toAscii().constData()) == 0 )
            sessParams.setZeroOffset(camIdxShown3, 0);
        else{
            char buffer[20];
            printf(buffer, "%li", sessParams.getZeroOffset(camIdxShown3));
            ui->editOffset3->setPlainText(QString(buffer));
        }
    }else
        sessParams.setZeroOffset(camIdxShown3, tmpInt);
    changingOffsetText3 = false;
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
    jumpVideo(10000);
}

void MainWindow::slot_seek_m10(){
    LOG(INFO) << "slot_seek_m10";
    jumpVideo(-10000);
}

void MainWindow::jumpVideo(qint64 offset){
    qint64 newGlobalTime = getGlobalTime() + offset;

    if( ui->chkPlay1->isChecked() )
        ui->videoPlayer1->seek( newGlobalTime + sessParams.getZeroOffset(camIdxShown1) );
    if( ui->chkPlay2->isChecked() )
        ui->videoPlayer2->seek( newGlobalTime + sessParams.getZeroOffset(camIdxShown2) );
    if( ui->chkPlay3->isChecked() )
        ui->videoPlayer3->seek( newGlobalTime + sessParams.getZeroOffset(camIdxShown3) );
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
    QString path = QFileDialog::getOpenFileName(this, tr("Choose video file to play"), QString("/media/data/11_classroom_recording/"), QString::Null());
    switch(playerIndex){
        case 0:
            sessParams.setFilename(camIdxShown1, path.toStdString());
            ui->editFilename1->setPlainText(path); // should display just the end filename
            ui->videoPlayer1->load(Phonon::MediaSource(path));
            break;
        case 1:
            sessParams.setFilename(camIdxShown2, path.toStdString());
            ui->editFilename2->setPlainText(path); // should display just the end filename
            ui->videoPlayer2->load(Phonon::MediaSource(path));
            break;
        case 2:
            sessParams.setFilename(camIdxShown3, path.toStdString());
            ui->editFilename3->setPlainText(path); // should display just the end filename
            ui->videoPlayer3->load(Phonon::MediaSource(path));
            break;
    }
    slot_updateTimeLabels();
}

void MainWindow::slot_openSession(){
    char buffer[20]; ifstream testFile;
    LOG(INFO) << "slot_openSession";
    QString path = QFileDialog::getOpenFileName(this, tr("Choose session filename to save"), QString("/media/data/11_classroom_recording/"), QString::Null());
    if( path.isEmpty() )
        return;
    sessParams.load(path.toStdString());
    // filenames
    if( !sessParams.getFilename(camIdxShown1).empty() ){
        testFile.open(sessParams.getFilename(camIdxShown1).c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename1->setPlainText(QString::fromStdString(sessParams.getFilename(camIdxShown1)));
            ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.getFilename(camIdxShown1) << " not found";
    }
    if( !sessParams.getFilename(camIdxShown2).empty() ){
        testFile.open(sessParams.getFilename(camIdxShown2).c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename2->setPlainText(QString::fromStdString(sessParams.getFilename(2)));
            ui->videoPlayer2->load(Phonon::MediaSource(ui->editFilename2->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.getFilename(2) << " not found";
    }
    if( !sessParams.getFilename(camIdxShown3).empty() ){
        testFile.open(sessParams.getFilename(camIdxShown3).c_str());
        if(testFile.is_open()){
            testFile.close();
            ui->editFilename3->setPlainText(QString::fromStdString(sessParams.getFilename(3)));
            ui->videoPlayer3->load(Phonon::MediaSource(ui->editFilename3->toPlainText()));
        } else
            LOG(ERROR) << "File " << sessParams.getFilename(3) << " not found";
    }

    if(sessParams.getZeroOffset(camIdxShown1) != -1){
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown1));
        ui->editOffset1->setPlainText(QString::fromAscii(buffer));
        startPos1 = sessParams.getZeroOffset(camIdxShown1);
    }
    if(sessParams.getZeroOffset(2) != -1){
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown2));
        ui->editOffset2->setPlainText(QString::fromAscii(buffer));
        startPos2 = sessParams.getZeroOffset(camIdxShown2);
    }
    if(sessParams.getZeroOffset(3) != -1){
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown3));
        ui->editOffset3->setPlainText(QString::fromAscii(buffer));
        startPos3 = sessParams.getZeroOffset(camIdxShown3);
    }
    // update windows title
    this->setWindowTitle(QString("FilmSyncr [") + path.right(path.length()-path.lastIndexOf("/")+1) + QString("]"));
}

void MainWindow::slot_saveSession(){
    QString path = QFileDialog::getSaveFileName(this, tr("Choose session filename to save"), QString("/media/data/11_classroom_recording"), QString::Null());
    if(path.isEmpty())
        return;
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
    char buffer[70];
    if( ui->chkPlay1->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer1->currentTime());
        ui->editFrame1->setPlainText(QString(buffer));
        ui->pbVideo1->setValue( (int) ((float)ui->videoPlayer1->currentTime())/((float)ui->videoPlayer1->totalTime())*100.0f );
    }
    if( ui->chkPlay2->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer2->currentTime());
        ui->editFrame2->setPlainText(QString(buffer));
        ui->pbVideo2->setValue( (int) ((float)ui->videoPlayer2->currentTime())/((float)ui->videoPlayer2->totalTime())*100.0f );
    }
    if( ui->chkPlay3->isChecked() ){
        sprintf(buffer,"%ld", ui->videoPlayer3->currentTime());
        ui->editFrame3->setPlainText(QString(buffer));
        ui->pbVideo3->setValue( (int) ((float)ui->videoPlayer3->currentTime())/((float)ui->videoPlayer3->totalTime())*100.0f );
    }
    qint64 globTime = getGlobalTime();
    int hour = globTime / 3600000, min = globTime / 60000, sec = (globTime % 60000) / 1000, msec = globTime % 1000;
    sprintf(buffer,"Global time %02d : %02d : %02d : %03d", hour, min, sec, msec);
    ui->lblGlobalTime->setText(QString(buffer));
}

qint64 MainWindow::getGlobalTime(){
    // find valid trackers and get a mean time to minimize error in missing ticks
    qint64 timeEst = 0, numOfCams = 0;
    if( ui->videoPlayer1->currentTime() < ui->videoPlayer1->totalTime() && ui->chkPlay1->isChecked() ){
        timeEst += (ui->videoPlayer1->currentTime() - sessParams.getZeroOffset(camIdxShown1));
        ++numOfCams;
    }
    if( ui->videoPlayer2->currentTime() < ui->videoPlayer2->totalTime() && ui->chkPlay2->isChecked() ){
        timeEst += (ui->videoPlayer2->currentTime() - sessParams.getZeroOffset(camIdxShown2));
        ++numOfCams;
    }
    if( ui->videoPlayer3->currentTime() < ui->videoPlayer3->totalTime() && ui->chkPlay3->isChecked() ){
        timeEst += (ui->videoPlayer3->currentTime() - sessParams.getZeroOffset(camIdxShown3));
        ++numOfCams;
    }
    qint64 globTime = 0;
    if( numOfCams > 0 ){
        globTime = round( timeEst/numOfCams );
    }
    DLOG(INFO) << "Global time is " << globTime << " based on " << numOfCams << " cameras";
    return globTime; // TODO: check this
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

void MainWindow::slot_seek_p5m(){
    DLOG(INFO) << "Seeking +5min";
    jumpVideo(300000);
}

void MainWindow::slot_seek_m5m(){
    DLOG(INFO) << "Seeking -5min";
    jumpVideo(-300000);
}

void MainWindow::slot_markRegionsCam1(){
    DLOG(INFO) << "marking regions in cam1";
    DLOG(INFO) << "Marking at global time: " << getGlobalTime() << " which is for video1: " << ui->videoPlayer1->currentTime();
    regionSelectDialog.getAreas(&mapPersons, camIdxShown1, sessParams.getFilename(camIdxShown1), ui->videoPlayer1->currentTime(), getGlobalTime());
}

void MainWindow::slot_markRegionsCam2(){
    DLOG(INFO) << "marking regions in cam2";
    DLOG(INFO) << "Marking at global time: " << getGlobalTime() << " which is for video2: " << ui->videoPlayer2->currentTime();
    regionSelectDialog.getAreas(&mapPersons, camIdxShown2, sessParams.getFilename(camIdxShown2), ui->videoPlayer2->currentTime(), getGlobalTime());
}

void MainWindow::slot_markRegionsCam3(){
    DLOG(INFO) << "marking regions in cam3";
    DLOG(INFO) << "Marking at global time: " << getGlobalTime() << " which is for video3: " << ui->videoPlayer3->currentTime();
    regionSelectDialog.getAreas(&mapPersons, camIdxShown3, sessParams.getFilename(camIdxShown3), ui->videoPlayer3->currentTime(), getGlobalTime());
}

void MainWindow::slot_dumpPersonInfo(){
    DLOG(INFO) << "Dumping person information" ;
    for( map<int, CPerson*>::iterator iter = mapPersons.begin();
         iter != mapPersons.end(); ++iter )
    {
        CPerson* person = iter->second;
        DLOG(INFO) << "Person id: " << person->getId();
        DLOG(INFO) << "Person desc: " << person->getDesc();
        for( std::map<int, CImageRegion>::iterator iterInner = person->beginRegions();
             iterInner != person->endRegions(); ++iterInner )
        {
            DLOG(INFO) << "\tCam #" << iterInner->first << " : " << iterInner->second.toString();
        }
    }
}

void MainWindow::slot_changeSelectedCam1(){
    DLOG(INFO) << "Changing cam displayed in section 1";
    DLOG(INFO) << "Previous cam index: " << camIdxShown1;
    changingOffsetText1 = true;
    camIdxShown1 = ui->comboCamSelect1->currentIndex()+1;
    // update the video display
    std::string curText = sessParams.getFilename(camIdxShown1);
    char buffer[100];
    if(curText.length() > 0){
        DLOG(INFO) << "Video set for cam #" << camIdxShown1 << " : " << curText;
        DLOG(INFO) << "\tNew offset: " << sessParams.getZeroOffset(camIdxShown1);
        ui->editFilename1->setPlainText(QString::fromStdString(curText));
        ui->videoPlayer1->load(Phonon::MediaSource(ui->editFilename1->toPlainText()));
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown1));
        ui->editOffset1->setPlainText(QString::fromAscii(buffer));
        startPos1 = sessParams.getZeroOffset(camIdxShown1);
        if( ui->chkPlay1->isChecked() )
            ui->videoPlayer1->seek( getGlobalTime() + startPos1 );
    } else {
        DLOG(INFO) << "No video set for cam #" << camIdxShown1;
        ui->videoPlayer1->load(dummyBuffer);
    }
    changingOffsetText1 = false;
    DLOG(INFO) << "New cam index: " << camIdxShown1;
}

void MainWindow::slot_changeSelectedCam2(){
    DLOG(INFO) << "Changing cam displayed in section 2";
    DLOG(INFO) << "Previous cam index: " << camIdxShown2;
    changingOffsetText2 = true;
    camIdxShown2 = ui->comboCamSelect2->currentIndex()+1;
    // update the video display
    std::string curText = sessParams.getFilename(camIdxShown2);
    char buffer[100];
    if(curText.length() > 0){
        DLOG(INFO) << "Video set for cam #" << camIdxShown2 << " : " << curText;
        DLOG(INFO) << "\tNew offset: " << sessParams.getZeroOffset(camIdxShown2);
        ui->editFilename2->setPlainText(QString::fromStdString(curText));
        ui->videoPlayer2->load(Phonon::MediaSource(ui->editFilename2->toPlainText()));
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown2));
        ui->editOffset2->setPlainText(QString::fromAscii(buffer));
        startPos2 = sessParams.getZeroOffset(camIdxShown2);
        if( ui->chkPlay2->isChecked() )
            ui->videoPlayer2->seek( getGlobalTime() + startPos2 );
    } else {
        DLOG(INFO) << "No video set for cam #" << camIdxShown2;
        ui->videoPlayer2->load(dummyBuffer);
    }
    changingOffsetText2 = false;
    DLOG(INFO) << "New cam index: " << camIdxShown2;
}

void MainWindow::slot_changeSelectedCam3(){
    DLOG(INFO) << "Changing cam displayed in section 3";
    DLOG(INFO) << "Previous cam index: " << camIdxShown3;
    changingOffsetText3 = true;
    camIdxShown3 = ui->comboCamSelect3->currentIndex()+1;
    // update the video display
    std::string curText = sessParams.getFilename(camIdxShown3);
    char buffer[100];
    if(curText.length() > 0){
        DLOG(INFO) << "Video set for cam #" << camIdxShown3 << " : " << curText;
        DLOG(INFO) << "\tNew offset: " << sessParams.getZeroOffset(camIdxShown3);
        ui->editFilename3->setPlainText(QString::fromStdString(curText));
        ui->videoPlayer3->load(Phonon::MediaSource(ui->editFilename3->toPlainText()));
        sprintf(buffer,"%d", sessParams.getZeroOffset(camIdxShown3));
        ui->editOffset3->setPlainText(QString::fromAscii(buffer));
        startPos3 = sessParams.getZeroOffset(camIdxShown3);
        if( ui->chkPlay3->isChecked() )
            ui->videoPlayer3->seek( getGlobalTime() + startPos3 );
    } else {
        DLOG(INFO) << "No video set for cam #" << camIdxShown3;
        ui->videoPlayer3->load(dummyBuffer);
    }
    changingOffsetText3 = false;
    DLOG(INFO) << "New cam index: " << camIdxShown3;
}

