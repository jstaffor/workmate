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
        const headers = new HttpHeaders({
            authorization : 'Basic ' + btoa(username + ':' + password),
            Origin: `http://localhost:4200`,
            'Access-Control-Allow-Origin':'http://localhost:4200',
        });
        
        this.http.get('http://localhost:8082/user', {headers: headers}).subscribe(response => {
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