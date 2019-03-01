import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';

import { SessionService } from '../services/session-service';
import { FeedbackService } from '../services/feedback-service';


@Injectable()
export class AuthenticationHttp {

    authenticated = false;

    constructor(
        private http: HttpClient, 
        private sessionService: SessionService, 
        private feedbackService: FeedbackService) { }

    isAuthenticated(callback) {
        if(this.authenticated == false) {
            callback();
        }
    }

    authenticate(username: string, password: string, callback, callbackForError) {
        let headers = new HttpHeaders();
        let token = btoa(username + ':' + password);
        headers = headers.append("Authorization", "Basic " + token);
        headers = headers.append("Content-Type", "application/json");
        
        this.http.get('http://localhost:8082/auth/user', {headers: headers}).subscribe(response => {
            if (response['name']) {
                this.sessionService.setToken(username, password);
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });
    }

    logout() {
        this.sessionService.removeToken();
    }
}