import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { SessionService } from '../services/session-service';
import { RouterService } from '../services/router-service';


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(private sessionService: SessionService, private routerService: RouterService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const xhr = request.clone({
            headers: request.headers.set('X-Requested-With', 'XMLHttpRequest')
          });

        return next.handle(xhr).pipe(catchError(err => {
            if (err.status === 401 || err.status === 0) {
                // auto logout if 401 response returned from api
                // 0 is for CORS error
                // this.sessionService.removeToken();
                // location.reload(true);
                // this.routerService.logout();
            }            
            const error = err.error.message || err.statusText;
            return throwError(error);
        }))
    }
}