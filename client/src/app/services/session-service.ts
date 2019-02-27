import { Injectable } from '@angular/core';

@Injectable()
export class SessionService {
    constructor() { }

    setToken(user: string, password: string) {
        
        sessionStorage.setItem('token', btoa(user + ':' + password));
        // 
    }
    getEncryptedToken() {
        return sessionStorage.getItem('token');
    }
    getUnencryptedUser() {
        let unEncyptedtoken = atob(sessionStorage.getItem('token'));
        var index = unEncyptedtoken.indexOf(":");  
        var name = unEncyptedtoken.substr(0, index); 
        return name;
    }
    removeToken() {
        sessionStorage.removeItem('token');
    }
    
}