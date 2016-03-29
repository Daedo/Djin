package daedalusExecution.runner;

import java.util.Vector;

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
}
