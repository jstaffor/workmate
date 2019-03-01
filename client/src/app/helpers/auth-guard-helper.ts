import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, Route } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationHttp } from '../http/authentication-http';
import { RouterService } from '../services/router-service';
import { SessionService } from '../services/session-service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(
      private authenticationHttp: AuthenticationHttp, 
      private routeRouterService: RouterService, 
      private sessionService: SessionService) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
      let isAuthorised = this.isAuthorised();      
      let isRoleAllowed = false;

      if(next.data != null && next.data.length > 0) {
        isRoleAllowed = this.isRoleAllowed(next.data.role, JSON.parse(this.sessionService.getAuthorities()));
      } else {
        isRoleAllowed = true;
      }
     let allowed = isRoleAllowed && isAuthorised;
     if(!allowed) {
        this.routeRouterService.toLogin();
     }      
    return allowed;
  }

  isAuthorised() {
    let encryptedToken = this.sessionService.getEncryptedToken();
    if(encryptedToken == null || encryptedToken == ''){
      this.routeRouterService.toLogin();
      return false;
    }
    return true;
  }

  isRoleAllowed(role: String, roles: String[]) {
    let jsonRoles = JSON.parse(this.sessionService.getAuthorities());
    for(var i = 0; i < jsonRoles.length; i++) {
        var authority = jsonRoles[i];
        if(authority == role){
            return true;
        }
    }
    return false;
  }

}