package daedalusCodeComponents.generic.codeConstruct;

import java.util.Optional;

import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.expression.DaedalusOperator;
import daedalusCodeComponents.generic.type.DaedalusNamedTypePattern;
import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusCodeComponents.statements.DaedalusBlock;

public class DaedalusOperatorOverload extends DaedalusSyntaxElement{
	DaedalusType returnType;
	DaedalusBlock code;
	DaedalusOperator identifier;
	Optional<DaedalusNamedTypePattern> arguments;
	
	public DaedalusOperatorOverload(DaedalusType ret,DaedalusOperator id,DaedalusNamedTypePattern arg,DaedalusBlock block) {
		if(arg==null) {
			this.arguments = Optional.empty();
		} else {
			this.arguments = Optional.of(arg);
		}
		
		this.returnType = ret;
		this.identifier = id;
		this.code = block;
	}
}
