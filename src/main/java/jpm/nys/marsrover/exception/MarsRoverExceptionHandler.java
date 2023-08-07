package jpm.nys.marsrover.exception;

import jpm.nys.marsrover.model.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@ResponseBody
public class MarsRoverExceptionHandler {

    @ExceptionHandler(RoverExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiException handleRoverExistsException(Exception exception, WebRequest request) {
        return new ApiException(exception.getMessage());
    }

    @ExceptionHandler(RoverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiException handleRoverNotFoundException(Exception exception, WebRequest request) {
        return new ApiException(exception.getMessage());
    }

    @ExceptionHandler(value = {InvalidCommandRequestException.class, InvalidCreateRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleBadRequestException(Exception exception, WebRequest request) {
        return new ApiException(exception.getMessage());
    }

}
