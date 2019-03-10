import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { UserHttp } from '../../http/user-http';
import { AuthenticationHttp } from '../../http/authentication-http';

import { RouterService } from '../../services/router-service';
import { SessionService } from '../../services/session-service';


@Component({
    selector: 'home',
    templateUrl: './home.component.html'
})

export class HomeComponent implements OnInit {
    loading = true;

    constructor(
        private userHttp: UserHttp, 
        private authenticationHttp: AuthenticationHttp, 
        private routerService: RouterService,
        private translate: TranslateService,
        private sessionService: SessionService) {
        }

    ngOnInit() {
        this.loading = false;
    }

    toAdminMenu() {
        this.routerService.toAdminMenu();
    }

    logout() {
        this.sessionService.removeToken();
        this.routerService.toLogin();
    }

    helloAdmin() {
        this.userHttp.helloAdmin();
    }

    helloCompanyAdmin() {
        this.userHttp.helloCompanyAdmin();
    }

    helloCompanyUser() {
        this.userHttp.helloCompanyUser();
    }
}