package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.custom.ExecuteException;
import ru.practicum.exception.custom.ParticipationRequestLimitException;
import ru.practicum.exception.custom.PublishedEventUpdateException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentTypeMismatch(MissingServletRequestParameterException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNoSuchElementException(NoSuchElementException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgumentException(IllegalArgumentException e) {
        return new ApiError(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(PSQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePSQLException(PSQLException e) {
        if (e.getSQLState().equals("23505")) {
            return new ApiError(
                    HttpStatus.CONFLICT.toString(),
                    "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_category_name]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement",
                    Collections.singletonList(e.getMessage()),
                    LocalDateTime.now()
            );
        }
        return new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Internal server error.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Cannot delete object because it has related entities.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(PublishedEventUpdateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePublishEventUpdate(PublishedEventUpdateException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ExecuteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePublishEventUpdate(ExecuteException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ParticipationRequestLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handlePublishEventUpdate(ParticipationRequestLimitException e) {
        return new ApiError(
                HttpStatus.CONFLICT.toString(),
                "For the requested operation the conditions are not met.",
                e.getMessage(),
                Collections.singletonList(e.getMessage()),
                LocalDateTime.now()
        );
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
//        log.info(e.getMessage());
//        return new ErrorResponse(e.getMessage());
//    }
//
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleThrowable(final Throwable e) {
//        log.info(e.getMessage());
//        return new ErrorResponse(e.toString());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handleThrowable(final ParticipationRequestLimitException e) {
//        log.info(e.getMessage());
//        return new ErrorResponse(e.toString());
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handleThrowable(final StatusRequestException e) {
//        log.info(e.getMessage());
//        return new ErrorResponse(e.toString());
//    }
}
