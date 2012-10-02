#include "CMarkDialog.h"
#include "ui_CMarkDialog.h"

#include "globalInclude.h"

CMarkDialog::CMarkDialog(QWidget *parent, qint64 _timeMark ,std::vector<qint64>* _vecData) :
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
        vecData->push_back(timeMark);
        return;
    }
    if( ui->radioQBegin->isChecked() ){
        LOG(INFO) << "Noting question asked at " << timeMark;
        // TODO: mark it somehow
        return;
    }
    if( ui->radioQEnd->isChecked() ){
        LOG(INFO) << "Noting question answered at " << timeMark;
        // TODO: mark it somehow
        return;
    }
}

void CMarkDialog::slot_cancel(){
    LOG(INFO) << "Cancel button pressed";
}

CMarkDialog::~CMarkDialog()
{
    delete ui;
}
