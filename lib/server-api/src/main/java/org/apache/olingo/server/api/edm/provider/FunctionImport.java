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
import org.apache.olingo.commons.api.edm.Target;

public class FunctionImport extends OperationImport {

  private FullQualifiedName function;

  private boolean includeInServiceDocument;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public FunctionImport setName(final String name) {
    this.name = name;
    return this;
  }

  @Override
  public FunctionImport setEntitySet(final Target entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  public FullQualifiedName getFunction() {
    return function;
  }

  public FunctionImport setFunction(final FullQualifiedName function) {
    this.function = function;
    return this;
  }

  public boolean isIncludeInServiceDocument() {
    return includeInServiceDocument;
  }

  public FunctionImport setIncludeInServiceDocument(final boolean includeInServiceDocument) {
    this.includeInServiceDocument = includeInServiceDocument;
    return this;
  }
}
