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
import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.Target;
import org.apache.olingo.commons.core.edm.AbstractEdmBindingTarget;
import org.apache.olingo.commons.core.edm.EdmNavigationPropertyBindingImpl;
import org.apache.olingo.server.api.edm.provider.BindingTarget;
import org.apache.olingo.server.api.edm.provider.NavigationPropertyBinding;

import java.util.ArrayList;
import java.util.List;

public abstract class EdmBindingTargetImpl extends AbstractEdmBindingTarget {

  private final BindingTarget target;
  private List<EdmNavigationPropertyBinding> navigationPropertyBindings;

  public EdmBindingTargetImpl(final Edm edm, final EdmEntityContainer container, final BindingTarget target) {
    super(edm, container, target.getName(), target.getType());
    this.target = target;
  }

  @Override
  public List<EdmNavigationPropertyBinding> getNavigationPropertyBindings() {
    if (navigationPropertyBindings == null) {
      List<NavigationPropertyBinding> providerBindings = target.getNavigationPropertyBindings();
      navigationPropertyBindings = new ArrayList<EdmNavigationPropertyBinding>();
      if (providerBindings != null) {
        for (NavigationPropertyBinding binding : providerBindings) {
          Target providerTarget = binding.getTarget();
          String targetString = "";
          if (providerTarget.getEntityContainer() != null) {
            targetString = targetString + providerTarget.getEntityContainer().getFullQualifiedNameAsString() + "/";
          }
          targetString = targetString + providerTarget.getTargetName();
          navigationPropertyBindings.add(new EdmNavigationPropertyBindingImpl(binding.getPath(), targetString));
        }
      }
    }
    return navigationPropertyBindings;
  }
}
