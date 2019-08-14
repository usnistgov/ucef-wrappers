package gov.nist.hla;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Responsible for transmitting messages to the LabVIEW client component of this LabVIEW federate.  When the thread executes,
 * a socket connection is first established with the client.  It will continue to wait until the connection is made or the
 * entire process is aborted.  Once a client has successfully connected to the socket (of known port) the outgoing message queue
 * is constantly monitored for any processed messages in the Controller and immedately sent over the wire.
 * 
 * @author vagrant
 *
 */
public class TransmitToLabview implements Runnable{
	
	private static final Logger log = LogManager.getLogger();
	
	private BlockingDeque<String> queue;
	private ServerSocket serverSocket;
	private Socket socket;
	private Boolean connected = false;
	private Volatiles volatiles;
	private volatile boolean stopMe = false;
	private Integer outgoingPort;
	private static final Integer SOCKET_TIMEOUT_SEC = 5;
    
    public TransmitToLabview(BlockingDeque<String> q, Volatiles volatiles, Integer outgoingPort) {
        this.queue=q;
        this.volatiles = volatiles;
        this.outgoingPort = outgoingPort;
    }
    
    public void stopThread() {
    	stopMe = true;
    }
    
    @Override
    public void run() {
    	
    	log.info("initiating LabVIEW Message Transmitter...");
	    
    	// Create a new ServerSocket based on port and assign a timeout
    	try {
			log.debug("Establishing a server socket...");
			serverSocket = new ServerSocket(outgoingPort);
			serverSocket.setSoTimeout(SOCKET_TIMEOUT_SEC * 1000);
			
		} catch (IOException ex) {
			log.error("Exception while attempting a socket connection: " +ex.getMessage() +"\n");
			ex.printStackTrace();
		} catch (Exception ex) {
			log.error("Exception while attempting a socket connection\n");
			ex.printStackTrace();
		}
    	
    	// Wait for a socket to be established by waiting on the client to make the connection.
    	// If the client lags behind (or the client simply isn't started for some time) a
    	// timeout occurs and exception is handled accordingly.  Repeated attempts are made
    	// until the connection is made.
    	while (!connected) {
			
    		if (stopMe) 
				break;
    		
    		try {
				
				// waits until connection has been made or until setSoTimeout has been reached
				log.info("Waiting on a LabVIEW client to establish connection for Message Transmission...");
				socket = serverSocket.accept();
				volatiles.setSocket(socket);
				
				// set flag to move on
				connected = true;
				log.info("LabVIEW client is connected!\n");
				
			} catch (IOException ex) {
				log.warn("A LabVIEW client failed to connect in the timeout period for Message Transmission.  retrying in " +SOCKET_TIMEOUT_SEC +" seconds.");
			}
		}
    	
		try {
			
			PrintWriter out = null;
			
			if (!stopMe)
			out = new PrintWriter(socket.getOutputStream(), true);
			
			Integer numSent = 0;
			while (!stopMe) {
				
				// check for a non-empty queue.  process if not empty, pause and check again if it is
				if (!queue.isEmpty()) {
					String msg = queue.poll();
					out.print(msg +"\r\n");
					out.flush();
					numSent++;
				} else {
					// if the queue is empty, pause for 50 ms before checking again
					Thread.sleep(50);
				}
			}
			
			// if stopping, close everything out
			log.info("Outside command was received to halt further message transmissions to LabVIEW client");
			if (out != null)
				out.close();
			if (socket != null)
				socket.close();
			if (serverSocket != null)
				serverSocket.close();
		} catch (Exception ex) {
			log.error("Nasty exception took place while polling queue and sending messages: " +ex.getMessage());
		}
		
		log.info("terminating LabVIEW Message Transmitter...");
    }
}
