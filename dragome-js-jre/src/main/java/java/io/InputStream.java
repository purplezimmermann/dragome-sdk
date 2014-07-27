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
package java.io;

public abstract class InputStream implements Closeable
{
	public abstract int read() throws IOException;

	public int read(byte[] buffer, int i, int bufferSize)
    {
	    // TODO Auto-generated method stub
	    return 0;
    }
}
