/*
 * Copyright (c) 2011-2014 Fernando Petrola
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package java.lang;

import com.dragome.commons.javascript.ScriptHelper;

/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */
public class Integer extends Number implements Comparable<Integer>
{

	/**
	 * The largest value of type int.
	 */
	public static int MAX_VALUE= 2147483647;

	/**
	 * The smallest value of type int.
	 */
	public static int MIN_VALUE= -2147483648;

	public static final Class<Integer> TYPE= Class.getType("int");

	private int value;

	/**
	 * Constructs a newly allocated Integer object that represents the specified int value.
	 */
	public Integer(int newValue)
	{
		value= newValue;
	}

	public Integer(String aValue)
	{
		value= parseInt(aValue);
	}

	/**
	 * Compares this object to the specified object.
	 */
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Integer))
			return false;
		return ((Integer) obj).value == value;
	}

	/**
	 * Returns a string representation of the first argument in the radix specified by the second argument.
	 */
	public static String toString(int i, int radix)
	{
		ScriptHelper.put("i", i, null);
		ScriptHelper.put("radix", radix, null);
		return (String) ScriptHelper.eval("new Number(i).toString(radix)", null);
	}

	/**
	 * Returns a string representation of the integer argument as an unsigned integer in base 16.
	 */
	public static String toHexString(int i)
	{
		return toString(i, 16);
	}

	/**
	 * Returns a string representation of the integer argument as an unsigned integer in base 8.
	 */
	public static String toOctalString(int i)
	{
		return toString(i, 8);
	}

	/**
	 * Returns a string representation of the integer argument as an unsigned integer in base 2.
	 */
	public static String toBinaryString(int i)
	{
		return toString(i, 2);
	}

	/**
	 * Returns a String object representing the specified integer.
	 */
	public static String toString(int i)
	{
		return toString(i, 10);
	}

	/**
	 * Parses the string argument as a signed integer in the radix specified by the second argument.
	 */
	public static int parseInt(String s, int radix)
	{
		return __parseAndValidateInt(s, radix, MIN_VALUE, MAX_VALUE);
	}

	protected static int __parseAndValidateInt(String s, int radix, int lowerBound, int upperBound) throws NumberFormatException
	{
		if (s == null)
		{
			throw new NumberFormatException("null");
		}
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
		{
			throw new NumberFormatException("radix " + radix + " out of range");
		}

		int length= s.length();
		int startIndex= (length > 0) && (s.charAt(0) == '-' || s.charAt(0) == '+') ? 1 : 0;

		for (int i= startIndex; i < length; i++)
		{
			if (Character.digit(s.charAt(i), radix) == -1)
			{
				throw new NumberFormatException(s);
			}
		}
		int toRet = 0;
		ScriptHelper.put("s", s, null);
		ScriptHelper.put("radix", radix, null);
		ScriptHelper.evalInt("var toReturn = parseInt(s, radix);", null);
		// isTooLow is separated into its own variable to avoid a bug in BlackBerry OS 7. See
		// https://code.google.com/p/google-web-toolkit/issues/detail?id=7291.
		if (ScriptHelper.evalBoolean("isNaN(toReturn)", null))
		{
			throw new NumberFormatException(s);
		}
		else {
			toRet = ScriptHelper.evalInt("toReturn", null);
			boolean isTooLow = toRet < lowerBound;
			if (isTooLow || toRet > upperBound)
			{
				throw new NumberFormatException(s);
			}
		}
		return toRet;
	}

	/**
	 * Parses the string argument as a signed decimal integer.
	 */
	public static int parseInt(String s)
	{
		return parseInt(s, 10);
	}

	/**
	 * Returns an Integer object holding the specified value. Calls to this
	 * method may be generated by the autoboxing feature.
	 */
	public static Integer valueOf(int value)
	{
		return new Integer(value);
	}

	/**
	 * Returns an Integer object holding the value extracted from the specified String
	 * when parsed with the radix given by the second argument.
	 */
	public static Integer valueOf(String s, int radix)
	{
		return new Integer(parseInt(s, radix));
	}

	/**
	 * Returns an Integer object holding the value of the specified String.
	 */
	public static Integer valueOf(String s)
	{
		return new Integer(parseInt(s));
	}

	/**
	 * Returns the value of this Integer as a byte.
	 */
	public byte byteValue()
	{
		return (byte) value;
	}

	/**
	 * Returns the value of this Integer as a short.
	 */
	public short shortValue()
	{
		return (short) value;
	}

	/**
	 * Returns the value of this Integer as an int.
	 */
	public int intValue()
	{
		return value;
	}

	/**
	 * Returns the value of this Integer as a long.
	 */
	public long longValue()
	{
		return value;
	}

	/**
	 * Returns the value of this Integer as a float.
	 */
	public float floatValue()
	{
		return value;
	}

	/**
	 * Returns the value of this Integer as a double.
	 */
	public double doubleValue()
	{
		return value;
	}

	/**
	 * Returns a String object representing this Integer's value.
	 */
	public String toString()
	{
		return Integer.toString(value, 10);
	}

	/**
	 * Returns a hash code for this Integer.
	 */
	public int hashCode()
	{
		return value;
	}

	public int compareTo(Integer object)
	{
		return value > object.value ? 1 : (value < object.value ? -1 : 0);
	}

	public static int compare(int x, int y) {
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	public static int hashCode(int value)
	{
		return value ^ (value >>> 32);
	}
	
	

	public static int numberOfTrailingZeros(int i)
	{
		int y;
		if (i == 0)
			return 32;
		int n= 31;
		y= i << 16;
		if (y != 0)
		{
			n= n - 16;
			i= y;
		}
		y= i << 8;
		if (y != 0)
		{
			n= n - 8;
			i= y;
		}
		y= i << 4;
		if (y != 0)
		{
			n= n - 4;
			i= y;
		}
		y= i << 2;
		if (y != 0)
		{
			n= n - 2;
			i= y;
		}
		return n - ((i << 1) >>> 31);
	}
	
	// The java.lang.Integer.bitCount() method returns the number of one-bits in the two's complement binary representation of the specified int value i. This is sometimes referred to as the population count.
	// https://blogs.msdn.microsoft.com/jeuge/2005/06/08/bit-fiddling-3/
	public static int bitCount(final int i) {
		final int uCount = i - ((i >> 1) & 033333333333) - ((i >> 2) & 011111111111);
        return ((uCount + (uCount >> 3)) & 030707070707) % 63;
	}

	public static int numberOfLeadingZeros(int i)
	{
		if (i == 0)
			return 32;
		int n= 1;
		if (i >>> 16 == 0)
		{
			n+= 16;
			i<<= 16;
		}
		if (i >>> 24 == 0)
		{
			n+= 8;
			i<<= 8;
		}
		if (i >>> 28 == 0)
		{
			n+= 4;
			i<<= 4;
		}
		if (i >>> 30 == 0)
		{
			n+= 2;
			i<<= 2;
		}
		n-= i >>> 31;
		return n;
	}

	public static int highestOneBit(int i)
	{
		i|= (i >> 1);
		i|= (i >> 2);
		i|= (i >> 4);
		i|= (i >> 8);
		i|= (i >> 16);
		return i - (i >>> 1);
	}

	public static int rotateLeft(int i, int distance)
	{
		i= i & 0xf; //TODO fix me!
		return (i << distance) | (i >>> -distance);
	}

	public static int rotateRight(int i, int distance)
	{
		i= i & 0xf; //TODO fix me!
		return (i >>> distance) | (i << -distance);
	}

	//	public static int rotateLeft(int i, int distance)
	//	{
	//		while (distance-- > 0)
	//		{
	//			i= i << 1 | ((i < 0) ? 1 : 0);
	//		}
	//		return i;
	//	}
	//
	//	public static int rotateRight(int i, int distance)
	//	{
	//		int ui= i & MAX_VALUE; // avoid sign extension
	//		int carry= (i < 0) ? 0x40000000 : 0; // MIN_VALUE rightshifted 1
	//		while (distance-- > 0)
	//		{
	//			int nextcarry= ui & 1;
	//			ui= carry | (ui >> 1);
	//			carry= (nextcarry == 0) ? 0 : 0x40000000;
	//		}
	//		if (carry != 0)
	//		{
	//			ui= ui | MIN_VALUE;
	//		}
	//		return ui;
	//	}
}
