import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';

import { RouterService } from '../../services/router-service';
import { AuthenticationService } from '../../services/authentication-service';

@Component({
    selector: 'login',
    templateUrl: './login.component.html'
})

export class LoginComponent implements OnInit {
    submitted = false;
    returnUrl: string;
    error = '';
    loading = false;
    model: any = {};

    constructor(
        private http: HttpClient,
        private routerService: RouterService,
        private authenticationService: AuthenticationService,
    ) { }

    ngOnInit() {
        sessionStorage.setItem('token', '');
        this.model.username = 'killesk@gmail.com';
        this.model.password = 'm123';

    }

    login() {
        this.authenticationService.authenticate(this.model.username, this.model.password, () => {
            this.routerService.toHome();
        });
    }
}
