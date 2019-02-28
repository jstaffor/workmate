import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { User } from '../models/user';

@Injectable()
export class UserService {
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
        this.http.get('http://localhost:8082/companyadmin/hello').subscribe(data => {
            console.log(data);
        },
        error => {
            console.log(error);
        });
    }

    helloCompanyUser() {
        this.http.get('http://localhost:8082/companyuser/hellor').subscribe(data => {
            console.log(data);
        },
        error => {
            console.log(error);
        });
    }
}