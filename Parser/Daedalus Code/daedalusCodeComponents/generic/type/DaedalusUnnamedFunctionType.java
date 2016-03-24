package daedalusCodeComponents.generic.type;

public class DaedalusUnnamedFunctionType extends DaedalusType {
	private DaedalusType returnType;
	private DaedalusTypePattern inputTypes;
	
	public DaedalusUnnamedFunctionType(DaedalusType ret,DaedalusTypePattern inp) {
		this.returnType = ret;
		this.inputTypes = inp;
	}
	
	@Override
	public String toString() {
		return super.toString()+"Function "+this.returnType+"("+this.inputTypes+")";
	}
}
