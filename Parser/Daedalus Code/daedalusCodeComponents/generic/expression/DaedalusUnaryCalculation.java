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
	
	@Override
	public String toString() {
		String out;
		if(this.isPrefix) {
			out = this.operator.toString()+this.expression.toString();
		} else {
			out = this.expression.toString()+this.operator.toString();
		}
		return out;
	}
	
}
