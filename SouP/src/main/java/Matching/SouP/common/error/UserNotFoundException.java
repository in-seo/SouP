package Matching.SouP.common.error;

public class UserNotFoundException extends RuntimeException {
    public static String USER_NOT_FOUND ="존재하지 않는 유저를 조회하고 있습니다.";
    public static String USER_NOT_LOGIN ="로그인이 필요한 작업입니다.";
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {return this;}
}
