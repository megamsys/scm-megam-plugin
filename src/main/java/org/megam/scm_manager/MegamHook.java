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



package org.megam.scm_manager;

//~--- non-JDK imports --------------------------------------------------------

import com.google.common.base.Objects;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sebastian Sdorra
 */
@XmlRootElement(name = "megamhook")
@XmlAccessorType(XmlAccessType.FIELD)
public class MegamHook
{

  /**
   * Constructs ...
   *
   */
  MegamHook() {}

  /**
   * Constructs ...
   *
   *
   * @param email
   */
  public MegamHook(String email, String apikey, String appname)
  {
    this(email, apikey, appname, false, false);
  }

  /**
   * Constructs ...
   *
   *
   * @param email
   * @param executeOnEveryCommit
   * @param apikey
   */
  public MegamHook(String email, String apikey, String appname, boolean executeOnEveryCommit, boolean sendCommitData)
  {
    this.email = email;
    this.apikey = apikey;
    this.appname = appname;
    this.executeOnEveryCommit = executeOnEveryCommit; 
    this.sendCommitData = sendCommitData;  
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param obj
   *
   * @return
   */
  @Override
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    final MegamHook other = (MegamHook) obj;

    return Objects.equal(email, other.email)
      && Objects.equal(apikey, other.apikey)
      && Objects.equal(appname, other.appname)
      && Objects.equal(executeOnEveryCommit, other.executeOnEveryCommit)
      && Objects.equal(sendCommitData, other.sendCommitData);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    return Objects.hashCode(email, apikey, appname, executeOnEveryCommit, sendCommitData);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    //J-
    return Objects.toStringHelper(this)
                  .add("email", email)
                  .add("apikey", apikey)
                  .add("appname", appname)
                  .add("executeOnEveryCommit", executeOnEveryCommit)
                  .add("sendCommitData", sendCommitData)
                  .toString();
    //J+
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isExecuteOnEveryCommit()
  {
    return executeOnEveryCommit;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getApiKey()
  {
    return apikey;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAppName()
  {
    return appname;
  }
  
  /**
  * Method description
  *
  *
  * @return
  */
    public boolean isSendCommitData()
    {
      return sendCommitData;
    }
  
  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean executeOnEveryCommit;
  
  /** Field description */
  private boolean sendCommitData;

  /** Field description */
  private String appname;
  
  /** Field description */
  private String apikey;

  /** Field description */
  private String email;
}
