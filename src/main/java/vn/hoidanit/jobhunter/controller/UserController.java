package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        // Mã hóa password
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        // ghi đè lại password
        postManUser.setPassword(hashPassword);
        // save database
        User user = this.userService.handelCreateUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("ID khong lon hon 1500");
        }

        this.userService.handelDeleteUser(id);
        // return ResponseEntity.ok("delete user");
        // return ResponseEntity.status(HttpStatus.OK).body("delete user");
        // status 204
        return ResponseEntity.noContent().build();
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchUserById(id));
    }

    // fetch all users
    @GetMapping("/users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUser(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {

        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";

        // Convert String to int

        int current = Integer.parseInt(sCurrent);
        int pageSize = Integer.parseInt(sPageSize);

        Pageable pageable = PageRequest.of(current - 1, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(pageable));
    }

    // update a user
    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User postManUser) {
        User user = this.userService.handleUpdateUser(postManUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

}
