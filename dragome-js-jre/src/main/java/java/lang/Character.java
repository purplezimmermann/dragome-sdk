/*******************************************************************************
 * Copyright (c) 2011-2014 Fernando Petrola
 * 
 *  This file is part of Dragome SDK.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package java.lang;

import com.dragome.commons.javascript.ScriptHelper;

/**
 * The Character class wraps a value of the primitive type char in an object.
 *
 * 
 */
public final class Character
{

	public static final Class<Character> TYPE= Class.getType("char");

	private char value;

	/**
	 * Constructs a newly allocated Character object that represents the specified char value.
	 */
	public Character(char c)
	{
		value= c;
	}

	/**
	 * Compares this object to the specified object.
	 */
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Character))
			return false;
		return ((Character) obj).value == value;
	}

	/**
	 * Determines if the specified character is a digit.
	 * <br/>Warning: This method will only detect ISO-LATIN-1 digits ('0' through '9').
	 */
	public static boolean isDigit(char ch)
	{
		return String.valueOf(ch).matches("[0-9]");
	}

	/**
	 * Determines if the specified character is a letter.
	 * <br/>Warning: This method will only detect ISO-LATIN-1 letters ('a' through 'Z').
	 */
	public static boolean isLetter(char ch)
	{
		return String.valueOf(ch).matches("[a-zA-Z]");
	}

	/**
	 * Returns an Long object holding the specified value. Calls to this
	 * method may be generated by the autoboxing feature.
	 */
	public static Character valueOf(char value)
	{
		return new Character(value);
	}

	/**
	 * Returns the value of this Character object.
	 */
	public char charValue()
	{
		return value;
	}

	/**
	 * Returns a String object representing this Character's value.
	 */
	public String toString()
	{
		//		 Duplicate code to String#valueOf(char)
		ScriptHelper.put("c", value, this);
		return (String) ScriptHelper.eval("String.fromCharCode(c)", this);
	}
	public static char toUpperCase(char c)
	{
		return ("" + c).toUpperCase().charAt(0);
	}

	public static char toLowerCase(char c)
	{
		return ("" + c).toLowerCase().charAt(0);
	}

	public static boolean isISOControl(char ch)
	{
		return isISOControl((int) ch);
	}

	public static boolean isISOControl(int codePoint)
	{
		return (codePoint >= 0x0000 && codePoint <= 0x001F) || (codePoint >= 0x007F && codePoint <= 0x009F);
	}

	public static boolean isUpperCase(char c)
	{
		if ('A' <= c && c <= 'Z')
		{
			return true;
		}
		return false;
	}

	public static boolean isLowerCase(char c)
	{
		if ('a' <= c && c <= 'z')
		{
			return true;
		}
		return false;
	}

	public static boolean isSpaceChar(char c)
	{
		return isSpace(c);
	}

	public static boolean isWhitespace(char ch)
	{
		return isSpace(ch);
	}

	public static boolean isSpace(char c)
	{
		switch (c)
		{
			case ' ':
				return true;
			case '\n':
				return true;
			case '\t':
				return true;
			case '\f':
				return true;
			case '\r':
				return true;
			default:
				return false;
		}
	}

	public static boolean isLetterOrDigit(char c)
	{
		ScriptHelper.put("c", c, null);
		return ScriptHelper.evalBoolean("null != String.fromCharCode(c).match(/[A-Z\\d]/i)", null);
	};

}
