package daedalusExecution.runner;

import java.util.Vector;

import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.exception.DaedalusIllegalVarAccessException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;

public class DaedalusExecutionContext {
	private Vector<DaedalusExecutionLayer> callstack;
	
	public DaedalusExecutionContext() {
		this.callstack = new Vector<>();
	}
	
	public void pushLayer(DaedalusExecutionLayer layer) {
		if(layer!=null) {
			this.callstack.addElement(layer);
		}
	}
	
	public void popLayer() {
		this.callstack.remove(this.callstack.size()-1);
	}

	// resolve Method, Operator, Var
	// set Method, Operator, Var
	
	//Variable
	public void declareVariable(String name,DaedalusType type) throws DaedalusIllegalVarAccessException {
		this.callstack.lastElement().declareVariable(name, type);
	}
	
	public void setVariable(String name,DaedalusIntermediateValue value) throws DaedalusIllegalVarAccessException {
		for(int i=0;i<this.callstack.size();i++) {
			try {
				this.callstack.get(this.callstack.size()-1-i).setVariable(name, value);
				return;
			} catch (DaedalusIllegalVarAccessException e) {
				System.err.println("Couldn't find Var, try next Layer");
			}
		}
		throw new DaedalusIllegalVarAccessException("Undeclared Variable \""+name+"\"");
	}
	
	
}
