package daedalusCodeComponents.statements;

import java.util.Vector;

public class DaedalusBlock extends DaedalusStatement {
	private Vector<DaedalusStatement> lines;
	
	public DaedalusBlock() {
		this.lines = new Vector<>();
	}
	
	public boolean addStatement(DaedalusStatement statement){
		this.lines.add(statement);
		return true;
	}
}
