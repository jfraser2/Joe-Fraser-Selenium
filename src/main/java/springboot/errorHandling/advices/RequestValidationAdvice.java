package springboot.errorHandling.advices;

import java.nio.file.AccessDeniedException;

//import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.enums.MapperEnum;
import springboot.errorHandling.helpers.ApiError;

/*
	One thing to keep in mind here is to match the exceptions declared with @ExceptionHandler with the exception used as the argument of the method.
	If these don’t match, the compiler will not complain – no reason it should, and Spring will not complain either.

	However, when the exception is actually thrown at runtime, the exception resolving mechanism will fail with:

	1 java.lang.IllegalStateException: No suitable resolver for argument [0] [type=...]
	2 HandlerMethod details: ...

*/

//  Advice execution precedence
//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RequestValidationAdvice
	extends  ResponseEntityExceptionHandler 
{
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
	{
		ApiError apiError = new ApiError();
		
		String error = "Malformed JSON request";
		
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		apiError.setMessage(error);
		apiError.setDebugMessage(ex.getLocalizedMessage());
		
		String json = convertApiErrorToJson(apiError);
		apiError = null;
		
	    return buildResponseEntity(json, HttpStatus.OK);
	}

	//other exception handlers or handler overrides below
	
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
    		AccessDeniedException ex, WebRequest request)
    {
		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.FORBIDDEN);
    	
 		String error = ex.getMessage();
        apiError.setMessage(error);
        
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.OK);
    }
	
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
    		IllegalArgumentException ex)
    {
		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		
        apiError.setMessage(ex.getMessage());
        
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.OK);
    }
    
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Object> handleRequestValidationException(
    	RequestValidationException ex)
    {
		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		
		String error = "Validation errors";
        apiError.setMessage(error);
        apiError.setSubErrors(ex.getSubErrorList());
   
		String json = convertApiErrorToJson(apiError);
		apiError = null;
        
        return buildResponseEntity(json, HttpStatus.OK);
    }
	 
	private ResponseEntity<Object> buildResponseEntity(String json, HttpStatus aStatus)
	{
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		
		return new ResponseEntity<>(json, aResponseHeader, aStatus);
	}
	
	private String convertApiErrorToJson(ApiError apiError)
	{
		String json = null;
		try {
			ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
			
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			json = ow.writeValueAsString(apiError);
		}
		catch(JsonProcessingException jpe)
		{
			json = null;
		}
		
		return json;
	}
	
	private HttpHeaders createResponseHeader()
	{
		// support CORS
		HttpHeaders aResponseHeader = new HttpHeaders();
//		aResponseHeader.add("Access-Control-Allow-Origin", request.getHeader("Access-Control-Allow-Origin"));
//		aResponseHeader.add("Access-Control-Allow-Origin", "*");
		aResponseHeader.add("Content-Type", "application/json");
		
		return aResponseHeader;
		
	}
	

}
