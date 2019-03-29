import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';


@Injectable()
export class LocalService {
    constructor(private translate: TranslateService) { }

    DEFAULT_PAGE_SIZE: number = 10;
    VAR_STORAGE_DEFAULT_PAGE_SIZE = 'VAR_STORAGE_DEFAULT_PAGE_SIZE';
    setDefaultPageSize(size: number) {
        localStorage.setItem(this.VAR_STORAGE_DEFAULT_PAGE_SIZE, ""+size);
    }

    getDefaultPageSize(): number {
        var value = localStorage.getItem(this.VAR_STORAGE_DEFAULT_PAGE_SIZE);
        if(!value) {
            value = ""+this.DEFAULT_PAGE_SIZE;
        }
        return +value;
    }
}