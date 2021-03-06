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
package org.apache.olingo.client.core.communication.request.retrieve;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.olingo.client.api.CommonODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.commons.api.data.EntitySet;
import org.apache.olingo.commons.api.data.ResWrap;
import org.apache.olingo.commons.api.domain.CommonODataEntitySet;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.commons.api.serialization.ODataDeserializerException;

/**
 * This class implements an OData EntitySet query request.
 *
 * @param <ES> concrete ODataEntitySet implementation
 */
public class ODataEntitySetRequestImpl<ES extends CommonODataEntitySet>
        extends AbstractODataRetrieveRequest<ES> implements ODataEntitySetRequest<ES> {

  private ES entitySet = null;

  /**
   * Private constructor.
   *
   * @param odataClient client instance getting this request
   * @param query query to be executed.
   */
  public ODataEntitySetRequestImpl(final CommonODataClient<?> odataClient, final URI query) {
    super(odataClient, query);
  }

  @Override
  public ODataFormat getDefaultFormat() {
    return odataClient.getConfiguration().getDefaultPubFormat();
  }

  @Override
  public ODataRetrieveResponse<ES> execute() {
    final HttpResponse res = doExecute();
    return new ODataEntitySetResponseImpl(odataClient, httpClient, res);
  }

  /**
   * Response class about an ODataEntitySetRequest.
   */
  protected class ODataEntitySetResponseImpl extends AbstractODataRetrieveResponse {

    private ODataEntitySetResponseImpl(final CommonODataClient<?> odataClient, final HttpClient httpClient,
            final HttpResponse res) {

      super(odataClient, httpClient, res);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ES getBody() {
      if (entitySet == null) {
        try {
          final ResWrap<EntitySet> resource = odataClient.getDeserializer(ODataFormat.fromString(getContentType())).
                  toEntitySet(getRawResponse());

          entitySet = (ES) odataClient.getBinder().getODataEntitySet(resource);
        } catch (final ODataDeserializerException e) {
          throw new IllegalArgumentException(e);
        } finally {
          this.close();
        }
      }
      return entitySet;
    }
  }
}
