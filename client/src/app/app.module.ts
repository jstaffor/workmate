import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule }    from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateService } from '@ngx-translate/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule, MatInputModule } from '@angular/material';
import { MatSelectModule}  from '@angular/material/select';
import { MatPaginatorModule } from '@angular/material';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminMenuComponent } from './components/admin/admin-menu.component';
import { CompanyDialogComponent } from './components/admin/companies/company/company.component';
import { CompaniesComponent } from './components/admin/companies/companies.component';
import { YesNoDialogComponent } from './components/shared/dialog/yes-no-dialog/yes-no-dialog';

import { AuthenticationHttp } from './http/authentication-http'
import { UserHttp } from './http/user-http';
import { CompanyHttp } from './http/company-http';

import { RouterService } from './services/router-service';
import { SessionService } from './services/session-service';
import { FeedbackService } from './services/feedback-service';

import { ErrorInterceptor } from './helpers/error-interceptor-helper';
import { JwtInterceptor } from './helpers/jwt-interceptor';
import { AuthGuard } from './helpers/auth-guard-helper';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule,
        routing,
        BrowserAnimationsModule,
        MatSnackBarModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatPaginatorModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        })
    ],
    entryComponents: [YesNoDialogComponent],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        AdminMenuComponent,
        CompanyDialogComponent,
        CompaniesComponent,
        YesNoDialogComponent
    ],
    providers: [
        UserHttp,
        CompanyHttp,
        AuthenticationHttp,
        AuthGuard,
        RouterService,
        SessionService,
        FeedbackService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule {  
    constructor(private translate: TranslateService) {
        translate.setDefaultLang('en');
    }
}

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}