import { Component, OnInit, Inject } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Company } from '../../../../models/company';
import { ENUM_active } from '../../../../models/enums/enum-enable';



@Component({
    selector: 'company',
    templateUrl: './company.component.html'
})

export class CompanyDialogComponent  {
  options: FormGroup;
  submitted = true;
  active: ENUM_active;
  constructor(
    public dialogRef: MatDialogRef<CompanyDialogComponent>,
    fb: FormBuilder,
    @Inject(MAT_DIALOG_DATA) public data: JSON) {
      let company = data['Company'] as Company;
       this.options = fb.group({
        name: company.name,
        active: ENUM_active
      });
    }

    onNoClick(): void {
      this.dialogRef.close();
    }

    ngOnInit() {
      this.submitted = false;
    }

    save() {
      alert('hi');
    }
}