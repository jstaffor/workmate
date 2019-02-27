import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { first } from 'rxjs/operators';

import { RouterService } from '../../services/router-service';
import { SessionService } from '../../services/session-service';
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
        private sessionService: SessionService,
        private authenticationService: AuthenticationService,
    ) { }

    ngOnInit() {
        sessionStorage.setItem('token', '');
        this.model.username = 'killesk@gmail.com';
        this.model.password = 'm123';

    }

    login() {

        // debugger;
        // this.authenticationService.login(this.model.username, this.model.password)
        //     .pipe(first())
        //     .subscribe(
        //         data => {
        //             this.routerService.toHome();
        //             // this.router.navigate([this.returnUrl]);
        //         },
        //         error => {
        //             this.error = error;
        //             this.loading = false;
        //         });

        this.authenticationService.authenticate(this.model.username, this.model.password, () => {
            this.routerService.toHome();
        });

        // let url = 'http://localhost:8082/user';
        // this.http.post<Observable<boolean>>(url, {
        //     userName: this.model.username,
        //     password: this.model.password
        // }).subscribe(isValid => {
        //     debugger;
        //     if (isValid) {
        //         this.sessionService.setToken(this.model.username, this.model.password);
        //         this.routerService.toHome();
        //     } else {
        //         alert("Authentication failed.")
        //     }
        // });
    }
}
