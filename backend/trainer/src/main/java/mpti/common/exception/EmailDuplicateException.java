package mpti.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException(String email) {
        super("This email already exist : " + email);
    }
}
