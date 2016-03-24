package daedalusCodeComponents.generic.expression;

public class DaedalusConditionalCalculation extends DaedalusExpression {
	private DaedalusExpression condition,trueExpr,falseExpr;
	public DaedalusConditionalCalculation(DaedalusExpression cond,DaedalusExpression trExpr,DaedalusExpression flExpr) {
		this.condition = cond;
		this.trueExpr = trExpr;
		this.falseExpr = flExpr;
	}
	
	@Override
	public String toString() {
		return super.toString()+"Cond "+this.condition+"? "+this.trueExpr+": "+this.falseExpr;
	}
}
