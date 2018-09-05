package gov.nist.hla.domain.labview;

public class Entity {

	private EntityType type;
	private String classPath;
	private String[][] params;
	private Double timeStep;
	private String instanceName;
	
	public EntityType getType() {
		return type;
	}
	public void setType(EntityType type) {
		this.type = type;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String[][] getParams() {
		return params;
	}
	public void setParams(String[][] params) {
		this.params = params;
	}
	public Double getTimeStep() {
		return timeStep;
	}
	public void setTimeStep(Double timeStep) {
		this.timeStep = timeStep;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

}
