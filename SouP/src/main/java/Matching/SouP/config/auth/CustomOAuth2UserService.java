package Matching.SouP.config.auth;

import Matching.SouP.config.auth.dto.CoveringUser;
import Matching.SouP.config.auth.dto.OAuthAttributes;
import Matching.SouP.config.auth.dto.SessionUser;
import Matching.SouP.domain.user.User;
import Matching.SouP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        // OAuth2 서비스 id (구글, 카카오, 네이버)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // OAuth2 로그인 진행 시 키가 되는 필드 값(PK)  ex)구글의 기본 코드는 sub
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // SessionUser (직렬화된 dto 클래스 사용하되, 커버링 인덱스를 사용하기 위해 id, email만 이용한다.)

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    // 유저 생성 및 수정 서비스 로직
    private synchronized User saveOrUpdate(OAuthAttributes attributes){
        Optional<CoveringUser> optionalUser = userRepository.findByEmailWithIndex(attributes.getEmail()); // id, email, role만 가져오는 커버링 인덱스!
        if(optionalUser.isPresent())
            return new User(optionalUser.get());

        return userRepository.save(attributes.toEntity());
    }
}