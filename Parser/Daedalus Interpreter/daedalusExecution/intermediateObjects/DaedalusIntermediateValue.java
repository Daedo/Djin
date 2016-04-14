package daedalusExecution.intermediateObjects;

import daedalusExecution.runner.pointer.DaedalusCallable;

public abstract class DaedalusIntermediateValue implements DaedalusCallable{
	private DaedalusReturnLiteral state;
	
	protected DaedalusIntermediateValue(DaedalusReturnLiteral s) {
		this.state = s;
	}
	
	public DaedalusReturnLiteral getState() {
		return this.state;
	}
	
	
}
