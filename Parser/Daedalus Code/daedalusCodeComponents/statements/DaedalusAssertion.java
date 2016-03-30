package daedalusCodeComponents.statements;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusExecution.exception.DaedalusException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;

public class DaedalusAssertion extends DaedalusStatement{
	private DaedalusExpression expression;
	public DaedalusAssertion(DaedalusExpression ast) {
		this.expression = ast;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Assert "+this.expression;
	}
	
	@Override
	public DaedalusIntermediateValue resolve() throws DaedalusException {
		DaedalusIntermediateValue as = this.expression.resolve();
		
		// if as can be cast to boolean 
		
		return super.resolve();
	}
}
