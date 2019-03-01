import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { User } from '../models/user';

@Injectable()
export class UserHttp {
    constructor(private http: HttpClient) { }
    
    helloAdmin() {
        this.http.get('http://localhost:8082/admin/hello').subscribe(data => {
            console.log(data);
        },
        error => {
            console.log(error);
        });
    }

    helloCompanyAdmin() {
        this.http.get('http://localhost:8082/company/admin/hello').subscribe(data => {
            console.log(data);
        },
        error => {
            console.log(error);
        });
    }

    helloCompanyUser() {
        this.http.get('http://localhost:8082/company/user/hello').subscribe(data => {
            console.log(data);
        },
        error => {
            console.log(error);
        });
    }
}