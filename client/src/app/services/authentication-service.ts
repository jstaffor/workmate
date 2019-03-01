import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { SessionService } from './session-service';

@Injectable()
export class AuthenticationService {

    authenticated = false;

    constructor(private http: HttpClient, private sessionService: SessionService) { }

    isAuthenticated(callback) {
        if(this.authenticated == false) {
            callback();
        }
    }

    authenticate(username: string, password: string, callback) {
        let headers = new HttpHeaders();
        let token = btoa(username + ':' + password);
        headers = headers.append("Authorization", "Basic " + token);
        headers = headers.append("Content-Type", "application/json");
        // headers = headers.append("Access-Control-Allow-Origin", "*");
        // headers = headers.append("Access-Control-Allow-Headers", "origin, x-requested-with, accept, apikey, authorization");
        // headers = headers.append("Access-Control-Max-Age", "3628800");
        // headers = headers.append("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
        
        
        // const headers = new HttpHeaders({
        //     'Accept': 'application/json',  
        //     'Content-Type': 'application/json',
        //     Authorization : 'Basic ' + btoa(username + ':' + password),
        // });
        
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