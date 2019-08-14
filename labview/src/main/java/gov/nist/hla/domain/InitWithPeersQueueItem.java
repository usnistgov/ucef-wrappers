package gov.nist.hla.domain;

public class InitWithPeersQueueItem {

	private MethodAction action;
	private String msg;
	
	public MethodAction getAction() {
		return action;
	}
	public void setAction(MethodAction action) {
		this.action = action;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
