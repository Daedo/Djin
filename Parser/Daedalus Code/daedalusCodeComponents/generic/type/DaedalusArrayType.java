package daedalusCodeComponents.generic.type;

import java.util.Optional;
import daedalusCodeComponents.generic.expression.DaedalusExpression;

@Deprecated
public class DaedalusArrayType extends DaedalusType {
	DaedalusType arrayOf;
	Optional<DaedalusExpression> size;
	
	public DaedalusArrayType(DaedalusType array,DaedalusExpression arraySize) {
		this(array);
		this.size = Optional.of(arraySize);
	}
	
	public DaedalusArrayType(DaedalusType array) {
		this.arrayOf = array;
		this.size = Optional.empty();
	}
}
