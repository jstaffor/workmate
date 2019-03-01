import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class FeedbackService {
    constructor(
        private snackBar: MatSnackBar,
        private translate: TranslateService) { }


    createSnackBarMessage(message: string, action: string) {
        this.snackBar.open(message, action, {
            duration: 2000,
          } );
    }
    
}