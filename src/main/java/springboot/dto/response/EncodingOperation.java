package springboot.dto.response;

public class EncodingOperation {
	
	private String message;
	
	public EncodingOperation(String aMessage)
	{
		this.message = aMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
