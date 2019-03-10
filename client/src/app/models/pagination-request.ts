export class PaginationRequest {
    page: number;
    size: number;

    constructor(page:number, size:number) { 
        this.page = page;
        this.size = size;
    }

    getPaginationParameters() {
        return {
            page: `${this.page}`,
            size: `${this.size}`
          };
    }
}