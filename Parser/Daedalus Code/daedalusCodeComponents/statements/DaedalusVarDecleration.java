package daedalusCodeComponents.statements;

import java.util.Optional;
import java.util.Vector;

import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusExecution.exception.DaedalusException;
import daedalusExecution.intermediateObjects.DaedalusIntermediateValue;

public class DaedalusVarDecleration extends DaedalusStatement {
	Vector<DaedalusName> names;
	DaedalusType type;
	Vector<Optional<DaedalusExpression>> values;
	
	public DaedalusVarDecleration(DaedalusType t) {
		this.type = t;
		
		this.names = new Vector<>();
		this.values = new Vector<>();
	}
	
	public boolean addName(DaedalusName name) {
		this.names.add(name);
		this.values.add(Optional.empty());
		return true;
	}
	
	public boolean addName(DaedalusName name,DaedalusExpression expr) {
		if(expr==null) {
			return addName(name);
		}
		this.names.add(name);
		this.values.add(Optional.of(expr));
		return true;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i=0;i<this.names.size();i++) {
			DaedalusName n = this.names.get(i);
			Optional<DaedalusExpression> e = this.values.get(i);
			
			if(!s.equals("")) {
				s+= ", ";
			}
			s+=n;
			
			if(e.isPresent()) {
				s+=" = "+e.get();
			}
			
		}	
		return super.toString()+" Var Decleration "+this.type+" "+s;
	}
	
	
	@Override
	public DaedalusIntermediateValue resolve() throws DaedalusException {
		//TODO check for void values
		
		
		for(int i=0;i<this.names.size();i++) {
			DaedalusName name = this.names.get(i);
			Optional<DaedalusExpression> value = this.values.get(i);
			
			if(value.isPresent()) {
				//TODO Build Correct Dimensions
				DaedalusIntermediateValue val = value.get().resolve();
				System.out.println("Declare: "+name+" = "+val);
			}
		}
		return super.resolve();
	}
}
