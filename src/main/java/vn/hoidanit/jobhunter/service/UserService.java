package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handelCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handelDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        // Get trang hiện tại
        mt.setPage(pageable.getPageNumber() + 1);
        // Get size
        mt.setPageSize(pageable.getPageSize());

        // Get tổng số trang
        mt.setPages(pageUser.getTotalPages());
        // Get tổng số phần tử
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // Remove sensitive data

        List<ResUserDTO> listUser = pageUser.getContent().stream().map(item -> new ResUserDTO(
                item.getId(),
                item.getName(),
                item.getEmail(),
                item.getGender(),
                item.getAddress(),
                item.getAge(),
                item.getCreatedAt(),
                item.getUpdateAt()

        )).collect(Collectors.toList());

        rs.setResult(listUser);
        /*
         * // Cách convert thông thường
         * // Khởi tạo danh sách để chứa các đối tượng ResUserDTO
         * List<ResUserDTO> listUser = new ArrayList<>();
         * 
         * // Lặp qua từng đối tượng User trong danh sách pageUser.getContent()
         * for (User itemUser : pageUser.getContent()) {
         * // Tạo một đối tượng ResUserDTO mới từ thông tin của đối tượng User hiện tại
         * ResUserDTO resUserDTO = new ResUserDTO(
         * itemUser.getId(),
         * itemUser.getName(),
         * itemUser.getEmail(),
         * itemUser.getGender(),
         * itemUser.getAddress(),
         * itemUser.getAge(),
         * itemUser.getCreatedAt(),
         * itemUser.getUpdateAt());
         * // Thêm đối tượng ResUserDTO vào danh sách listUser
         * listUser.add(resUserDTO);
         * }
         * 
         * rs.setResult(listUser);
         */
        return rs;

    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            // Không cập nhật Email và password
            currentUser.setName(reqUser.getName());
            currentUser.setAge(reqUser.getAge());
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setName(reqUser.getName());
            currentUser.setGender(reqUser.getGender());

            // update
            currentUser = this.userRepository.save(currentUser);

        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    // check email exit
    public boolean exist(String email) {
        return userRepository.existsByEmail(email);
    }

    // Convert User to ResCreateUserDTO
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());

        return res;

    }

    // convert User to ResUserDTO

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdateAt(user.getUpdateAt());

        return res;

    }

    // convert to Update UserDTO
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setAddress(user.getAddress());
        res.setGender(user.getGender());
        res.setUpdateAt(user.getUpdateAt());

        return res;
    }

}
