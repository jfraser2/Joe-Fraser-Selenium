package response.dto;

public class ResponseData {
	Integer httpStatusCode = null;
	StringBuilder outBuffer = null;


	public ResponseData()
	{
		this.httpStatusCode = new Integer(-1);
		this.outBuffer = new StringBuilder("");
	}
	
	public ResponseData(int aStatusCode)
	{
		this.httpStatusCode = new Integer(aStatusCode);
		this.outBuffer = new StringBuilder("");
	}

	public void append(String aString)
	{
		this.outBuffer.append(aString);
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public StringBuilder getOutBuffer() {
		return outBuffer;
	}
	
	public void clearStringBuffer()
	{
		// clear the Buffer
		this.outBuffer.delete(0, this.outBuffer.length());
	}
	
}
