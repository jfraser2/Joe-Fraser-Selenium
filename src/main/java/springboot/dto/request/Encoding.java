package springboot.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Encoding {
	@NotBlank(message = "Url to Encode must not be blank")
	@Pattern(regexp = "^(https://).+|(HTTPS://).+", message="Url to Encode must start with https://")
	private String urlLongName;

	public Encoding()
	{
	}
	
	public Encoding(String urlLongName)
	{
		this.urlLongName = urlLongName;
	}


	public String getUrlLongName() {
		return urlLongName;
	}

	public void setUrlLongName(String urlLongName) {
		this.urlLongName = urlLongName;
	}
	
}
