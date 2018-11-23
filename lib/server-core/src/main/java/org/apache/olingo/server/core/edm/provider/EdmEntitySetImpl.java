/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.server.core.edm.provider;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmAnnotation;
import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmTerm;
import org.apache.olingo.server.api.edm.provider.EntitySet;

import java.util.List;

public class EdmEntitySetImpl extends EdmBindingTargetImpl implements EdmEntitySet {

  private EntitySet entitySet;

  public EdmEntitySetImpl(final Edm edm, final EdmEntityContainer container, final EntitySet entitySet) {
    super(edm, container, entitySet);
    this.entitySet = entitySet;
  }

  @Override
  public boolean isIncludeInServiceDocument() {
    return entitySet.isIncludeInServiceDocument();
  }

  @Override
  public TargetType getAnnotationsTargetType() {
    return TargetType.EntitySet;
  }

  @Override
  public EdmAnnotation getAnnotation(final EdmTerm term) {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<EdmAnnotation> getAnnotations() {
    // TODO: implement
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
