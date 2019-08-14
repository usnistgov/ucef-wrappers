package gov.nist.hla.trnsys;

import gov.nist.hla.gateway.GatewayFederateConfig;
import gov.nist.hla.gateway.exception.ValueNotSet;

public class Configuration extends GatewayFederateConfig {
    private int portNumber = 1345;
    
    private String variableMapping = null;
    private boolean variableMappingSet = false;
    
    public void setPortNumber(int portNumber) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new RuntimeException("invalid port number " + portNumber);
        }
        this.portNumber = portNumber;
    }
    
    public int getPortNumber() {
        return portNumber;
    }
    
    public void setVariableMapping(String filepath) {
        this.variableMapping = filepath;
        this.variableMappingSet = true;
    }
    
    public String getVariableMapping() {
        if (!variableMappingSet) {
            throw new ValueNotSet("variableMapping");
        }
        return variableMapping;
    }
}
