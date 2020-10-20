package pl.sokolak87.MyBooks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason = "User already exists")
public class DuplicateUserException extends RuntimeException {
}
