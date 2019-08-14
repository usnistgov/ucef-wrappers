package gov.nist.hla.domain.labview;

import java.util.List;

public class Group {

	private List<Entity> entities;
	private Double timeStep;
	
	public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}
	public Double getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}
}
