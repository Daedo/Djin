package daedalusCodeComponents.statements.loop;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.statements.DaedalusStatement;

public class DaedalusWhileLoop extends DaedalusLoop{
	private boolean isDoWhileLoop;
	private DaedalusExpression condition;
		
	public DaedalusWhileLoop(DaedalusExpression cond,DaedalusStatement code) {
		this(false,cond,code);
	}

	public DaedalusWhileLoop(boolean b, DaedalusExpression cond, DaedalusStatement code) {
		super(code);
	}
}
