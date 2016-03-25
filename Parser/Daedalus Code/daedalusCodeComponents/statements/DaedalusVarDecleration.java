package daedalusCodeComponents.statements;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.type.DaedalusType;

public class DaedalusVarDecleration extends DaedalusStatement {
	Vector<DaedalusName> names;
	DaedalusType type;
	Vector<Optional<DaedalusExpression>> value;
	
	public DaedalusVarDecleration(DaedalusType t) {
		this.type = t;
		
		this.names = new Vector<>();
		this.value = new Vector<>();
	}
	
	public boolean addName(DaedalusName name) {
		this.names.add(name);
		this.value.add(Optional.empty());
		return true;
	}
	
	public boolean addName(DaedalusName name,DaedalusExpression expr) {
		if(expr==null) {
			return addName(name);
		}
		this.names.add(name);
		this.value.add(Optional.of(expr));
		return true;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i=0;i<this.names.size();i++) {
			DaedalusName n = this.names.get(i);
			Optional<DaedalusExpression> e = this.value.get(i);
			
			if(!s.equals("")) {
				s+= ", ";
			}
			s+=n;
			
			if(e.isPresent()) {
				s+=" = "+e.get();
			}
			
		}

		
		return super.toString()+" Var Decleration "+type+" "+s;
	}
}
