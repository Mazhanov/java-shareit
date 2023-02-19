package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.OtherUserException;
import ru.practicum.shareit.user.EmailDuplicateException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException exception) {
        log.info("Exception ObjectNotFoundException {}", exception.getMessage(), exception);
        return new ErrorResponse(
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailDuplicateException(final EmailDuplicateException exception) {
        log.info("Exception EmailDuplicateException {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleOtherUserException(final OtherUserException exception) {
        log.info("Exception OtherUserException {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception exception) {
        log.info("500 {}", exception.getMessage(), exception);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(out));
        return new ErrorResponse(out.toString(StandardCharsets.UTF_8));
    }*/
}
