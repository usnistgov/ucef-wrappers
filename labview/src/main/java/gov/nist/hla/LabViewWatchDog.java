package gov.nist.hla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Responsible for monitoring the established socket connection in TransmitToLabview to ensure
 * the connection is still kept alive.  Unfortunately there is no real easy way to make this determination
 * once a socket is established (in the case of TransmitToLabview).  Therefore, using the same socket, a simple
 * yet dedicated message transmission scheme is established between the LabVIEW side of this federate and this (the
 * Java side) to actively monitor whether a connection is still truly active and open. So, while the TransmitToLabview
 * class/thread is responsible for sending messages to the LabVIEW client, this class/thread shares the same
 * socket, but instead reads back a "connectivity pulse" response.  Should the response ever go null, connectivity is 
 * assumed lost and the watchdog notifies any dependents to react accordingly.
 * 
 * @author vagrant
 *
 */
public class LabViewWatchDog implements Runnable{
	
	private static final Logger log = LogManager.getLogger();
	
	private Volatiles volatiles;
	private volatile boolean stopMe = false;
	private static final Integer SOCKET_RETRY_SEC = 2;
    
    public LabViewWatchDog(Volatiles volatiles) {
        this.volatiles = volatiles;
    }
    
    public void stopThread() {
    	stopMe = true;
    }
    
    @Override
    public void run() {
    	
    	log.info("initiating LabVIEW WatchDog...");
    	
    	// Wait for a socket to be established by waiting on the client to make the connection.
    	// If the client lags behind (or the client simply isn't started for some time) a
    	// timeout occurs and exception is handled accordingly.  Repeated attempts are made
    	// until the connection is made.
    	
    	InputStreamReader in = null;
    	Boolean socketReady = false;
		try {
			
			// to avoid a race condition, be sure the socket itself is established
			// (see TransmitToLabview) before attempting to monitor a non existing
			// connection...
			while (!socketReady) {
				if (stopMe) 
					break;
				if (volatiles.getSocket() != null) {
					socketReady = true;
				} else {
					try {
						log.info("LabVIEW WatchDog waiting for a valid socket to read from.  Trying again in " +SOCKET_RETRY_SEC +" seconds...");
						Thread.sleep(SOCKET_RETRY_SEC * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (!stopMe)
				in = new InputStreamReader(volatiles.getSocket().getInputStream());
		} catch (IOException ex) {
			log.error("IOException triggered while establishing an InputStreamReader in LabVIEW WatchDog: " +ex.getMessage());
		}
		try {
	    	while (!stopMe) {
					
				BufferedReader br = new BufferedReader(in);
				String theLine;
				
				if ((theLine = br.readLine()) != null) {
					log.info("watchdog read back: " +theLine);
				} else {
					log.info("watchdog read back is NULL, breaking and restarting");
					break;
				}
				
				//TODO determine how many consecutive NULLs there have been and determined
				//whether loop needs to kick out allowing for a stop-all threads to be initiated
			}
	    	
	    	log.info("WatchDog thread was just issued an external stop command");
		} catch (InterruptedIOException ex) {
			log.error("Timed out at " +new Date() +" waiting on LabVIEW client connection for WatchDog, trying again...\n");
		} catch (IOException ex) {
			log.error("Exception while waiting for a pulse from the LabVIEW client to make a connection for WatchDog.\n");
		} catch (Exception ex) {
			log.error("Exception while waiting for a pulse from the LabVIEW client to make a connection for WatchDog.\n");
		}
		
		// if this watchdog kicks out of its monitoring loop for any reason, then that's 
		// just cause to issue a stop-all on pertinent threads and attempt reconnection
		// note this is the only class responsible for making this call.
		log.info("Issuing a stop-all on pertinent threads");
		volatiles.setStopThreads(true);
		
		log.info("terminating LabVIEW WatchDog...");
    }
}
