package gov.nist.hla.trnsys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initializeSelf() {
        try {
            JsonRoot root = parseJSON(configuration.getVariableMapping());
            
            for (VariableMapping entry : root.getInputs()) {
                log.info("{} {} {}", entry.getIndex(), entry.getVariable(), entry.getHlaClass());
            }
            for (VariableMapping entry : root.getOutputs()) {
                log.info("{} {} {}", entry.getIndex(), entry.getVariable(), entry.getHlaClass());
            }
        } catch (IOException e) {
            log.error("failed to read JSON file {}", configuration.getVariableMapping());
        }
    }

    @Override
    public void initializeWithPeers() {
        // this is wrong - here to test the Windows to UCEF-VM communication path
        server.startServer();
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
}
