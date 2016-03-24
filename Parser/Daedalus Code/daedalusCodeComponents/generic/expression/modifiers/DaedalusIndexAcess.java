package daedalusCodeComponents.generic.expression.modifiers;

import java.util.Vector;

import daedalusCodeComponents.generic.expression.DaedalusExpression;
import support.Tools;

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
		String out = Tools.list("[", "]", this.slices);
		return super.toString() + this.index+out;
	}
}
