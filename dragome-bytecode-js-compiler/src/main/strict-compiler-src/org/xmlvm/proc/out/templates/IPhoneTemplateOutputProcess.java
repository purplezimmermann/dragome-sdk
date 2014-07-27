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
/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.xmlvm.proc.out.templates;

import static org.xmlvm.proc.out.templates.TemplateFile.Mode.BACKUP;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.KEEP;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.OVERWRITE;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.ABORT;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.IGNORE;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.NEWFILE;
import static org.xmlvm.proc.out.templates.TemplateFile.Mode.DELETE;

import java.util.ArrayList;
import org.xmlvm.main.Arguments;

/**
 * Template for new iPhone projects
 */
public class IPhoneTemplateOutputProcess extends TemplateOutputProcess
{

	public IPhoneTemplateOutputProcess(Arguments arguments, boolean migrate)
	{
		super(arguments, migrate);
	}

	ArrayList<TemplateFile> getTemplateList()
	{
		ArrayList<TemplateFile> list= new ArrayList<TemplateFile>();

		list.add(new TemplateFile("build.xml", migrate ? BACKUP : ABORT));
		list.add(new TemplateFile("project.xml", "nbproject", migrate ? BACKUP : ABORT));
		list.add(new TemplateFile("xmlvm.xml", "nbproject", migrate ? OVERWRITE : ABORT));
		list.add(new TemplateFile("build-Xcode.xml", "nbproject", migrate ? OVERWRITE : ABORT));
		list.add(new TemplateFile("build-Java.xml", "nbproject", migrate ? OVERWRITE : ABORT));
		list.add(new TemplateFile("build-impl.xml", "nbproject", KEEP));

		list.add(new TemplateFile("project.properties", "nbproject", NEWFILE));

		list.add(new TemplateFile("xmlvm.properties", KEEP));
		list.add(new TemplateFile("manifest.mf", KEEP));
		list.add(new TemplateFile("genfiles.properties", "nbproject", KEEP));
		list.add(new TemplateFile("empty.properties", "Java.properties", "nbproject/configs", KEEP));
		list.add(new TemplateFile("empty.properties", "Xcode.properties", "nbproject/configs", KEEP));
		list.add(new TemplateFile(".classpath", KEEP));
		list.add(new TemplateFile(".project", KEEP));
		list.add(new TemplateFile("org.eclipse.jdt.core.prefs", ".settings", KEEP));

		list.add(new TemplateFile("demo.png", "resources", migrate ? IGNORE : KEEP));
		list.add(new TemplateFile("Main.java", "src/java/" + pack_name.replace(".", "/"), migrate ? IGNORE : KEEP));

		list.add(new TemplateFile("xcode.xml", "nbproject", DELETE));
		return list;
	}

	String getTemplateLocation()
	{
		return "/templates/iphone/";
	}
}
