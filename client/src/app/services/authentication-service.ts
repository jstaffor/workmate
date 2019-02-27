import { Injectable } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { SessionService } from './session-service';

@Injectable()
export class AuthenticationService {

    authenticated = false;

    constructor(private http: HttpClient, private sessionService: SessionService) { }
    greeting = "";

    authenticate(username: string, password: string, callback) {

        // debugger;
        // this.http.get('http://localhost:8082/user').subscribe(response => {
        //     if (response['name']) {
        //         this.authenticated = true;
        //         this.http.get('resource').subscribe(data => alert('hello' + data));
        //     } else {
        //         this.authenticated = false;
        //     }
        // }, () => { this.authenticated = false; });

        const headers = new HttpHeaders({
            authorization : 'Basic ' + btoa(username + ':' + password),
            Origin: `http://localhost:4200`,
            'Access-Control-Allow-Origin':'http://localhost:4200',
        });
        
        this.http.get('http://localhost:8082/user', {headers: headers}).subscribe(response => {
            if (response['name']) {
                this.authenticated = true;
            } else {
                this.authenticated = false;
            }
            return callback && callback();
        });

    }
    
    // login(username: string, password: string) {
    //     return this.http.post<any>(`/users/authenticate`, { username, password })
    //         .pipe(map(user => {
    //             // login successful if there's a user in the response
    //             if (user) {
    //                 // store user details and basic auth credentials in local storage 
    //                 // to keep user logged in between page refreshes
    //                 this.sessionService.setToken(username, password);
    //                 //user.authdata = window.btoa(username + ':' + password);
    //                 //localStorage.setItem('currentUser', JSON.stringify(user));
    //             }

    //             return user;
    //         }));
    // }

    logout() {

        this.sessionService.removeToken();
        // remove user from local storage to log user out
        // localStorage.removeItem('currentUser');
    }
}