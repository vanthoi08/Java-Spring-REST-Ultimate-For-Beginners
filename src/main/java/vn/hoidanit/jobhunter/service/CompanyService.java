package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company handelCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public Company fetchCompanyById(long id) {
        Optional<Company> companyOptional = this.companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            return companyOptional.get();
        }
        return null;
    }

    public Optional<Company> findById(long id) {
        return this.companyRepository.findById(id);
    }

    public ResultPaginationDTO fetchAllCompany(Specification<Company> spec, Pageable pageable) {

        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        // Get trang hiện tại
        mt.setPage(pageable.getPageNumber() + 1);
        // Get size
        mt.setPageSize(pageable.getPageSize());

        // Get tổng số trang
        mt.setPages(pageCompany.getTotalPages());
        // Get tổng số phần tử
        mt.setTotal(pageCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());

        return rs;
    }

    public void handleDeleteCompany(long id) {
        // Tìm Company
        Optional<Company> comOptional = this.companyRepository.findById(id);
        if (comOptional.isPresent()) {
            // Lấy ra company
            Company com = comOptional.get();
            // fetch all user belong to this company
            // Lấy ra danh sách tất cả user có trong công ty đó
            List<User> users = this.userRepository.findByCompany(com);
            // Xóa tất cả người dùng
            this.userRepository.deleteAll(users);

        }
        this.companyRepository.deleteById(id);
    }

    public Company handleUpdateCompany(Company company) {
        Optional<Company> companyOptional = this.companyRepository.findById(company.getId());
        if (companyOptional.isPresent()) {
            Company currentCompany = companyOptional.get();
            currentCompany.setLogo(company.getLogo());
            currentCompany.setName(company.getName());
            currentCompany.setDescription(company.getDescription());
            currentCompany.setAddress(company.getAddress());

            // Update
            return this.companyRepository.save(currentCompany);
        }
        return null;
    }

}
