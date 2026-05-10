package com.proje.service;

import com.proje.dto.RegistrationForm;
import com.proje.entity.Role;
import com.proje.entity.User;
import com.proje.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + username));
    }

    public List<User> ranking() {
        return userRepository.findTop10ByRoleOrderByScoreDescUsernameAsc(Role.USER);
    }

    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }
        return userRepository.existsByUsername(username.trim());
    }

    @Transactional
    public User registerUser(RegistrationForm form) {
        if (usernameExists(form.getUsername())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor.");
        }

        User user = new User();
        user.setUsername(form.getUsername().trim());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setRole(Role.USER);
        user.setScore(0);
        return userRepository.save(user);
    }
}
