package codeNode;

import java.util.HashMap;

public class Variable {
	private static HashMap<String, Variable> varMap;
	static {
		varMap = new HashMap<>();
	}
	
	private HashMap<Integer, Integer> indexMap;
	
	public static Variable getVar(String name) {
		Variable out = varMap.get(name);
		if(out==null) {
			out = new Variable();
			varMap.put(name, out);
		}
		return out;
	}
	
	
	public Variable() {
		this.indexMap = new HashMap<>();
	}
	
	public void assign(int index,int value) {
		if(index<0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		this.indexMap.put(new Integer(index), new Integer(value));
	}
	
	public void assign(int value) {
		this.indexMap.put(new Integer(0), new Integer(value));
	}
	
	public int get() {
		Integer ret = this.indexMap.get(new Integer(0));
		return ret==null ? 0 : ret.intValue();
	}
	
	public int get(int index) {
		if(index<0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		Integer ret = this.indexMap.get(new Integer(index));
		return ret==null ? 0 : ret.intValue();
	}


	public static void printAll() {
		String out = "Vars:\n";
		for(String var:varMap.keySet()) {
			out+="\t"+var+"\n";
			for(Integer index:varMap.get(var).indexMap.keySet()) {
				out+= "\t\t"+index.intValue()+" = "+varMap.get(var).get(index.intValue())+"\n";
			}
			out+="\n";
		}
		System.out.println(out.trim());
	}
}
