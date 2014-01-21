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
 * @author Sebastian Sdorra
 */
@XmlRootElement(name = "webhooks")
@XmlAccessorType(XmlAccessType.FIELD)
public class WebHookConfiguration implements Iterable<WebHook>
{

  /** Field description */
  public static final String PROPERTY_WEBHOOKS = "webhooks";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public WebHookConfiguration() {}

  /**
   * Constructs ...
   *
   *
   * @param properies
   */
  public WebHookConfiguration(PropertiesAware properies)
  {
    String webHookString = properies.getProperty(PROPERTY_WEBHOOKS);

    if (Util.isNotEmpty(webHookString))
    {
      parseWebHookProperty(webHookString);
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param webhooks
   */
  public WebHookConfiguration(Set<WebHook> webhooks)
  {
    this.webhooks.addAll(webhooks);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Iterator<WebHook> iterator()
  {
    return webhooks.iterator();
  }

  /**
   * Method description
   *
   *
   * @param otherConfiguration
   *
   * @return
   */
  public WebHookConfiguration merge(WebHookConfiguration otherConfiguration)
  {
    Set<WebHook> allHooks = new HashSet<WebHook>();

    allHooks.addAll(webhooks);
    allHooks.addAll(otherConfiguration.webhooks);

    return new WebHookConfiguration(allHooks);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isWebHookAvailable()
  {
    return !webhooks.isEmpty();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param webHookConfig
   */
  private void parseWebHookConfig(String webHookConfig)
  {
    String[] configParts = webHookConfig.split(";");

    if (configParts.length > 0)
    {
      String urlPattern = configParts[0];
      boolean executeOnEveryCommit = false;
      boolean sendCommitData = false;

      if (configParts.length > 1)
      {
        executeOnEveryCommit = Boolean.parseBoolean(configParts[1]);

        if (configParts.length > 2)
        {
          sendCommitData = Boolean.parseBoolean(configParts[2]);
        }
      }

      webhooks.add(new WebHook(urlPattern, executeOnEveryCommit,
        sendCommitData));
    }
  }

  /**
   * Method description
   *
   *
   * @param webHookString
   */
  private void parseWebHookProperty(String webHookString)
  {
    String[] webHookConfigs = webHookString.split("\\|");

    for (String webHookConfig : webHookConfigs)
    {
      parseWebHookConfig(webHookConfig);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /** Field description */
  @XmlElement(name = "webhook")
  private Set<WebHook> webhooks = new HashSet<WebHook>();
}
