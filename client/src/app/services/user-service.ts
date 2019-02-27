import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { User } from '../models/user';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.post<any>(`/users/authenticate`, { username, password })
            .pipe(map(user => {
                // login successful if there's a user in the response
                if (user) {
                    // store user details and basic auth credentials in local storage 
                    // to keep user logged in between page refreshes
                    user.authdata = window.btoa(username + ':' + password);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return user;
            }));
    }

    getAll() {
        return this.http.get<User[]>(`http://localhost:8002/users`);
    }

    getById(id: number) {
        return this.http.get(`http://localhost:8002/users/` + id);
    }

    getByName(name: string) {
        return this.http.get(`http://localhost:8002/user/` + name);

        // this.http.post<Observable<Object>>(url, {}).
        //     subscribe(principal => {
        //         debugger;
        //         this.userName = principal['name'];
        //     },
        //     error => {
        //         debugger;
        //         if(error.status == 401) {
        //             debugger;
        //             alert('paul');
        //         }
        //     }
        // );
    }


    register(user: User) {
        return this.http.post(`http://localhost:8002/users/register`, user);
    }

    update(user: User) {
        return this.http.put(`http://localhost:8002/users/` + user.username, user);
    }

    delete(id: number) {
        return this.http.delete(`http://localhost:8002/users/` + id);
    }
}