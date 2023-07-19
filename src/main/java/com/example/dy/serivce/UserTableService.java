package com.example.dy.service;

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import com.example.dy.serivce.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserTableService {

    private final UserRepository userRepository;

    @Autowired
    public UserTableService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public boolean isAdmin(UserPrincipal principal) {
        return principal.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
    }

    @Transactional
    public void deleteUsers(List<Integer> userIds) {
        userRepository.deleteAllById(userIds);
    }


}
