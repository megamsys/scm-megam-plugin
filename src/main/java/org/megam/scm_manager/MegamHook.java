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

import com.google.common.base.Objects;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rajthilak
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
