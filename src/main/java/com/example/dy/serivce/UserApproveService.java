package com.example.dy.serivce;

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserApproveService {

    private UserRepository userRepository;

    public UserApproveService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void approveUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        user.setApproved(true);
        userRepository.save(user);
    }
    public boolean isAdmin(UserPrincipal principal) {
        return principal.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
    }
}
