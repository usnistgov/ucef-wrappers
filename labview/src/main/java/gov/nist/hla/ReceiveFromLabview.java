package gov.nist.hla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.BlockingDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsible for acting as a TCP client, forming a connection with the LabVIEW based TCP server for
 * receiving responses back form the LabVIEW side of this combined LabVIEW federate.  Once the thread executes,
 * it will attempt to establish a socket connectiion with the LabVIEW TCP server at a known IP address and port.
 * If it fails to establish that connection, it will continue to do so until either that connection has been
 * established or the application has been aborted.
 * 
 * Once the connection is established, it will continuously read lines of translated ASCII responses and queue
 * them up to be independently processed in the Controller.
 * 
 * @author vagrant
 *
 */
public class ReceiveFromLabview implements Runnable{
	
	private static final Logger log = LogManager.getLogger();
	
	private volatile boolean stopMe = false;
	private BlockingDeque<String> queue;
	private String lvHostIp;
	private Integer incomingPort;
	private static final Integer SOCKET_RETRY_SEC = 2;
    
    public ReceiveFromLabview(BlockingDeque<String> q, String lvHostIp, Integer incomingPort) {
    	this.queue = q;
    	this.lvHostIp = lvHostIp;
    	this.incomingPort = incomingPort;
    }
    
    public void stopThread() {
    	stopMe = true;
    }
    
    @Override
    public void run() {
    	
    	log.info("initiating LabVIEW Message Receiver...");
    	
    	// run until outside sources trigger a "stopMe" on the thread resulting in proper closure
    	while (!stopMe) {
	    	
	    	InputStreamReader in = null;
	    	Socket socket = null;
	    	Boolean socketReady = false;
			try {
				// ensure the TCP server on the LabVIEW end is up and running, waiting for this connection
				// before proceeding.
				while (!socketReady) {
					if (stopMe) 
						break;
					try {
						socket = new Socket(lvHostIp, incomingPort);
						socketReady = true;
						log.info("socket connection for LabVIEW Message Receiver established without error");
					} catch (IOException ex) {
						log.warn("Socket connection for LabVIEW Message Receiver failed, it probably still needs to come online.  Retrying in " +SOCKET_RETRY_SEC +" seconds.");
						try {
							Thread.sleep(SOCKET_RETRY_SEC * 1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				if (!stopMe)
					in = new InputStreamReader(socket.getInputStream());
			} catch (IOException ex) {
				log.error("IOException triggered while establishing an InputStreamReader (receiver): " +ex.getMessage());
			}
			
			try {
		    	while (!stopMe) {
						
					BufferedReader br = new BufferedReader(in);
					String theLine;
					
					if ((theLine = br.readLine()) != null) {
						log.info("message received, appending it to the queue for further processing: " +theLine);
						queue.offer(theLine);
					} else {
						//log.debug("message received is NULL, break out and attempt a reconnection");
						break;
					}
					
					// TODO may want to wait for a certain number of consecutive NULL readings before assuming
					// lost connection.
				}
		    	
			} catch (InterruptedIOException ex) {
				log.error("Timed out at " +new Date() +" waiting on LabVIEW server to send out messages.  Retrying a fresh connection...\n");
			} catch (IOException ex) {
				log.error("Exception while waiting for a pulse from the LabVIEW server to send out messages.  Retrying a fresh connection...\n");
			} catch (Exception ex) {
				log.error("Exception while waiting for a pulse from the LabVIEW server to send out messages.  Retrying a fresh connection...\n");
			}
    	}
    	
    	log.info("terminating LabVIEW Message Receiver...");
    }
}
