package daedalusCodeComponents.statements;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.generic.expression.leaves.DaedalusLiteral;

public class DaedalusCase extends DaedalusStatement {
	private Optional<DaedalusCase> nextCase;
	private Optional<DaedalusLiteral> condition;
	private Vector<DaedalusStatement> body;
	
	public DaedalusCase() {
		this.body = new Vector<>();
		this.nextCase = Optional.empty();
		this.condition = Optional.empty();
	}
	
	public DaedalusCase(DaedalusLiteral cond) {
		this();
		this.condition = Optional.of(cond);
	}
	
	public boolean addStatement(DaedalusStatement st) {
		this.body.addElement(st);
		return true;
	}
	
	public boolean setNextCase(DaedalusCase next) {
		this.nextCase = Optional.of(next);
		return true;
	}
}
