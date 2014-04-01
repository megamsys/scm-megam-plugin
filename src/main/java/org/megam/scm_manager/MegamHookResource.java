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

import org.apache.shiro.SecurityUtils;
import sonia.scm.security.Role;
//~--- JDK imports ------------------------------------------------------------

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 *
 * @author rajthilak
 */
@Path("plugins/megamhook")
public class MegamHookResource
{

  /**
   * Constructs ...
   *
   *
   * @param securityContext
   * @param context
   */
  @Inject
  public MegamHookResource(MegamHookContext context)
  {	
	SecurityUtils.getSubject().checkRole(Role.ADMIN);
    this.context = context;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public MegamHookConfiguration getConfiguration()
  {	
    return context.getGlobalConfiguration();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param configuration
   */
  @POST
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public void setConfiguration(MegamHookConfiguration configuration)
  {
    context.setGlobalConfiguration(configuration);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final MegamHookContext context;
}
