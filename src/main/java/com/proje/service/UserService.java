package com.proje.service;

import com.proje.entity.User;
import com.proje.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Kullanici bulunamadi: " + username));
    }

    public List<User> ranking() {
        return userRepository.findTop10ByOrderByScoreDescUsernameAsc();
    }
}
