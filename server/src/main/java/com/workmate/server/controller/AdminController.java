package com.workmate.server.controller;

import com.workmate.server.model.Company;
import com.workmate.server.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController
{
    private final CompanyService companyService;

    @Autowired
    public AdminController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @GetMapping(path="/hello", produces = "application/json")
    public boolean helloAdmin()
    {
        return true;
    }

    @RequestMapping(value = "/company/getAll", method = RequestMethod.GET)
    public List<Company> getAllCompanies()
    {
        return companyService.getAllCompanies();
    }



    @RequestMapping(value = "/company/{id}", method = RequestMethod.GET)
    public Company getCompany(@PathVariable("id") Long id)
    {
        return companyService.findById(id);
    }


    @RequestMapping(value = "/company/", method = RequestMethod.POST)
    public ResponseEntity<Void> createCompany(@RequestBody Company company, UriComponentsBuilder ucBuilder)
    {
        if (companyService.isCompanyExist(company))
        {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        Company savedCompany = companyService.save(company);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/company/{id}").buildAndExpand(savedCompany.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Company> updateCompany(@PathVariable("id") Long id, @RequestBody Company company)
    {
        Company currentCompany = companyService.findById(id);
        if (currentCompany==null)
        {
            return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
        }
        currentCompany.setName(company.getName());
        currentCompany.setActive(company.getActive());
        companyService.save(currentCompany);
        return new ResponseEntity<Company>(currentCompany, HttpStatus.OK);
    }

    @RequestMapping(value = "/company/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Company> deleteCompany(@PathVariable("id") Long id)
    {
        Company user = companyService.findById(id);
        if (user == null)
        {
            return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
        }

        companyService.deleteCompanyById(id);
        return new ResponseEntity<Company>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/company/get", params = { "page", "size" })
    public List<Company> get(@RequestParam("page") int page,
                                   @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
                                   HttpServletResponse response) {
        Page<Company> resultPage = companyService.findPaginated(page, size);
        if (page > resultPage.getTotalPages())
        {
            return new ArrayList();
        }
        return resultPage.getContent();
    }


}
