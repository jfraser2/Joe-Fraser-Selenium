package springboot.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import springboot.entities.UrlShortNameEntity;
import springboot.repositories.UrlShortNameRepository;
import springboot.services.interfaces.EncodeDecode;

@Service
public class EncodeDecodeImpl
	implements EncodeDecode
{
	private static final String LONG_URL_PREFIX = "https://";
	private static final String SHORT_URL_PREFIX = "http://";
	private static final int SHORT_URL_SIZE = 10;
	private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	@Autowired
	private UrlShortNameRepository urlShortNameRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public UrlShortNameEntity checkUrlShortNameExists(String urlShortName) {
		UrlShortNameEntity retVar = null;
		
		UrlShortNameEntity usne = urlShortNameRepository.findByUrlShortName(urlShortName);
		if (null != usne)
			retVar = usne;
		
		return retVar;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean checkUrlLongNameExists(String urlLongName) {
		boolean retVar = false;
		// TODO Auto-generated method stub
		UrlShortNameEntity usne = urlShortNameRepository.findByUrlLongName(urlLongName);
		if (null != usne)
			retVar = true;
		
		return retVar;
	}

    private String encodeBase62(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
          int value = (b & 0xFF);
          sb.append(BASE62.charAt(value % 62));
        }
        
        return sb.toString();
    }
    
	@Override
	public String generateUrlShortName(String urlLongName) {
		String retVar = null;
		
        try {
        	String strippedLongName = urlLongName.substring(LONG_URL_PREFIX.length() - 1, urlLongName.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(strippedLongName.getBytes(StandardCharsets.UTF_8));
            String base62Encoded = encodeBase62(hashBytes);
            
            String shortUrl = null;
            if (base62Encoded.length() >= SHORT_URL_SIZE) {
            	int beginIdx = base62Encoded.length() - SHORT_URL_SIZE;
            	shortUrl = base62Encoded.substring(beginIdx); // last 10 characters
            } else {
            	shortUrl = base62Encoded;
            }
            retVar = SHORT_URL_PREFIX + shortUrl;
        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
            retVar = null;
        } 
        
		return retVar;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public UrlShortNameEntity persistData(UrlShortNameEntity urlShortNameEntity) {
		
		UrlShortNameEntity retVar = null;
		
		try {
			retVar = urlShortNameRepository.save(urlShortNameEntity);
		} catch (Exception e) {
			retVar = null;
		}
		
		return retVar;
	}

	

}
