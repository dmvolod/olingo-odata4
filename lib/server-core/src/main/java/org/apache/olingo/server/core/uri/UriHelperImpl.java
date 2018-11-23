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
package org.apache.olingo.server.core.uri;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmStructuredType;
import org.apache.olingo.commons.core.Encoder;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriHelper;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.core.serializer.utils.ContextURLHelper;

public class UriHelperImpl implements UriHelper {

  @Override
  public String buildContextURLSelectList(final EdmStructuredType type,
      final ExpandOption expand, final SelectOption select) throws SerializerException {
    return ContextURLHelper.buildSelectList(type, expand, select);
  }

  @Override
  public String buildContextURLKeyPredicate(final List<UriParameter> keys) throws SerializerException {
    return ContextURLHelper.buildKeyPredicate(keys);
  }

  @Override
  public String buildCanonicalURL(final EdmEntitySet edmEntitySet, final Entity entity) throws SerializerException {
    StringBuilder result = new StringBuilder(edmEntitySet.getName());
    result.append('(');
    final EdmEntityType entityType = edmEntitySet.getEntityType();
    final List<String> keyNames = entityType.getKeyPredicateNames();
    boolean first = true;
    for (final String keyName : keyNames) {
      if (first) {
        first = false;
      } else {
        result.append(',');
      }
      if (keyNames.size() > 1) {
        result.append(Encoder.encode(keyName)).append('=');
      }
      final EdmProperty edmProperty = entityType.getStructuralProperty(keyName);
      final EdmPrimitiveType type = (EdmPrimitiveType) edmProperty.getType();
      final Object propertyValue = entity.getProperty(keyName).getValue();
      try {
        final String value = type.toUriLiteral(
            type.valueToString(propertyValue,
                edmProperty.isNullable(), edmProperty.getMaxLength(),
                edmProperty.getPrecision(), edmProperty.getScale(), edmProperty.isUnicode()));
        result.append(Encoder.encode(value));
      } catch (final EdmPrimitiveTypeException e) {
        throw new SerializerException("Wrong key value!",
            SerializerException.MessageKeys.WRONG_PROPERTY_VALUE, edmProperty.getName(), propertyValue.toString());
      }
    }
    result.append(')');
    return result.toString();
  }
}