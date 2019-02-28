import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable()
export class RouterService {
    constructor(private router: Router) { }

    toLogin() {
        this.router.navigateByUrl('/login');
    }

    toHome() {
        this.router.navigateByUrl('/home');
    }
}