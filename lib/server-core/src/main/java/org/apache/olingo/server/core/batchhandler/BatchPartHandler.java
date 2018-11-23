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
package org.apache.olingo.server.core.batchhandler;

import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.batch.BatchFacade;
import org.apache.olingo.server.api.batch.exception.BatchDeserializerException;
import org.apache.olingo.server.api.deserializer.batch.BatchRequestPart;
import org.apache.olingo.server.api.deserializer.batch.ODataResponsePart;
import org.apache.olingo.server.api.processor.BatchProcessor;
import org.apache.olingo.server.core.ODataHandler;
import org.apache.olingo.server.core.batchhandler.referenceRewriting.BatchReferenceRewriter;
import org.apache.olingo.server.core.deserializer.batch.BatchParserCommon;

public class BatchPartHandler {
  private final ODataHandler oDataHandler;
  private final BatchProcessor batchProcessor;
  private final BatchFacade batchFascade;
  private final BatchReferenceRewriter rewriter;

  public BatchPartHandler(final ODataHandler oDataHandler, final BatchProcessor processor,
      final BatchFacade batchFascade) {
    this.oDataHandler = oDataHandler;
    this.batchProcessor = processor;
    this.batchFascade = batchFascade;
    this.rewriter = new BatchReferenceRewriter();
  }

  public ODataResponse handleODataRequest(ODataRequest request) throws BatchDeserializerException {
    return handle(request, true);
  }

  public ODataResponsePart handleBatchRequest(BatchRequestPart request) throws BatchDeserializerException {
    if (request.isChangeSet()) {
      return handleChangeSet(request);
    } else {
      final ODataResponse response = handle(request.getRequests().get(0), false);

      return new ODataResponsePart(response, false);
    }
  }

  public ODataResponse handle(ODataRequest request, boolean isChangeSet)
      throws BatchDeserializerException {
    final ODataResponse response;

    if (isChangeSet) {
      rewriter.replaceReference(request);

      response = oDataHandler.process(request);

      rewriter.addMapping(request, response);
    } else {
      response = oDataHandler.process(request);
    }

    // Add content id to response
    final String contentId = request.getHeader(BatchParserCommon.HTTP_CONTENT_ID);
    if (contentId != null) {
      response.setHeader(BatchParserCommon.HTTP_CONTENT_ID, contentId);
    }

    return response;
  }

  private ODataResponsePart handleChangeSet(BatchRequestPart request) throws BatchDeserializerException {
    return batchProcessor.processChangeSet(batchFascade, request.getRequests());
  }

}
