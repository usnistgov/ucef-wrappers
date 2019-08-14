package gov.nist.hla.domain.labview;

/**
 * Entity types, as of this writing, are simply Interaction and Object.  These identifiers assist
 * in the logic when translating flattened messages (in json) from LabVIEW and determining which
 * queues to place the translated objects into
 * 
 * @author clemieux
 *
 */
public enum EntityType {
	OBJECT,
	INTERACTION;
}