package springboot.services.validation.request;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import springboot.services.validation.request.interfaces.functional.ValidateRequestLogic;

@Service
@Scope("prototype")
public class RequestValidationDefaultMethods<RequestType>
{
	private final ValidateRequestLogic<RequestType> defaultValidateRequest = (aRequest, aListContainer) ->
	{
		// Since it is AutoWired clear the List before you use it
		aListContainer.clearValidationErrors();

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<RequestType>> violations = validator.validate(aRequest);
		Path propertyPath;
		String fieldName = "";
		String objectName = aRequest.getClass().getSimpleName();
		String message;
		Object invalidValue;
		
		for(ConstraintViolation<RequestType> aViolation : violations)
		{
			invalidValue = aViolation.getInvalidValue();
			message = aViolation.getMessage();
			propertyPath = aViolation.getPropertyPath();
			for (Path.Node aNode : propertyPath)
			{
				fieldName = aNode.getName();
			}
			aListContainer.addToList(objectName, fieldName, invalidValue, message);
		}
		
		return;
	};
	
	RequestValidationDefaultMethods()
	{
		
	}
	
	public ValidateRequestLogic<RequestType> getDefaultValidateRequest()
	{
		return this.defaultValidateRequest;
	}

}
