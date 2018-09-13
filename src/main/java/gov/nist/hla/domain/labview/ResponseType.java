package gov.nist.hla.domain.labview;

/**
 * Response types are used to help decipher the types of flattened messages (in json) coming from
 * LabVIEW to the Java component of the federate.  Once the type is known, the method by which that
 * information is translated and disseminated is also known.
 * @author clemieux
 *
 */
public enum ResponseType {
	DOSTEP_RESP,
	INIT_W_PEERS_RESP;
}