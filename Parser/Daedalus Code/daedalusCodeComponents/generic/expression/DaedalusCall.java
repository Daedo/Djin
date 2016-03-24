package daedalusCodeComponents.generic.expression;

import daedalusCodeComponents.generic.expression.modifiers.DaedalusParameterList;

public class DaedalusCall {
	private DaedalusExpression method;
	private DaedalusParameterList parameter;
	
	public DaedalusCall(DaedalusExpression met,DaedalusParameterList par) {
		this.method = met;
		this.parameter = par;
	}
}
