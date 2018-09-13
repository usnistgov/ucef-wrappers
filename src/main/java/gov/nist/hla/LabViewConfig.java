package gov.nist.hla;

import gov.nist.hla.gateway.GatewayFederateConfig;
import gov.nist.hla.gateway.exception.ValueNotSet;

/**
 * This class defines the structure of the LabVIEW federate configuration file for use with Jackson.
 */
public class LabViewConfig extends GatewayFederateConfig {
    private String hostAddress = "localhost";

    private int outgoingPort = 1234;

    private int incomingPort = 9119;

    public void setHostAddress(String address) {
        this.hostAddress = address;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setOutgoingPort(int portNumber) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new RuntimeException("invalid port number " + portNumber);
        }
        this.outgoingPort = portNumber;
    }

    public int getOutgoingPort() {
        return outgoingPort;
    }

    public void setIncomingPort(int portNumber) {
        if (portNumber < 0 || portNumber > 65535) {
            throw new RuntimeException("invalid port number " + portNumber);
        }
        this.incomingPort = portNumber;
    }

    public int getIncomingPort() {
        return incomingPort;
    }
}

