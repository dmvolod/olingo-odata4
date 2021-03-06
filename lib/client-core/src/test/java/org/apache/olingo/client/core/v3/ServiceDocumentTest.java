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
package org.apache.olingo.client.core.v3;

import org.apache.olingo.client.api.v3.ODataClient;
import org.apache.olingo.client.core.AbstractTest;
import org.apache.olingo.commons.api.domain.ODataServiceDocument;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.commons.api.serialization.ODataDeserializerException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ServiceDocumentTest extends AbstractTest {

  @Override
  protected ODataClient getClient() {
    return v3Client;
  }

  private String getFileExtension(final ODataFormat format) {
    return format == ODataFormat.XML ? "xml" : "json";
  }

  private void parse(final ODataFormat format) throws ODataDeserializerException {
    final ODataServiceDocument serviceDocument = getClient().getReader().readServiceDocument(
            getClass().getResourceAsStream("serviceDocument." + getFileExtension(format)), format);
    assertNotNull(serviceDocument);
    assertTrue(serviceDocument.getEntitySetNames().contains("Persons"));
  }

  @Test
  public void json() throws Exception {
    parse(ODataFormat.JSON);
  }

  @Test
  public void xml() throws Exception {
    parse(ODataFormat.XML);
  }
}
