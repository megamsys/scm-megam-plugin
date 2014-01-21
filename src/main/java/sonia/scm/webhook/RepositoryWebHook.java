/**
 * Copyright (c) 2010, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of SCM-Manager; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
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



package sonia.scm.webhook;

//~--- non-JDK imports --------------------------------------------------------

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.net.HttpClient;
import sonia.scm.plugin.ext.Extension;
import sonia.scm.repository.Changeset;
import sonia.scm.repository.PostReceiveRepositoryHook;
import sonia.scm.repository.Repository;
import sonia.scm.repository.RepositoryHookEvent;
import sonia.scm.webhook.jexl.JexlUrlParser;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;

/**
 *
 * @author Sebastian Sdorra
 */
@Extension
public class RepositoryWebHook extends PostReceiveRepositoryHook
{

  /**
   * the logger for RepositoryWebHook
   */
  private static final Logger logger =
    LoggerFactory.getLogger(RepositoryWebHook.class);

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param httpClientProvider
   * @param context
   */
  @Inject
  public RepositoryWebHook(Provider<HttpClient> httpClientProvider,
    WebHookContext context)
  {
    this.httpClientProvider = httpClientProvider;
    this.urlParser = new JexlUrlParser();
    this.context = context;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  @Override
  public void onEvent(RepositoryHookEvent event)
  {
    Repository repository = event.getRepository();

    if (repository != null)
    {
      WebHookConfiguration configuration = context.getConfiguration(repository);

      if (configuration.isWebHookAvailable())
      {
        executeWebHooks(configuration, repository, event.getChangesets());
      }
      else if (logger.isDebugEnabled())
      {
        logger.debug("no webhook defined for repository {}",
          repository.getName());
      }
    }
    else if (logger.isErrorEnabled())
    {
      logger.error("received hook without repository");
    }
  }

  /**
   * Method description
   *
   *
   * @param configuration
   * @param repository
   * @param changesets
   */
  private void executeWebHooks(WebHookConfiguration configuration,
    Repository repository, Collection<Changeset> changesets)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("execute webhooks for repository {}", repository.getName());
    }

    for (WebHook webHook : configuration)
    {

      // async ??
      new WebHookExecutor(httpClientProvider.get(), urlParser, webHook,
        repository, changesets).run();
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final WebHookContext context;

  /** Field description */
  private final Provider<HttpClient> httpClientProvider;

  /** Field description */
  private final UrlParser urlParser;
}
