import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { PaginationRequest } from '../../../src/app/models/pagination-request';
import { PaginationResponse } from '../../../src/app/models/pagination-response';

 import { Company } from '../models/company';

@Injectable()
export class CompanyHttp {
    constructor(private http: HttpClient) { }

    private company_url = 'http://localhost:8082/admin/company/';

    getCompanies (page: number, size: number): Observable<PaginationResponse> {   
        let paginationRequest = new PaginationRequest(page, size);
        return this.http.get<PaginationResponse>(this.company_url + 'get', {
            params: paginationRequest.getPaginationParameters()
          }).pipe();
    }

    getCompany(company: Company): Observable<Company> {
        const id = typeof company === 'number' ? company : company.id;
        const url = `${this.company_url}${id}`; 
        return this.http.get<Company>(url).pipe();
    }
 
    addCompany (company: Company): Observable<Company> {
        return this.http.post<Company>(this.company_url, company).pipe();
    }
    
    deleteCompany (company: Company | number): Observable<Company> {
        const id = typeof company === 'number' ? company : company.id;
        const url = `${this.company_url}${id}`; 
        return this.http.delete<Company>(url).pipe();
    }
    
    updateCompany (company: Company): Observable<any> {
        const id = typeof company === 'number' ? company : company.id;
        const url = `${this.company_url}${id}`; 
        return this.http.put(url, company).pipe();
    }
}