package daedalusCodeComponents.statements;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusVarDecleration extends DaedalusStatement {
	Vector<DaedalusName> names;
	DaedalusType type;
	Optional<DaedalusExpression> value;
	
	public DaedalusVarDecleration(DaedalusType t) {
		this.type = t;
		
		this.names = new Vector<>();
		this.value = Optional.empty();
	}
	
	public boolean addName(DaedalusName name) {
		this.names.add(name);
		return true;
	}
	
	public boolean setExpression(DaedalusExpression val) {
		if(val!=null) {
			this.value = Optional.of(val);
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(DaedalusName n:this.names) {
			if(!s.equals("")) {
				s+= ", ";
			}
			s+=n;
		}
		if(this.value.isPresent()) {
			s+= " = "+this.value.get();
		}
		
		return super.toString()+" Var Decleration "+type+" "+s;
	}
}
