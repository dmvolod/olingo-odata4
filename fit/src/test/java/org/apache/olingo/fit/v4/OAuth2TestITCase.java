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
package org.apache.olingo.fit.v4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntityRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.uri.v4.URIBuilder;
import org.apache.olingo.client.api.v4.EdmEnabledODataClient;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.net.URI;
import org.apache.olingo.client.core.http.DefaultHttpClientFactory;
import org.apache.olingo.fit.CXFOAuth2HttpClientFactory;

public class OAuth2TestITCase extends AbstractTestITCase {

  private static final URI OAUTH2_GRANT_SERVICE_URI =
          URI.create("http://localhost:9080/stub/StaticService/oauth2/authorize");

  private static final URI OAUTH2_TOKEN_SERVICE_URI =
          URI.create("http://localhost:9080/stub/StaticService/oauth2/token");

  private EdmEnabledODataClient _edmClient;

  @BeforeClass
  public static void enableOAuth2() {
    client.getConfiguration().setHttpClientFactory(
            new CXFOAuth2HttpClientFactory(OAUTH2_GRANT_SERVICE_URI, OAUTH2_TOKEN_SERVICE_URI));
  }

  @AfterClass
  public static void disableOAuth2() {
    client.getConfiguration().setHttpClientFactory(new DefaultHttpClientFactory());
  }

  protected EdmEnabledODataClient getEdmClient() {
    if (_edmClient == null) {
      _edmClient = ODataClientFactory.getEdmEnabledV4(testOAuth2ServiceRootURL);
      _edmClient.getConfiguration().setHttpClientFactory(
              new CXFOAuth2HttpClientFactory(OAUTH2_GRANT_SERVICE_URI, OAUTH2_TOKEN_SERVICE_URI));
    }

    return _edmClient;
  }

  private void read(final ODataClient client, final ODataFormat format) {
    final URIBuilder uriBuilder =
            client.newURIBuilder(testOAuth2ServiceRootURL).appendEntitySetSegment("Orders").appendKeySegment(8);

    final ODataEntityRequest<ODataEntity> req = client.getRetrieveRequestFactory().getEntityRequest(uriBuilder.build());
    req.setFormat(format);

    final ODataRetrieveResponse<ODataEntity> res = req.execute();
    assertEquals(200, res.getStatusCode());

    final String etag = res.getETag();
    assertTrue(StringUtils.isNotBlank(etag));

    final ODataEntity order = res.getBody();
    assertEquals(etag, order.getETag());
    assertEquals("Microsoft.Test.OData.Services.ODataWCFService.Order", order.getTypeName().toString());
    assertEquals("Edm.Int32", order.getProperty("OrderID").getPrimitiveValue().getTypeName());
    assertEquals("Edm.DateTimeOffset", order.getProperty("OrderDate").getPrimitiveValue().getTypeName());
    assertEquals("Edm.Duration", order.getProperty("ShelfLife").getPrimitiveValue().getTypeName());
    assertEquals("Collection(Edm.Duration)", order.getProperty("OrderShelfLifes").getCollectionValue().getTypeName());
  }

  @Test
  public void readAsAtom() {
    read(client, ODataFormat.ATOM);
  }

  @Test
  public void readAsFullJSON() {
    read(client, ODataFormat.JSON_FULL_METADATA);
  }

  @Test
  public void readAsJSON() {
    read(getEdmClient(), ODataFormat.JSON);
  }

  @Test
  public void createAndDelete() {
    createAndDeleteOrder(testOAuth2ServiceRootURL, ODataFormat.JSON, 1002);
  }

}
