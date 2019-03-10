import { Component, OnInit, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatPaginatorModule } from '@angular/material';
import { Company } from '../../../../models/company';
import { ENUM_active } from '../../../../models/enums/enum-enable';
import { CompanyHttp } from '../../../../http/company-http';
import { FeedbackService } from '../../../../services/feedback-service';

@Component({
    selector: 'company',
    templateUrl: './company.component.html'
})

export class CompanyDialogComponent  {
  company: Company;
  formGroup: FormGroup;
  actives = Object.keys(ENUM_active).filter(v => isNaN(parseInt(v, 10)));
  submitted = true;  

  constructor(
    public dialogRef: MatDialogRef<CompanyDialogComponent>,
    public dialog: MatDialog,
    private companyHttp: CompanyHttp,
    private feedbackService: FeedbackService,
    private translate: TranslateService,
    fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: JSON) {
      this.company = data['Company'] as Company; 
      this.formGroup = fb.group({
        name: new FormControl((this.company.name === undefined ? '' :  this.company.name), [Validators.required]),
        active: new FormControl((this.company.active === undefined ? '' : this.company.active ), [Validators.required])
      });
    }

    onNoClick(): void {
      this.dialogRef.close();
    }

    ngOnInit() {
     this.enableSubmitButton();
    }

    enableSubmitButton() {
      this.submitted = false;
    }

    disableSubmitButton() {
      this.submitted = true;
    }

    closeDialog(updatedCompany: Company) {
      this.dialogRef.close(updatedCompany);
    }

    save() {
      var submittedCompany = new Company;
      submittedCompany.id = this.company.id;
      submittedCompany.name = this.formGroup.get('name').value;
      submittedCompany.active = this.formGroup.get('active').value;
      if(JSON.stringify(this.company) == JSON.stringify(submittedCompany)) {
        this.translate.get(['notingChanged', 'close']).subscribe(text => {
            this.feedbackService.createSnackBarMessage(text['notingChanged'], text['close']);});
      } else {
        this.disableSubmitButton();       
        if(submittedCompany.id === undefined){
          this.add(submittedCompany, this.enableSubmitButtonMethod());
        } else {
          this.update(submittedCompany, this.enableSubmitButtonMethod());
        }  
      }    
    }

    getErrorMessage() {
      return this.formGroup.get('name').hasError('required') ? 'You must enter a value' : 
      this.formGroup.get('active').hasError('email') ? 'You must enter a value' :
      '';
    }

    add(company: Company, callBackIfError): void {
      this.companyHttp.addCompany(company)
        .subscribe(success => {
          this.translate.get(['successfully', 'saved', 'close']).subscribe(text => {
              this.feedbackService.createSnackBarMessage(text['successfully'] + ' ' + text['saved'], text['close']);});
          this.closeDialog(success);
        }, error => {
          callBackIfError();
          alert('error: ' + error);
        });
    }

    update(company: Company, callBackIfError): void {
      this.companyHttp.updateCompany(company)
        .subscribe(success => {
          this.translate.get(['successfully', 'saved', 'close']).subscribe(text => {
              this.feedbackService.createSnackBarMessage(text['successfully'] + ' ' + text['saved'], text['close']);});
          this.closeDialog(success);
        }, error => {
          callBackIfError();
          alert('error: ' + error);
        });
    }
   
    deleteCompany(company: Company, callBackIfError): void {
      this.companyHttp.deleteCompany(company)
        .subscribe(success => {
          this.translate.get(['successfully', 'deleted', 'close']).subscribe(text => {
              this.feedbackService.createSnackBarMessage(text['successfully'] + ' ' + text['deleted'], text['close']);});          
          this.closeDialog(success);
        }, error => {
          callBackIfError();
          alert('error');
        });
    }

    delete() {
      if(this.company.id === undefined) {
        this.translate.get(['cant', 'delete','dumbass', 'close']).subscribe(text => {
          this.feedbackService.createSnackBarMessage(text['cant'] + ' ' + text['delete'] + ' ' + text['dumbass'], text['close']);}); 
      } else {
        this.disableSubmitButton();
        this.translate.get(['question', 'areYouSureYouWantTo', 'delete', 'close']).subscribe(text => {          
          let actionIfTrue = () => {
            this.enableSubmitButtonMethod();
            this.deleteCompany(this.company, this.enableSubmitButtonMethod());
          }
           this.feedbackService.createYesNoDialog(text['question'], text['areYouSureYouWantTo'] + ' ' + text['delete'] + '?', 
           actionIfTrue, this.enableSubmitButtonMethod());  
        }); 
      }
    }

    enableSubmitButtonMethod() {
      return () => {
        this.enableSubmitButton();
      };
    }
  
}