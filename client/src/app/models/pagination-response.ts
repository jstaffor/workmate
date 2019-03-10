export class PaginationResponse {
    constructor(page: number,
        itemsPerPage: number,
        totalPages: number,
        totalElements: number,
        list: any[]) {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.list = list;
    }    
    page: number;
    itemsPerPage: number;
    totalPages: number;
    totalElements: number;
    list: any[];
}