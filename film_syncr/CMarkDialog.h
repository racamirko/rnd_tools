#ifndef CMARKDIALOG_H
#define CMARKDIALOG_H

#include <QDialog>
#include "CTimeMark.h"
#include <vector>

namespace Ui {
class CMarkDialog;
}

class CMarkDialog : public QDialog
{
    Q_OBJECT
    
public:
    explicit CMarkDialog(QWidget *parent ,qint64 _timeMark ,std::vector<CTimeMark>* _vecData);
    ~CMarkDialog();

    void setTimeMark(qint64 _timeMark) { timeMark = _timeMark; }

    void show();
    
private:
    Ui::CMarkDialog *ui;
    std::vector<CTimeMark>* vecData;
    qint64 timeMark;

private slots:
    void slot_cancel();
    void slot_ok();
};

#endif // CMARKDIALOG_H
