import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { SessionService } from '../services/session-service';
import { RouterService } from '../services/router-service';
import { FeedbackService } from '../services/feedback-service';
import { TranslateService } from '@ngx-translate/core';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(
        private sessionService: SessionService, 
        private routerService: RouterService, 
        private feedbackService: FeedbackService,
        private translate: TranslateService) {
            translate.setDefaultLang(this.sessionService.getLanguage());
        }
    
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(catchError(err => {
            if (err.status === 401) {
                this.translate.get(['wrongCredentials', 'close']).subscribe(text => {
                    this.feedbackService.createSnackBarMessage(text['wrongCredentials'], text['close']);});
                this.sessionService.removeToken();
                this.routerService.toLogin();
            } else if(err.status === 403){
                this.translate.get(['permissionDenied', 'close']).subscribe(text => {
                    this.feedbackService.createSnackBarMessage(text['permissionDenied'], text['close']);});
            } else {
                this.translate.get(['httpError', 'close']).subscribe(text => {
                    this.feedbackService.createSnackBarMessage(text['httpError'], text['close']);});
            }                     
            const error = err.error.message || err.statusText;
            return throwError(error);
        }))
    }
}