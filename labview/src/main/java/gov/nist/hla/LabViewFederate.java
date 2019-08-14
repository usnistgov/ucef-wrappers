package gov.nist.hla;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.hla.domain.labview.EntityType;
import gov.nist.hla.domain.labview.Group;
import gov.nist.hla.domain.labview.Entity;
import gov.nist.hla.domain.labview.Response;
import gov.nist.hla.gateway.GatewayCallback;
import gov.nist.hla.gateway.GatewayFederate;
import gov.nist.hla.gateway.GatewayFederateConfig;
import hla.rti.AttributeNotOwned;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InvalidFederationTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectAlreadyRegistered;
import hla.rti.ObjectClassNotPublished;
import hla.rti.ObjectNotKnown;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LabViewFederate implements GatewayCallback {
    
	private static final Logger log = LogManager.getLogger();
	
    private static final String INTERACTION_SIM_END = "InteractionRoot.C2WInteractionRoot.SimulationControl.SimEnd";

    private GatewayFederate gateway;
    private String lvHostIp;
    private Integer outgoingTcpPort;
    private Integer incomingTcpPort;
    
    // main outgoing and incoming queues that are either directly sent over 
    // or received from TCP exchange...
    private BlockingDeque<String> outgoingMsgQueue;
	private BlockingDeque<String> incomingMsgQueue;
	private Double simEndTimeStep; //required once prepareToResign() is fully functional
	
	// establish a set of blocking queues used purely in this class for distributing
	// and maintaining specific types of information received from the LabVIEW portion
	// of this federate...
	private BlockingDeque<Group> initWithPeersQueue;
	private BlockingDeque<Group> doTimeStepQueue;
	
	// for maintaining a list of instantiated objects at runtime
	private List<String> registeredObjects;
	
    private TcpAdapter tcpAdapter;
    private IncomingQueueDelegator incomingQueueDelegator;
    private boolean stopAllThreads = false;

    public static LabViewConfig readConfiguration(String filePath)
            throws IOException {
        log.info("reading JSON configuration file {}", filePath);
        File configFile = Paths.get(filePath).toFile();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(configFile, LabViewConfig.class);
    }

    public static void main(String[] args)
            throws IOException {
        if (args.length != 1) {
            log.error("missing command line argument for JSON configuration file");
            return;
        }
        
        LabViewConfig config = LabViewFederate.readConfiguration(args[0]);
        LabViewFederate labviewFederate = new LabViewFederate(config);
        labviewFederate.run();
        log.info("Done.");
    }

    public LabViewFederate(LabViewConfig configuration) {
    	this.gateway = new GatewayFederate(configuration, this);
        this.lvHostIp = configuration.getHostAddress();
        this.outgoingTcpPort = configuration.getOutgoingPort();
        this.incomingTcpPort = configuration.getIncomingPort();

        log.info("LabVIEW machine IP: {}", lvHostIp);
        log.info("outgoing port: {}", outgoingTcpPort);
        log.info("incoming port: {}", incomingTcpPort);
    }

    public void run() {
        log.trace("run");
        gateway.run();
    }

    public void initializeSelf() {
        log.trace("initializeSelf");
        
        // clear out any and all known registered objects from the cache
        registeredObjects = null;
        
        // init the message queues
    	outgoingMsgQueue = new LinkedBlockingDeque<String>(5000);
    	incomingMsgQueue = new LinkedBlockingDeque<String>(5000);
    	
    	// init the local queues
    	initWithPeersQueue = new LinkedBlockingDeque<Group>(100);
    	doTimeStepQueue = new LinkedBlockingDeque<Group>(100);
    	
    	// fire off tcp management thread
    	tcpAdapter = new TcpAdapter(this.incomingMsgQueue, this.outgoingMsgQueue, this.lvHostIp, this.incomingTcpPort, this.outgoingTcpPort);
    	Thread tcpProcessingThread = new Thread(tcpAdapter);
    	tcpProcessingThread.start();
    	
    	// fire off the received message delegator thread, localized to this class
    	incomingQueueDelegator = new IncomingQueueDelegator(this.incomingMsgQueue);
    	Thread incomingQueueDelegatorThread = new Thread(incomingQueueDelegator);
    	incomingQueueDelegatorThread.start();
    }

    public void initializeWithPeers() {
        log.trace("initializeWithPeers");
        
        Boolean satisfied = false;
        while(!satisfied) {
        	Group peersGroup = null;
			try {
				log.info("waiting on proper notification to proceed with initialization with peers...");
				peersGroup = initWithPeersQueue.poll(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if (peersGroup != null) {
    			for (Entity entity : peersGroup.getEntities()) {
    				this.processIncomingObject(entity, peersGroup.getTimeStep());
    			}
    			satisfied = true;
    		} else
    			log.info("timeout occurred while waiting for initialization with peers notification.  Retrying...");
        }
    }

    public void receiveInteraction(Double timeStep, String className, Map<String, String> parameters) {
        log.info("received interaction {} at t={}", className, timeStep);
        
        String lineItem = "";
        
        // check to see if we're receiving a simulation end request
        if (className.equals(INTERACTION_SIM_END)) {
        	simEndTimeStep = timeStep;
        	lineItem = Utils.toSimEndReceivedLineItem(timeStep);
        	log.info("received interaction confirmed SIMULATION END");
        
        } else {
        	try {
				lineItem = Utils.toInteractionReceivedLineItem(Utils.serializeInteraction(timeStep, className, parameters));
			} catch (JsonProcessingException e) {
				log.error("Exception occurred while attempting to seralize an interaction in preparation for LabVIEW message delivery");
				e.printStackTrace();
			}
        }
        
        // regardless of interaction, queue it up to be processed and delivered to LV client
		outgoingMsgQueue.offer(lineItem);
		log.info("interaction received queued up: " +className);
    }

    public void receiveObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) {
        
        String lineItem = "";
        Map<String, String> fullAttributes = gateway.getObjectState(instanceName);
        
        log.info("received object {}.{} at t={} with {}", className, instanceName, timeStep, fullAttributes.toString());
        
        // cache any 'new' instances of an object to ensure the federate is aware of objects that have already
        // been instantiated vs those that require registration...
        updateObjectCache(className, instanceName);
        
        // check to see if we're receiving a simulation end request
        if (className.equals(INTERACTION_SIM_END)) {
        	simEndTimeStep = timeStep;
        	log.info("received object confirmed SIMULATION END");
        
        } else {
        	try {
				lineItem = Utils.toObjectReceivedLineItem(Utils.serializeObject(timeStep, className, instanceName, fullAttributes));
			} catch (JsonProcessingException e1) {
				log.error("Exception occurred while attempting to seralize an object in preparation for LabVIEW message delivery");
				e1.printStackTrace();
			}
        }
        
        // regardless of interaction, queue it up to be processed and delivered to LV client
		outgoingMsgQueue.offer(lineItem);
		log.info("object received queued up: " +className);
    }

    public void doTimeStep(Double timeStep) {
        log.info("t={}", timeStep);
        
        // issue the doTimeStep notification to the LabVIEW client
        String lineItem = Utils.toTimeStepReceivedLineItem(timeStep);
        try {
			outgoingMsgQueue.offer(lineItem);
			log.info("dostep received queued up: " +timeStep);
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
        
        Boolean satisfied = false;
        while(!satisfied) {
        	Group stepGroup = null;
			try {
				log.info("waiting on proper notification to proceed with the timestep {}...", timeStep);
				stepGroup = doTimeStepQueue.poll(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if (stepGroup != null) {
    			for (Entity entity : stepGroup.getEntities()) {
    				
    				if (entity.getType() == EntityType.OBJECT) {
    					this.processIncomingObject(entity, stepGroup.getTimeStep());
	    			} else if (entity.getType() == EntityType.INTERACTION) {
	    				try {
	    		            gateway.sendInteraction(entity.getClassPath(), Utils.paramsOrAttribsToMap(entity.getParams()));
	    		        } catch (FederateNotExecutionMember | NameNotFound | InteractionClassNotPublished e) {
	    		            log.error(e);
	    		        }
	    			}
    			}
    			satisfied = true;
    		} else
    			log.info("timeout occurred while waiting for timestep notification.  Retrying...");
        }
    }

    public void prepareToResign() {
        log.trace("prepareToResign");
    }

    public void terminate() {
    	log.info("***** terminate *****");
    	if (!stopAllThreads) {
    		tcpAdapter.stopAllThreads();
    		incomingQueueDelegator.stopThread();
    		stopAllThreads = true;
    	}
    }
    
    /**
     * For maintaining an ongoing list of registered objects either seen or created via
     * this federate.  
     * @param className
     * @param instanceName
     * @return isNew - returns true if the object is new (doesn't already exist in the cache)
     */
    private boolean updateObjectCache(String className, String instanceName) {
    	
    	boolean isNew = false;
    	
    	if (registeredObjects == null)
    		registeredObjects = new LinkedList<String>();
    	if (!isObjectCached(className, instanceName)) {
    		registeredObjects.add(className +":" +instanceName);
    		isNew = true;
    	}
    	
    	return isNew;
    }
    
    private boolean isObjectCached(String className, String instanceName) {
    	return registeredObjects.contains(className +":" +instanceName);
    }
    
    /**
     * Processes all objects passed into this Java component of the federate from
     * the LabVIEW component of the federate.  If the passed object is found NOT to 
     * exist in the cache, it is assumed to have never been registered, therefore requires
     * registration.  If the object DOES exist in the cache (recall ALL objects that pass
     * through this federate, whether received or updated are included in the cache) then an
     * update is assumed. 
     * 
     * @param object
     * @param timeStep
     * @return
     */
    private String processIncomingObject(Entity object, Double timeStep) {
    	
    	String instanceName = null;
    	
    	// if returns true, then the object instance does not exist and we must
    	// instantiate/register said object.
    	if (updateObjectCache(object.getClassPath(), object.getInstanceName())) {
    		// register the object
    		try {
                log.info("registering new object: class[{}] instanceName[{}]", object.getClassPath(), object.getInstanceName());
    			instanceName = gateway.registerObjectInstance(object.getClassPath(), object.getInstanceName());
            } catch (FederateNotExecutionMember | NameNotFound | ObjectClassNotPublished | ObjectAlreadyRegistered e) {
                log.error("failed to register object instance {}", object.getInstanceName());
                e.printStackTrace();
            }
    	} else {
    		// update the object
    		try {
                log.info("updating existing object: class[{}] instanceName[{}]", object.getClassPath(), object.getInstanceName());
    			//gateway.updateObject(object.getInstanceName(), Utils.paramsOrAttribsToMap(object.getParams()), timeStep);
    			gateway.updateObject(object.getInstanceName(), Utils.paramsOrAttribsToMap(object.getParams()), gateway.getTimeStamp());
				instanceName = object.getInstanceName();
    		} catch (FederateNotExecutionMember | ObjectNotKnown | NameNotFound | AttributeNotOwned
					| InvalidFederationTime e) {
				log.error("failed to update the object instance {}", object.getInstanceName());
				e.printStackTrace();
			}
    	}
    	
    	return instanceName;
    }
    
    public class IncomingQueueDelegator implements Runnable {
    	
    	private Boolean stopMe = false;
    	private BlockingDeque<String> incomingMessages;
    	
    	public IncomingQueueDelegator(BlockingDeque<String> incomingQueue) {
    		this.incomingMessages = incomingQueue;
    	}
    	
    	public void stopThread() {
        	stopMe = true;
        }
        
        @Override
        public void run() {
        	
        	log.info("initiating queue delegator...");
        	
        	ObjectMapper respMapper = new ObjectMapper();
        	ObjectMapper groupMapper = new ObjectMapper();
        	
        	try {
    	    	while (!stopMe) {
    	    		
    	    		// top level message should be JSON equiv of ReceivedItem pojo
    	    		String flattenedItemMsg = incomingMessages.poll(500, TimeUnit.MILLISECONDS);
    	    		if (flattenedItemMsg != null && flattenedItemMsg.trim().length() > 0) {
	    	    		
    	    			log.info("flattened message to be processed: " +flattenedItemMsg);
    	    			
    	    			Response resp = null;
    	    			try {
    	    				resp = respMapper.readValue(flattenedItemMsg, Response.class);
    	    			} catch (Exception ex) {
    	    				log.error("could not process flattened message: " +ex.getMessage());
    	    				ex.printStackTrace();
    	    			}
	    	    		
    	    			log.info("{} response type found", resp.getRespType());
    	    			
	    	    		// determine what type of message was just received, delegate martial
	    	    		// and delegate accordingly...
	    	    		switch(resp.getRespType()) {
	    	    			    	    		
		    	    		case DOSTEP_RESP: 
		    	    				    	    			
		    	    			// knowing the type of response, we also know the flattened content is of type Group...
		    	    			Group stepGroup = groupMapper.readValue(resp.getFlattenedContent(), Group.class);
		    	    			doTimeStepQueue.offer(stepGroup);
		    	    			break;
		    	    			
		    	    		case INIT_W_PEERS_RESP: 
		    	    				    	    			
		    	    			// knowing the type of response, we also know the flattened content is of type List<Entity>
		    	    			// Furthermore, it is assumed that any entity passed within the group will be of type
		    	    			// object as interactions only ever get transmitted with a timestep, not at initialization.
		    	    			List<Entity> entities = groupMapper.readValue(resp.getFlattenedContent(), new TypeReference<List<Entity>>() { });
		    	    			Group objBundle = new Group();
		    	    			objBundle.setEntities(entities);
		    	    			initWithPeersQueue.offer(objBundle);
		    	    			break;
	    	    		}
    	    		} else {
    	    			log.info("delegator thread timed out waiting on incoming message, retrying...");
    	    		}
    	    	}
    	    	
    	    	log.info("terminating queue delegator...");
    	    	
        	}catch(Exception ex) {
        		log.error("exception in receivinga and delegating messages: " +ex.getMessage());
        		ex.printStackTrace();
        	}
        }
    }
}

