package daedalusCodeComponents.generic.codeConstruct;

import java.util.Optional;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.type.DaedalusNamedTypePattern;
import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusCodeComponents.statements.DaedalusBlock;

public class DaedalusMethod extends DaedalusSyntaxElement{
	DaedalusType returnType;
	DaedalusBlock code;
	DaedalusName identifier;
	Optional<DaedalusNamedTypePattern> arguments;
	
	public DaedalusMethod(DaedalusType ret,DaedalusName id,DaedalusNamedTypePattern arg,DaedalusBlock block) {
		if(arg==null) {
			this.arguments = Optional.empty();
		} else {
			this.arguments = Optional.of(arg);
		}
		
		this.returnType = ret;
		this.identifier = id;
		this.code = block;
	}
	
	@Override
	public String toString() {
		String a = "";
		if(this.arguments.isPresent()) {
			a = this.arguments.get().toString();
		}
		
		return super.toString()+" Method "+this.returnType.toString()+" "+this.identifier.toString()+"("+a+")";
	}
}
