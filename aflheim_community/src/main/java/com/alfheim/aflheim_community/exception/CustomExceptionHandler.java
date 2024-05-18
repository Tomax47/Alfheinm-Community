package com.alfheim.aflheim_community.exception;

import com.alfheim.aflheim_community.exception.blacklist_record.UserBlacklistActiveRecordException;
import com.alfheim.aflheim_community.exception.password.PasswordActiveRecordExistException;
import com.alfheim.aflheim_community.exception.password.PasswordResetRequestExpiredException;
import com.alfheim.aflheim_community.exception.password.PasswordResetRequestNotFoundException;
import com.alfheim.aflheim_community.exception.profile.AlreadyConfirmedException;
import com.alfheim.aflheim_community.exception.profile.ConfirmationRecordNotFoundException;
import com.alfheim.aflheim_community.exception.profile.RoleAlreadyExistException;
import com.alfheim.aflheim_community.exception.publication.PublicationNotFoundException;
import com.alfheim.aflheim_community.exception.publication.PublicationPageNotFoundException;
import com.alfheim.aflheim_community.exception.server.BadRequestException;
import com.alfheim.aflheim_community.exception.server.InternalServerErrorException;
import com.alfheim.aflheim_community.exception.blacklist_record.UserBlacklistRecordNotFoundException;
import com.alfheim.aflheim_community.exception.stripe.CardTokenGeneratingFailureException;
import com.alfheim.aflheim_community.exception.stripe.ChargeRequestFailureException;
import com.alfheim.aflheim_community.exception.stripe.InvalidChargeAmountException;
import com.alfheim.aflheim_community.exception.user.EmailOrUsernameAlreadyExistException;
import com.alfheim.aflheim_community.exception.user.UserNotFoundException;
import com.alfheim.aflheim_community.exception.user.UserPageNotFoundException;
import com.alfheim.aflheim_community.exception.user.UserUnauthorizedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class CustomExceptionHandler {

    // Publication or publications ain't been found
    @ExceptionHandler(value = PublicationNotFoundException.class)
    public ResponseEntity<Object> handlePublicationNotFoundException(PublicationNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Publication's page ain't been found
    @ExceptionHandler(value = PublicationPageNotFoundException.class)
    public String handlePublicationPageNotFoundException(PublicationPageNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return "redirect:/publications/feed";
    }

    // User ain't been found
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // User's page ain't been found
    @ExceptionHandler(value = UserPageNotFoundException.class)
    public ResponseEntity<Object> handleUserPageNotFoundException(UserPageNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // User unauthorized request
    @ExceptionHandler(value = UserUnauthorizedRequestException.class)
    public ResponseEntity<Object> handleUserUnauthorizedRequestException(UserUnauthorizedRequestException e) {

        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // 500
    @ExceptionHandler(value = InternalServerErrorException.class)
    public ResponseEntity<Object> handleServerInternalErrorException(InternalServerErrorException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // 400
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Blacklist record not found
    @ExceptionHandler(value = UserBlacklistRecordNotFoundException.class)
    public ResponseEntity<Object> handleUserBlacklistRecordNotFoundException(UserBlacklistRecordNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Blacklist report already exist
    @ExceptionHandler(value = UserBlacklistActiveRecordException.class)
    public ResponseEntity<Object> handleUserBlacklistActiveRecordException(UserBlacklistActiveRecordException e) {
        // 409 Already existing active record
        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Email confirmation record not found
    @ExceptionHandler(value = ConfirmationRecordNotFoundException.class)
    public String handleEmailConfirmationRecordNotFoundException(ConfirmationRecordNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return "redirect:/login?error="+exception.getMessage();
    }

    // Account is already confirmed
    @ExceptionHandler(value = AlreadyConfirmedException.class)
    public ResponseEntity<Object> handleAccountAlreadyConfirmedException(AlreadyConfirmedException e) {
        // 409 Already existing active record
        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Role already exist to user
    @ExceptionHandler(value = RoleAlreadyExistException.class)
    public ResponseEntity<Object> handleRoleAlreadyExistExceptionException(RoleAlreadyExistException e) {
        // 409 Already existing role
        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Invalid charge amount
    @ExceptionHandler(value = InvalidChargeAmountException.class)
    public ResponseEntity<Object> handleInvalidChargeAmountException(InvalidChargeAmountException e) {
        // 400. Invalid charge amount
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Card token generating error
    @ExceptionHandler(value = CardTokenGeneratingFailureException.class)
    public ResponseEntity<Object> handleCardTokenGeneratingFailureException(CardTokenGeneratingFailureException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Payment charge req error
    @ExceptionHandler(value = ChargeRequestFailureException.class)
    public ResponseEntity<Object> handleChargeRequestFailureExceptionException(ChargeRequestFailureException e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Email or username exist already
    @ExceptionHandler(value = EmailOrUsernameAlreadyExistException.class)
    public ResponseEntity<Object> handleEmailOrUsernameAlreadyExistException(EmailOrUsernameAlreadyExistException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Already exist active password reset record
    @ExceptionHandler(value = PasswordActiveRecordExistException.class)
    public ResponseEntity<Object> handlePasswordActiveRecordExistException(PasswordActiveRecordExistException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    // Password reset request not found
    @ExceptionHandler(value = PasswordResetRequestNotFoundException.class)
    public ResponseEntity<Object> handlePasswordResetRequestNotFoundException(PasswordResetRequestNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }

    @ExceptionHandler(value = PasswordResetRequestExpiredException.class)
    public ResponseEntity<Object> handlePasswordResetRequestExpiredException(PasswordResetRequestExpiredException e) {
        // 410, not anymore available 'record has expired'
        HttpStatus httpStatus = HttpStatus.GONE;;
        ExceptionPayload exception = new ExceptionPayload(
                e.getMessage(),
                e,
                httpStatus,
                ZonedDateTime.now(ZoneId.systemDefault())
        );

        return new ResponseEntity<>(exception, httpStatus);
    }
}
