package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser)
            throws IdInvalidException {

        boolean isEmailExit = this.userService.exist(postManUser.getEmail());
        if (isEmailExit) {
            throw new IdInvalidException("Email" + postManUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        // Mã hóa password
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        // ghi đè lại password
        postManUser.setPassword(hashPassword);
        // save database
        User user = this.userService.handelCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(user));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")

    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }

        this.userService.handelDeleteUser(id);
        return ResponseEntity.ok(null);
        // return ResponseEntity.status(HttpStatus.OK).body("delete user");
        // status 204
        // return ResponseEntity.noContent().build();
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")

    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(currentUser));
    }

    // fetch all users
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
    }

    // update a user
    @PutMapping("/users")
    @ApiMessage("Update a user")

    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User postManUser) throws IdInvalidException {

        User currentUser = this.userService.fetchUserById(postManUser.getId());

        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + postManUser.getId() + " không tồn tại");
        }
        currentUser = this.userService.handleUpdateUser(postManUser);

        // if (user == null) {
        // throw new IdInvalidException("User với id = " + user.getId() + " không tồn
        // tại ");
        // }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(currentUser));
    }

}
