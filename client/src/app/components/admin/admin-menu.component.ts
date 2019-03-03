import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { UserHttp } from '../../http/user-http';
import { AuthenticationHttp } from '../../http/authentication-http';

import { RouterService } from '../../services/router-service';
import { SessionService } from '../../services/session-service';


@Component({
    selector: 'admin-menu',
    templateUrl: './admin-menu.component.html'
})

export class AdminMenuComponent implements OnInit {
    loading = true;

    constructor(
        private userHttp: UserHttp, 
        private authenticationHttp: AuthenticationHttp, 
        private routerService: RouterService,
        private translate: TranslateService,
        private sessionService: SessionService) { }

    ngOnInit() {
        this.loading = false;
    }

    toCompanies() {
        this.routerService.toCompanies();
    }
}