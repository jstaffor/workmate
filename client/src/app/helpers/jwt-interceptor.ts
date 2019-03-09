import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SessionService } from '../services/session-service';

@Injectable()
export class    JwtInterceptor implements HttpInterceptor {

    constructor(private sessionService: SessionService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let unencryptedToken = this.sessionService.getEncryptedToken();
        if(unencryptedToken !== null && unencryptedToken !== "")
        {
            request = request.clone({
                setHeaders: { 
                    Authorization: `Basic ${unencryptedToken}`,
                    'Content-Type':  'application/json'
                }
            });
        } 
        return next.handle(request);
    }
}