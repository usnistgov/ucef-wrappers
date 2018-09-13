package gov.nist.hla;

import java.net.Socket;

/**
 * In place for cross-thread communications
 * @author vagrant
 *
 */
public class Volatiles {
	private volatile Socket socket;
	private volatile Boolean stopThreads;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Boolean getStopThreads() {
		return stopThreads;
	}

	public void setStopThreads(Boolean stopThreads) {
		this.stopThreads = stopThreads;
	}
	
	public void reset() {
		socket = null;
		stopThreads = false;
	}
}
