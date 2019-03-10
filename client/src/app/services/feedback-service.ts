import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';
import { YesNoDialogData } from '../models/dialog/yes-no-dialog-data';
import { YesNoDialogComponent } from '../../app/components/shared/dialog/yes-no-dialog/yes-no-dialog';

@Injectable()
export class FeedbackService {
    constructor(
        private snackBar: MatSnackBar,
        private translate: TranslateService,
        public dialog: MatDialog) { }


    createSnackBarMessage(message: string, action: string) {
        this.snackBar.open(message, action, {
            duration: 2000,
          } );
    }


    createYesNoDialog(title: string, question: string, actionForTrue, actionForFalse) {
        let yesNoDialogData = new YesNoDialogData();
        yesNoDialogData.title = title;
        yesNoDialogData.question = question;
        const dialogRef = this.dialog.open(YesNoDialogComponent, {
            width: '250px',
            data: {YesNoDialogData: yesNoDialogData}
            });
        
            dialogRef.afterClosed().subscribe(result => {
                if(result !== undefined) {
                    if(result.answer) {
                        actionForTrue();
                    } else {
                        actionForFalse();
                    }
                }
            });
        
    }
    
}