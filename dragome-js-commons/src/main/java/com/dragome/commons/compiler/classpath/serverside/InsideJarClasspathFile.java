package com.dragome.commons.compiler.classpath.serverside;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.dragome.commons.compiler.classpath.AbstractClasspathFile;
import com.dragome.commons.compiler.classpath.ClasspathFile;

public class InsideJarClasspathFile extends AbstractClasspathFile implements ClasspathFile
{
	private JarEntry entry;
	private JarFile jarFile;

	public String getFilename()
	{
		return entry.getName();
	}

	public InsideJarClasspathFile(JarFile jarFile, JarEntry entry, String path)
	{
		super(path);
		this.jarFile= jarFile;

		this.entry= entry;

		lastModified= entry.getTime();
	}

	public InputStream openInputStream()
	{
		try
		{
			return inputStream= jarFile.getInputStream(entry);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public long getCRC()
	{
		return entry.getCrc();
	}
}
