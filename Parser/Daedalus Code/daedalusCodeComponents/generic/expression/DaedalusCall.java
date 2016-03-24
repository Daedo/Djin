package daedalusCodeComponents.generic.expression;

import daedalusCodeComponents.generic.expression.modifiers.DaedalusParameterList;

public class DaedalusCall extends DaedalusExpression{
	private DaedalusExpression method;
	private DaedalusParameterList parameter;
	
	public DaedalusCall(DaedalusExpression met,DaedalusParameterList par) {
		this.method = met;
		this.parameter = par;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Call "+this.method+this.parameter;
	}
}
