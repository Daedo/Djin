package daedalusCodeComponents.generic.expression;

public class DaedalusCalculation extends DaedalusExpression {
	DaedalusExpression value1,value2;
	DaedalusOperator op;
	
	public DaedalusCalculation(DaedalusExpression exp1,DaedalusOperator operator,DaedalusExpression exp2) {
		
		this.value1 = exp1;
		this.value2 = exp2;
		this.op = operator;
	}
	
	/*public void addOpeation(DaedalusOperator operator, DaedalusExpression expression) {
		this.op.add(operator);
		this.values.add(expression);
	} */
	
	@Override
	public String toString() {
		return super.toString()+ " "+this.value1+this.op+this.value2;
	}
}
