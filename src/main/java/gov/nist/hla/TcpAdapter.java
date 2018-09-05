package gov.nist.hla;

import java.util.concurrent.BlockingDeque;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Serves as the main thread delegator and controller.  If an outside call is made to either start or stop
 * all threads, it is managed through this manager.  If the LabVIEW watch dog detects a disconnect between
 * the LabVIEW code and the java side of this federate, that message is also conveyed here, where attempts
 * to reconnect take place.
 * 
 * @author vagrant
 *
 */
public class TcpAdapter implements Runnable{
	
	private static final Logger log = LogManager.getLogger();
	
	private BlockingDeque<String> incomingQueue;
	private BlockingDeque<String> outgoingQueue;
	private String lvHostIp;
	private Integer incomingPort;
	private Integer outgoingPort;
	private volatile boolean stopAll = false;
    
    public TcpAdapter(BlockingDeque<String> incoming, BlockingDeque<String> outgoing, String lvHostIp, Integer incomingPort, Integer outgoingPort) {
        this.incomingQueue = incoming;
        this.outgoingQueue = outgoing;
        this.lvHostIp = lvHostIp;
        this.incomingPort = incomingPort;
        this.outgoingPort = outgoingPort;
    }
    
    public void stopAllThreads() {
    	stopAll = true;
    }
    
    @Override
    public void run() {
    	
    	log.info("initiating TCP Adapter...");
    	
    	Volatiles volatiles = new Volatiles();
    	
    	TransmitToLabview msgTransmitter = null;
    	ReceiveFromLabview msgReceiver = null;
    	LabViewWatchDog watchDog = null;
    	
    	Thread msgTransmitterThread = null;
    	Thread msgReceiverThread = null;
    	Thread watchDogThread = null;
    	
    	while (!stopAll) {
			volatiles.reset();
			
			msgTransmitter = new TransmitToLabview(outgoingQueue, volatiles, outgoingPort);
			msgReceiver = new ReceiveFromLabview(incomingQueue, lvHostIp, incomingPort);
			watchDog = new LabViewWatchDog(volatiles);
			
			msgTransmitterThread = new Thread(msgTransmitter);
			msgReceiverThread = new Thread(msgReceiver);
			watchDogThread = new Thread(watchDog);
			
			msgTransmitterThread.start();
			msgReceiverThread.start();
			watchDogThread.start();
			
			// watch to see if either the watchdog detects an interruption OR if an outside
			// request to simply stop all threads has been issued.  If an interruption is 
			// detected via watchdog, attempts to reconnect take place.  If an external request
			// to simply stop all threads is issued, no further attempts to connect are made.
			
			// TODO in its current state, this code will never exit if TCP connections are not established
			// for receiving and transmitting messages yet the user attempts to terminate at the federation
			// manager level.  If logical time never advances before the user attempts to terminate, the
			// SIMEND flag is never seen, nor is the terminate() method.  Therefore a stopAll() is never
			// called.  Something needs to be done to properly manage this.
			while (!volatiles.getStopThreads() && !stopAll) {
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			// above loop wasn't satisfied, close out threads in preparation for restart
			try {
				log.info("Outside command issued to halt all tcp communications and close out threads.");
				log.info("Stopping watchdog thread (if not already stopped)...");
				watchDog.stopThread();
				watchDogThread.join();
				log.info("Watchdog thread stopped.");
				
				log.info("Stopping message transmitter thread (if not already stopped)...");
				msgTransmitter.stopThread();
				msgTransmitterThread.join();
				log.info("Message transmitter thread stopped.");
				
				log.info("Stopping message receiver thread (if not already stopped)...");
				msgReceiver.stopThread();
				msgReceiverThread.join();
				log.info("Message receiver thread stopped.");
				
				
			} catch (InterruptedException ex) {
				log.error("Exception while waiting on threads to complete.  This is acceptable");
			}
		}
    	
    	log.info("terminating TCP Adapter...");
    }
}
