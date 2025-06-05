package springboot.services.interfaces;

import springboot.entities.UrlShortNameEntity;

public interface EncodeDecode {
	
	public UrlShortNameEntity checkUrlShortNameExists(String urlShortName);
	public boolean checkUrlLongNameExists(String urlLongName);
	
	public String generateUrlShortName(String urlLongName);
	public UrlShortNameEntity persistData(UrlShortNameEntity urlShortNameEntity);
}
