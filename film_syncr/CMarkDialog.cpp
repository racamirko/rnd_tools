#include "CMarkDialog.h"
#include "ui_CMarkDialog.h"

#include <string>
#include "globalInclude.h"

using namespace std;

CMarkDialog::CMarkDialog(QWidget *parent, qint64 _timeMark ,std::vector<CTimeMark>* _vecData):
    QDialog(parent),
    ui(new Ui::CMarkDialog),
    timeMark(_timeMark)
{
    ui->setupUi(this);
    vecData = _vecData;

    // connect slots
    connect(ui->buttonBox, SIGNAL(accepted()), this, SLOT(slot_ok()));
    connect(ui->buttonBox, SIGNAL(rejected()), this, SLOT(slot_cancel()));
}

void CMarkDialog::slot_ok(){
    LOG(INFO) << "Ok button pressed";
    if( ui->radioSlideChange->isChecked() ){
        LOG(INFO) << "Noting slide change at " << timeMark;
        string tmpStr = string(ui->textNotes->toPlainText().toUtf8().constData());
        LOG(INFO) << "With notes: " << tmpStr;
        vecData->push_back(CTimeMark(TMT_CHANGE_SLIDE, timeMark, tmpStr));
        close();
        return;
    }
    if( ui->radioQBegin->isChecked() ){
        LOG(INFO) << "Noting question start at " << timeMark;
        vecData->push_back(CTimeMark(TMT_BEGIN_QUESTION, timeMark));
        close();
        return;
    }
    if( ui->radioQEnd->isChecked() ){
        LOG(INFO) << "Noting question end at " << timeMark;
        vecData->push_back(CTimeMark(TMT_END_QUESTION, timeMark));
        close();
        return;
    }
    if( ui->radioABegin->isChecked() ){
        LOG(INFO) << "Noting answer start at " << timeMark;
        vecData->push_back(CTimeMark(TMT_BEGIN_ANSWER, timeMark));
        close();
        return;
    }
    if( ui->radioAEnd->isChecked() ){
        LOG(INFO) << "Noting answer end at " << timeMark;
        vecData->push_back(CTimeMark(TMT_END_ANSWER, timeMark));
        close();
        return;
    }
    close();
}

void CMarkDialog::slot_cancel(){
    LOG(INFO) << "Cancel button pressed";
    close();
}

void CMarkDialog::show(){
    LOG(INFO) << "Showing timemark dialog";
    ui->textNotes->clear();
    ((QDialog*)this)->show();
}

CMarkDialog::~CMarkDialog()
{
    delete ui;
}
