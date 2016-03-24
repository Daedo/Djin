package daedalusCodeComponents.generic.type;

import java.util.Optional;

import daedalusCodeComponents.generic.DaedalusName;

public class DaedalusSimpleType extends DaedalusType {
	private DaedalusTypeLiteral value;
	private Optional<DaedalusName> typeIdentifier;
	
	public DaedalusSimpleType(DaedalusTypeLiteral lit) {
		this.value = lit;
		this.typeIdentifier = Optional.empty();
	}
	
	public DaedalusSimpleType(DaedalusTypeLiteral lit,DaedalusName typeIdent) {
		this(lit);
		if(typeIdent!=null) {
			this.typeIdentifier = Optional.of(typeIdent);
		} else {
			this.typeIdentifier = Optional.empty();
		}
	}
	
	@Override
	public String toString() {
		String t = this.value.toString();
		
		if(this.typeIdentifier.isPresent()) {
			t+= "("+this.typeIdentifier.get()+")";
		}
		
		return super.toString()+" "+t;
	}
}
