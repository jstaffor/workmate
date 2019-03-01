import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { UserHttp } from '../../../http/user-http';
import { AuthenticationHttp } from '../../../http/authentication-http';

import { RouterService } from '../../../services/router-service';
import { SessionService } from '../../../services/session-service';


@Component({
    selector: 'company',
    templateUrl: './company.component.html'
})

export class CompanyComponent implements OnInit {
    loading = true;
    userName: string;

    constructor(
        private userHttp: UserHttp, 
        private authenticationHttp: AuthenticationHttp, 
        private routerService: RouterService,
        private translate: TranslateService,
        private sessionService: SessionService) {
            translate.setDefaultLang(this.sessionService.getLanguage());
        }

    ngOnInit() {
        this.loading = false;
        // this.authenticationHttp.isAuthenticated(() => {
        //     this.routerService.toLogin();
        // });
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