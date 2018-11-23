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
package org.apache.olingo.server.core.deserializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.olingo.server.api.batch.exception.BatchDeserializerException;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.deserializer.FixedFormatDeserializer;
import org.apache.olingo.server.api.deserializer.batch.BatchOptions;
import org.apache.olingo.server.api.deserializer.batch.BatchRequestPart;
import org.apache.olingo.server.core.deserializer.batch.BatchParser;

public class FixedFormatDeserializerImpl implements FixedFormatDeserializer {

  @Override
  public byte[] binary(InputStream content) throws DeserializerException {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[128];
    int count = -1;
    try {
      while ((count = content.read(buffer)) > -1) {
        result.write(buffer, 0, count);
      }
      result.flush();
    } catch (final IOException e) {
      throw new DeserializerException("An I/O exception occurred.", e,
          DeserializerException.MessageKeys.IO_EXCEPTION);
    }
    return result.toByteArray();
  }

  // TODO: Deserializer
  @Override
  public List<BatchRequestPart> parseBatchRequest(InputStream content, String boundary, BatchOptions options)
      throws BatchDeserializerException {
    final BatchParser parser = new BatchParser();

    return parser.parseBatchRequest(content, boundary, options);
  }
}
