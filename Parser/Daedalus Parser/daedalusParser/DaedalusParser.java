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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Vector;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.annotations.DontLabel;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressNode;
import org.parboiled.annotations.SuppressSubnodes;
import org.parboiled.support.Var;

import daedalusCodeComponents.DaedalusCompilationUnit;
import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.generic.DaedalusArrayName;
import daedalusCodeComponents.generic.DaedalusName;
import daedalusCodeComponents.generic.codeConstruct.DaedalusMethod;
import daedalusCodeComponents.generic.codeConstruct.DaedalusOperatorOverload;
import daedalusCodeComponents.generic.expression.DaedalusCall;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.expression.DaedalusExpressionList;
import daedalusCodeComponents.generic.expression.DaedalusOperator;
import daedalusCodeComponents.generic.expression.leaves.DaedalusConstuctorCall;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLambdaExpression;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLambdaParameter;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLiteral;
import daedalusCodeComponents.generic.expression.modifiers.DaedalusIndexAcess;
import daedalusCodeComponents.generic.expression.modifiers.DaedalusParameterList;
import daedalusCodeComponents.generic.expression.modifiers.DaedalusSlice;
import daedalusCodeComponents.generic.type.DaedalusArrayDimension;
import daedalusCodeComponents.generic.type.DaedalusNamedTypePattern;
import daedalusCodeComponents.generic.type.DaedalusSimpleType;
import daedalusCodeComponents.generic.type.DaedalusTupleType;
import daedalusCodeComponents.generic.type.DaedalusType;
import daedalusCodeComponents.generic.type.DaedalusTypeLiteral;
import daedalusCodeComponents.generic.type.DaedalusTypeModifier;
import daedalusCodeComponents.generic.type.DaedalusTypePattern;
import daedalusCodeComponents.generic.type.DaedalusUnnamedFunctionType;
import daedalusCodeComponents.imports.DaedalusImport;
import daedalusCodeComponents.imports.DaedalusImportSpecifier;
import daedalusCodeComponents.statements.DaedalusAssertion;
import daedalusCodeComponents.statements.DaedalusBlock;
import daedalusCodeComponents.statements.DaedalusIfCondition;
import daedalusCodeComponents.statements.DaedalusStatement;
import daedalusCodeComponents.statements.DaedalusVarDecleration;
import daedalusParser.combonents.LetterMatcher;
import daedalusParser.combonents.LetterOrDigitMatcher;
import daedalusParser.combonents.LiteralMatcher;


@SuppressWarnings({"InfiniteRecursion"})
@BuildParseTree
public class DaedalusParser extends BaseParser<DaedalusSyntaxElement> {
	@Override
	public String match() {
		return filterWhitespace(super.match());
	}

	private static String filterWhitespace(String inp) {
		String out = inp.trim();
		//REMOVE /**/
		out = out.replaceAll("/\\*.*?\\*/", "");
		return out;
	}

	static LiteralMatcher lit = new LiteralMatcher();

	//-------------------------------------------------------------------------
	//  Compilation Unit
	//-------------------------------------------------------------------------

	public Rule CompilationUnit() {
		Var<DaedalusCompilationUnit> unit = new Var<>();

		return Sequence(
				unit.set(new DaedalusCompilationUnit()),
				Spacing(),
				ZeroOrMore(ImportDeclaration(),unit.get().addImport((DaedalusImport) pop())),
				ZeroOrMore(
						FirstOf(
								Sequence(CodeDeclaration(),unit.get().addStatement(pop())),
								SEMI
								)
						),
				EOI,
				push(unit.get())
				);
	}

	//Import
	Rule ImportDeclaration() {
		Var<DaedalusImportSpecifier> spec= new Var<>();
		Var<String> pack = new Var<>();
		Var<DaedalusName> rename = new Var<>();
		return Sequence(
				IMPORT,
				Optional(ImportSpecifier(),spec.set((DaedalusImportSpecifier) pop())),
				lit.StringLiteral(),
				pack.set(match().trim()),
				Spacing(),
				Optional(ImportRename(),rename.set((DaedalusName) pop())),
				SEMI,
				push(new DaedalusImport(pack.get(), rename.get(), spec.get()))
				);
	}

	Rule ImportSpecifier() {
		Var<DaedalusImportSpecifier> spec = new Var<>();
		Var<DaedalusName> name = new Var<>();
		Var<DaedalusName> rename = new Var<>();

		return Sequence(
				spec.set(new DaedalusImportSpecifier()),
				Identifier(),
				name.set((DaedalusName)pop()),
				Optional(ImportRename(),rename.set((DaedalusName)pop())),
				spec.get().addComponent(name.get(), rename.get()),
				rename.clear(),
				ZeroOrMore(
						COMMA,
						Identifier(),
						name.set((DaedalusName)pop()),
						Optional(ImportRename(),rename.set((DaedalusName)pop())),
						spec.get().addComponent(name.get(), rename.get()),
						rename.clear()
						)
				,FROM,
				push(spec.get()));
	}

	Rule ImportRename() {
		return Sequence(AS,Identifier());
	}

	//Method,Operation,VarDecl
	Rule CodeDeclaration() {
		return FirstOf(
				MethodDecl(),
				VarDecl(),
				OperatorDecl()
				);
	}

	Rule OperatorDecl() {
		Var<DaedalusType> t = new Var<>();
		Var<DaedalusOperator> op = new Var<>();
		Var<DaedalusNamedTypePattern> p = new Var<>();

		return Sequence(
				Type(),t.set((DaedalusType) pop()),
				OPERATOR,
				OverloadableOp(),op.set(DaedalusOperator.fromString(match())),
				LPAR,Optional(NamedPattern(),p.set((DaedalusNamedTypePattern) pop())),RPAR,
				Block(),
				push(new DaedalusOperatorOverload(t.get(), op.get(), p.get(), (DaedalusBlock) pop()))
				);
	}

	Rule OverloadableOp() {
		return FirstOf(AND,ANDAND,ANDEQU,BSR,BSREQU,BSL,BSLEQU,COMMA,DEC,
				DIV,DIVEQU,EQU,EQUAL,GE,GT,HAT,HATHAT,HATEQU,INC,
				Sequence(LBRK,RBRK),LE,LT,MINUS,MINUSEQU,MOD,MODEQU,NOTEQUAL,
				OR,OREQU,OROR,PLUS,PLUSEQU,SL,SLEQU,SR,SREQU,STAR,
				STAREQU,STARSTAR,STARSTAREQU,TILDA);
	}

	Rule MethodDecl() {
		Var<DaedalusType> t = new Var<>();
		Var<DaedalusName> n = new Var<>();
		Var<DaedalusNamedTypePattern> p = new Var<>();
		return Sequence(
				Type(),t.set((DaedalusType) pop()),
				Identifier(),n.set((DaedalusName) pop()),
				LPAR,
				Optional(
						NamedPattern(),
						p.set((DaedalusNamedTypePattern) pop())
						),

				RPAR,
				Block(),
				push(new DaedalusMethod(t.get(), n.get(), p.get(), (DaedalusBlock) pop()))
				);
	}

	Rule NamedPattern() {
		Var<DaedalusType> t = new Var<>();
		Var<DaedalusNamedTypePattern> n = new Var<>();

		return Sequence(
				n.set(new DaedalusNamedTypePattern()),

				DimType(),
				t.set((DaedalusType) pop()),
				Identifier(),
				n.get().addType(t.get(),(DaedalusName)pop()),

				ZeroOrMore(COMMA,
						DimType(),
						t.set((DaedalusType) pop()),
						Identifier(),
						n.get().addType(t.get(),(DaedalusName)pop())
						),
				t.clear(),
				push(n.getAndClear())
				);
	}

	Rule VarDecl() {
		Var<DaedalusVarDecleration> v = new Var<>();

		return Sequence(
				DimType(),
				v.set(new DaedalusVarDecleration((DaedalusType) pop())),

				DimIdentifier(),
				v.get().addName((DaedalusName) pop()),

				ZeroOrMore(
						COMMA,
						DimIdentifier(),
						v.get().addName((DaedalusName) pop())
						),

				Optional(
						EQU,
						Expression()//, XXX UNCOMMENT
						//v.get().setExpression((DaedalusExpression) pop())
						),
				SEMI,
				Spacing(),
				push(v.getAndClear())
				);
	}

	Rule ArrayDimensions() {
		Var<DaedalusArrayDimension> d = new Var<>();

		return Sequence(
				d.set(new DaedalusArrayDimension()),
				LBRK,
				Optional(Expression(), d.set(new DaedalusArrayDimension((DaedalusExpression) pop()))),
				RBRK,
				push(d.getAndClear())
				);
	}

	Rule DimType() {
		Var<DaedalusType> t = new Var<>();

		return Sequence(Type(),
				t.set((DaedalusType) pop()),
				ZeroOrMore(
						ArrayDimensions(),
						t.get().addDimension((DaedalusArrayDimension) pop())
						),
				push(t.getAndClear())
				);
	}

	@MemoMismatches
	Rule Type() {
		Var<Vector<DaedalusTypeModifier>> mod = new Var<>();
		return Sequence(
				mod.set(new Vector<>()),
				ZeroOrMore(
						TypeSpecifier(),
						mod.get().add(DaedalusTypeModifier.valueOf(match().trim().toUpperCase()))
						),
				FirstOf(
						FunctionType(),
						PlainType()
						),
				((DaedalusType)peek()).addModifiers(mod.getAndClear())
				);
	}

	Rule TypeSpecifier() {
		return FirstOf(FINAL,UNSIGNED);
	}

	Rule PlainType() {
		return FirstOf(
				SimpleType(),
				TupleType()
				);
	}

	@MemoMismatches
	Rule SimpleType() {
		return FirstOf(
				Sequence(
						FirstOf(
								INT,
								DECIMAL,
								BOOLEAN,
								STRING,
								VOID),
						push(new DaedalusSimpleType(DaedalusTypeLiteral.valueOf(match().trim().toUpperCase())))
						),
				Sequence(
						Identifier(),
						push(new DaedalusSimpleType(DaedalusTypeLiteral.INSTANCE, ((DaedalusName)pop())))
						)
				);
	}

	Rule TypePattern() {
		Var<DaedalusTypePattern> p = new Var<>();
		return Sequence(
				p.set(new DaedalusTypePattern()),
				Type(),
				p.get().addType((DaedalusType) pop()),
				ZeroOrMore(COMMA,Type(),p.get().addType((DaedalusType) pop())),
				push(p.getAndClear())
				);
	}

	Rule TupleType() {
		Var<Boolean> end= new Var<>();
		Var<DaedalusTypePattern> p = new Var<>();

		return Sequence(
				end.set(new Boolean(false)),
				p.set(new DaedalusTypePattern()),
				TUP,LPAR,
				Optional(TypePattern(),p.set((DaedalusTypePattern) pop())),
				Optional(COMMA,ELLIPSIS,end.set(new Boolean(true))),RPAR,
				push(new DaedalusTupleType(p.getAndClear(), end.getAndClear().booleanValue()))
				);
	}

	Rule FunctionType() {
		Var<DaedalusType> t = new Var<>();
		Var<DaedalusTypePattern> p = new Var<>();
		return Sequence(
				PlainType(),
				t.set((DaedalusType) pop()),
				OneOrMore(
						LPAR,
						p.set(new  DaedalusTypePattern()),
						Optional(TypePattern(),p.set((DaedalusTypePattern) pop())),
						RPAR,
						t.set(new DaedalusUnnamedFunctionType(t.get(), p.get()))
						),
				push(t.get())
				);
	}

	Rule DimIdentifier() {
		Var<DaedalusArrayName> a = new Var<>();

		return Sequence(
				Identifier(),
				a.set(new DaedalusArrayName((DaedalusName)pop())),
				ZeroOrMore(
						ArrayDimensions(),
						a.get().addDimension((DaedalusArrayDimension) pop())
						),
				push(a.getAndClear())
				);
	}


	/*
	//Expression
	//TODO Expression
	Rule Primary() {
		return FirstOf(
				ParExpression(),	// a * (b + c)
				Literal(),	// Variable

				Sequence(NEW, Creator()),
				Sequence(QualifiedIdentifier(), Optional(IdentifierSuffix())),
				Sequence(DimType(), DOT, CLASS),

				ExpressionList(),		// {x,y,z}
				LambdaExpression()		// n -> n**2
				);
	}

	Rule ExpressionList() {
		Var<DaedalusExpressionList> list = new Var<>();
		return Sequence(
				list.set(new DaedalusExpressionList()),
				LWING,Expression(),list.get().addExperssion((DaedalusExpression)pop()),
				ZeroOrMore(COMMA,Expression(),list.get().addExperssion((DaedalusExpression)pop())),RWING,
				push(list.get()),
				list.clear()
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
	}*/

	Rule NonWildcardTypeArguments() {
		return Sequence(LPOINT, ReferenceType(), ZeroOrMore(COMMA, ReferenceType()), RPOINT);
	}

	Rule Expression() {
		return Sequence(
				JoinExpression(),
				ZeroOrMore(AssignmentOperator(), JoinExpression())
				);
	}

	Rule AssignmentOperator() {
		return FirstOf(EQU, PLUSEQU, MINUSEQU, STAREQU, STARSTAREQU, DIVEQU, ANDEQU, OREQU, HATEQU, MODEQU, SLEQU, SREQU, BSREQU,BSLEQU);
	}

	// a,b
	Rule JoinExpression() {
		return Sequence(
				ConditionalExpression(),
				ZeroOrMore(COMMA, ConditionalExpression())
				);
	}

	Rule JoinFreeExpression() {
		return Sequence(
				ConditionalExpression(),
				ZeroOrMore(AssignmentOperator(), ConditionalExpression())
				);
	}

	// x ? y : z
	Rule ConditionalExpression() {
		return Sequence(
				BooleanORExpression(),
				ZeroOrMore(QUERY, Expression(), COLON, BooleanORExpression())
				);
	}

	// ||
	Rule BooleanORExpression() {
		return Sequence(
				BooleanXORExpression(),
				ZeroOrMore(OROR, BooleanXORExpression())
				);
	}

	// ^^
	Rule BooleanXORExpression() {
		return Sequence(
				BooleanANDExpression(),
				ZeroOrMore(HATHAT,BooleanANDExpression())
				);
	}

	// &&
	Rule BooleanANDExpression() {
		return Sequence(
				EqualityExpression(),
				ZeroOrMore(ANDAND, EqualityExpression())
				);
	}

	// == !=
	Rule EqualityExpression() {
		return Sequence(
				ComparisonExpression(),
				ZeroOrMore(FirstOf(EQUAL, NOTEQUAL), ComparisonExpression())
				);
	}

	// > < >= <= instanceof
	Rule ComparisonExpression() {
		return Sequence(
				BitORExpression(),
				ZeroOrMore(
						FirstOf(
								Sequence(FirstOf(LE, GE, LT, GT),BitORExpression()),
								Sequence(INSTANCEOF,ParDimType())
								)
						)
				);
	}

	Rule ParDimType() {
		return FirstOf(
				DimType(),
				Sequence(LPAR,ParDimType(),RPAR)
				);
	}

	// |
	Rule BitORExpression() {
		return Sequence(
				BitXORExpression(),
				ZeroOrMore(OR, BitXORExpression())
				);
	}

	// ^
	Rule BitXORExpression() {
		return Sequence(
				BitANDExpression(),
				ZeroOrMore(HAT, BitANDExpression())
				);
	}

	// &
	Rule BitANDExpression() {
		return Sequence(
				ShiftExpression(),
				ZeroOrMore(AND, ShiftExpression())
				);
	}

	// <<(<) >>(>)
	Rule ShiftExpression() {
		return Sequence(
				AdditiveExpression(),
				ZeroOrMore(FirstOf(SL, SR, BSR, BSL), AdditiveExpression())
				);
	}

	// + -
	Rule AdditiveExpression() {
		return Sequence(
				MultiplicativeExpression(),
				ZeroOrMore(FirstOf(PLUS, MINUS), MultiplicativeExpression())
				);
	}

	// * /
	Rule MultiplicativeExpression() {
		return Sequence(
				ExponentialExpression(),
				ZeroOrMore(FirstOf(STAR, DIV, MOD), ExponentialExpression())
				);
	}

	// a**b
	Rule ExponentialExpression() {
		return Sequence(
				UnaryExpression(),
				ZeroOrMore(STARSTAR,UnaryExpression())
				);
	}

	// Cast ++ -- ! ~ (+ - Unary)
	Rule UnaryExpression() {
		return Sequence(
				ZeroOrMore(PrefixOp()),
				ReferencePath(),
				ZeroOrMore(PostfixOp())
				);
	}

	Rule PrefixOp() {
		return FirstOf( INC, DEC, BANG, TILDA, PLUS, MINUS, Cast());
	}

	Rule Cast() {
		return Sequence(LPAR,DimType(),RPAR);
	}

	Rule PostfixOp() {
		return FirstOf(INC, DEC);
	}

	// a.b().c
	Rule ReferencePath() {
		return Sequence(
				ModifiedExpression(),
				ZeroOrMore(DOT,ModifiedExpression())
				);
	}
	
	//[] () new 
	Rule ModifiedExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusIndexAcess> ia = new Var<>();
		Var<DaedalusExpression> orginal = new Var<>();
		return Sequence(
				FirstOf(
						DirectExpression(),
						Sequence(
								NEW,ParDimType(),
								push(new DaedalusConstuctorCall((DaedalusType) pop()))
								)
						),
				e.set((DaedalusExpression) pop()),
				orginal.set(e.get()),
				
				ZeroOrMore(
						FirstOf(
								Sequence(
										DimSlice(),
										ia.set((DaedalusIndexAcess) pop()),
										ia.get().setIndex(e.get()),
										e.set(ia.getAndClear())
										),
								
								Sequence(
										Call(),
										e.set(new DaedalusCall(e.get(),(DaedalusParameterList) pop()))
										)
								)
						),
				push(orginal.get())
				);
	}

	Rule Call() {
		Var<DaedalusParameterList> p = new Var<>();
		return Sequence(
				p.set(new DaedalusParameterList()),
				LPAR,
				Optional(
						JoinFreeExpression(),
						p.get().addParameter((DaedalusExpression) pop()),
						ZeroOrMore(
								COMMA,
								JoinFreeExpression(),
								p.get().addParameter((DaedalusExpression) pop())
								)
						),
				RPAR,
				push(p.getAndClear())
				);
	}

	Rule DirectExpression() {
		return FirstOf(
				LambdaExpression(),
				Literal(),							//NUMBER
				Identifier(),						//VAR / Method Name
				Sequence(LPAR,Expression(),RPAR),	// a*(...)
				ExpressionList()					// List
				); 
	}
	
	Rule ExpressionList() {
		Var<DaedalusExpressionList> l = new Var<>();
		return Sequence(
				l.set(new  DaedalusExpressionList()),
				LWING,
				JoinFreeExpression(),
				l.get().addExperssion((DaedalusExpression) pop()),

				ZeroOrMore(
						COMMA,
						JoinFreeExpression(),
						l.get().addExperssion((DaedalusExpression) pop())
						),
				RWING,
				push(l.getAndClear())
				);
	}

	Rule LambdaExpression() {
		Var<DaedalusLambdaParameter> l = new Var<>();
		return Sequence(
				LambdaParam(),
				l.set((DaedalusLambdaParameter) pop()),
				ARROW, 
				FirstOf(
						Expression(),
						Statement()
						),
				push(new DaedalusLambdaExpression(l.getAndClear(), (DaedalusStatement) pop()))
				);
	}

	Rule LambdaParam() {
		Var<DaedalusLambdaParameter> p = new Var<>();
		return Sequence(
				FirstOf(
						Sequence(
								Identifier(),
								p.set(new DaedalusLambdaParameter((DaedalusName) pop()))
								),

						Sequence(
								p.set(new DaedalusLambdaParameter()),
								LPAR,Identifier(),
								p.get().addName((DaedalusName) pop()),

								ZeroOrMore(
										COMMA,
										Identifier(),
										p.get().addName((DaedalusName) pop())										
										),RPAR),
						Sequence(LPAR,NamedPattern(),p.set(new DaedalusLambdaParameter((DaedalusNamedTypePattern)pop())),RPAR)
						),
				push(p.getAndClear())
				);
	}


	Rule Dim() {
		return Sequence(LBRK, RBRK);
	}

	Rule DimExpr() {
		return Sequence(LBRK, Expression(), RBRK);
	}

	Rule DimSlice() {
		Var<DaedalusIndexAcess> i = new Var<>();
		return Sequence(
				i.set(new DaedalusIndexAcess()),
				LBRK,
				Slice(),
				i.get().addSlice((DaedalusSlice) pop()),
				ZeroOrMore(
						COMMA,
						Slice(),
						i.get().addSlice((DaedalusSlice) pop())
						),
				RBRK,
				push(i.getAndClear())
				);
	}

	Rule Slice() {
		Var<DaedalusSlice> s = new Var<>();
		return 	Sequence(
				s.set(new DaedalusSlice()),
				FirstOf(
						Sequence(
								JoinFreeExpression(),
								s.get().setStart((DaedalusExpression) pop())
								),
						
						Sequence(
								Test(COLON),
								s.get().setHasEnd()
								)
						),
				
				Optional(
						COLON,
						s.get().setHasEnd(),
						
						Optional(
								JoinFreeExpression(),
								s.get().setEnd((DaedalusExpression) pop())
								),
						Optional(
								COLON,
								s.get().setHasStep(),
								Optional(
										JoinFreeExpression(),
										s.get().setStep((DaedalusExpression) pop())
										)
								)
						),
				push(s.getAndClear())
				);
	}

	

	//Statements
	Rule Block() {
		Var<DaedalusBlock> block = new Var<>();
		return Sequence( block.set(new DaedalusBlock()),
				LWING,ZeroOrMore(Statement()/*,block.get().addStatement((DaedalusStatement)pop()) XXX Uncomment*/),Spacing(),RWING,
				push(block.get()),block.clear());
	}

	Rule Statement() {
		return Sequence(
				FirstOf(
						Block(),
						VarDecl(),
						Assertion(),
						Stream(),
						Sequence(Expression(),SEMI),
						ifElseStatement(),
						Switch(),
						Loop(),
						ControlCMD(),
						ReturnCMD()),
				Spacing());
	}

	Rule Assertion() {
		return Sequence(
				ASSERT,Expression(),
				push(new DaedalusAssertion((DaedalusExpression)pop())),
				SEMI);
	}

	Rule ifElseStatement() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusStatement> s = new Var<>();
		Var<DaedalusIfCondition> orginal = new Var<>();
		Var<DaedalusIfCondition> d = new Var<>();
		Var<DaedalusIfCondition> t = new Var<>();

		return Sequence(
				IF, LPAR, 

				Expression(),
				e.set((DaedalusExpression) pop()),

				RPAR,
				Statement(),
				s.set((DaedalusStatement) pop()),
				d.set(new DaedalusIfCondition(e.getAndClear(), s.getAndClear())),
				orginal.set(d.get()),

				ZeroOrMore(
						elifStatement(),
						t.set((DaedalusIfCondition) pop()),
						d.get().setOnFail(t.get()),
						d.set(t.getAndClear())
						),

				Optional(
						elseStatement(),
						t.set((DaedalusIfCondition) pop()),
						d.get().setOnFail(t.get()),
						d.set(t.getAndClear())
						),
				d.clear(),
				push(orginal.getAndClear())
				);
	}

	Rule elifStatement() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusStatement> s = new Var<>();

		return Sequence(
				ELIF, LPAR, 
				Expression(),
				e.set((DaedalusExpression) pop()),
				RPAR,
				Statement(),
				s.set((DaedalusStatement) pop()),
				push(new DaedalusIfCondition(e.getAndClear(), s.getAndClear()))
				);
	}

	Rule elseStatement() {
		return Sequence(ELSE, Statement(),push(new DaedalusIfCondition((DaedalusStatement) pop())));
	}

	//TODO
	Rule Switch() {
		return Sequence(SWITCH,LPAR,Expression(),RPAR,SwitchBlock());
	}

	Rule SwitchBlock() {
		return Sequence(OneOrMore(Case()),Optional(Default()));
	}

	Rule Case() {
		return Sequence(CASE, Literal(),COLON,ZeroOrMore(Statement()));
	}

	Rule Default() {
		return Sequence(DEFAULT,COLON,ZeroOrMore(Statement()));
	}

	//TODO
	Rule Loop() {
		return Sequence(Optional(Label()),FirstOf(whileLoop(),doWhileLoop(),forLoop(),forEachLoop()));
	}

	Rule Label() {
		return Sequence(Identifier(),COLON);
	}

	Rule whileLoop() {
		return Sequence(WHILE,LPAR,Expression(),RPAR,Block());
	}

	Rule doWhileLoop() {
		return Sequence(DO, Block(),WHILE,LPAR,Expression(),RPAR);
	}

	Rule forLoop() {
		return Sequence(FOR,LPAR,FirstOf(VarDecl(),SEMI),Optional(Expression()),SEMI,Optional(FirstOf(Statement(),Expression())),RPAR,Block());
	}

	Rule forEachLoop() {
		return Sequence(FirstOf(FOR,FORALL),LPAR,TypePattern(),Identifier(),Optional(INT,Identifier()),IN,Expression(),RPAR,Block());
	}

	//TODO
	Rule Stream() {
		return Sequence(Expression(), OneOrMore(COLARROW,StreamElement()),SEMI);
	}

	Rule StreamElement() {
		return FirstOf(LambdaExpression(),StreamMethodCall());
	}

	Rule StreamMethodCall() {
		return Sequence(Identifier(),Optional(LPAR,StreamParam(),ZeroOrMore(COMMA,StreamParam()),RPAR));
	}

	Rule StreamParam() {
		return FirstOf(STAR,Expression());
	}


	Rule ControlCMD () {
		return Sequence(FirstOf(BREAK,CONTINUE),Optional(Identifier()),SEMI);
	}

	Rule ReturnCMD() {
		return Sequence(RETURN, Optional(Expression()),SEMI);
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
	//  Expressions
	//-------------------------------------------------------------------------


	// The following definition is part of the modification in JLS Chapter 18
	// to minimize look ahead. In JLS Chapter 15.27, Expression is defined
	// as AssignmentExpression, which is effectively defined as
	// (LeftHandSide AssignmentOperator)* ConditionalExpression.
	// The following is obtained by allowing ANY ConditionalExpression
	// as LeftHandSide, which results in accepting statements like 5 = a.


	Rule Arguments() {
		return Sequence(
				LPAR,
				Optional(Expression(), ZeroOrMore(COMMA, Expression())),
				RPAR
				);
	}

	Rule Creator() {
		return Sequence(Optional(NonWildcardTypeArguments()), FirstOf(ClassType(), SimpleType()), ArrayCreatorRest());
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

	
	//-------------------------------------------------------------------------
	//  Types and Modifiers
	//-------------------------------------------------------------------------
	Rule ReferenceType() {
		return Sequence(Type(), OneOrMore(Dim()));
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
		return Sequence(
				Sequence(TestNot(Keyword()), Letter(), ZeroOrMore(LetterOrDigit()), Spacing()),
				push(new DaedalusName(match().trim()))
				);
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
				FirstOf("assert","as", "boolean", "break", "case", "catch", "class", "const", "continue", "decimal","default", "do", "elif", "else",
						"enum", "extends", "finally", "final",  "forall", "for", "from", "goto", "if",  "implements", "import", "interface", "int",
						"instanceof","in", "new", "operator", "package", "return", "static", "string", "super", "switch", "synchronized", "this", "tup",
						"throws", "throw", "try", "unsigned", "void", "while"),
				TestNot(LetterOrDigit())
				);
	}

	public final Rule AS = Keyword("as");
	public final Rule FROM = Keyword("from");

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

	public final Rule TUP = Keyword("tup");
	public final Rule INT = Keyword("int");
	public final Rule DECIMAL = Keyword("decimal");
	public final Rule BOOLEAN = Keyword("boolean");
	public final Rule STRING = Keyword("string");
	public final Rule OPERATOR = Keyword("operator");

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
						//LiteralList(),
						Sequence(lit.FloatLiteral(),push(new DaedalusLiteral(new BigDecimal(match())))),
						Sequence(lit.IntegerLiteral(),push(new DaedalusLiteral(new BigInteger(match())))),
						//lit.CharLiteral(),
						Sequence(lit.StringLiteral(),push(new DaedalusLiteral(match()))),
						Sequence("true", TestNot(LetterOrDigit()),push(new DaedalusLiteral(new Boolean(true)))),
						Sequence("false", TestNot(LetterOrDigit()),push(new DaedalusLiteral(new Boolean(false)))),
						Sequence("null", TestNot(LetterOrDigit()),push(new DaedalusLiteral(null)))
						),
				Spacing()
				);
	}

	/*Rule LiteralList() {
		return Sequence(LWING,Literal(),ZeroOrMore(COMMA,Literal()),RWING);
	}*/

	//-------------------------------------------------------------------------
	//  JLS 3.11-12  Separators, Operators
	//-------------------------------------------------------------------------
	final Rule ARROW = Terminal("->");
	final Rule AT = Terminal("@");
	final Rule AND = Terminal("&", AnyOf("=&"));
	final Rule ANDAND = Terminal("&&");
	final Rule ANDEQU = Terminal("&=");
	final Rule BANG = Terminal("!", Ch('='));
	final Rule BSR = Terminal(">>>", Ch('='));
	final Rule BSREQU = Terminal(">>>=");
	final Rule BSL = Terminal("<<<", Ch('='));
	final Rule BSLEQU = Terminal("<<<=");
	final Rule COLON = Terminal(":",Ch('>'));
	final Rule COLARROW = Terminal(":>");
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
	final Rule MINUS = Terminal("-", AnyOf("=->"));
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
