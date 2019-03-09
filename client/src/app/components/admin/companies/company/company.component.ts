import { Component, OnInit, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
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
  options: FormGroup;
  actives = Object.keys(ENUM_active).filter(v => isNaN(parseInt(v, 10)));
  submitted = true;  

  constructor(
    public dialogRef: MatDialogRef<CompanyDialogComponent>,
    private companyHttp: CompanyHttp,
    private feedbackService: FeedbackService,
    private translate: TranslateService,
    fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: JSON) {
      this.company = data['Company'] as Company; 
      debugger;
      this.options = fb.group({
        name: new FormControl((this.company.name === undefined ? this.company.name : ''), [Validators.required]),
        active: new FormControl((this.company.active === undefined ? this.company.active : ENUM_active.ACTIVE), [Validators.required])
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
      submittedCompany.name = this.options.get('name').value;
      submittedCompany.active = this.options.get('active').value;
      if(JSON.stringify(this.company) == JSON.stringify(submittedCompany)) {
        this.translate.get(['notingChanged', 'close']).subscribe(text => {
            this.feedbackService.createSnackBarMessage(text['notingChanged'], text['close']);});
      } else {
        this.disableSubmitButton();
        let callBackIfError = () => {
          this.enableSubmitButton();
        };
        if(submittedCompany.id === undefined){
          this.add(submittedCompany, callBackIfError);
        } else {
          this.update(submittedCompany, callBackIfError);
        }  
      }    
    }

    getErrorMessage() {
      return this.options.get('name').hasError('required') ? 'You must enter a value' : 
      this.options.get('active').hasError('email') ? 'You must enter a value' :
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
   
    delete(company: Company, callBackIfError): void {
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
  
}