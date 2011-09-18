package org.github.rnubel.miskatonic.aal;

import java.util.regex.Pattern;

/**
 *
 */
public class AALToken {
	public enum Type {
		StringLiteral ("(\\\".*?\\\"|'.*?')"),
		Let ("Let"),
		Semicolon (";"),
		If ("If"),
		Then ("Then"),
		Else ("Else"),
		End ("End"),
		While ("While"),
		Do ("Do"),
		Call ("Call"),		
		LeftParen ("\\("),
		RightParen ("\\)"),
		LeftSquare ("\\["),
		RightSquare ("\\]"),
		Comma (","),
		Plus ("\\+"),
		Minus ("\\-"),
		Times ("\\*"),
		Divide ("\\/"),
		Or ("\\|\\|"),
		Not ("!"),
		And ("&&"),
		LessThan ("<"),
		GreaterThan (">"),
		Equals ("\\=="),
		Assign ("\\="),		
		FloatLiteral ("\\d+\\.\\d+"),
		IntLiteral ("\\d+"),		
		BoolLiteral ("(True|False)"),
		Identifier ("\\w+"), 
		EOF ("$");
		
		
		private String mPattern;		
		Type(String pattern) {
			mPattern = pattern;			
		}
		
		public String getPattern() {
			return mPattern;
		}
	}

	private Type mType;
	private AALValue mValue;
	private String mToken;
	
	public AALToken(String token) {
		setToken(token);
		
		/* Find the token type. */
		for (Type t : Type.values()) {
			if (Pattern.matches(t.getPattern(), token)) {
				// If this has a specific value, get it.
				if (t == Type.BoolLiteral) {
					mValue = new AALValue(Boolean.parseBoolean(token));
				} else if (t == Type.IntLiteral) {
					mValue = new AALValue(Integer.parseInt(token));
				} else if (t == Type.FloatLiteral) {
					mValue = new AALValue(Float.parseFloat(token));
				} else if (t == Type.StringLiteral) {
					mValue = new AALValue(token.substring(1,token.length()-1));
				} else if (t == Type.Identifier) {
					mValue = new AALValue(token); // Will be pulled out later.
				}
				mType = t;
				break;
			}
		}
	}
		
	
	public AALToken(Type t) {
		mType = t;
	}
	
	public AALToken(Type t, Integer i) {
		this(t);
		mValue = new AALValue(i);
	}
	
	public AALToken(Type t, Float i) {
		this(t);
		mValue = new AALValue(i);
	}
	
	public AALToken(Type t, String i) {
		this(t);
		mValue = new AALValue(i);
	}
	
	public AALToken(Type t, Boolean i) {
		this(t);
		mValue = new AALValue(i);
	}
	
	
	public AALToken.Type getType() {
		return mType;
	}
	
	public AALValue getValue() {
		return mValue;
	}

	public String toString() {
		return mType.getPattern();
	}
	
	public boolean isOperator() {
		return (getType() == Type.And ||
				getType() == Type.Or ||
				getType() == Type.Plus ||
				getType() == Type.Minus ||
				getType() == Type.Divide ||
				getType() == Type.Times ||
				getType() == Type.LessThan ||
				getType() == Type.GreaterThan ||
				getType() == Type.Equals ||
				getType() == Type.Not);		 
	}

	public boolean isLiteral() {
		return (getType() == Type.IntLiteral ||
				getType() == Type.StringLiteral ||
				getType() == Type.FloatLiteral ||
				getType() == Type.BoolLiteral);
	}


	public void setToken(String mToken) {
		this.mToken = mToken;
	}


	public String getToken() {
		return mToken;
	}
}
