/**
 * Copyright (c) 2010, Sebastian Sdorra
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
 * @author Sebastian Sdorra
 */
public class MegamHookExecutor implements Runnable {

	/**
	 * the logger for WebHookExecutor
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
		System.out.println(rs.json());
	}

	// ~--- fields
	// ---------------------------------------------------------------
		

	/** Field description */
	private Repository repository;

	/** Field description */
	private MegamHook megamHook;
}