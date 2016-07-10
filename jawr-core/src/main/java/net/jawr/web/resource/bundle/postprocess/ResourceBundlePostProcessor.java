/**
 * Copyright 2007-2016 Jordi Hernández Sellés, Ibrahim Chaehoi
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
package net.jawr.web.resource.bundle.postprocess;

/**
 * Interface for bundle postprocessors, which will perform different kinds of
 * processing on joined resource bundles, such as compression.
 * 
 * @author Jordi Hernández Sellés
 * @author ibrahim Chaehoi
 */
public interface ResourceBundlePostProcessor {

	/**
	 * Postprocess a bundle of resources.
	 * 
	 * @param bundleString
	 *            Joined resources.
	 * @return StringBuffer a buffer containing the postprocessed bundle.
	 */
	public StringBuffer postProcessBundle(BundleProcessingStatus status, StringBuffer bundleString);

}
