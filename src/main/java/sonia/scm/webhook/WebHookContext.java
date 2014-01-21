/**
 * Copyright (c) 2010, Sebastian Sdorra All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. Neither the name of SCM-Manager;
 * nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://bitbucket.org/sdorra/scm-manager
 *
 */



package sonia.scm.webhook;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.Singleton;

import sonia.scm.repository.Repository;
import sonia.scm.store.Store;
import sonia.scm.store.StoreFactory;

/**
 *
 * @author Sebastian Sdorra
 */
@Singleton
public final class WebHookContext
{

  /** Field description */
  private static final String STORE_NAME = "webhook";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param storeFactory
   */
  @Inject
  public WebHookContext(StoreFactory storeFactory)
  {
    this.store = storeFactory.getStore(WebHookConfiguration.class, STORE_NAME);
    globalConfiguration = store.get();

    if (globalConfiguration == null)
    {
      globalConfiguration = new WebHookConfiguration();
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param repository
   *
   * @return
   */
  public WebHookConfiguration getConfiguration(Repository repository)
  {
    WebHookConfiguration repoConf = new WebHookConfiguration(repository);

    return globalConfiguration.merge(repoConf);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public WebHookConfiguration getGlobalConfiguration()
  {
    return globalConfiguration;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param globalConfiguration
   */
  public void setGlobalConfiguration(WebHookConfiguration globalConfiguration)
  {
    this.globalConfiguration = globalConfiguration;
    store.set(globalConfiguration);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Store<WebHookConfiguration> store;

  /** Field description */
  private WebHookConfiguration globalConfiguration;
}
