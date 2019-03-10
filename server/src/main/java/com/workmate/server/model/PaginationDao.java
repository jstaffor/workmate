package com.workmate.server.model;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationDao<T>
{
    private int page;
    private int itemsPerPage;
    private int totalPages;
    private long totalElements;
    private List<T> list;

    public PaginationDao(Page data, int page, int itemsPerPage)
    {
        this.page = page;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = data.getTotalPages();
        this.totalElements = data.getTotalElements();
        if (page > data.getTotalPages())
        {
            this.list = new ArrayList<>();
        }
        else
        {
            this.list = data.getContent();
        }
    }

    public int getPage()
    {
        return page;
    }

    public int getItemsPerPage()
    {
        return itemsPerPage;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public long getTotalElements()
    {
        return totalElements;
    }

    public List<T> getList()
    {
        return list;
    }
}
