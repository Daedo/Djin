package daedalusParser.combonents;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.MemoMismatches;
import org.parboiled.annotations.SuppressSubnodes;

public class LiteralMatcher extends BaseParser<Object> {

	// String and Char
	public Rule CharLiteral() {
		return Sequence(
				'\'',
				FirstOf(Escape(), Sequence(TestNot(AnyOf("'\\")), ANY)).suppressSubnodes(),
				'\''
				);
	}

	public Rule StringLiteral() {
		return Sequence(
				'"',
				ZeroOrMore(
						FirstOf(
								Escape(),
								Sequence(TestNot(AnyOf("\r\n\"\\")), ANY)
								)
						).suppressSubnodes(),
				'"'
				);
	}

	Rule Escape() {
		return Sequence('\\', FirstOf(AnyOf("btnfr\"\'\\"), OctalEscape(), UnicodeEscape()));
	}

	Rule OctalEscape() {
		return FirstOf(
				Sequence(CharRange('0', '3'), CharRange('0', '7'), CharRange('0', '7')),
				Sequence(CharRange('0', '7'), CharRange('0', '7')),
				CharRange('0', '7')
				);
	}

	public Rule UnicodeEscape() {
		return Sequence(OneOrMore('u'), HexDigit(), HexDigit(), HexDigit(), HexDigit());
	}
	
	// Numbers
	@SuppressSubnodes
	public Rule IntegerLiteral() {
		return FirstOf(HexNumeral(), OctalNumeral(), DecimalNumeral());
	}

	@SuppressSubnodes
	Rule DecimalNumeral() {
		return FirstOf('0', Sequence(CharRange('1', '9'), ZeroOrMore(Digit())));
	}

	@SuppressSubnodes
	@MemoMismatches
	Rule HexNumeral() {
		return Sequence('0', IgnoreCase('x'), OneOrMore(HexDigit()));
	}

	public Rule HexDigit() {
		return FirstOf(CharRange('a', 'f'), CharRange('A', 'F'), CharRange('0', '9'));
	}

	@SuppressSubnodes
	Rule OctalNumeral() {
		return Sequence('0', OneOrMore(CharRange('0', '7')));
	}

	public Rule FloatLiteral() {
		return FirstOf(HexFloat(), DecimalFloat());
	}

	@SuppressSubnodes
	Rule DecimalFloat() {
		return FirstOf(
				Sequence(OneOrMore(Digit()), '.', ZeroOrMore(Digit()), Optional(Exponent())),
				Sequence('.', OneOrMore(Digit()), Optional(Exponent())),
				Sequence(OneOrMore(Digit()), Exponent())
				);
	}

	Rule Exponent() {
		return Sequence(AnyOf("eE"), Optional(AnyOf("+-")), OneOrMore(Digit()));
	}

	Rule Digit() {
		return CharRange('0', '9');
	}

	@SuppressSubnodes
	Rule HexFloat() {
		return Sequence(HexSignificant(), BinaryExponent());
	}

	Rule HexSignificant() {
		return FirstOf(
				Sequence(FirstOf("0x", "0X"), ZeroOrMore(HexDigit()), '.', OneOrMore(HexDigit())),
				Sequence(HexNumeral(), Optional('.'))
				);
	}

	Rule BinaryExponent() {
		return Sequence(AnyOf("pP"), Optional(AnyOf("+-")), OneOrMore(Digit()));
	}
}
