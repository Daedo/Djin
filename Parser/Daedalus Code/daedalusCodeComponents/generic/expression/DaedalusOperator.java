package daedalusCodeComponents.generic.expression;

public enum DaedalusOperator {
	NONE(""),AND("&"),ANDAND("&&"),ANDEQ("&="),ROTRIGHT(">>>"),
	ROTRIGHTEQ(">>>="),ROTLEFT("<<<"), ROTLEFTEQ("<<<="), COMMA(","),
	DECREMENT("--"),DIV("/"),DIVEQ("/="),DOT("."),EQ("="),COMPERE("=="),
	GREATEREQ(">="),GREATER(">"),XOR("^"),XORXOR("^^"),XOREQ("^="),
	INCREMENT("++"),ACESS("[]"),LESSEQ("<="),LESS("<"),MINUS("-"),
	MINUSEQ("-="),MOD("%"),MODEQ("%="),NOTEQ("!="),NOT("!"),OR("|"),
	OREQ("|="),OROR("||"),PLUS("+"),PLUSEQ("+="),SHIFTLEFT("<<"),
	SHIFTLEFTEQ("<<="),SHIFTRIGHT(">>"),SHIFTRIGHTEQ(">>="),MUL("*"),
	MULEQ("*="),POW("**"),POWEQ("**="),NEG("~");
	
	private String opCode;
	private DaedalusOperator(String val) {
		this.opCode = val;
	}
	
	public static DaedalusOperator fromString(String val) {
		for (DaedalusOperator op : DaedalusOperator.values()) {
			if(op.opCode.equals(val)) {
				return op;
			}
		}
		return NONE;
	}
	
	@Override
	public String toString() {
		return this.opCode;
	}
}
