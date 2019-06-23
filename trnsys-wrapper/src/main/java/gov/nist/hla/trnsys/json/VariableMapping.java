package gov.nist.hla.trnsys.json;

public class VariableMapping {
    private long index;
    private String variable;
    private String hlaClass;
    
    public long getIndex() {
        return index;
    }
    
    public void setIndex(long index) {
        this.index = index;
    }
    
    public String getVariable() {
        return variable;
    }
    
    public void setVariable(String variable) {
        this.variable = variable;
    }
    
    public String getHlaClass() {
        return hlaClass;
    }
    
    public void setHlaClass(String hlaClass) {
        this.hlaClass = hlaClass;
    }
}
