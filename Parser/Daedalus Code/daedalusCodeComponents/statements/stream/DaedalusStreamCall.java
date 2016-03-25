package daedalusCodeComponents.statements.stream;

import java.util.Optional;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusStreamCall extends DaedalusExpression{
	private DaedalusName identifier;
	private Optional<DaedalusStreamParameterList> pattern;
	
	public DaedalusStreamCall(DaedalusName name) {
		this.identifier = name;
		this.pattern = Optional.empty();
	}
	
	public boolean setParameter(DaedalusStreamParameterList list) {
		if(list!=null) {
			this.pattern = Optional.of(list);
		}
		return true;
	}
	
	@Override
	public String toString() {
		String out = "";
		if(this.pattern.isPresent()) {
			out = this.pattern.get().toString();
		}
		return this.identifier+out;
	}
}
