package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@RequestBody @Valid Company company) {

        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handelCreateCompany(company));
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {
        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        // Convert String to int

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        // return
        // ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany());
        // List<Company> companies = this.companyService.fetchAllCompany(pageable);
        // return ResponseEntity.ok(companies);

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany(pageable));
    }

    @DeleteMapping("companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        // return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.ok(null);
    }

    @PutMapping("companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updateCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(updateCompany);
    }

}
