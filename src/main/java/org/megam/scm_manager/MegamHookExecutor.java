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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.repository.Repository;
import org.megam.api.APIClient;
import org.megam.api.Nodes;
import org.megam.api.Requests;
import org.megam.api.result.NodeResult;
import org.megam.api.result.RequestResult;
import org.megam.api.exception.APIContentException;
import org.megam.api.exception.APIInvokeException;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author rajthilak
 */
public class MegamHookExecutor implements Runnable {

	/**
	 * the logger for MegamHookExecutor
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MegamHookExecutor.class);

	// ~--- constructors
	// ---------------------------------------------------------

	/**
	 * Constructs ...
	 * 
	 * 
	 * 
	 * @param httpClient
	 * @param urlParser
	 * @param webHook
	 * @param repository
	 * @param changesets
	 */
	public MegamHookExecutor(MegamHook megamHook, Repository repository) {			
		this.megamHook = megamHook;
		this.repository = repository;		
	}

	// ~--- methods
	// --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 */
	@Override
	public void run() {
		try {
			logger.debug("execute megamhook: {}", megamHook);
			if (megamHook.isExecuteOnEveryCommit()) {				
				execute();
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
	 * @param url
	 * @param data
	 */
	private void execute() throws APIInvokeException,
			APIContentException {
		if (logger.isInfoEnabled()) {
			logger.info("execute megamhook for url {}");
		}
		APIClient build = new APIClient(megamHook.getEmail(), megamHook.getApiKey());
		RequestResult rs = (RequestResult) new Requests(build)
				.post((NodeResult) new Nodes(build).list(megamHook.getAppName(),
						""));		
	}

	// ~--- fields
	// ---------------------------------------------------------------
		

	/** Field description */
	private Repository repository;

	/** Field description */
	private MegamHook megamHook;
}