import { PaginationResponse } from '../../models/pagination-response';
import { PageEvent } from '@angular/material';
import { LocalService } from '../../services/local-service';


export class PaginationSuper  {
    paginationResponse: PaginationResponse;
    pageSizeOptions: number[] = [5, 10, 25, 100];    
    pageEvent: PageEvent;

    constructor(public localService: LocalService) {        
        this.paginationResponse = new PaginationResponse(0,localService.getDefaultPageSize(),0,0,[]);
    }

    ngOnInit() {
    }

    setPageSizeOptions(setPageSizeOptionsInput: string) {
        this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }

    onPaginateChange(event){
        this.paginationResponse.page = event.pageIndex;
        this.paginationResponse.pageSize = event.pageSize;
    }
}

