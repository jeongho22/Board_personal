package com.example.dy.serivce;

import com.example.dy.entity.Role;
import com.example.dy.entity.User;
import com.example.dy.repository.RoleRepository;
import com.example.dy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User createUserWithRoles(User user, Collection<String> roleNames) {
        // 역할 이름으로 역할 찾기
        Collection<Role> roles = roleNames.stream()
                .map(roleName -> {
                    Role role = roleRepository.findByName(roleName);
                    if (role == null) {
                        throw new RuntimeException("Role not found: " + roleName);
                    }
                    return role;
                })
                .collect(Collectors.toList());

        // 사용자에게 역할 설정
        user.setRoles(roles);

        // 사용자 저장
        return userRepository.save(user);
    }
}