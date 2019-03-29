import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserHttp } from '../../../http/user-http';
import { CompanyHttp } from '../../../http/company-http';
import { AuthenticationHttp } from '../../../http/authentication-http';
import { RouterService } from '../../../services/router-service';
import { SessionService } from '../../../services/session-service';
import { LocalService } from '../../../services/local-service';
import { Company } from '../../../models/company';
import { CompanyDialogComponent } from './company/company.component';
import { PaginationResponse } from '../../../models/pagination-response';
import { PaginationSuper } from '../../shared/pagination-super';

@Component({
    selector: 'companies',
    templateUrl: './companies.component.html'
})

export class CompaniesComponent extends PaginationSuper implements OnInit {
    loading = true;

    constructor(
        private userHttp: UserHttp, 
        private companyHttp: CompanyHttp,
        private authenticationHttp: AuthenticationHttp, 
        private routerService: RouterService,
        private translate: TranslateService,
        private sessionService: SessionService,
        public localService: LocalService,
        public dialog: MatDialog) {
            super(localService);
            // super();
        }

    ngOnInit() {
        this.loading = false;
        this.getCompanies(this.paginationResponse);
    }

    getCompanies(paginationResponse: PaginationResponse): void {
        this.companyHttp.getCompanies(paginationResponse.page, paginationResponse.pageSize)
        .subscribe(response => {
            this.paginationResponse = response;
        });
    }

    toCompanyDialog(company: Company) {
        const dialogRef = this.dialog.open(CompanyDialogComponent, {
            width: '250px',
            data: {Company: company}
            });
        
            dialogRef.afterClosed().subscribe(result => {
                if(result !== undefined) {
                    if(JSON.stringify(company) != JSON.stringify(result)) {
                        this.getCompanies(this.paginationResponse);
                    }
                }
            });
    }

    create() {
        const company = new Company();
        this.toCompanyDialog(company);
    }

    onPaginateChange(event){        
        this.localService.setDefaultPageSize(event.pageSize);
        super.onPaginateChange(event);
        this.getCompanies(this.paginationResponse);
    }
}

