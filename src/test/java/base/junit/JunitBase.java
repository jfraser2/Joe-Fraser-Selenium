package base.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import response.dto.ResponseData;

public abstract class JunitBase
{
	
	// don't create OS specific code get the line separator for NT a "\n"
	protected static final String lineSeparator = System.getProperties().getProperty("line.separator");
	protected static final String defaultServerAndPort = "http://localhost:8080";
	protected static final String defaultSecretKey = "myKey";
	
//	Long futureEpochInSeconds = 1692702201L; // future time
//	Long pastEpochInSeconds = 1492702201L; // past time
	

	protected static final String defaultTokenHeader = "{" + 
										"  \"alg\": \"HS256\"," + 
										"  \"typ\": \"JWT\"" + 
										"}";
	
	protected static final String defaultTokenPayLoadFuture = "{" +
			  							" \"sub\": \"1234567890\"," +
			  							" \"name\": \"John Doe\"," +
			  							" \"iat\": 1516239022," +
			  							" \"refId\": 5," +
			  							" \"exp\": 1692702201" +
			  							"}";
	
	protected static final String defaultTokenPayLoadPast = "{" +
			  							" \"sub\": \"1234567890\"," +
			  							" \"name\": \"John Doe\"," +
			  							" \"iat\": 1516239022," +
			  							" \"refId\": 6," +
			  							" \"exp\": 1492702201" +
			  							"}";

	
	/**
	 * @param serverAndPort
	 * 	Example: http://localhost:8080
	 * @param restApiPath
	 * 	Example: SpringBootMicroServices/register
	 * @param jsonString
	 * 	Example: {"user":{"firstName":"Bob", "lastName":"Jones"}}
	 * @return HttpResponse
	 */
	protected ResponseData postJsonRequest(String serverAndPort, String restApiPath, 
		String jsonString, String aJwtToken)
	{
		
		ResponseData retVar = new ResponseData();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 		
		StringBuilder tempUrl = new StringBuilder("");
		
		if (null != serverAndPort)
		{
			tempUrl.append(serverAndPort);
		}
		else
		{
			tempUrl.append(defaultServerAndPort);
		}
		tempUrl.append("/");
		tempUrl.append(restApiPath);
		
		try {
			HttpPost request = new HttpPost(tempUrl.toString());
			
		    StringEntity param = new StringEntity(jsonString);
		    request.addHeader("Content-Type", "application/json");
		    request.addHeader("Accept", "application/json");
		    if (null != aJwtToken)
		    {
		    	request.addHeader("Authorization", aJwtToken);
		    }
		    request.setEntity(param);
		    CloseableHttpResponse httpResponse = httpClient.execute(request);
		    HttpEntity entityData = httpResponse.getEntity();
			if (null != entityData)
			{
//				long outLength = entityData.getContentLength();
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				
				retVar.setHttpStatusCode(statusCode);
//				System.out.println("output Length is: " + outLength);
				try {
					InputStream outStream = entityData.getContent();
					readInputStream(outStream, retVar.getOutBuffer());
				}
				catch (UnsupportedOperationException | IOException ioe)
				{
//					System.out.println("couldn't display response data. Error: " + ioe.getMessage());
				}
			}
		}
		catch (Exception ex)
		{
		}
		finally
		{
			try {
				httpClient.close();
			}
			catch (IOException ioe)
			{
				
			}
			tempUrl = null;
		}
		
		return retVar;
	}

	protected void readInputStream(InputStream responseData, StringBuilder loadBuffer)
	{
        BufferedReader br = null;
        
        try {
             
            br = new BufferedReader(new InputStreamReader(responseData));
             
            String line = null;
             
            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase("quit")) {
                    break;
                }
                loadBuffer.append(line);
                loadBuffer.append(lineSeparator);
            }
             
        }
        catch (IOException ioe) {
//            System.out.println("Exception while reading input " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException ioe) {
 //               System.out.println("Error while closing stream: " + ioe);
            }
 
        }
        
		return;
	}
	
	protected void defaultHttpResponse(ResponseData response)
	{
		if (response.getOutBuffer().length() == 0 && response.getHttpStatusCode().intValue() == -1)
		{
			System.out.print(lineSeparator);
			System.out.println("No Response. Server is probably not running!!!");
			System.out.print(lineSeparator);
		}
		else
		{
			System.out.print(lineSeparator);
			System.out.println("Status Code is: " + response.getHttpStatusCode());
			System.out.print("Response is: " + response.getOutBuffer().toString());
		}
		
	}
	
	protected void startSwagger(String serverAndPort, String restApiPath)
	{
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
		String serverAndPortToUse = null;
		// The underlying HTTP connection is still held by the response object
		// to allow the response content to be streamed directly from the network socket.
		// In order to ensure correct deallocation of system resources
		// the user MUST call CloseableHttpResponse#close() from a finally clause.
		// Please note that if response content is not fully consumed the underlying
		// connection cannot be safely re-used and will be shut down and discarded
		// by the connection manager. 		
		StringBuilder tempUrl = new StringBuilder("");
		
		if (null != serverAndPort)
		{
			tempUrl.append(serverAndPort);
			serverAndPortToUse = serverAndPort;
		}
		else
		{
			tempUrl.append(defaultServerAndPort);
			serverAndPortToUse = defaultServerAndPort;
		}
		tempUrl.append("/");
		tempUrl.append(restApiPath);
		
		try {
			HttpGet request = new HttpGet(tempUrl.toString());
			
		    request.addHeader("Access-Control-Allow-Origin", serverAndPortToUse);
		    request.addHeader("Origin", serverAndPortToUse);
		    
		    httpClient.execute(request);
		}
		catch (Exception ex)
		{
		}
		finally
		{
			try {
				httpClient.close();
			}
			catch (IOException ioe)
			{
				
			}
			tempUrl = null;
		}
		
		return;
	}
	
	
}
