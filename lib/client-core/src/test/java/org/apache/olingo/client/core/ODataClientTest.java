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
package org.apache.olingo.client.core;

import org.apache.olingo.client.api.CommonODataClient;
import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ODataClientTest {

  @Test
  public void before() {
    CommonODataClient<?> client = ODataClientFactory.getV3();
    assertNotNull(client);
    assertEquals(ODataServiceVersion.V30, client.getServiceVersion());

    client = ODataClientFactory.getV4();
    assertNotNull(client);
    assertEquals(ODataServiceVersion.V40, client.getServiceVersion());
  }
}
