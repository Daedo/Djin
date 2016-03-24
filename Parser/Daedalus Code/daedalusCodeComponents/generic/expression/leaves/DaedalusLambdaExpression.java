package daedalusCodeComponents.generic.expression.leaves;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.statements.DaedalusStatement;

public class DaedalusLambdaExpression extends DaedalusExpression {
	private DaedalusLambdaParameter parameter;
	private DaedalusStatement block;
	
	public DaedalusLambdaExpression(DaedalusLambdaParameter param, DaedalusStatement code) {
		this.parameter = param;
		this.block = code;
	}
	
	@Override
	public String toString() {
		return super.toString()+" Lambda Expression: "+this.parameter+" -> "+this.block;
	}
	
}
