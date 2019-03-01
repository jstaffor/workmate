import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class FeedbackService {
    constructor(private snackBar: MatSnackBar) { }


    createSnackBarMessage(message: string) {
        this.snackBar.open(message);
    }
    
}