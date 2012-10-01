#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include <QTimer>

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
    void setupHooks();
    void setupAdditionalUI();
    void getVideoFile(int playerIndex);
    qint64 startPos1, startPos2, startPos3;

    Ui::MainWindow *ui;
    QTimer* tickTimer;

private slots:
    void slot_quit();

    void slot_play();
    void slot_play10sec();
    void slot_pause();
    void slot_seek_p10();
    void slot_seek_m10();
    void slot_rewind();

    void slot_filename1();
    void slot_filename2();
    void slot_filename3();

    void slot_checkPlay1();
    void slot_checkPlay2();
    void slot_checkPlay3();

    void slot_updateTimeLabels();
    void slot_endPeriodPlaying();
};

#endif // MAINWINDOW_H
