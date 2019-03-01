import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';

import { SessionService } from '../services/session-service';
import { FeedbackService } from '../services/feedback-service';
import { debug } from 'util';


@Injectable()
export class AuthenticationHttp {

    constructor(
        private http: HttpClient, 
        private sessionService: SessionService, 
        private feedbackService: FeedbackService) { }


    authenticate(username: string, password: string, callback, callbackForError) {
        let headers = new HttpHeaders();
        let token = btoa(username + ':' + password);
        headers = headers.append("Authorization", "Basic " + token);
        headers = headers.append("Content-Type", "application/json");
        
        this.http.get('http://localhost:8082/auth/user', {headers: headers}).subscribe(response => {
            if (response['name']) {
                this.sessionService.setToken(username, password);
                this.sessionService.setAuthorities(JSON.stringify(response['authorities']));
            } else {                
                this.sessionService.removeToken();
                this.sessionService.removeAuthorities();
                
            }
            return callback && callback();
        });
    }

    logout() {
        this.sessionService.removeToken();
    }
}