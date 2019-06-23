package gov.nist.hla.trnsys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ieee.standards.ieee1516._2010.InteractionClassType;
import org.ieee.standards.ieee1516._2010.ParameterType;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.hla.gateway.GatewayCallback;
import gov.nist.hla.gateway.GatewayFederate;
import gov.nist.hla.trnsys.json.JsonRoot;
import gov.nist.hla.trnsys.json.VariableMapping;

public class TRNSYSFederate implements GatewayCallback {
    private static final Logger log = LogManager.getLogger();
    
    private final Configuration configuration;
    
    private ServerRunnable server = new ServerRunnable();
    
    private GatewayFederate gateway;
    
    private Map<String, Long> parameterToIndex = new HashMap<String, Long>();
    private Map<String, Map<String, Long>> interactionToMapping = new HashMap<String, Map<String, Long>>();
    
    public static void main(String[] args)
            throws IOException {
        if (args.length != 1) {
            log.error("missing command line argument for JSON configuration file");
            return;
        }
        
        Configuration config = TRNSYSFederate.readConfiguration(args[0]);
        TRNSYSFederate federate = new TRNSYSFederate(config);
        federate.run();
    }
    
    private static Configuration readConfiguration(String filepath)
            throws IOException {
        log.info("reading JSON configuration file at " + filepath);
        File configFile = Paths.get(filepath).toFile();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(configFile, Configuration.class);
    }
    
    public TRNSYSFederate(Configuration configuration) {
        this.configuration = configuration;
        this.gateway = new GatewayFederate(configuration, this);
    }
    
    public void run() {
        gateway.run();
    }
    
    @Override
    public void doTimeStep(Double timeStep) {
        log.info("Waiting on TRNSYS simulation...");
        while (!server.isReadyForData()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
        double[] data = server.getData();
        log.info("received {}", data);
        server.setData(null);
    }

    @Override
    public void initializeSelf() {
        try {
            JsonRoot root = parseJSON(configuration.getVariableMapping());
            
            Set<Long> inputIndices = new HashSet<Long>();
            Set<Long> outputIndices = new HashSet<Long>();
            for (VariableMapping entry : root.getInputs()) {
                if (!inputIndices.add(entry.getIndex())) {
                    log.error("multiple input variables use index {}", entry.getIndex());
                }
                parseInputVariable(entry);
            }
            for (VariableMapping entry : root.getOutputs()) {
                if (!outputIndices.add(entry.getIndex())) {
                    log.error("multiple output variables use index {}", entry.getIndex());
                }
                parseOutputVariable(entry);
            }
            if (Collections.min(inputIndices) != 1 || Collections.max(inputIndices) != inputIndices.size()) {
                log.error("input indices are not consecutive starting from 1");
            }
            if (Collections.min(outputIndices) != 1 || Collections.max(outputIndices) != outputIndices.size()) {
                log.error("output indices are not consecutive starting from 1");
            }
            
            log.info("{}", parameterToIndex.toString());
            log.info("{}", interactionToMapping.toString());
        } catch (IOException e) {
            log.error("failed to read JSON file {}", configuration.getVariableMapping());
        }
        
        server.startServer();
    }

    @Override
    public void initializeWithPeers() {
        log.info("Waiting on TRNSYS simulation...");
        while (!server.isReadyToSimulate()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void prepareToResign() {
        // TODO Auto-generated method stub
    }

    @Override
    public void receiveInteraction(Double timeStep, String className, Map<String, String> parameters) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receiveObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void terminate() {
        // TODO Auto-generated method stub
        
    }
    
    private JsonRoot parseJSON(String filepath) throws IOException {
        log.info("reading JSON from {}", filepath);
        File file = new File(filepath);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, JsonRoot.class);
    }
    
    private void parseInputVariable(VariableMapping entry) {
        log.trace("on input i={} var={} class={}", entry.getIndex(), entry.getVariable(), entry.getHlaClass());
        checkIfSubscribedNumeric(entry.getHlaClass(), entry.getVariable());
        final String parameter = entry.getHlaClass() + ":" + entry.getVariable();
        parameterToIndex.put(parameter, entry.getIndex());
    }
    
    private void parseOutputVariable(VariableMapping entry) {
        log.trace("on output i={} var={} class={}", entry.getIndex(), entry.getVariable(), entry.getHlaClass());
        checkIfPublishedNumeric(entry.getHlaClass(), entry.getVariable());
        
        Map<String, Long> parameterToIndex = interactionToMapping.get(entry.getHlaClass());
        if (parameterToIndex == null) {
            parameterToIndex = new HashMap<String, Long>();
            interactionToMapping.put(entry.getHlaClass(), parameterToIndex);
        }
        
        if (parameterToIndex.put(entry.getVariable(), entry.getIndex()) != null) {
            log.error("multiple output indices are mapped to {}:{}", entry.getHlaClass(), entry.getVariable());
        }
    }
    
    private void checkIfSubscribedNumeric(String classpath, String parameter) {
        InteractionClassType interaction = gateway.getObjectModel().getInteraction(classpath);
        if (interaction == null) {
            log.error("{} does not refer to a known interaction class", classpath);
        }
        
        if (!gateway.getObjectModel().getSubscribedInteractions().contains(interaction)) {
            log.error("{} is not a subscribed interaction class", classpath);
        }
        
        ParameterType theParameter = gateway.getObjectModel().getParameter(interaction, parameter);
        if (theParameter == null) {
            log.error("{} does not contain the parameter {}", classpath, parameter);
        }
        
        final String dataType = theParameter.getDataType().getValue();
        switch (dataType) {
            case "boolean":
            case "double":
            case "float":
            case "long":
            case "int":
            case "short":
                break;
            default:
                log.error("{}:{} cannot be represented as a numeric type", classpath, parameter);
        }
    }
    
    private void checkIfPublishedNumeric(String classpath, String parameter) {
        InteractionClassType interaction = gateway.getObjectModel().getInteraction(classpath);
        if (interaction == null) {
            log.error("{} does not refer to a known interaction class", classpath);
        }
        
        if (!gateway.getObjectModel().getPublishedInteractions().contains(interaction)) {
            log.error("{} is not a published interaction class", classpath);
        }
        
        ParameterType theParameter = gateway.getObjectModel().getParameter(interaction, parameter);
        if (theParameter == null) {
            log.error("{} does not contain the parameter {}", classpath, parameter);
        }
        
        final String dataType = theParameter.getDataType().getValue();
        switch (dataType) {
            case "boolean":
            case "double":
            case "float":
            case "long":
            case "int":
            case "short":
                break;
            default:
                log.error("{}:{} cannot be represented as a numeric type", classpath, parameter);
        }
    }
}
