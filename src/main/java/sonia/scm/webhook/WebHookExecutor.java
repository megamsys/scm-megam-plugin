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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sonia.scm.net.HttpClient;
import sonia.scm.net.HttpResponse;
import sonia.scm.repository.Changeset;
import sonia.scm.repository.Repository;
import sonia.scm.webhook.data.ImmutableEncodedChangeset;
import sonia.scm.webhook.data.ImmutableEncodedRepository;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class WebHookExecutor implements Runnable
{

  /**
   * the logger for WebHookExecutor
   */
  private static final Logger logger =
    LoggerFactory.getLogger(WebHookExecutor.class);

  //~--- constructors ---------------------------------------------------------

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
  public WebHookExecutor(HttpClient httpClient, UrlParser urlParser,
    WebHook webHook, Repository repository, Collection<Changeset> changesets)
  {
    this.httpClient = httpClient;
    this.expression = urlParser.parse(webHook.getUrlPattern());
    this.webHook = webHook;
    this.repository = repository;
    this.changesets = changesets;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void run()
  {
    logger.debug("execute webhook: {}", webHook);
    if (webHook.isExecuteOnEveryCommit())
    {
      for (Changeset c : changesets)
      {
        String url = createUrl(repository, c);

        if (webHook.isSendCommitData())
        {
          execute(url, c);
        }
        else
        {
          execute(url, null);
        }
      }
    }
    else
    {
      String url = createUrl(repository, changesets);

      if (webHook.isSendCommitData())
      {
        execute(url, new Changesets(changesets));
      }
      else
      {
        execute(url, null);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param repository
   *
   * @return
   */
  private Map<String, Object> createBaseEnvironment(Repository repository)
  {
    Map<String, Object> env = new HashMap<String, Object>();

    env.put("repository", new ImmutableEncodedRepository(repository));

    return env;
  }

  /**
   * Method description
   *
   *
   * @param repository
   * @param changeset
   *
   * @return
   */
  private String createUrl(Repository repository, Changeset changeset)
  {
    Map<String, Object> env = createBaseEnvironment(repository);

    ImmutableEncodedChangeset iec = new ImmutableEncodedChangeset(changeset);

    env.put("changeset", iec);
    env.put("commit", iec);

    return expression.evaluate(env);
  }

  /**
   * Method description
   *
   *
   * @param repository
   * @param changesets
   *
   * @return
   */
  private String createUrl(Repository repository,
    Collection<Changeset> changesets)
  {
    Map<String, Object> env = createBaseEnvironment(repository);
    Iterator<Changeset> it = changesets.iterator();
    Changeset changeset = it.next();

    env.put("last", new ImmutableEncodedChangeset(changeset));

    while (it.hasNext())
    {
      changeset = it.next();
    }

    env.put("first", new ImmutableEncodedChangeset(changeset));

    return expression.evaluate(env);
  }

  /**
   * Method description
   *
   *
   * @param url
   * @param data
   */
  private void execute(String url, Object data)
  {
    if (logger.isInfoEnabled())
    {
      logger.info("execute webhook for url {}", url);
    }

    // TODO implement data
    try
    {
      HttpResponse response = httpClient.get(url);
      int statusCode = response.getStatusCode();

      if ((statusCode >= 200) && (statusCode < 300))
      {
        if (logger.isInfoEnabled())
        {
          logger.info("webhook {} ended successfully with status code {}", url,
            statusCode);
        }
      }
      else if (logger.isWarnEnabled())
      {
        logger.warn("webhook {} failed with statusCode {}", url, statusCode);
      }
    }
    catch (Exception ex)
    {
      logger.error("error during webhook execution for ".concat(url), ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Collection<Changeset> changesets;

  /** Field description */
  private UrlExpression expression;

  /** Field description */
  private HttpClient httpClient;

  /** Field description */
  private Repository repository;

  /** Field description */
  private WebHook webHook;
}
