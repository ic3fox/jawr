/**
 * Copyright 2009-2016 Ibrahim Chaehoi
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package net.jawr.web.resource.handler.reader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;

import net.jawr.web.JawrConstant;
import net.jawr.web.config.JawrConfig;
import net.jawr.web.resource.bundle.JoinableResourceBundle;

/**
 * This class defines the resource reader which is based on the servlet context
 * 
 * @author Ibrahim Chaehoi
 *
 */
public class BaseServletContextResourceReader implements ServletContextResourceReader {

	/** The servlet context */
	private ServletContext context;

	/** The charset */
	private Charset charset;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.handler.reader.ServletContextResourceReader#init(
	 * javax.servlet.ServletContext, net.jawr.web.config.JawrConfig)
	 */
	@Override
	public void init(ServletContext context, JawrConfig config) {
		this.context = context;
		this.charset = config.getResourceCharset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.handler.reader.TextResourceReader#getResource(net.
	 * jawr.web.resource.bundle.JoinableResourceBundle, java.lang.String)
	 */
	@Override
	public Reader getResource(JoinableResourceBundle bundle, String resourceName) {

		return getResource(bundle, resourceName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jawr.web.resource.handler.ResourceReader#getResource(java.lang.
	 * String, boolean)
	 */
	@Override
	public Reader getResource(JoinableResourceBundle bundle, String resourceName, boolean processingBundle) {

		Reader rd = null;
		if (!resourceName.contains(":")) {
			InputStream is = context.getResourceAsStream(resourceName);
			if (is != null) {
				rd = new InputStreamReader(is, charset);
			}
		}
		return rd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jawr.web.resource.handler.reader.StreamResourceReader#
	 * getResourceAsStream(net.jawr.web.resource.bundle.JoinableResourceBundle,
	 * java.lang.String)
	 */
	@Override
	public InputStream getResourceAsStream(String resourceName) {

		return getResourceAsStream(resourceName, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.jawr.web.resource.handler.reader.StreamResourceReader#
	 * getResourceAsStream(net.jawr.web.resource.bundle.JoinableResourceBundle,
	 * java.lang.String, boolean)
	 */
	@Override
	public InputStream getResourceAsStream(String resourceName, boolean processingBundle) {

		InputStream is = null;
		if (!resourceName.contains(":") && isAccessPermitted(resourceName)) {
			is = context.getResourceAsStream(resourceName);
		}
		return is;
	}

	/**
	 * Checks if the resource should be accessible
	 * 
	 * @param resourceName
	 *            the resource name
	 * @return true if the resource should be accessible
	 */
	protected boolean isAccessPermitted(String resourceName) {

		return !resourceName.startsWith(JawrConstant.WEB_INF_DIR_PREFIX)
				&& !resourceName.startsWith(JawrConstant.META_INF_DIR_PREFIX);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.handler.reader.ResourceBrowser#getResourceNames(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getResourceNames(String path) {
		Set<String> paths = context.getResourcePaths(path);
		Set<String> names = new HashSet<String>();
		int length = path.length();
		if (null != paths) {
			for (Iterator<String> it = paths.iterator(); it.hasNext();) {
				String resourcePath = (String) it.next();
				names.add(resourcePath.substring(length, resourcePath.length()));
			}
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.handler.reader.ResourceBrowser#isDirectory(java.
	 * lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isDirectory(String path) {
		Set<String> paths = context.getResourcePaths(path);
		return (null != paths && paths.size() > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.handler.reader.ResourceBrowser#getFilePath(java.
	 * lang.String)
	 */
	@Override
	public String getFilePath(String resourcePath) {
		return context.getRealPath(resourcePath);
	}

}
