package daedalusCodeComponents.generic.type;

import java.util.List;
import java.util.Vector;

import daedalusCodeComponents.DaedalusSyntaxElement;

public abstract class DaedalusType extends DaedalusSyntaxElement{
	private Vector<DaedalusTypeModifier> modifiers;
	private Vector<DaedalusArrayDimension> dimensions;
	
	public DaedalusType() {
		this.modifiers = new Vector<>();
		this.dimensions = new Vector<>();
	}
	
	public void addModifier(DaedalusTypeModifier modifier) {
		this.modifiers.add(modifier);
	}
	
	public boolean addModifiers(List<DaedalusTypeModifier> mod) {
		for(DaedalusTypeModifier m:mod) {
			addModifier(m);
		}
		return true;
	}
	
	public boolean addDimension(DaedalusArrayDimension dim) {
		this.dimensions.add(dim);
		return true;
	}
	
	
	@Override
	public String toString() {
		String m = "";
		for(DaedalusTypeModifier mod:this.modifiers) {
			m+= mod.toString()+" ";
		}
		
		
		for(DaedalusArrayDimension dim:this.dimensions) {
			m+=dim.toString()+" ";
		}
		m = m.trim();
		
		return "Type "+m;
	}
}
