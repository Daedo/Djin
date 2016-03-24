package codeNode;

public class VariableNode extends CalculationNode {
	String name,index;
	
	public VariableNode(String n,String id) {
		super(null,null,null);
		this.name = n;
		if(id!=null && !id.equals("")) {
			this.index = id;
		}
	}
	
	@Override
	public int getValue() {
		Variable var = Variable.getVar(this.name);
		
		if(this.index!=null) {
			return var.get(Integer.parseInt(this.index));
		}
		return var.get();
	}

	public void assign(int val) {
		Variable var = Variable.getVar(this.name);
		
		if(this.index!=null) {
			var.assign(Integer.parseInt(this.index),val);
		} else {
			var.assign(val);
		}		
	}

	@Override
	public String toString() {
		String out = this.name;
		if(this.index!=null) {
			out+="["+this.index+"]";
		}
		out+=" = "+getValue();
		return out;
	}
}
