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



package sonia.scm.webhook.data;

//~--- non-JDK imports --------------------------------------------------------

import sonia.scm.repository.Changeset;

/**
 *
 * @author Sebastian Sdorra
 */
public final class ImmutableEncodedChangeset
{

  /**
   * Constructs ...
   *
   *
   * @param changeset
   */
  public ImmutableEncodedChangeset(Changeset changeset)
  {
    this.changeset = changeset;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public ImmutableEncodedPerson getAuthor()
  {
    return new ImmutableEncodedPerson(changeset.getAuthor());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EncodedStringList getBranches()
  {
    return new EncodedStringList(changeset.getBranches());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getDate()
  {
    return changeset.getDate();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return Encoder.encode(changeset.getDescription());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getId()
  {
    return changeset.getId();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ImmutableEncodedModifications getModifications()
  {
    return new ImmutableEncodedModifications(changeset.getModifications());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EncodedStringList getParents()
  {
    return new EncodedStringList(changeset.getParents());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EncodedStringList getTags()
  {
    return new EncodedStringList(changeset.getTags());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Changeset changeset;
}
