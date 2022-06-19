package Matching.SouP;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class Tt {
    @Value("${my.password}")
    private String password;
}
