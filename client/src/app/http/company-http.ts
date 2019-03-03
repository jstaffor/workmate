import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

 import { Company } from '../models/company';

@Injectable()
export class CompanyHttp {
    constructor(private http: HttpClient) { }
    
    getCompanies (): Observable<Company[]> {
        return this.http.get<Company[]>('http://localhost:8082/admin/getCompanies')
        .pipe(
            // tap(_ => this.log('fetched companies')),
            // catchError(this.handleError('getCompanies', []))
        );
    }
}