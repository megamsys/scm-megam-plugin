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

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.plugin.ext.Extension;
import sonia.scm.repository.PostReceiveRepositoryHook;
import sonia.scm.repository.Repository;
import sonia.scm.repository.RepositoryHookEvent;
import org.megam.api.exception.APIContentException;
import org.megam.api.exception.APIInvokeException;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;


/**
 * 
 * @author rajthilak
 */

@Extension
public class RepositoryMegamHook extends PostReceiveRepositoryHook {

	/**
	 * the logger for RepositoryWebHook
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryMegamHook.class);

	// ~--- constructors
	// ---------------------------------------------------------

	/**
	 * Constructs ...
	 * 
	 * 	 
	 * @param context
	 */
	@Inject
	public RepositoryMegamHook(MegamHookContext context) {	
		this.context = context;
	}

	// ~--- methods
	// --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param event
	 */
	@Override
	public void onEvent(RepositoryHookEvent event) {
		try {
			Repository repository = event.getRepository();			
			if (repository != null) {
				MegamHookConfiguration configuration = context
						.getConfiguration(repository);

				if (configuration.isMegamHookAvailable()) {
					executeMegamHooks(configuration, repository);										
				} else if (logger.isDebugEnabled()) {
					logger.debug("no megamhook defined for repository {}",
							repository.getName());
				}
			} else if (logger.isErrorEnabled()) {
				logger.error("received hook without repository");
			}
		} catch (APIInvokeException ex) {
			logger.error("error during megamhook execution for ", ex);
		} catch (APIContentException apc) {
			logger.error("error during megamhook execution for ", apc);
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param configuration
	 * @param repository
	 */
	private void executeMegamHooks(MegamHookConfiguration configuration,
			Repository repository) throws APIInvokeException,
			APIContentException {
		if (logger.isDebugEnabled()) {
			logger.debug("execute megamhooks for repository {}",
					repository.getName());
		}

		for (MegamHook megamHook : configuration) {
			// async ??
			new MegamHookExecutor(megamHook,
					repository).run();
		}
	}

	// ~--- fields
	// ---------------------------------------------------------------

	/** Field description */
	private final MegamHookContext context;

}
