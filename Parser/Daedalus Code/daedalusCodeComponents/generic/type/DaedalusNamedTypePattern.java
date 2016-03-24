package daedalusCodeComponents.generic.type;

import java.util.Vector;

import daedalusCodeComponents.generic.DaedalusName;

public class DaedalusNamedTypePattern extends DaedalusTypePattern {
	private Vector<DaedalusName> names;
	
	public DaedalusNamedTypePattern() {
		super();
		this.names = new Vector<>();
	}
	
	@Override
	public boolean addType(DaedalusType type) {
		super.addType(type);
		return false;
	}
	
	public boolean addType(DaedalusType type,DaedalusName name) {
		addType(type);
		this.names.add(name);
		return true;
	}
	
	@Override
	public String toString() {
		String n = "";
		for(DaedalusName nm:this.names) {
			if(!n.equals("")) {
				n+=", ";
			}
			n+=nm;
		}
		
		return "Pattern: "+(super.toString()+"\tNames: "+n).trim();
	}
}
