package springboot.controllers.rest;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import springboot.autowire.helpers.StringBuilderContainer;
import springboot.enums.MapperEnum;

public abstract class ControllerBase
{
	protected static final String GODD_RESPONSE_PREFIX = "{\"status\":\"OK\", \"modelData\":";
	protected static final String GODD_RESPONSE_SUFFIX = "}";
	
	private String convertToJson(Object anObject)
	{
		String jsonString = null;
		
		try {
			if (null != anObject)
			{
				ObjectMapper mapper = MapperEnum.INSTANCE.getObjectMapper();				
				ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
				
				jsonString = ow.writeValueAsString(anObject);
			}
		}
		catch(JsonProcessingException jpe)
		{
			jsonString = null;
		}
		
		return jsonString;
	}
	
	protected String goodResponse(Object anObject, StringBuilderContainer aContainer)
	{
		String jsonString = convertToJson(anObject);
		
		// Since it is Autowired clear the buffer before you use it
		aContainer.clearStringBuffer();
		StringBuilder aBuilder = aContainer.getStringBuilder();
		
		aBuilder.append(GODD_RESPONSE_PREFIX);
		aBuilder.append(jsonString);
		aBuilder.append(GODD_RESPONSE_SUFFIX);
		
		return aBuilder.toString();
	}

	protected Method getMethodOfClass(Class<?> aClass, String methodName)
	{
		Method retVar = null;
		
		if (null != aClass && null != methodName)
		{
			Method [] classMethods = aClass.getMethods();
			
			for (Method method : classMethods)
			{
				if (methodName.equals(method.getName()))
				{
					retVar = method;
					break;
				}
			}
		}
		
		return retVar;
		
	}
	
	protected HttpHeaders createResponseHeader(HttpServletRequest request)
	{
		// support CORS
		HttpHeaders aResponseHeader = new HttpHeaders();
		aResponseHeader.add("Access-Control-Allow-Origin", request.getHeader("Origin"));
//		aResponseHeader.add("Access-Control-Allow-Origin", "*");
		aResponseHeader.add("Content-Type", "application/json");
		
		return aResponseHeader;
		
	}
	
}
