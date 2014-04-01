/* 
** Copyright [2014] [Megam Systems]
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
** http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
** 
** https://github.com/megamsys/scm-megam-plugin
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