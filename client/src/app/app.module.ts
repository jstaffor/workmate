import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent }  from './app.component';
import { routing }        from './app.routing';

import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';

import { UserService } from './services/user-service';
import { RouterService } from './services/router-service';
import { SessionService } from './services/session-service';


import { AuthenticationService } from './services/authentication-service'
import { ErrorInterceptor } from './helpers/error-interceptor-helper';
import { JwtInterceptor } from './helpers/jwt-interceptor';

@NgModule({
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        routing
    ],
    declarations: [
        AppComponent,
        HomeComponent,
        LoginComponent
    ],
    providers: [
        UserService,
        RouterService,
        SessionService,
        AuthenticationService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }