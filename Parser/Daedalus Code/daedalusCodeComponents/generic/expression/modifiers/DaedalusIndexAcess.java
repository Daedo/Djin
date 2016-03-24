package daedalusCodeComponents.generic.expression.modifiers;

import java.util.Vector;

import daedalusCodeComponents.generic.expression.DaedalusExpression;

public class DaedalusIndexAcess extends DaedalusExpression {
	private Vector<DaedalusSlice> slices;
	private DaedalusExpression index;
	
	public DaedalusIndexAcess() {
		this.slices = new Vector<>();
	}
	
	public boolean addSlice(DaedalusSlice slice) {
		this.slices.add(slice);
		return true;
	}
	
	public boolean setIndex(DaedalusExpression expr) {
		this.index = expr;
		return true;
	}
	
	@Override
	public String toString() {
		String out = "[";
		for(DaedalusSlice s:this.slices) {
			if(!out.equals("")) {
				out+=", ";
			}
			out+=s.toString();
		}
		out+="]";
		return super.toString() + this.index+out;
	}
}
