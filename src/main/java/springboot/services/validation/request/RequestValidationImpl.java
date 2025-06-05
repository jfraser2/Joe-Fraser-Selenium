package springboot.services.validation.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import springboot.autowire.helpers.ValidationErrorContainer;
import springboot.services.interfaces.RequestValidation;
import springboot.services.validation.request.interfaces.functional.ValidateRequestLogic;

// uses java generics
@Service
@Scope("prototype")
public class RequestValidationImpl<RequestType>
	implements RequestValidation<RequestType>
{
	
	@Autowired
	private RequestValidationDefaultMethods<RequestType> requestValidationDefaultMethods;
	
	public RequestValidationImpl()
	{
	}
	
	public void validateRequest(RequestType aRequest, ValidationErrorContainer aListContainer, ValidateRequestLogic<RequestType> overRide)
	{
		ValidateRequestLogic<RequestType> methodToExecute = null;
		if (null != overRide)
		{
			methodToExecute = overRide;
		}
		else
		{
			methodToExecute = this.requestValidationDefaultMethods.getDefaultValidateRequest();
		}
		
		methodToExecute.validateRequest(aRequest, aListContainer);
		
		return;
	}
	
}
