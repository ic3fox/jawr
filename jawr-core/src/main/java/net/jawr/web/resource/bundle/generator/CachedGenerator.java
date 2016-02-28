/**
 * Copyright 2016 Ibrahim Chaehoi
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
package net.jawr.web.resource.bundle.generator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used by generator which needs to cache their generated
 * resource. This annotation is used by the {@link AbstractCachedGenerator}
 * 
 * @author Ibrahim Chaehoi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface CachedGenerator {

	/**
	 * Returns the name of the generator
	 * @return the name of the generator
	 */
	String name();
	
	/**
	 * Returns the cache mapping file name
	 * @return cache mapping file name
	 */
	String mappingFileName();
	
	/**
	 * Returns the cache directory name, which will be placed in the working directory
	 * @return cache directory name
	 */
	String cacheDirectory();
	
	CacheMode mode() default CacheMode.ALL;
	
	/**
	 * The cache mode.
	 * With DEBUG, only debug content will be cached
	 * With PROD, only production content will be cached
	 * With ALL, debug and production content will be cached
	 */
	public enum CacheMode {
		
		DEBUG,
		PROD,
		ALL;
	}
	
}
