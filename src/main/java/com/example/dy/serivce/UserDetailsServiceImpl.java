package com.example.dy.serivce;  // 이 클래스가 com.example.dy.service 패키지에 속한다는 것을 선언합니다.

// 필요한 클래스나 인터페이스를 임포트합니다.
// 사용자 정보를 저장하고 처리하는 데 필요한 엔티티, 인터페이스, 예외 클래스, 어노테이션 등을 임포트하고 있습니다.
import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


// 사용자 인증 처리를 담당하는 부분입니다. 구체적인 기능은 다음과 같습니다.


@Service  // @Service 어노테이션은 이 클래스가 스프링의 서비스 계층 컴포넌트임을 선언합니다.
public class UserDetailsServiceImpl implements UserDetailsService {  // UserDetailsService 인터페이스를 구현하는 클래스를 정의합니다.

    private UserRepository userRepository;  // UserRepository 타입의 변수를 선언합니다. 이 변수를 통해 사용자 정보를 DB에서 조회할 수 있습니다.

    @Autowired  // userRepository 변수에 자동으로 의존성을 주입합니다.
    public UserDetailsServiceImpl(UserRepository userRepository) {  // 생성자를 정의하고, 이를 통해 UserRepository를 주입받습니다.
        this.userRepository = userRepository;  // 주입받은 UserRepository를 클래스 변수에 할당합니다.
    }



    @Override  // 부모 인터페이스의 메서드를 오버라이드합니다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {  // 사용자 이름을 인자로 받아 UserDetails를 반환하는 메서드를 정의합니다.
        User user = userRepository.findByUsername(username)  // UserRepository를 사용해 DB에서 사용자를 조회합니다.
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));  // 사용자를 찾을 수 없으면 예외를 던집니다.


        return new UserPrincipal(user);  // 조회한 사용자 정보를 UserPrincipal로 래핑해 반환합니다.

        // UserPrincipal 클래스는 UserDetails 인터페이스를 구현하며, 인증에 필요한 사용자 정보(아이디, 비밀번호, 권한 정보 등)를 가집니다.
    }
}


