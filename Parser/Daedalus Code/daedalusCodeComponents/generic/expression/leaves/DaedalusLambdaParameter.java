package daedalusCodeComponents.generic.expression.leaves;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.type.DaedalusNamedTypePattern;

public class DaedalusLambdaParameter extends DaedalusSyntaxElement{
	Optional<Vector<DaedalusName>> names;
	Optional<DaedalusNamedTypePattern> types;

	public DaedalusLambdaParameter() {
		this.names = Optional.of(new Vector<>());
		this.types = Optional.empty();
	}

	public DaedalusLambdaParameter(DaedalusName singleName) {
		this();
		this.names.get().add(singleName);

	}

	public DaedalusLambdaParameter(DaedalusNamedTypePattern pattern) {
		this();
		this.names = Optional.empty();
		this.types = Optional.of(pattern);
	}

	public boolean addName(DaedalusName name) {
		if(this.names.isPresent()) {
			this.names.get().add(name);
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		String out = "";
		
		if(this.names.isPresent()) {
			Vector<DaedalusName> n = this.names.get();
			for(DaedalusName name: n) {
				if(!out.equals("")) {
					out+=", ";
				}
				out+=name;
			}
			
			out = "("+out+")";
		}
		
		if(this.types.isPresent()) {
			out = this.types.get().toString();
		}
		
		return out;
	}
}
