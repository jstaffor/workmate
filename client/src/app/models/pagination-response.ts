export class PaginationResponse {
    constructor(page: number,
        pageSize: number,
        totalPages: number,
        length: number,
        list: any[]) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.length = length;
        this.list = list;
    }    
    page: number;
    pageSize: number;
    totalPages: number;
    length: number;
    list: any[];
}