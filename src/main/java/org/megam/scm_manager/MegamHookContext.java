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
import com.google.inject.Singleton;

import sonia.scm.repository.Repository;
import sonia.scm.store.Store;
import sonia.scm.store.StoreFactory;

/**
 *
 * @author rajthilak
 */
@Singleton
public final class MegamHookContext
{

  /** Field description */
  private static final String STORE_NAME = "megamhook";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param storeFactory
   */
  @Inject
  public MegamHookContext(StoreFactory storeFactory)
  {
    this.store = storeFactory.getStore(MegamHookConfiguration.class, STORE_NAME);
    globalConfiguration = store.get();

    if (globalConfiguration == null)
    {
      globalConfiguration = new MegamHookConfiguration();
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
  public MegamHookConfiguration getConfiguration(Repository repository)
  {
    MegamHookConfiguration repoConf = new MegamHookConfiguration(repository);

    return globalConfiguration.merge(repoConf);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MegamHookConfiguration getGlobalConfiguration()
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
  public void setGlobalConfiguration(MegamHookConfiguration globalConfiguration)
  {
    this.globalConfiguration = globalConfiguration;
    store.set(globalConfiguration);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Store<MegamHookConfiguration> store;

  /** Field description */
  private MegamHookConfiguration globalConfiguration;
}
