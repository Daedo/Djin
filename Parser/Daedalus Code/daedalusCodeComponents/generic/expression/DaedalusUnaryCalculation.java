package daedalusCodeComponents.generic.expression;

public class DaedalusUnaryCalculation extends DaedalusExpression {
	private DaedalusOperator operator;
	private DaedalusExpression expression;
	private boolean isPrefix;
	
	public DaedalusUnaryCalculation(DaedalusOperator op,DaedalusExpression expr, boolean prefix) {
		this.operator = op;
		this.expression = expr;
		this.isPrefix = prefix;
	}
}
