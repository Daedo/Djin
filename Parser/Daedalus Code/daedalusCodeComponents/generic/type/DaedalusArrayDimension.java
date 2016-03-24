package daedalusCodeComponents.generic.type;

import java.util.Optional;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusArrayDimension extends DaedalusSyntaxElement{
	Optional<DaedalusExpression> size;
	
	public DaedalusArrayDimension() {
		this.size = Optional.empty();
	}
	
	public DaedalusArrayDimension(DaedalusExpression exp) {
		this();
		
		if(exp!=null) {
			this.size = Optional.of(exp);
		}
	}
	
	@Override
	public String toString() {
		String e = "";
		if(this.size.isPresent()) {
			e = this.size.get().toString();
		}
		
		return "["+e+"]";
	}
}
