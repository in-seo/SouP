package Matching.SouP.config.auth.dto;

import Matching.SouP.domain.user.Role;
import Matching.SouP.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Slf4j
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 Map
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String origin;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String origin) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.origin = origin;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        // 여기서 네이버와 카카오 등 구분 (ofNaver, ofKakao)
        if("naver".equals(registrationId)) return ofNaver("id",attributes);
        else if("kakao".equals(registrationId)) return ofKakao("id", attributes);
//        else if("facebook".equals(registrationId)) return ofFaceBook(userNameAttributeName,attributes);
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakao_account.get("email"))
                .picture((String) profile.get("profile_image_url"))
                .origin("Kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .origin("Naver")
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .origin("Google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        log.error("새로운 회원가입자 이름 = {} , 이메일 = {}, 경로 = {} , 가입 시간 = {}",name,email,origin, LocalDateTime.now());
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .origin(origin)
                .role(Role.GUEST) // 기본 권한 GUEST
                .build();
    }

}