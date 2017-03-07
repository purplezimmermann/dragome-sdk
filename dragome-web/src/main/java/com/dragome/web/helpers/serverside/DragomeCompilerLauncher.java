/*******************************************************************************
 * Copyright (c) 2011-2014 Fernando Petrola
 *
 * This file is part of Dragome SDK.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.dragome.web.helpers.serverside;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.dragome.commons.DragomeConfigurator;
import com.dragome.commons.compiler.BytecodeToJavascriptCompiler;
import com.dragome.commons.compiler.BytecodeToJavascriptCompilerConfiguration;
import com.dragome.commons.compiler.BytecodeTransformer;
import com.dragome.commons.compiler.annotations.CompilerType;
import com.dragome.commons.compiler.classpath.Classpath;
import com.dragome.commons.compiler.classpath.ClasspathEntry;
import com.dragome.commons.compiler.classpath.ClasspathFile;
import com.dragome.commons.compiler.classpath.ClasspathFileFilter;
import com.dragome.commons.compiler.classpath.serverside.JarClasspathEntry;
import com.dragome.services.ServiceLocator;
import com.dragome.services.WebServiceLocator;
import com.dragome.view.VisualActivity;
import com.dragome.web.helpers.DefaultClasspathFileFilter;

import proguard.Configuration;
import proguard.ConfigurationParser;
import proguard.ProGuard;

public class DragomeCompilerLauncher
{
	private static Logger LOGGER= Logger.getLogger(DragomeCompilerLauncher.class.getName());

	public static void compileWithMainClass(Classpath classPath, String target)
	{
		ServiceLocator serviceLocator= ServiceLocator.getInstance();
		DragomeConfigurator configurator= serviceLocator.getConfigurator();
		String mainClassName= VisualActivity.class.getName();
		CompilerType defaultCompilerType= configurator.getDefaultCompilerType();
		BytecodeTransformer bytecodeTransformer= configurator.getBytecodeTransformer();

		ClasspathFileFilter classpathFilter= configurator.getClasspathFilter();
		if (classpathFilter == null)
			classpathFilter= new DefaultClasspathFileFilter();

		BytecodeToJavascriptCompiler bytecodeToJavascriptCompiler= WebServiceLocator.getInstance().getBytecodeToJavascriptCompiler();

		List<ClasspathEntry> extraClasspath= configurator.getExtraClasspath(classPath);
		classPath.addEntries(extraClasspath);
		configurator.sortClassPath(classPath);
		classPath= process(classPath, configurator);

		BytecodeToJavascriptCompilerConfiguration compilerConfiguration= new BytecodeToJavascriptCompilerConfiguration(classPath, target, mainClassName, defaultCompilerType, bytecodeTransformer, new DefaultClasspathFileFilter(), configurator.isCheckingCast(), configurator.isCaching(), configurator.isStopOnMissingClass());
		bytecodeToJavascriptCompiler.configure(compilerConfiguration);
		bytecodeToJavascriptCompiler.compile();
	}

	private static Classpath process(Classpath classPath, DragomeConfigurator configurator)
	{
		try
		{
			String path= null;

			String tempDir= System.getProperty("java.io.tmpdir");
			File tmpDir= new File(tempDir + File.separatorChar + "dragomeTemp");
			Path tmpPath= tmpDir.toPath();
			FileUtils.deleteDirectory(tmpDir);
			Files.createDirectories(tmpPath);
			File file= Files.createTempFile(tmpPath, "dragome-merged-", ".jar").toFile();
			file.deleteOnExit();

			try (JarOutputStream jos= new JarOutputStream(new FileOutputStream(file)))
			{
				final ArrayList<String> keepClass= new ArrayList<>();
				final ClasspathFileFilter classpathFilter = configurator.getClasspathFilter();
				List<ClasspathEntry> entries= classPath.getEntries();
				for (ClasspathEntry classpathEntry : entries) {
					classpathEntry.copyFilesToJar(jos, new DefaultClasspathFileFilter()
					{
						public boolean accept(ClasspathFile classpathFile)
						{
							boolean result= super.accept(classpathFile);

							String entryName= classpathFile.getPath();
							entryName = entryName.replace("\\", "/");
							
							if (!keepClass.contains(entryName))
							{
								keepClass.add(entryName);

								if (entryName.endsWith(".js") || entryName.endsWith(".class") || entryName.contains("MANIFEST") || entryName.contains(".html") || entryName.contains(".css"))
									result&= true;
								
								if(entryName.contains("CollapsableTextWindow$1")) {
									System.out.println("");
								}
								if(classpathFilter != null)
									result&= classpathFilter.accept(classpathFile);
							}
							else 
								result = false;
							return result;
						}
					});
				}
			}
			if (configurator.isRemoveUnusedCode())
			{
				file = runProguard(file, configurator);
				file.deleteOnExit();
			}
			if(configurator.isObfuscateCode()) {
				file = runProguardObf(file, configurator);
				file.deleteOnExit();
			}
			path= file.getAbsolutePath();
			return new Classpath(JarClasspathEntry.createFromPath(path));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	private static File runProguard(File file, DragomeConfigurator configurator) throws Exception
	{
		URI uri= DragomeCompilerLauncher.class.getResource("/proguard.conf").toURI();
		Properties properties= System.getProperties();
		properties.put("in-jar-filename", file.getAbsolutePath());
		String outFilename= file.getAbsolutePath().replace(".jar", "-proguard.jar");
		File file2 = new File(outFilename);
		properties.put("out-jar-filename", outFilename);
		ConfigurationParser parser= new ConfigurationParser(uri.toURL(), properties);
		ArrayList<URL> urls = new ArrayList<URL>();
		configurator.getAdditionalCodeKeepConfigFile(urls);
		Configuration configuration= new Configuration();
		parser.parse(configuration);

		for(int i = 0; i < urls.size();i++) {
			URL url = urls.get(i);
			ConfigurationParser parserForAdditionalKeepCodeConfigFile= new ConfigurationParser(url, properties);
			parserForAdditionalKeepCodeConfigFile.parse(configuration);
		}

		new ProGuard(configuration).execute();
//		return runProguardObf(file2, configurator);
//		return new Classpath(JarClasspathEntry.createFromPath(outFilename));
		return file2;
	}
	
	private static File runProguardObf(File file, DragomeConfigurator configurator) throws Exception
	{
		URI uri= DragomeCompilerLauncher.class.getResource("/proguardObf.conf").toURI();
		Properties properties= System.getProperties();
		properties.put("in-jar-filename", file.getAbsolutePath());
		String outFilename= file.getAbsolutePath().replace(".jar", "-Obf.jar");
		File file2 = new File(outFilename);
		properties.put("out-jar-filename", outFilename);
		ConfigurationParser parser= new ConfigurationParser(uri.toURL(), properties);
		ArrayList<URL> urls = new ArrayList<URL>();
		configurator.getAdditionalObfuscateCodeKeepConfigFile(urls);
		Configuration configuration= new Configuration();
			for(int i = 0; i < urls.size();i++) {
				URL url = urls.get(i);
				ConfigurationParser parserForAdditionalKeepCodeConfigFile= new ConfigurationParser(url, properties);
				parserForAdditionalKeepCodeConfigFile.parse(configuration);
			}
		
		parser.parse(configuration);
		
		String class_path = System.getProperty("java.class.path");
		class_path = outFilename + ";" + class_path; 
		System.setProperty("java.class.path", class_path);
		new ProGuard(configuration).execute();
//		return new Classpath(JarClasspathEntry.createFromPath(outFilename));
		return file2;
	}
}
