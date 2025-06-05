package springboot.controllers.rest;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import springboot.autowire.helpers.ConcurrentRequestLimit;
//import io.swagger.annotations.ApiImplicitParam;
import springboot.autowire.helpers.StringBuilderContainer;
import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.dto.request.Encoding;
import springboot.dto.request.Decoding;
import springboot.dto.response.EncodingOperation;
import springboot.dto.response.DecodingOperation;
import springboot.dto.validation.exceptions.RequestValidationException;
import springboot.entities.UrlShortNameEntity;
import springboot.errorHandling.helpers.ApiValidationError;
import springboot.services.interfaces.EncodeDecode;
import springboot.services.interfaces.RequestValidation;

@RestController
@RequestMapping(path="/rest/api")
public class EncodeDecodeController
	extends ControllerBase
{
	@Autowired
	@Qualifier("concurrentRequestLimit")
	private ConcurrentRequestLimit concurrentRequestLimit;
	
	@Autowired
	private EncodeDecode encodeDecodeService;
	
	@Autowired
	private RequestValidation<Encoding> encodingValidation;
	
	@Autowired
	private RequestValidation<Decoding> decodingValidation;
	
	@Autowired
	@Qualifier("requestValidationErrorsContainer")
	private ValidationErrorContainer requestValidationErrorsContainer;
	
	@Autowired
	@Qualifier("requestStringBuilderContainer")
	private StringBuilderContainer requestStringBuilderContainer;
	
	@RequestMapping(method = {RequestMethod.POST},
			path = "/v1/encode",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> encode(@RequestBody Encoding data, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		
		if (concurrentRequestLimit.atLimit()) {
			throw new AccessDeniedException("Concurrent Request Limit has been reached. Try again later");
		} else {
			concurrentRequestLimit.increment();
		}
		
		encodingValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			concurrentRequestLimit.decrement();
			throw new RequestValidationException(errorList);
		}
		
		boolean encodingAlreadyDone = encodeDecodeService.checkUrlLongNameExists(data.getUrlLongName());
		if(encodingAlreadyDone) {
			concurrentRequestLimit.decrement();
			throw new IllegalArgumentException("You have already shortened this Url.");
		}

		UrlShortNameEntity usne = new UrlShortNameEntity();
		usne.setUrlLongName(data.getUrlLongName());
		usne.setUrlShortName(encodeDecodeService.generateUrlShortName(data.getUrlLongName()));
		UrlShortNameEntity savedEntity = encodeDecodeService.persistData(usne);
		
		String jsonString = goodResponse(savedEntity, requestStringBuilderContainer);
		usne = null;
		savedEntity = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		concurrentRequestLimit.decrement();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
	@RequestMapping(method = {RequestMethod.GET},
			path = "/v1/decode",
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> decode(@RequestParam(required = true) String shortenedUrl, HttpServletRequest request)
		throws RequestValidationException, IllegalArgumentException, AccessDeniedException
	{
		if (concurrentRequestLimit.atLimit()) {
			throw new AccessDeniedException("Concurrent Request Limit has been reached. Try again later");
		} else {
			concurrentRequestLimit.increment();
		}
		
		Decoding data = new Decoding(shortenedUrl);
		decodingValidation.validateRequest(data, requestValidationErrorsContainer, null);
		List<ApiValidationError> errorList = requestValidationErrorsContainer.getValidationErrorList();
		
		if (errorList.size() > 0)
		{
//			System.out.println("Right before the throw");
			concurrentRequestLimit.decrement();
			throw new RequestValidationException(errorList);
		}
		
		UrlShortNameEntity record = encodeDecodeService.checkUrlShortNameExists(data.getUrlShortName());
		if(null == record) {
			concurrentRequestLimit.decrement();
			throw new IllegalArgumentException("This shortened url does not exist.");
		}
		
		String jsonString = goodResponse(record, requestStringBuilderContainer);
		record = null;
		
		// support CORS
		HttpHeaders aResponseHeader = createResponseHeader();
		concurrentRequestLimit.decrement();
		
		return new ResponseEntity<>(jsonString, aResponseHeader, HttpStatus.OK);
	}
	
}
