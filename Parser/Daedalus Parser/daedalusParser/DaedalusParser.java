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
import daedalusCodeComponents.generic.expression.DaedalusCalculation;
import daedalusCodeComponents.generic.expression.DaedalusCast;
import daedalusCodeComponents.generic.expression.DaedalusConditionalCalculation;
import daedalusCodeComponents.generic.expression.DaedalusExpression;
import daedalusCodeComponents.generic.expression.DaedalusExpressionList;
import daedalusCodeComponents.generic.expression.DaedalusInstanceof;
import daedalusCodeComponents.generic.expression.DaedalusOperator;
import daedalusCodeComponents.generic.expression.DaedalusUnaryCalculation;
import daedalusCodeComponents.generic.expression.leaves.DaedalusConstuctorCall;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLambdaExpression;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLambdaParameter;
import daedalusCodeComponents.generic.expression.leaves.DaedalusLiteral;
import daedalusCodeComponents.generic.expression.modifiers.DaedalusCall;
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
import daedalusCodeComponents.statements.DaedalusControlCommand;
import daedalusCodeComponents.statements.DaedalusControlLiteral;
import daedalusCodeComponents.statements.DaedalusIfCondition;
import daedalusCodeComponents.statements.DaedalusReturnCommand;
import daedalusCodeComponents.statements.DaedalusStatement;
import daedalusCodeComponents.statements.DaedalusVarDecleration;
import daedalusCodeComponents.statements.loop.DaedalusForEachLoop;
import daedalusCodeComponents.statements.loop.DaedalusForLoop;
import daedalusCodeComponents.statements.loop.DaedalusLoop;
import daedalusCodeComponents.statements.loop.DaedalusWhileLoop;
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
						Expression(),
						v.get().setExpression((DaedalusExpression) pop())
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

	Rule Expression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				JoinExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						AssignmentOperator(), 
						o.set(DaedalusOperator.fromString(match())),
						JoinExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	Rule AssignmentOperator() {
		return FirstOf(EQU, PLUSEQU, MINUSEQU, STAREQU, STARSTAREQU, DIVEQU, ANDEQU, OREQU, HATEQU, MODEQU, SLEQU, SREQU, BSREQU,BSLEQU);
	}

	// a,b
	Rule JoinExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ConditionalExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						COMMA, 
						o.set(DaedalusOperator.fromString(match())),
						ConditionalExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	Rule JoinFreeExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ConditionalExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						AssignmentOperator(), 
						o.set(DaedalusOperator.fromString(match())),
						ConditionalExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// x ? y : z
	Rule ConditionalExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusExpression> e2 = new Var<>();
		return Sequence(
				BooleanORExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						QUERY, 
						Expression(),
						e2.set((DaedalusExpression) pop()),
						COLON, 
						BooleanORExpression(),
						e.set(new DaedalusConditionalCalculation(e.get(), e2.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// ||
	Rule BooleanORExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				BooleanXORExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						OROR, 
						o.set(DaedalusOperator.fromString(match())),
						BooleanXORExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// ^^
	Rule BooleanXORExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				BooleanANDExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						HATHAT,
						o.set(DaedalusOperator.fromString(match())),
						BooleanANDExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// &&
	Rule BooleanANDExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				EqualityExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						ANDAND,
						o.set(DaedalusOperator.fromString(match())),
						EqualityExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// == !=
	Rule EqualityExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ComparisonExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						FirstOf(EQUAL, NOTEQUAL),
						o.set(DaedalusOperator.fromString(match())),
						ComparisonExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// > < >= <= instanceof
	Rule ComparisonExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				BitORExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						FirstOf(
								Sequence(
										FirstOf(LE, GE, LT, GT),
										o.set(DaedalusOperator.fromString(match())),
										BitORExpression(),
										e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
										),
								
								Sequence(
										INSTANCEOF,
										ParDimType(),
										e.set(new DaedalusInstanceof(e.get(),(DaedalusType) pop()))
										)
								)
						),
				push(e.getAndClear())
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
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				BitXORExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						OR, 
						o.set(DaedalusOperator.fromString(match())),
						BitXORExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// ^
	Rule BitXORExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				BitANDExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						HAT, 
						o.set(DaedalusOperator.fromString(match())),
						BitANDExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// &
	Rule BitANDExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ShiftExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						AND, 
						o.set(DaedalusOperator.fromString(match())),
						ShiftExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// <<(<) >>(>)
	Rule ShiftExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				AdditiveExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						FirstOf(SL, SR, BSR, BSL), 
						o.set(DaedalusOperator.fromString(match())),
						AdditiveExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// + -
	Rule AdditiveExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				MultiplicativeExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						FirstOf(PLUS, MINUS), 
						o.set(DaedalusOperator.fromString(match())),
						MultiplicativeExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// * /
	Rule MultiplicativeExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ExponentialExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						FirstOf(STAR, DIV, MOD),
						o.set(DaedalusOperator.fromString(match())),
						ExponentialExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// a**b
	Rule ExponentialExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				UnaryExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						STARSTAR,
						o.set(DaedalusOperator.fromString(match())),
						UnaryExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}

	// Cast ++ -- ! ~ (+ - Unary)
	Rule UnaryExpression() {
		Var<DaedalusOperator> o = new Var<>();
		Var<DaedalusType> t = new Var<>();
		return FirstOf(
				Sequence(
						PrefixOp(),
						o.set(DaedalusOperator.fromString(match())),
						UnaryExpression(),
						push(new DaedalusUnaryCalculation(o.getAndClear(), (DaedalusExpression) pop(), true))
						),
				
				Sequence(
						Cast(),
						t.set((DaedalusType) pop()),
						UnaryExpression(),
						push(new DaedalusCast(t.getAndClear(), (DaedalusExpression) pop()))
						),
				
				Sequence(
						ReferencePath(),
						ZeroOrMore(
								PostfixOp(),
								o.set(DaedalusOperator.fromString(match())),
								push(new DaedalusUnaryCalculation(o.getAndClear(), (DaedalusExpression) pop(), false))
								)
						)
				);
	}

	Rule PrefixOp() {
		return FirstOf( INC, DEC, BANG, TILDA, PLUS, MINUS);
	}

	Rule Cast() {
		return Sequence(LPAR,DimType(),RPAR);
	}

	Rule PostfixOp() {
		return FirstOf(INC, DEC);
	}

	// a.b().c
	Rule ReferencePath() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusOperator> o = new Var<>();
		return Sequence(
				ModifiedExpression(),
				e.set((DaedalusExpression) pop()),
				ZeroOrMore(
						DOT,
						o.set(DaedalusOperator.fromString(match())),
						ModifiedExpression(),
						e.set(new DaedalusCalculation(e.get(), o.getAndClear(), (DaedalusExpression) pop()))
						),
				push(e.getAndClear())
				);
	}
	
	//[] () new 
	Rule ModifiedExpression() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusIndexAcess> ia = new Var<>();
		return Sequence(
				FirstOf(
						DirectExpression(),
						Sequence(
								NEW,ParDimType(),
								push(new DaedalusConstuctorCall((DaedalusType) pop()))
								)
						),
				e.set((DaedalusExpression) pop()),
				
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
				push(e.get())
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

	
	Rule Loop() {
		Var<DaedalusName> n = new Var<>();
		return Sequence(
				Optional(
						Label(),
						n.set((DaedalusName) pop())
						),
				FirstOf(whileLoop(),doWhileLoop(),forLoop(),forEachLoop()),
				((DaedalusLoop)peek()).setLabel(n.get())
				);
	}

	Rule Label() {
		return Sequence(Identifier(),COLON);
	}

	Rule whileLoop() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusStatement> s = new Var<>();
		return Sequence(
				WHILE,
				LPAR,
				Expression(),
				e.set((DaedalusExpression) pop()),
				RPAR,
				Statement(),
				s.set((DaedalusStatement) pop()),
				push(new DaedalusWhileLoop(e.getAndClear(), s.getAndClear()))
				);
	}

	Rule doWhileLoop() {
		Var<DaedalusExpression> e = new Var<>();
		Var<DaedalusStatement> s = new Var<>();
		return Sequence(
				DO, 
				Statement(),
				s.set((DaedalusStatement) pop()),
				WHILE,
				LPAR,
				Expression(),
				e.set((DaedalusExpression) pop()),
				RPAR,
				SEMI,
				push(new DaedalusWhileLoop(true,e.getAndClear(), s.getAndClear()))
				);
	}
	
	Rule forLoop() {
		Var<DaedalusForLoop> f = new Var<>();
		Var<DaedalusStatement> s1 = new Var<>();
		Var<DaedalusStatement> s3 = new Var<>();
		Var<DaedalusExpression> e2 = new Var<>();
		
		return Sequence(
				FOR,LPAR,
				FirstOf(
						Sequence(
								VarDecl(),
								s1.set((DaedalusStatement) pop())
								),
						SEMI),
				Optional(
						Expression(),
						e2.set((DaedalusExpression) pop())
						),
				SEMI,
				Optional(
						FirstOf(Statement(),Expression()),
						s3.set((DaedalusStatement) pop())
						),
				RPAR,
				Statement(),
				f.set(new DaedalusForLoop((DaedalusStatement) pop())),
				f.get().setInit(s1.getAndClear()),
				f.get().setConditional(e2.getAndClear()),
				f.get().setUpdate(s3.getAndClear()),
				push(f.getAndClear())
				);
	}
	
	Rule forEachLoop() {
		Var<Boolean> b = new Var<>();
		Var<DaedalusForEachLoop> f = new Var<>();
		Var<DaedalusTypePattern> p = new Var<>();
		Var<DaedalusName> n1 = new Var<>();
		Var<DaedalusName> n2 = new Var<>();
		Var<DaedalusExpression> e = new Var<>();
		
		return Sequence(
				b.set(new Boolean(false)),
				FirstOf(
						FOR,
						Sequence(FORALL,
								b.set(new Boolean(true))
								)
						),
				LPAR,
				TypePattern(),
				p.set((DaedalusTypePattern) pop()),
				Identifier(),
				n1.set((DaedalusName) pop()),
				Optional(
						INT,
						Identifier(),
						n2.set((DaedalusName) pop())
						),
				IN,
				Expression(),
				e.set((DaedalusExpression) pop()),
				RPAR,
				Block(),
				f.set(new DaedalusForEachLoop((DaedalusStatement) pop())),
				f.get().setContainer(e.getAndClear()),
				f.get().setIndexName(n2.get()),
				f.get().setSubpattern(p.getAndClear(), n1.getAndClear()),
				push(f.getAndClear())
				);
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
		Var<DaedalusControlCommand> c = new Var<>();
		return Sequence(
				FirstOf(BREAK,CONTINUE),
				c.set(new DaedalusControlCommand(DaedalusControlLiteral.valueOf(match().trim().toUpperCase()))),
				
				Optional(
						Identifier(),
						c.get().setLabel((DaedalusName) pop())
						),
				push(c.getAndClear()),
				SEMI);
	}

	Rule ReturnCMD() {
		Var<DaedalusReturnCommand> r = new Var<>();
		return Sequence(
				RETURN,
				r.set(new DaedalusReturnCommand()),
				Optional(
						Expression(),
						r.get().setValue((DaedalusExpression) pop())
						),
				SEMI);
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
						"enum", "extends", "false", "finally", "final",  "forall", "for", "from", "goto", "if",  "implements", "import", "interface", "int",
						"instanceof","in", "new", "null", "operator", "package", "return", "static", "string", "super", "switch", "synchronized", "this", "tup",
						"throws", "throw", "try", "true", "unsigned", "void", "while"),
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
	public final Rule NULL = Keyword("null");
	public final Rule TRUE = Keyword("true");
	public final Rule FALSE = Keyword("false");

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
						Sequence(TRUE, TestNot(LetterOrDigit()),push(new DaedalusLiteral(new Boolean(true)))),
						Sequence(FALSE, TestNot(LetterOrDigit()),push(new DaedalusLiteral(new Boolean(false)))),
						Sequence(NULL, TestNot(LetterOrDigit()),push(new DaedalusLiteral(null)))
						),
				Spacing()
				);
	}

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
