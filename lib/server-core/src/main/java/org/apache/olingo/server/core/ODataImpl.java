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

import java.util.List;

import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.deserializer.FixedFormatDeserializer;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.apache.olingo.server.api.edmx.EdmxReference;
import org.apache.olingo.server.api.serializer.FixedFormatSerializer;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriHelper;
import org.apache.olingo.server.core.deserializer.FixedFormatDeserializerImpl;
import org.apache.olingo.server.core.deserializer.json.ODataJsonDeserializer;
import org.apache.olingo.server.core.serializer.FixedFormatSerializerImpl;
import org.apache.olingo.server.core.serializer.json.ODataJsonSerializer;
import org.apache.olingo.server.core.serializer.xml.ODataXmlSerializerImpl;
import org.apache.olingo.server.core.uri.UriHelperImpl;

public class ODataImpl extends OData {

  @Override
  public ODataSerializer createSerializer(final ODataFormat format) throws SerializerException {
    ODataSerializer serializer;
    switch (format) {
    case JSON:
    case JSON_NO_METADATA:
    case JSON_FULL_METADATA:
      serializer = new ODataJsonSerializer(format);
      break;
    case XML:
      serializer = new ODataXmlSerializerImpl();
      break;
    default:
      throw new SerializerException("Unsupported format: " + format,
          SerializerException.MessageKeys.UNSUPPORTED_FORMAT, format.toString());
    }

    return serializer;
  }

  @Override
  public FixedFormatSerializer createFixedFormatSerializer() {
    return new FixedFormatSerializerImpl();
  }

  @Override
  public ODataHttpHandler createHandler(final ServiceMetadata edm) {
    return new ODataHttpHandlerImpl(this, edm);
  }

  @Override
  public ServiceMetadata createServiceMetadata(EdmProvider edmProvider, List<EdmxReference> references) {
    return new ServiceMetadataImpl(ODataServiceVersion.V40, edmProvider, references);
  }

  @Override
  public FixedFormatDeserializer createFixedFormatDeserializer() {
    return new FixedFormatDeserializerImpl();
  }

  @Override
  public UriHelper createUriHelper() {
    return new UriHelperImpl();
  }

  @Override
  public ODataDeserializer createDeserializer(ODataFormat format) throws DeserializerException{
    ODataDeserializer serializer;
    switch (format) {
    case JSON:
    case JSON_NO_METADATA:
    case JSON_FULL_METADATA:
      serializer = new ODataJsonDeserializer();
      break;
    case XML:
      //We do not support xml deserialization right now so this mus lead to an error
    default:
      throw new DeserializerException("Unsupported format: " + format,
          SerializerException.MessageKeys.UNSUPPORTED_FORMAT, format.toString());
    }

    return serializer;
  }
}
