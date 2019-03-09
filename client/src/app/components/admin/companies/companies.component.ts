import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserHttp } from '../../../http/user-http';
import { CompanyHttp } from '../../../http/company-http';
import { AuthenticationHttp } from '../../../http/authentication-http';
import { RouterService } from '../../../services/router-service';
import { SessionService } from '../../../services/session-service';
import { Company } from '../../../models/company';
import { CompanyDialogComponent } from './company/company.component';


@Component({
    selector: 'companies',
    templateUrl: './companies.component.html'
})

export class CompaniesComponent implements OnInit {
    loading = true;
    companies: Company[];

    constructor(
        private userHttp: UserHttp, 
        private companyHttp: CompanyHttp,
        private authenticationHttp: AuthenticationHttp, 
        private routerService: RouterService,
        private translate: TranslateService,
        private sessionService: SessionService,
        public dialog: MatDialog) {
            translate.setDefaultLang(this.sessionService.getLanguage());
        }

    ngOnInit() {
        this.loading = false;
        this.getCompanies();
    }

    getCompanies(): void {
        this.companyHttp.getCompanies(0, 100)
        .subscribe(companies => {
            this.companies = companies
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
                    this.getCompanies();
                }
            }
            });
    }

    create() {
        const company = new Company();
        debugger;
        this.toCompanyDialog(company);
    }
}

