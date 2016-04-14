package daedalusExecution.runner;

import java.util.HashMap;
import java.util.Map;

import daedalusCodeComponents.generic.expression.DaedalusOperator;
import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.exception.DaedalusIllegalVarAccessException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;
import daedalusExecution.runner.pointer.DaedalusMethodPointerList;
import daedalusExecution.runner.pointer.DaedalusVarPointer;

public class DaedalusExecutionLayer {
	Map<String, DaedalusVarPointer> varMap;
	Map<String, DaedalusMethodPointerList> methodMap;
	Map<DaedalusOperator, DaedalusMethodPointerList> operatorMap;
	
	public DaedalusExecutionLayer() {
		this.varMap = new HashMap<>();
		this.methodMap = new HashMap<>();
		this.operatorMap = new HashMap<>();
	}
	
	public void declareVariable(String name,DaedalusType type) throws DaedalusIllegalVarAccessException {
		if(this.varMap.containsKey(name)) {
			throw new DaedalusIllegalVarAccessException("Redundant Variable Decleration \""+name+"\"");
		}
		
		DaedalusVarPointer value = new DaedalusVarPointer(type);
		this.varMap.put(name, value);
	}
	
	public void setVariable(String name,DaedalusIntermediateValue value) throws DaedalusIllegalVarAccessException {
		if(!this.varMap.containsKey(name)) {
			throw new DaedalusIllegalVarAccessException("Undeclared Variable \""+name+"\"");
		}
		
		DaedalusVarPointer val =  this.varMap.get(name);
		val.setValue(value);
	}
	
	public DaedalusVarPointer getVariable(String name) throws DaedalusIllegalVarAccessException {
		if(!this.varMap.containsKey(name)) {
			throw new DaedalusIllegalVarAccessException("Undeclared Variable \""+name+"\"");
		}
		
		return this.varMap.get(name);		
	}
}
