import { Component, OnInit, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Company } from '../../../../models/company';
import { ENUM_active } from '../../../../models/enums/enum-enable';
import { CompanyHttp } from '../../../../http/company-http';



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
    fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: JSON) {
      this.company = data['Company'] as Company;     
       this.options = fb.group({
        name: new FormControl(this.company.name, [Validators.required]),
        active: new FormControl(ENUM_active[this.company.active], [Validators.required])
      });
    }

    onNoClick(): void {
      this.dialogRef.close();
    }

    ngOnInit() {
      this.submitted = false;
    }

    save(company: Company) {
      debugger;
      if(company.id != undefined){
        this.add(company);
      } else {
        this.update(company);
      }
      
    }

    getErrorMessage() {
      return this.options.get('name').hasError('required') ? 'You must enter a value' : 
      this.options.get('active').hasError('email') ? 'You must enter a value' :
      '';
    }

    add(company: Company): void {
      if (!name) { return; }
      this.companyHttp.addCompany(company)
        .subscribe(success => {
          debugger;
          alert('success: ' + success.name);
        }, error => {
          debugger;
          alert('error: ' + error);
        });
    }

    update(company: Company): void {
      if (!name) { return; }
      this.companyHttp.updateCompany(company)
        .subscribe(success => {
          debugger;
          alert('success: ' + success.name);
        }, error => {
          debugger;
          alert('error: ' + error);
        });
    }
   
    delete(company: Company): void {
      this.companyHttp.deleteCompany(company)
        .subscribe(success => {
          debugger;
          alert('success');
        }, error => {
          debugger;
          alert('error');
        });
    }
  
}