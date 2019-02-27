import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map, tap} from 'rxjs/operators';

import { UserService } from '../../services/user-service';
import { SessionService } from '../../services/session-service';


@Component({
    selector: 'home',
    templateUrl: './home.component.html'
})

export class HomeComponent implements OnInit {

    userName: string;

    constructor(
        private http: HttpClient,        private sessionService: SessionService, private userService: UserService) { }

    ngOnInit() {
        // let user = this.sessionService.getUnencryptedUser();
        // let url = `http://localhost:8082/user/${user}`;

        

        // this.http.get<Observable<Object>>(url, {}).
        //     subscribe(principal => {
        //         debugger;
        //         this.userName = principal['name'];
        //     },
        //     error => {
        //         debugger;
        //         if(error.status == 401) {
        //             debugger;
        //             alert('paul');
        //         }
        //     }
        // );

        // let headers: HttpHeaders = new HttpHeaders({
        //     'Authorization': 'Basic ' + sessionStorage.getItem('token'),
        //     'Origin': 'http://localhost:4200',
        //     'Access-Control-Allow-Origin': 'http://localhost:4200'
        // });

        // let options = { headers: headers };
        // this.http.post<Observable<Object>>(url, {}).
        //     subscribe(principal => {
        //         debugger;
        //         this.userName = principal['name'];
        //     },
        //     error => {
        //         debugger;
        //         if(error.status == 401) {
        //             debugger;
        //             alert('paul');
        //         }
        //     }
        // );
    }

    logout() {
        this.sessionService.removeToken();
    }
    private handleError(error: HttpErrorResponse) {
        if (error.error instanceof ErrorEvent) {
          console.error('An error occurred:', error.error.message);
        } else {
          console.error(
            `Backend returned code ${error.status}, ` +
            `body was: ${error.error}`);
        }
        return throwError(
          'Something bad happened; please try again later.');
      };
}