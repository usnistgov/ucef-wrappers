package gov.nist.hla;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.hla.domain.labview.Entity;
import gov.nist.hla.domain.labview.EntityType;
import gov.nist.hla.domain.labview.Group;

public class Utils {
		
	private static final String LINE_ITEM_HLA_ITEM = "received.item";
    private static final String LINE_ITEM_TIME_STEP = "received.dostep";
    private static final String LINE_ITEM_SIM_END = "received.simend";
	
	/**
	 * Provide the means to serialize an interaction into a JSON representation of an object that
	 * can then be interpreted by the LabVIEW based messaging processor
	 * 
	 * @param timeStep
	 * @param className
	 * @param parameters
	 * @return
	 * @throws JsonProcessingException
	 */
    public static String serializeInteraction(Double timeStep, String className, Map<String, String> parameters) throws JsonProcessingException {
		
		String serializedInteraction = "";
		
		Group group = new Group();
    	List<Entity> interactions = new LinkedList<Entity>();
    	Entity interaction = new Entity();
    	interaction.setClassPath(className);
    	interaction.setInstanceName("");
    	interaction.setTimeStep(timeStep);
    	interaction.setType(EntityType.INTERACTION);
    	
    	//String[][] array = new String[][]{parameters.keySet().toArray(new String[parameters.size()]), parameters.entrySet().toArray(new String[parameters.size()])};
    	
    	String[][] array = new String[parameters.size()][2];
    	int count = 0;
    	for(Map.Entry<String,String> entry : parameters.entrySet()){
    	    array[count][0] = entry.getKey();
    	    array[count][1] = entry.getValue();
    	    count++;
    	}
    	
    	interaction.setParams(array);
    	
    	interactions.add(interaction);
    	group.setTimeStep(timeStep);
    	group.setEntities(interactions);
    	
    	ObjectMapper interactionMapper = new ObjectMapper();
		serializedInteraction = interactionMapper.writeValueAsString(group);
    	
    	return serializedInteraction;
	}
    
    /**
     * Provide the means to serialize an object into a JSON representation of an object that
     * can then be interpreted by the LabVIEW based messaging processor
     * @param timeStep
     * @param className
     * @param instanceName
     * @param attributes
     * @return
     * @throws JsonProcessingException
     */
    public static String serializeObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) throws JsonProcessingException {
    	
    	String serializedObject = "";
    	
    	Group group = new Group();
    	List<Entity> objects = new LinkedList<Entity>();
    	Entity object = new Entity();
    	object.setClassPath(className);
    	object.setInstanceName(instanceName);
    	object.setTimeStep(timeStep);
    	object.setType(EntityType.OBJECT);
    	
    	//String[][] array = new String[][]{attributes.keySet().toArray(new String[attributes.size()]), attributes.entrySet().toArray(new String[attributes.size()])};
    	
    	String[][] array = new String[attributes.size()][2];
    	int count = 0;
    	for(Map.Entry<String,String> entry : attributes.entrySet()){
    	    array[count][0] = entry.getKey();
    	    array[count][1] = entry.getValue();
    	    count++;
    	}
    	
    	object.setParams(array);
    	
    	objects.add(object);
    	group.setTimeStep(timeStep);
    	group.setEntities(objects);
    	
    	ObjectMapper groupMapper = new ObjectMapper();
		serializedObject = groupMapper.writeValueAsString(group);
		
		return serializedObject;
		
    }
    
    /**
     * Return a single line item representing an interaction for purposes of transfer to LabVIEW client
     * @param serializedInteraction
     * @return
     */
    public static String toInteractionReceivedLineItem(String seriazliedInteraction) {
    	return toLineItem(LINE_ITEM_HLA_ITEM, seriazliedInteraction);
    }
    
    /**
     * Return a single line item representing an object for purposes of transfer to LabVIEW client
     * @param serializedInteraction
     * @return
     */
    public static String toObjectReceivedLineItem(String serializedObject) {
    	return toLineItem(LINE_ITEM_HLA_ITEM, serializedObject);
    }
    
    /**
     * Return a single line item representing a timestep invocation for purposes of transfer to LabVIEW client
     * @param timeStep
     * @return
     */
    public static String toTimeStepReceivedLineItem(Double timeStep) {
    	
    	StringBuffer sb  = new StringBuffer();
    	sb.append(LINE_ITEM_TIME_STEP);
    	sb.append(" ");
    	sb.append("[");
    	sb.append(timeStep);
    	sb.append("]");
    	
    	return sb.toString();
    }
    
    /**
     * Return a single line item representing a simulation-end invocation for purposes of transfer to LabVIEW client
     * @param timeStep
     * @return
     */
    public static String toSimEndReceivedLineItem(Double timeStep) {
    	
    	StringBuffer sb  = new StringBuffer();
    	sb.append(LINE_ITEM_SIM_END);
    	sb.append(" ");
    	sb.append("[");
    	sb.append(timeStep);
    	sb.append("]");
    	
    	return sb.toString();
    }
    
    /**
     * Return a single line item representing an interaction or object for purposes of transfer to LabVIEW client
     * @param linetimeType
     * @param serializedEntity
     * @return
     */
    public static String toLineItem(String lineItemType, String serializedEntity) {
    	
    	StringBuffer sb  = new StringBuffer();
    	sb.append(lineItemType);
    	sb.append(" ");
    	sb.append("[");
    	sb.append(serializedEntity);
    	sb.append("]");
    	return sb.toString();
    }
    
    public static Map<String,String> paramsOrAttribsToMap(String[][] paramsOrAttribs) {
    	Map<String,String> map = new HashMap<String,String>();
    	
    	for (String[] entry : paramsOrAttribs) {
    		map.put(entry[0], entry[1]);
    	}
    	
    	return map;
    }
}
