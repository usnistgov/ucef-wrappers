package gov.nist.hla.domain;

import java.util.List;

import gov.nist.hla.domain.labview.Entity;

public class EntityGroupingQueueItem {

	private MethodAction action;
	private List<Entity> entities;
	
	public MethodAction getAction() {
		return action;
	}
	public void setAction(MethodAction action) {
		this.action = action;
	}
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

}
