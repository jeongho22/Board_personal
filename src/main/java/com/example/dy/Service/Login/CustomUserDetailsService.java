// 로그인 할때 자격 증명

package com.example.dy.Service.Login;
import com.example.dy.Domain.User;
import com.example.dy.Dto.Login.CustomUserDetailsDto;
import com.example.dy.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //UserDetailsService @Override 함

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 데이터 베이스 에 있는 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 Email 을 찾을 수 없습니다.: " + email));

        return new CustomUserDetailsDto(user);
    }
}
