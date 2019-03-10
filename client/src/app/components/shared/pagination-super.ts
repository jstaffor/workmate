import { PaginationResponse } from '../../models/pagination-response';


export class PaginationSuper  {
    paginationResponse: PaginationResponse;

    constructor() {
        this.paginationResponse = new PaginationResponse(0,0,0,0,[]);
    }

    ngOnInit() {
    }
}

