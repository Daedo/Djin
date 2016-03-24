package daedalusCodeComponents.statements;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusAssertion extends DaedalusStatement{
	private DaedalusExpression expression;
	public DaedalusAssertion(DaedalusExpression ast) {
		this.expression = ast;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Assert "+this.expression;
	}
}
