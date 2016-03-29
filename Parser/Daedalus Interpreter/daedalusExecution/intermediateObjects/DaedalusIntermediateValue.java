package daedalusExecution.intermediateObjects;

public abstract class DaedalusIntermediateValue {
	private DaedalusReturnLiteral state;
	
	protected DaedalusIntermediateValue(DaedalusReturnLiteral s) {
		this.state = s;
	}
	
	public DaedalusReturnLiteral getState() {
		return this.state;
	}
}
