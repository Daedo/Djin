package daedalusCodeComponents.statements.stream;

import java.util.Optional;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusStreamElement extends DaedalusExpression {
	Optional<DaedalusStreamElement> nextCall;
	DaedalusExpression expression;
	
	public DaedalusStreamElement(DaedalusExpression expr) {
		this.nextCall = Optional.empty();
		this.expression = expr;
	}
	
	public boolean setNext(DaedalusStreamElement next) {
		if(next!=null) {
			this.nextCall = Optional.of(next);
		}
		return true;
	}
	
	@Override
	public String toString() {
		String out = "";
		if(this.nextCall.isPresent()) {
			out = this.nextCall.get().toString();
		}
		
		return this.expression+out;
	}
}
