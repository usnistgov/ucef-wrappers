	package gov.nist.hla.domain.labview;

public class Response {

	private ResponseType respType;
	private String flattenedContent;
	
	public ResponseType getRespType() {
		return respType;
	}
	public void setRespType(ResponseType respType) {
		this.respType = respType;
	}
	public String getFlattenedContent() {
		return flattenedContent;
	}
	public void setFlattenedContent(String flattenedContent) {
		this.flattenedContent = flattenedContent;
	}
}
