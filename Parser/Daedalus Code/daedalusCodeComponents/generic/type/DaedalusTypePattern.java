package daedalusCodeComponents.generic.type;

import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;

public class DaedalusTypePattern extends DaedalusSyntaxElement {
	private Vector<DaedalusType> types;
	
	public DaedalusTypePattern() {
		this.types = new Vector<>();
	}
	
	public boolean addType(DaedalusType type) {
		this.types.add(type);
		System.out.println(this.toString());
		return true;
	}
	
	@Override
	public String toString() {
		String out = "";
		for(DaedalusType t:this.types) {
			if(!out.equals("")) {
				out+=", ";
			}
			out+=t;
		}
		return out;
	}
}
