import { Component, OnInit } from '@angular/core';

import { UserService } from '../../services/user-service';
import { AuthenticationService } from '../../services/authentication-service';
import { RouterService } from '../../services/router-service';


@Component({
    selector: 'home',
    templateUrl: './home.component.html'
})

export class HomeComponent implements OnInit {
    loading = true;
    userName: string;

    constructor(
        private userService: UserService, private authenticationService: AuthenticationService, 
        private routerService: RouterService) { }

    ngOnInit() {
        this.loading = false;
        this.authenticationService.isAuthenticated(() => {
            this.routerService.toLogin();
        });
    }

    helloAdmin() {
        this.userService.helloAdmin();
    }

    helloCompanyAdmin() {
        this.userService.helloCompanyAdmin();
    }

    helloCompanyUser() {
        this.userService.helloCompanyUser();
    }
}