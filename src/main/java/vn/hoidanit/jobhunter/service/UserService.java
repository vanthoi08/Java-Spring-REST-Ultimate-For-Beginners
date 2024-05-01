package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
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

}
