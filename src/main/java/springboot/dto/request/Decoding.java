package springboot.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Decoding {
	@NotBlank(message = "Url to Decode must not be blank")
	@Pattern(regexp = "^(http://).+|(HTTP://).+", message="Url to Decode must start with http://")
	private String urlShortName;
    
    public Decoding()
    {
    }
    
	public Decoding(String urlShortName)
	{
		this.urlShortName = urlShortName;
	}


	public String getUrlShortName() {
		return urlShortName;
	}

	public void setUrlShortName(String urlShortName) {
		this.urlShortName = urlShortName;
	}
    
}

