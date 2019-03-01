import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateService } from '@ngx-translate/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AdminMenuComponent } from './components/admin/admin-menu.component';
import { CompanyComponent } from './components/admin/company/company.component';

import { AuthenticationHttp } from './http/authentication-http'
import { UserHttp } from './http/user-http';

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
        HttpClientModule,
        routing,
        BrowserAnimationsModule,
        MatSnackBarModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        })
    ],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent,
        AdminMenuComponent,
        CompanyComponent
    ],
    providers: [
        UserHttp,
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
    constructor(
        private translate: TranslateService,
        private sessionService: SessionService) {
        translate.setDefaultLang(this.sessionService.getLanguage());
    }
}

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}