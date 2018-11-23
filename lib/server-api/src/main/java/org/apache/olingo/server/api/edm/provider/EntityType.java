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
package org.apache.olingo.server.api.edm.provider;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import java.util.List;

public class EntityType extends StructuredType {

  private List<PropertyRef> key;

  private boolean hasStream;

  public boolean hasStream() {
    return hasStream;
  }

  public EntityType setHasStream(final boolean hasStream) {
    this.hasStream = hasStream;
    return this;
  }

  public List<PropertyRef> getKey() {
    return key;
  }

  public EntityType setKey(final List<PropertyRef> key) {
    this.key = key;
    return this;
  }

  @Override
  public EntityType setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public EntityType setOpenType(final boolean isOpenType) {
    this.isOpenType = isOpenType;
    return this;
  }

  @Override
  public EntityType setBaseType(final FullQualifiedName baseType) {
    this.baseType = baseType;
    return this;
  }

  @Override
  public EntityType setAbstract(final boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  @Override
  public EntityType setProperties(final List<Property> properties) {
    this.properties = properties;
    return this;
  }

  @Override
  public EntityType setNavigationProperties(final List<NavigationProperty> navigationProperties) {
    this.navigationProperties = navigationProperties;
    return this;
  }
}
