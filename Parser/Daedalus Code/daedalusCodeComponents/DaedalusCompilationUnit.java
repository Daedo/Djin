package daedalusCodeComponents;

import java.util.Vector;

import daedalusCodeComponents.imports.DaedalusImport;

public class DaedalusCompilationUnit extends DaedalusSyntaxElement{
	private Vector<DaedalusImport> imports;
	private Vector<DaedalusSyntaxElement> statements;
	
	public DaedalusCompilationUnit() {
		this.imports = new Vector<>();
		this.statements = new Vector<>();
	}
	
	public boolean addImport(DaedalusImport imp) {
		this.imports.addElement(imp);
		return true;
	}
	
	public boolean addStatement(DaedalusSyntaxElement statement) {
		this.statements.addElement(statement);
		return true;
	}
	
	@Override	
	public String toString() {
		String out = "COMPILATION UNIT:\n\tImports:\n";
		for(DaedalusImport i:this.imports) {
			out+="\t"+i+"\n";
		}
		out+="\n\tDeclerations:\n";
		
		for(DaedalusSyntaxElement s:this.statements) {
			out+="\t"+s+"\n";
		}
		
		return out;
	}

	public int statementCount() {
		return this.statements.size();
	}
	
	public DaedalusSyntaxElement getStatement(int pos) {
		return this.statements.get(pos);
	}
}
