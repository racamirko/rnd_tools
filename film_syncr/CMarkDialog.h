#ifndef CMARKDIALOG_H
#define CMARKDIALOG_H

#include <QDialog>
#include <vector>

namespace Ui {
class CMarkDialog;
}

class CMarkDialog : public QDialog
{
    Q_OBJECT
    
public:
    //enum { ESLIDE_CHANGE, EQUESTION_BEGIN, EQUESTION_END };

    explicit CMarkDialog(QWidget *parent ,qint64 _timeMark ,std::vector<qint64>* _vecData);
    ~CMarkDialog();
    
private:
    Ui::CMarkDialog *ui;
    std::vector<qint64>* vecData;
    qint64 timeMark;

private slots:
    void slot_cancel();
    void slot_ok();
};

#endif // CMARKDIALOG_H
