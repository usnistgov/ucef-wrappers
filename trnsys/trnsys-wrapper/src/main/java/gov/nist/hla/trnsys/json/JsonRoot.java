package gov.nist.hla.trnsys.json;

import java.util.List;

public class JsonRoot {
    private List<VariableMapping> inputs;
    private List<VariableMapping> outputs;
    
    public List<VariableMapping> getInputs() {
        return inputs;
    }
    
    public void setInputs(List<VariableMapping> inputs) {
        this.inputs = inputs;
    }
    
    public List<VariableMapping> getOutputs() {
        return outputs;
    }
    
    public void setOutputs(List<VariableMapping> outputs) {
        this.outputs = outputs;
    }
}
