package daedalusCodeComponents.generic.expression;

public enum DaedalusOperator {
	NONE(""),AND("&"),ANDAND("&&"),ANDEQ("&=",false),ROTRIGHT(">>>"),
	ROTRIGHTEQ(">>>=",false),ROTLEFT("<<<"), ROTLEFTEQ("<<<=",false), COMMA(","),
	DECREMENT("--",false),DIV("/"),DIVEQ("/=",false),DOT("."),EQ("=",false),COMPERE("=="),
	GREATEREQ(">="),GREATER(">"),XOR("^"),XORXOR("^^"),XOREQ("^=",false),
	INCREMENT("++",false),ACESS("[]"),LESSEQ("<="),LESS("<"),MINUS("-"),
	MINUSEQ("-=",false),MOD("%"),MODEQ("%="),NOTEQ("!="),NOT("!",false),OR("|"),
	OREQ("|="),OROR("||"),PLUS("+"),PLUSEQ("+=",false),SHIFTLEFT("<<"),
	SHIFTLEFTEQ("<<=",false),SHIFTRIGHT(">>"),SHIFTRIGHTEQ(">>=",false),MUL("*"),
	MULEQ("*=",false),POW("**",false),POWEQ("**=",false),NEG("~",false);
	
	private String opCode;
	private boolean isLeftAssociativ;
	private DaedalusOperator(String val) {
		this.opCode = val;
		this.isLeftAssociativ = true;
	}
	
	private DaedalusOperator(String val,boolean left) {
		this.opCode = val;
		this.isLeftAssociativ = left;
	}
	
	public boolean isLeftAssociativ() {
		return this.isLeftAssociativ;
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
