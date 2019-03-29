export class PaginationRequest {
    page: number;
    pageSize: number;

    constructor(page:number, pageSize:number) { 
        this.page = page;
        this.pageSize = pageSize;
    }

    getPaginationParameters() {
        return {
            page: `${this.page}`,
            pageSize: `${this.pageSize}`
          };
    }
}