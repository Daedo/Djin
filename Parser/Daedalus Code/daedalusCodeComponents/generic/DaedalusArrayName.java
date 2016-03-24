package daedalusCodeComponents.generic;

import java.util.Vector;

import daedalusCodeComponents.generic.type.DaedalusArrayDimension;
import support.Tools;

public class DaedalusArrayName extends DaedalusName{
	Vector<DaedalusArrayDimension> dimensions;
	
	public DaedalusArrayName(String name) {
		super(name);
		this.dimensions = new Vector<>();
	}
	
	public DaedalusArrayName(DaedalusName sub) {
		this(sub.value);
	}
	
	public boolean addDimension(DaedalusArrayDimension dim) {
		this.dimensions.add(dim);
		return true;
	}
	
	@Override
	public String toString() {
		String d = Tools.list(this.dimensions);
		return super.toString()+d;
	}
}
