package com.workmate.server.model;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class PaginationDao<T>
{
    private int page;
    private int pageSize;
    private int totalPages;
    private long length;
    private List<T> list;

    public PaginationDao(Page data, int page, int pageSize)
    {
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = data.getTotalPages();
        this.length = data.getTotalElements();
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

    public int getPageSize()
    {
        return pageSize;
    }

    public int getTotalPages()
    {
        return totalPages;
    }

    public long getLength()
    {
        return length;
    }

    public List<T> getList()
    {
        return list;
    }
}
