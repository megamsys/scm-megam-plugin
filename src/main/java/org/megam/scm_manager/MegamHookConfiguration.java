/**
 * Copyright (c) 2010, rajthilak
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 3. Neither the name of SCM-Manager; nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://bitbucket.org/sdorra/scm-manager
 *
 */

package org.megam.scm_manager;

//~--- non-JDK imports --------------------------------------------------------

import sonia.scm.PropertiesAware;
import sonia.scm.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author rajthilak
 */
@XmlRootElement(name = "megamhooks")
@XmlAccessorType(XmlAccessType.FIELD)
public class MegamHookConfiguration implements Iterable<MegamHook> {

	/** Field description */
	public static final String PROPERTY_MEGAMHOOKS = "megamhooks";

	// ~--- constructors
	// ---------------------------------------------------------

	/**
	 * Constructs ...
	 * 
	 */
	public MegamHookConfiguration() {
	}

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param properies
	 */
	public MegamHookConfiguration(PropertiesAware properies) {
		String megamHookString = properies.getProperty(PROPERTY_MEGAMHOOKS);

		if (Util.isNotEmpty(megamHookString)) {
			parseMegamHookProperty(megamHookString);
		}
	}

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param megamhooks
	 */
	public MegamHookConfiguration(Set<MegamHook> megamhooks) {
		this.megamhooks.addAll(megamhooks);
	}

	// ~--- methods
	// --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public Iterator<MegamHook> iterator() {
		return megamhooks.iterator();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param otherConfiguration
	 * 
	 * @return
	 */
	public MegamHookConfiguration merge(MegamHookConfiguration otherConfiguration) {
		Set<MegamHook> allHooks = new HashSet<MegamHook>();

		allHooks.addAll(megamhooks);
		allHooks.addAll(otherConfiguration.megamhooks);

		return new MegamHookConfiguration(allHooks);
	}

	// ~--- get methods
	// ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public boolean isMegamHookAvailable() {
		return !megamhooks.isEmpty();
	}

	// ~--- methods
	// --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param megamHookConfig
	 */
	private void parseMegamHookConfig(String megamHookConfig) {
		String[] configParts = megamHookConfig.split(";");

		if (configParts.length > 0) {
			String email = configParts[0];
			String apikey = configParts[1];
			String appname = configParts[2];
			boolean executeOnEveryCommit = false;
			boolean sendCommitData = false;

			if (configParts.length > 3) {
				executeOnEveryCommit = Boolean.parseBoolean(configParts[3]);

				if (configParts.length > 4) {
					sendCommitData = Boolean.parseBoolean(configParts[4]);
				}
			}

			megamhooks.add(new MegamHook(email, apikey, appname, executeOnEveryCommit,
					sendCommitData));
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param megamHookString
	 */
	private void parseMegamHookProperty(String megamHookString) {
		String[] megamHookConfigs = megamHookString.split("\\|");

		for (String megamHookConfig : megamHookConfigs) {
			parseMegamHookConfig(megamHookConfig);
		}
	}

	// ~--- get methods
	// ----------------------------------------------------------

	/** Field description */
	@XmlElement(name = "megamhook")
	private Set<MegamHook> megamhooks = new HashSet<MegamHook>();
}