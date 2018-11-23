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
package org.apache.olingo.server.core;

import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.apache.olingo.server.api.edmx.EdmxReference;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.apache.olingo.server.core.edm.provider.EdmProviderImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class ServiceMetadataImpl implements ServiceMetadata {

  private final EdmProviderImpl edm;
  private final ODataServiceVersion version;
  private final List<EdmxReference> references = new ArrayList<EdmxReference>();

  public ServiceMetadataImpl(ODataServiceVersion version, EdmProvider edmProvider, List<EdmxReference> references) {
    this.edm = new EdmProviderImpl(edmProvider);
    this.version = version;
    this.references.addAll(references);
  }

  @Override
  public Edm getEdm() {
    return edm;
  }

  @Override
  public ODataServiceVersion getDataServiceVersion() {
    return version;
  }

  @Override
  public List<EdmxReference> getReferences() {
    return Collections.unmodifiableList(references);
  }
}
