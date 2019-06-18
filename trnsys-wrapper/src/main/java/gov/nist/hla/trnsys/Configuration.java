package gov.nist.hla.trnsys;

import gov.nist.hla.gateway.GatewayFederateConfig;

public class Configuration extends GatewayFederateConfig {
    private static int portNumber = 1345;
    
    public void setPortNumber(int portNumber) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new RuntimeException("invalid port number " + portNumber);
        }
        this.portNumber = portNumber;
    }
    
    public int getPortNumber() {
        return portNumber;
    }
}
