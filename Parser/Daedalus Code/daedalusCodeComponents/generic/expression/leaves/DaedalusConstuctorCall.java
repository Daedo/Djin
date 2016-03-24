package daedalusCodeComponents.generic.expression.leaves;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusConstuctorCall extends DaedalusExpression{
	private DaedalusType conType;
	public DaedalusConstuctorCall(DaedalusType type) {
		this.conType = type;
	}
	
}
