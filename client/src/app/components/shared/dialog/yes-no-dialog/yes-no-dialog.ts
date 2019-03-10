import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { YesNoDialogData } from '../../../../models/dialog/yes-no-dialog-data';

@Component({
  selector: 'yes-no-dialog',
  templateUrl: 'yes-no-dialog.html',
})
export class YesNoDialogComponent {

  yesNoDialogData: YesNoDialogData;

  constructor(
    public dialogRef: MatDialogRef<YesNoDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: YesNoDialogData) {
      this.yesNoDialogData = data['YesNoDialogData'] as YesNoDialogData; 
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  yes() {
    this.yesNoDialogData.answer = true;
    this.closeDialog();
  }

  no() {
    this.yesNoDialogData.answer = false;
    this.closeDialog();
  }

  closeDialog() {
    this.dialogRef.close(this.yesNoDialogData);
  }
}