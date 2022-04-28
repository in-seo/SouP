package Matching.SouP.exception;

import java.util.NoSuchElementException;

public class NoUserException extends NoSuchElementException {
    private static final String MESSAGE = "존재하지 않는 회원입니다. 회원가입 후 요청 해주세요";
    public NoUserException(){
        super(MESSAGE);
    }
}
