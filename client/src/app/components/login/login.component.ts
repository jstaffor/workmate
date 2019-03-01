import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';

import { RouterService } from '../../services/router-service';
import { SessionService } from '../../services/session-service';

import { AuthenticationHttp } from '../../http/authentication-http';

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
        private authenticationHttp: AuthenticationHttp,
        private translate: TranslateService,
        private sessionService: SessionService
    ) {
        //translate.setDefaultLang(this.sessionService.getLanguage());
    }

    ngOnInit() {
        this.model.username = 'admin@gmail.com';
        this.model.password = '123';
    }

    login() {
        this.submitted = true;
        this.authenticationHttp.authenticate(this.model.username, this.model.password, () => {
            this.routerService.toHome();
            this.submitted = false;            
        }, () => {
            this.submitted = false;       
        });
    }
}
