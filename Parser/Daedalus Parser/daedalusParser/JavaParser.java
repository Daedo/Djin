//===========================================================================
//
//  Parsing Expression Grammar for Java 1.6 as a parboiled parser.
//  Based on Chapters 3 and 18 of Java Language Specification, Third Edition (JLS)
//  at http://java.sun.com/docs/books/jls/third_edition/html/j3TOC.html.
//
//---------------------------------------------------------------------------
//
//  Copyright (C) 2010 by Mathias Doenitz
//  Based on the Mouse 1.3 grammar for Java 1.6, which is
//  Copyright (C) 2006, 2009, 2010, 2011 by Roman R Redziejowski (www.romanredz.se).
//
//  The author gives unlimited permission to copy and distribute
//  this file, with or without modifications, as long as this notice
//  is preserved, and any changes are properly documented.
//
//  This file is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//---------------------------------------------------------------------------
//
//  Change log
//    2006-12-06 Posted on Internet.
//    2009-04-04 Modified to conform to Mouse syntax:
//               Underscore removed from names
//               \f in Space replaced by Unicode for FormFeed.
//    2009-07-10 Unused rule THREADSAFE removed.
//    2009-07-10 Copying and distribution conditions relaxed by the author.
//    2010-01-28 Transcribed to parboiled
//    2010-02-01 Fixed problem in rule "FormalParameterDecls"
//    2010-03-29 Fixed problem in "annotation"
//    2010-03-31 Fixed problem in unicode escapes, String literals and line comments
//               (Thanks to Reinier Zwitserloot for the finds)
//    2010-07-26 Fixed problem in LocalVariableDeclarationStatement (accept annotations),
//               HexFloat (HexSignificant) and AnnotationTypeDeclaration (bug in the JLS!)
//    2010-10-07 Added full support of Unicode Identifiers as set forth in the JLS
//               (Thanks for Ville Peurala for the patch)
//    2011-07-23 Transcribed all missing fixes from Romans Mouse grammar (http://www.romanredz.se/papers/Java.1.6.peg)
//
//===========================================================================

package daedalusParser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.*;

import daedalusParser.components.LetterMatcher;
import daedalusParser.components.LetterOrDigitMatcher;
import daedalusParser.components.LiteralMatcher;

@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class JavaParser extends BaseParser<Object> {
	static LiteralMatcher lit = new LiteralMatcher();

	//-------------------------------------------------------------------------
	//  Compilation Unit
	//-------------------------------------------------------------------------

	public Rule CompilationUnit() {
		return Sequence(
				Spacing(),
				ZeroOrMore(ImportDeclaration()),
				ZeroOrMore(CodeDeclaration()),
				EOI
				);
	}

	Rule ImportDeclaration() {
		return Sequence(
				IMPORT,
				Optional(STATIC),
				QualifiedIdentifier(),
				Optional(DOT, STAR),
				SEMI
				);
	}

	Rule CodeDeclaration() {
		return FirstOf(
				MemberDecl(),
				SEMI
				);
	}

	Rule MemberDecl() {
		return FirstOf(
				Sequence(Type(), Identifier(), MethodDeclaratorRest()),
				Sequence(Type(), VariableDeclarators(), SEMI),
				Sequence(VOID, Identifier(), VoidMethodDeclaratorRest())
				);
	}

	Rule MethodDeclaratorRest() {
		return Sequence(
				FormalParameters(),
				ZeroOrMore(Dim()),
				Optional(THROWS, ClassTypeList()),
				FirstOf(MethodBody(), SEMI)
				);
	}

	Rule VoidMethodDeclaratorRest() {
		return Sequence(
				FormalParameters(),
				Optional(THROWS, ClassTypeList()),
				FirstOf(MethodBody(), SEMI)
				);
	}

	Rule ConstructorDeclaratorRest() {
		return Sequence(FormalParameters(), Optional(THROWS, ClassTypeList()), MethodBody());
	}

	Rule MethodBody() {
		return Block();
	}


	//-------------------------------------------------------------------------
	//  Variable Declarations
	//-------------------------------------------------------------------------    

	Rule LocalVariableDeclarationStatement() {
		return Sequence(ZeroOrMore(FINAL), Type(), VariableDeclarators(), SEMI);
	}

	Rule VariableDeclarators() {
		return Sequence(VariableDeclarator(), ZeroOrMore(COMMA, VariableDeclarator()));
	}

	Rule VariableDeclarator() {
		return Sequence(Identifier(), ZeroOrMore(Dim()), Optional(EQU, VariableInitializer()));
	}

	//-------------------------------------------------------------------------
	//  Formal Parameters
	//-------------------------------------------------------------------------

	Rule FormalParameters() {
		return Sequence(LPAR, Optional(FormalParameterDecls()), RPAR);
	}

	Rule FormalParameter() {
		return Sequence(ZeroOrMore(FINAL), Type(), VariableDeclaratorId());
	}

	Rule FormalParameterDecls() {
		return Sequence(ZeroOrMore(FINAL), Type(), FormalParameterDeclsRest());
	}

	Rule FormalParameterDeclsRest() {
		return FirstOf(
				Sequence(VariableDeclaratorId(), Optional(COMMA, FormalParameterDecls())),
				Sequence(ELLIPSIS, VariableDeclaratorId())
				);
	}

	Rule VariableDeclaratorId() {
		return Sequence(Identifier(), ZeroOrMore(Dim()));
	}

	//-------------------------------------------------------------------------
	//  Statements
	//-------------------------------------------------------------------------    

	Rule Block() {
		return Sequence(LWING, BlockStatements(), RWING);
	}

	Rule BlockStatements() {
		return ZeroOrMore(BlockStatement());
	}

	Rule BlockStatement() {
		return FirstOf(
				LocalVariableDeclarationStatement(),
				Statement()
				);
	}

	Rule Statement() {
		return FirstOf(
				Block(),
				Sequence(IF, ParExpression(), Statement(), Optional(ELSE, Statement())),
				Sequence(FOR, LPAR, Optional(ForInit()), SEMI, Optional(Expression()), SEMI, Optional(ForUpdate()),
						RPAR, Statement()),
				Sequence(FOR, LPAR, FormalParameter(), COLON, Expression(), RPAR, Statement()),
				Sequence(WHILE, ParExpression(), Statement()),
				Sequence(DO, Statement(), WHILE, ParExpression(), SEMI),
				Sequence(TRY, Block(),
						FirstOf(Sequence(OneOrMore(Catch_()), Optional(Finally_())), Finally_())),
				Sequence(SWITCH, ParExpression(), LWING, SwitchBlockStatementGroups(), RWING),
				Sequence(RETURN, Optional(Expression()), SEMI),
				Sequence(THROW, Expression(), SEMI),
				Sequence(BREAK, Optional(Identifier()), SEMI),
				Sequence(CONTINUE, Optional(Identifier()), SEMI),
				Sequence(Sequence(Identifier(), COLON), Statement()),
				Sequence(StatementExpression(), SEMI),
				SEMI
				);
	}

	Rule Catch_() {
		return Sequence(CATCH, LPAR, FormalParameter(), RPAR, Block());
	}

	Rule Finally_() {
		return Sequence(FINALLY, Block());
	}

	Rule SwitchBlockStatementGroups() {
		return ZeroOrMore(SwitchBlockStatementGroup());
	}

	Rule SwitchBlockStatementGroup() {
		return Sequence(SwitchLabel(), BlockStatements());
	}

	Rule SwitchLabel() {
		return FirstOf(
				Sequence(CASE, ConstantExpression(), COLON),
				Sequence(CASE, EnumConstantName(), COLON),
				Sequence(DEFAULT, COLON)
				);
	}

	Rule ForInit() {
		return FirstOf(
				Sequence(ZeroOrMore(FINAL), Type(), VariableDeclarators()),
				Sequence(StatementExpression(), ZeroOrMore(COMMA, StatementExpression()))
				);
	}

	Rule ForUpdate() {
		return Sequence(StatementExpression(), ZeroOrMore(COMMA, StatementExpression()));
	}

	Rule EnumConstantName() {
		return Identifier();
	}

	//-------------------------------------------------------------------------
	//  Expressions
	//-------------------------------------------------------------------------

	// The following is more generous than the definition in section 14.8,
	// which allows only specific forms of Expression.

	Rule StatementExpression() {
		return Expression();
	}

	Rule ConstantExpression() {
		return Expression();
	}

	// The following definition is part of the modification in JLS Chapter 18
	// to minimize look ahead. In JLS Chapter 15.27, Expression is defined
	// as AssignmentExpression, which is effectively defined as
	// (LeftHandSide AssignmentOperator)* ConditionalExpression.
	// The following is obtained by allowing ANY ConditionalExpression
	// as LeftHandSide, which results in accepting statements like 5 = a.

	Rule Expression() {
		return Sequence(
				ConditionalExpression(),
				ZeroOrMore(AssignmentOperator(), ConditionalExpression())
				);
	}

	Rule AssignmentOperator() {
		return FirstOf(EQU, PLUSEQU, MINUSEQU, STAREQU, STARSTAREQU, DIVEQU, ANDEQU, OREQU, HATEQU, MODEQU, SLEQU, SREQU, BSREQU,BSLEQU);
	}

	// x ? y : z
	Rule ConditionalExpression() {
		return Sequence(
				ConditionalOrExpression(),
				ZeroOrMore(QUERY, Expression(), COLON, ConditionalOrExpression())
				);
	}

	Rule ConditionalOrExpression() {
		return Sequence(
				ConditionalAndExpression(),
				ZeroOrMore(OROR, ConditionalAndExpression())
				);
	}

	Rule ConditionalAndExpression() {
		return Sequence(
				ConditionalXorExpression(),
				ZeroOrMore(ANDAND, ConditionalXorExpression())
				);
	}

	Rule ConditionalXorExpression() {
		return Sequence(
				InclusiveOrExpression(),
				ZeroOrMore(HATHAT,InclusiveOrExpression())
				);
	}

	Rule InclusiveOrExpression() {
		return Sequence(
				ExclusiveOrExpression(),
				ZeroOrMore(OR, ExclusiveOrExpression())
				);
	}

	Rule ExclusiveOrExpression() {
		return Sequence(
				AndExpression(),
				ZeroOrMore(HAT, AndExpression())
				);
	}

	Rule AndExpression() {
		return Sequence(
				EqualityExpression(),
				ZeroOrMore(AND, EqualityExpression())
				);
	}

	Rule EqualityExpression() {
		return Sequence(
				RelationalExpression(),
				ZeroOrMore(FirstOf(EQUAL, NOTEQUAL), RelationalExpression())
				);
	}

	Rule RelationalExpression() {
		return Sequence(
				ShiftExpression(),
				ZeroOrMore(
						FirstOf(
								Sequence(FirstOf(LE, GE, LT, GT), ShiftExpression()),
								Sequence(INSTANCEOF, ReferenceType())
								)
						)
				);
	}

	Rule ShiftExpression() {
		return Sequence(
				AdditiveExpression(),
				ZeroOrMore(FirstOf(SL, SR, BSR, BSL), AdditiveExpression())
				);
	}

	Rule AdditiveExpression() {
		return Sequence(
				MultiplicativeExpression(),
				ZeroOrMore(FirstOf(PLUS, MINUS), MultiplicativeExpression())
				);
	}

	Rule MultiplicativeExpression() {
		return Sequence(
				Power(),
				ZeroOrMore(FirstOf(STAR, DIV, MOD), Power())
				);
	}

	Rule Power() {
		return Sequence(
				UnaryExpression(),
				ZeroOrMore(STARSTAR,UnaryExpression())
				);
	}

	Rule UnaryExpression() {
		return FirstOf(
				Sequence(PrefixOp(), UnaryExpression()),
				Sequence(LPAR, Type(), RPAR, UnaryExpression()),
				Sequence(Primary(), ZeroOrMore(Selector()), ZeroOrMore(PostFixOp()))
				);
	}

	Rule Primary() {
		return FirstOf(
				ParExpression(),
				Sequence(
						NonWildcardTypeArguments(),
						FirstOf(ExplicitGenericInvocationSuffix(), Sequence(THIS, Arguments()))
						),
				Sequence(THIS, Optional(Arguments())),
				Sequence(SUPER, SuperSuffix()),
				Literal(),
				Sequence(NEW, Creator()),
				Sequence(QualifiedIdentifier(), Optional(IdentifierSuffix())),
				Sequence(BasicType(), ZeroOrMore(Dim()), DOT, CLASS),
				Sequence(VOID, DOT, CLASS)
				);
	}

	Rule IdentifierSuffix() {
		return FirstOf(
				OneOrMore(DimSlice()),
				Arguments(),
				Sequence(
						DOT,
						FirstOf(
								CLASS,
								ExplicitGenericInvocation(),
								THIS,
								Sequence(SUPER, Arguments())
								)
						)
				);
	}

	Rule ExplicitGenericInvocation() {
		return Sequence(NonWildcardTypeArguments(), ExplicitGenericInvocationSuffix());
	}

	Rule NonWildcardTypeArguments() {
		return Sequence(LPOINT, ReferenceType(), ZeroOrMore(COMMA, ReferenceType()), RPOINT);
	}

	Rule ExplicitGenericInvocationSuffix() {
		return FirstOf(
				Sequence(SUPER, SuperSuffix()),
				Sequence(Identifier(), Arguments())
				);
	}

	Rule PrefixOp() {
		return FirstOf(INC, DEC, BANG, TILDA, PLUS, MINUS);
	}

	Rule PostFixOp() {
		return FirstOf(INC, DEC);
	}

	Rule Selector() {
		return FirstOf(
				Sequence(DOT, Identifier(), Optional(Arguments())),
				Sequence(DOT, ExplicitGenericInvocation()),
				Sequence(DOT, THIS),
				Sequence(DOT, SUPER, SuperSuffix()),
				DimSlice()
				);
	}

	Rule SuperSuffix() {
		return FirstOf(Arguments(), Sequence(DOT, Identifier(), Optional(Arguments())));
	}

	@MemoMismatches
	Rule BasicType() {
		return Sequence(
				FirstOf(
						Sequence(
								Optional(UNSIGNED),
								FirstOf("byte", "short", "char", "int", "long")
								),
						"float", "double", "boolean"
						),
				TestNot(LetterOrDigit()),
				Spacing()
				);
	}

	Rule Arguments() {
		return Sequence(
				LPAR,
				Optional(Expression(), ZeroOrMore(COMMA, Expression())),
				RPAR
				);
	}

	Rule Creator() {
		return Sequence(Optional(NonWildcardTypeArguments()), FirstOf(ClassType(), BasicType()), ArrayCreatorRest());
	}


	// The following is more generous than JLS 15.10. According to that definition,
	// BasicType must be followed by at least one DimExpr or by ArrayInitializer.
	Rule ArrayCreatorRest() {
		return Sequence(
				LBRK,
				FirstOf(
						Sequence(RBRK, ZeroOrMore(Dim()), ArrayInitializer()),
						Sequence(Expression(), RBRK, ZeroOrMore(DimExpr()), ZeroOrMore(Dim()))
						)
				);
	}

	Rule ArrayInitializer() {
		return Sequence(
				LWING,
				Optional(
						VariableInitializer(),
						ZeroOrMore(COMMA, VariableInitializer())
						),
				Optional(COMMA),
				RWING
				);
	}

	Rule VariableInitializer() {
		return FirstOf(ArrayInitializer(), Expression());
	}

	Rule ParExpression() {
		return Sequence(LPAR, Expression(), RPAR);
	}

	Rule QualifiedIdentifier() {
		return Sequence(Identifier(), ZeroOrMore(DOT, Identifier()));
	}

	Rule Dim() {
		return Sequence(LBRK, RBRK);
	}

	Rule DimExpr() {
		return Sequence(LBRK, Expression(), RBRK);
	}

	Rule DimSlice() {
		return Sequence(LBRK,Slice(),ZeroOrMore(COMMA,Slice()),RBRK);
	}

	Rule Slice() {
		return 	Sequence(
					FirstOf(Expression(),Test(COLON)),
					Optional(
						COLON,
						Optional(Expression()),
						Optional(
								COLON,
								Optional(Expression())
								)
						)
				);
	}

	//-------------------------------------------------------------------------
	//  Types and Modifiers
	//-------------------------------------------------------------------------

	Rule Type() {
		return Sequence(FirstOf(BasicType(), ClassType()), ZeroOrMore(Dim()));
	}

	Rule ReferenceType() {
		return FirstOf(
				Sequence(BasicType(), OneOrMore(Dim())),
				Sequence(ClassType(), ZeroOrMore(Dim()))
				);
	}

	Rule ClassType() {
		return Sequence(
				Identifier(), Optional(TypeArguments()),
				ZeroOrMore(DOT, Identifier(), Optional(TypeArguments()))
				);
	}

	Rule ClassTypeList() {
		return Sequence(ClassType(), ZeroOrMore(COMMA, ClassType()));
	}

	Rule TypeArguments() {
		return Sequence(LPOINT, TypeArgument(), ZeroOrMore(COMMA, TypeArgument()), RPOINT);
	}

	Rule TypeArgument() {
		return FirstOf(
				ReferenceType(),
				Sequence(QUERY, Optional(FirstOf(EXTENDS, SUPER), ReferenceType()))
				);
	}

	Rule TypeParameters() {
		return Sequence(LPOINT, TypeParameter(), ZeroOrMore(COMMA, TypeParameter()), RPOINT);
	}

	Rule TypeParameter() {
		return Sequence(Identifier(), Optional(EXTENDS, Bound()));
	}

	Rule Bound() {
		return Sequence(ClassType(), ZeroOrMore(AND, ClassType()));
	}

	//-------------------------------------------------------------------------
	//  JLS 3.6-7  Spacing
	//-------------------------------------------------------------------------

	@SuppressNode
	Rule Spacing() {
		return ZeroOrMore(FirstOf(

				// whitespace
				OneOrMore(AnyOf(" \t\r\n\f").label("Whitespace")),

				// traditional comment
				Sequence("/*", ZeroOrMore(TestNot("*/"), ANY), "*/"),

				// end of line comment
				Sequence(
						"//",
						ZeroOrMore(TestNot(AnyOf("\r\n")), ANY),
						FirstOf("\r\n", '\r', '\n', EOI)
						)
				));
	}

	//-------------------------------------------------------------------------
	//  JLS 3.8  Identifiers
	//-------------------------------------------------------------------------

	@SuppressSubnodes
	@MemoMismatches
	Rule Identifier() {
		return Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit()), Spacing());
	}

	// JLS defines letters and digits as Unicode characters recognized
	// as such by special Java procedures.

	Rule Letter() {
		// switch to this "reduced" character space version for a ~10% parser performance speedup
		//return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), '_', '$');
		return FirstOf(Sequence('\\', lit.UnicodeEscape()), new LetterMatcher());
	}

	@MemoMismatches
	Rule LetterOrDigit() {
		// switch to this "reduced" character space version for a ~10% parser performance speedup
		//return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), CharRange('0', '9'), '_', '$');
		return FirstOf(Sequence('\\', lit.UnicodeEscape()), new LetterOrDigitMatcher());
	}

	//-------------------------------------------------------------------------
	//  JLS 3.9  Keywords
	//-------------------------------------------------------------------------
	@MemoMismatches
	Rule Keyword() {
		return Sequence(
				FirstOf("assert", "break", "case", "catch", "class", "const", "continue", "default", "do", "elif", "else",
						"enum", "extends", "finally", "final", "for", "forall", "goto", "if", "in", "implements", "import", "interface",
						"instanceof", "new", "package", "return", "static", "super", "switch", "synchronized", "this",
						"throws", "throw", "try", "void", "while"),
				TestNot(LetterOrDigit())
				);
	}

	public final Rule ASSERT = Keyword("assert");
	public final Rule BREAK = Keyword("break");
	public final Rule CASE = Keyword("case");
	public final Rule CATCH = Keyword("catch");
	public final Rule CLASS = Keyword("class");
	public final Rule CONTINUE = Keyword("continue");
	public final Rule DEFAULT = Keyword("default");
	public final Rule DO = Keyword("do");
	public final Rule ELIF = Keyword("elif");
	public final Rule ELSE = Keyword("else");
	public final Rule ENUM = Keyword("enum");
	public final Rule EXTENDS = Keyword("extends");
	public final Rule FINALLY = Keyword("finally");
	public final Rule FINAL = Keyword("final");
	public final Rule FOR = Keyword("for");
	public final Rule FORALL = Keyword("forall");
	public final Rule IF = Keyword("if");
	public final Rule IN = Keyword("in");
	public final Rule IMPLEMENTS = Keyword("implements");
	public final Rule IMPORT = Keyword("import");
	public final Rule INTERFACE = Keyword("interface");
	public final Rule INSTANCEOF = Keyword("instanceof");
	public final Rule NEW = Keyword("new");
	public final Rule PACKAGE = Keyword("package");
	public final Rule RETURN = Keyword("return");
	public final Rule STATIC = Keyword("static");
	public final Rule SUPER = Keyword("super");
	public final Rule SWITCH = Keyword("switch");
	public final Rule SYNCHRONIZED = Keyword("synchronized");
	public final Rule THIS = Keyword("this");
	public final Rule THROWS = Keyword("throws");
	public final Rule THROW = Keyword("throw");
	public final Rule TRY = Keyword("try");
	public final Rule VOID = Keyword("void");
	public final Rule WHILE = Keyword("while");

	public final Rule UNSIGNED = Keyword("unsigned");

	@SuppressNode
	@DontLabel
	Rule Keyword(String keyword) {
		return Terminal(keyword, LetterOrDigit());
	}

	//-------------------------------------------------------------------------
	//  JLS 3.10  Literals
	//-------------------------------------------------------------------------

	Rule Literal() {
		return Sequence(
				FirstOf(
						lit.FloatLiteral(),
						lit.IntegerLiteral(),
						lit.CharLiteral(),
						lit.StringLiteral(),
						Sequence("true", TestNot(LetterOrDigit())),
						Sequence("false", TestNot(LetterOrDigit())),
						Sequence("null", TestNot(LetterOrDigit()))
						),
				Spacing()
				);
	}

	//-------------------------------------------------------------------------
	//  JLS 3.11-12  Separators, Operators
	//-------------------------------------------------------------------------

	final Rule AT = Terminal("@");
	final Rule AND = Terminal("&", AnyOf("=&"));
	final Rule ANDAND = Terminal("&&");
	final Rule ANDEQU = Terminal("&=");
	final Rule BANG = Terminal("!", Ch('='));
	final Rule BSR = Terminal(">>>", Ch('='));
	final Rule BSREQU = Terminal(">>>=");
	final Rule BSL = Terminal("<<<", Ch('='));
	final Rule BSLEQU = Terminal("<<<=");
	final Rule COLON = Terminal(":");
	final Rule COMMA = Terminal(",");
	final Rule DEC = Terminal("--");
	final Rule DIV = Terminal("/", Ch('='));
	final Rule DIVEQU = Terminal("/=");
	final Rule DOT = Terminal(".");
	final Rule ELLIPSIS = Terminal("...");
	final Rule EQU = Terminal("=", Ch('='));
	final Rule EQUAL = Terminal("==");
	final Rule GE = Terminal(">=");
	final Rule GT = Terminal(">", AnyOf("=>"));
	final Rule HAT = Terminal("^", AnyOf("^="));
	final Rule HATHAT =  Terminal("^^");
	final Rule HATEQU = Terminal("^=");
	final Rule INC = Terminal("++");
	final Rule LBRK = Terminal("[");
	final Rule LE = Terminal("<=");
	final Rule LPAR = Terminal("(");
	final Rule LPOINT = Terminal("<");
	final Rule LT = Terminal("<", AnyOf("=<"));
	final Rule LWING = Terminal("{");
	final Rule MINUS = Terminal("-", AnyOf("=-"));
	final Rule MINUSEQU = Terminal("-=");
	final Rule MOD = Terminal("%", Ch('='));
	final Rule MODEQU = Terminal("%=");
	final Rule NOTEQUAL = Terminal("!=");
	final Rule OR = Terminal("|", AnyOf("=|"));
	final Rule OREQU = Terminal("|=");
	final Rule OROR = Terminal("||");
	final Rule PLUS = Terminal("+", AnyOf("=+"));
	final Rule PLUSEQU = Terminal("+=");
	final Rule QUERY = Terminal("?");
	final Rule RBRK = Terminal("]");
	final Rule RPAR = Terminal(")");
	final Rule RPOINT = Terminal(">");
	final Rule RWING = Terminal("}");
	final Rule SEMI = Terminal(";");
	final Rule SL = Terminal("<<", AnyOf("=<"));
	final Rule SLEQU = Terminal("<<=");
	final Rule SR = Terminal(">>", AnyOf("=>"));
	final Rule SREQU = Terminal(">>=");
	final Rule STAR = Terminal("*", AnyOf("*="));
	final Rule STAREQU = Terminal("*=");
	final Rule STARSTAR = Terminal("**",Ch('='));
	final Rule STARSTAREQU = Terminal("**=");
	final Rule TILDA = Terminal("~");

	//-------------------------------------------------------------------------
	//  helper methods
	//-------------------------------------------------------------------------

	@Override
	protected Rule fromCharLiteral(char c) {
		// turn of creation of parse tree nodes for single characters
		return super.fromCharLiteral(c).suppressNode();
	}

	@SuppressNode
	@DontLabel
	Rule Terminal(String string) {
		return Sequence(string, Spacing()).label('\'' + string + '\'');
	}

	@SuppressNode
	@DontLabel
	Rule Terminal(String string, Rule mustNotFollow) {
		return Sequence(string, TestNot(mustNotFollow), Spacing()).label('\'' + string + '\'');
	}

}
