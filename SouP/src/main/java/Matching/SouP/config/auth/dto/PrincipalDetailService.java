package Matching.SouP.config.auth.dto;

import Matching.SouP.common.error.UserNotFoundException;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service  //Bean 등록
@RequiredArgsConstructor
@Transactional
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 스프링이 로그인 요청을 가로챌 때, username, passwrod 변수 2개를 가로채는데
     * password 부분처리는 알아서 함.
     * username (email) 이 DB 에 있는지만 확인해주면 됨. return
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CoveringUser coveringUser = userRepository.findByEmailWithIndex(email)
                .orElseThrow(UserNotFoundException::new);

        UserDetails result = org.springframework.security.core.userdetails.User.builder()
                .username(coveringUser.getEmail())
                .password(String.valueOf(coveringUser.getUser_id()))
                .roles(coveringUser.getRole())
                .build();

        return result;
    }

}