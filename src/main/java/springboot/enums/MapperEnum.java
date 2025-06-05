package springboot.enums;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum MapperEnum {
	INSTANCE;
	private final ObjectMapper mapper = new ObjectMapper();

	private MapperEnum() {
	    // Perform any configuration on the ObjectMapper here.
	}

	public ObjectMapper getObjectMapper() {
		return mapper;
	}
	
}
