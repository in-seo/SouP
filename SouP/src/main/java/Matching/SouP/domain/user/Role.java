package Matching.SouP.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "회원"), ADMIN("ROLE_ADMIN","관리자");
    private final String key;
    private final String title;
}
