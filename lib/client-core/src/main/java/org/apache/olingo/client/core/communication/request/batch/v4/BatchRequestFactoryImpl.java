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
package org.apache.olingo.client.core.communication.request.batch.v4;

import org.apache.olingo.client.api.communication.request.batch.v4.BatchRequestFactory;
import org.apache.olingo.client.api.communication.request.batch.v4.ODataBatchRequest;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.communication.request.batch.AbstractBatchRequestFactory;

public class BatchRequestFactoryImpl extends AbstractBatchRequestFactory
        implements BatchRequestFactory {

  public BatchRequestFactoryImpl(final ODataClient client) {
    super(client);
  }

  @Override
  public ODataBatchRequest getBatchRequest(final String serviceRoot) {
    return new ODataBatchRequestImpl(
            (ODataClient) client, client.newURIBuilder(serviceRoot).appendBatchSegment().build());
  }
}
