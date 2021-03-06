/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.client.core.edm.xml.v4;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.edm.xml.v4.Edmx;
import org.apache.olingo.client.api.edm.xml.v4.Reference;
import org.apache.olingo.client.core.edm.xml.AbstractEdmx;

public class EdmxImpl extends AbstractEdmx implements Edmx {

  private static final long serialVersionUID = -6293476719276092572L;

  private final List<Reference> references = new ArrayList<Reference>();

  @Override
  public DataServicesImpl getDataServices() {
    return (DataServicesImpl) super.getDataServices();
  }

  @Override
  public List<Reference> getReferences() {
    return references;
  }

}
