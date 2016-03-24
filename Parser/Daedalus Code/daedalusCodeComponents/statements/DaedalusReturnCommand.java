package daedalusCodeComponents.statements;

import java.util.Optional;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusReturnCommand extends DaedalusStatement {
	private Optional<DaedalusExpression> value;
	
	public DaedalusReturnCommand() {
		this.value = Optional.empty();
	}
	
	public boolean setValue(DaedalusExpression expr) {
		if(expr!=null) {
			this.value = Optional.of(expr);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String out = "";
		if(this.value.isPresent()) {
			out = this.value.get().toString();
		}
		
		return super.toString()+" Return "+out;
	}
}
