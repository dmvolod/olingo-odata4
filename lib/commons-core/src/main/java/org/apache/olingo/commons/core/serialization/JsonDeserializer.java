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
package org.apache.olingo.commons.core.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.data.Annotation;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntitySet;
import org.apache.olingo.commons.api.data.Linked;
import org.apache.olingo.commons.api.data.LinkedComplexValue;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ResWrap;
import org.apache.olingo.commons.api.data.Valuable;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.domain.ODataError;
import org.apache.olingo.commons.api.domain.ODataLinkType;
import org.apache.olingo.commons.api.domain.ODataPropertyType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.apache.olingo.commons.api.edm.geo.Geospatial;
import org.apache.olingo.commons.api.serialization.ODataDeserializer;
import org.apache.olingo.commons.api.serialization.ODataDeserializerException;
import org.apache.olingo.commons.core.data.AnnotationImpl;
import org.apache.olingo.commons.core.data.EntitySetImpl;
import org.apache.olingo.commons.core.data.LinkImpl;
import org.apache.olingo.commons.core.data.LinkedComplexValueImpl;
import org.apache.olingo.commons.core.data.PropertyImpl;
import org.apache.olingo.commons.core.edm.EdmTypeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonDeserializer implements ODataDeserializer {

  protected final Pattern CUSTOM_ANNOTATION = Pattern.compile("(.+)@(.+)\\.(.+)");

  protected final ODataServiceVersion version;

  protected final boolean serverMode;

  protected String jsonType;

  protected String jsonId;

  protected String jsonETag;

  protected String jsonReadLink;

  protected String jsonEditLink;

  protected String jsonMediaEditLink;

  protected String jsonMediaReadLink;

  protected String jsonMediaContentType;

  protected String jsonMediaETag;

  protected String jsonAssociationLink;

  protected String jsonNavigationLink;

  protected String jsonCount;

  protected String jsonNextLink;

  protected String jsonDeltaLink;

  protected String jsonError;

  private JsonGeoValueDeserializer geoDeserializer;

  private JsonParser parser;

  public JsonDeserializer(final ODataServiceVersion version, final boolean serverMode) {
    this.version = version;
    this.serverMode = serverMode;

    jsonType = version.getJsonName(ODataServiceVersion.JsonKey.TYPE);
    jsonId = version.getJsonName(ODataServiceVersion.JsonKey.ID);
    jsonETag = version.getJsonName(ODataServiceVersion.JsonKey.ETAG);
    jsonReadLink = version.getJsonName(ODataServiceVersion.JsonKey.READ_LINK);
    jsonEditLink = version.getJsonName(ODataServiceVersion.JsonKey.EDIT_LINK);
    jsonMediaReadLink = version.getJsonName(ODataServiceVersion.JsonKey.MEDIA_READ_LINK);
    jsonMediaEditLink = version.getJsonName(ODataServiceVersion.JsonKey.MEDIA_EDIT_LINK);
    jsonMediaContentType = version.getJsonName(ODataServiceVersion.JsonKey.MEDIA_CONTENT_TYPE);
    jsonMediaETag = version.getJsonName(ODataServiceVersion.JsonKey.MEDIA_ETAG);
    jsonAssociationLink = version.getJsonName(ODataServiceVersion.JsonKey.ASSOCIATION_LINK);
    jsonNavigationLink = version.getJsonName(ODataServiceVersion.JsonKey.NAVIGATION_LINK);
    jsonCount = version.getJsonName(ODataServiceVersion.JsonKey.COUNT);
    jsonNextLink = version.getJsonName(ODataServiceVersion.JsonKey.NEXT_LINK);
    jsonDeltaLink = version.getJsonName(ODataServiceVersion.JsonKey.DELTA_LINK);
    jsonError = version.getJsonName(ODataServiceVersion.JsonKey.ERROR);
  }

  private JsonGeoValueDeserializer getGeoDeserializer() {
    if (geoDeserializer == null) {
      geoDeserializer = new JsonGeoValueDeserializer(version);
    }
    return geoDeserializer;
  }

  protected String getJSONAnnotation(final String string) {
    return StringUtils.prependIfMissing(string, "@");
  }

  protected String getTitle(final Map.Entry<String, JsonNode> entry) {
    return entry.getKey().substring(0, entry.getKey().indexOf('@'));
  }

  protected String setInline(final String name, final String suffix, final JsonNode tree,
          final ObjectCodec codec, final LinkImpl link) throws IOException {

    final String entityNamePrefix = name.substring(0, name.indexOf(suffix));
    if (tree.has(entityNamePrefix)) {
      final JsonNode inline = tree.path(entityNamePrefix);
      JsonEntityDeserializer entityDeserializer = new JsonEntityDeserializer(version, serverMode);

      if (inline instanceof ObjectNode) {
        link.setType(ODataLinkType.ENTITY_NAVIGATION.toString());
        link.setInlineEntity(entityDeserializer.doDeserialize(inline.traverse(codec)).getPayload());

      } else if (inline instanceof ArrayNode) {
        link.setType(ODataLinkType.ENTITY_SET_NAVIGATION.toString());

        final EntitySet entitySet = new EntitySetImpl();
        for (final Iterator<JsonNode> entries = inline.elements(); entries.hasNext();) {
          entitySet.getEntities().add(entityDeserializer.doDeserialize(entries.next().traverse(codec)).getPayload());
        }

        link.setInlineEntitySet(entitySet);
      }
    }
    return entityNamePrefix;
  }

  protected void links(final Map.Entry<String, JsonNode> field, final Linked linked, final Set<String> toRemove,
          final JsonNode tree, final ObjectCodec codec) throws IOException {
    if (serverMode) {
      serverLinks(field, linked, toRemove, tree, codec);
    } else {
      clientLinks(field, linked, toRemove, tree, codec);
    }
  }

  private void clientLinks(final Map.Entry<String, JsonNode> field, final Linked linked, final Set<String> toRemove,
          final JsonNode tree, final ObjectCodec codec) throws IOException {

    if (field.getKey().endsWith(jsonNavigationLink)) {
      final LinkImpl link = new LinkImpl();
      link.setTitle(getTitle(field));
      link.setRel(version.getNamespace(ODataServiceVersion.NamespaceKey.NAVIGATION_LINK_REL) + getTitle(field));

      if (field.getValue().isValueNode()) {
        link.setHref(field.getValue().textValue());
        link.setType(ODataLinkType.ENTITY_NAVIGATION.toString());
      }

      linked.getNavigationLinks().add(link);

      toRemove.add(field.getKey());
      toRemove.add(setInline(field.getKey(), jsonNavigationLink, tree, codec, link));
    } else if (field.getKey().endsWith(jsonAssociationLink)) {
      final LinkImpl link = new LinkImpl();
      link.setTitle(getTitle(field));
      link.setRel(version.getNamespace(ODataServiceVersion.NamespaceKey.ASSOCIATION_LINK_REL) + getTitle(field));
      link.setHref(field.getValue().textValue());
      link.setType(ODataLinkType.ASSOCIATION.toString());
      linked.getAssociationLinks().add(link);

      toRemove.add(field.getKey());
    }
  }

  private void serverLinks(final Map.Entry<String, JsonNode> field, final Linked linked, final Set<String> toRemove,
          final JsonNode tree, final ObjectCodec codec) throws IOException {

    if (field.getKey().endsWith(Constants.JSON_BIND_LINK_SUFFIX)
            || field.getKey().endsWith(jsonNavigationLink)) {

      if (field.getValue().isValueNode()) {
        final String suffix = field.getKey().replaceAll("^.*@", "@");

        final LinkImpl link = new LinkImpl();
        link.setTitle(getTitle(field));
        link.setRel(version.getNamespace(ODataServiceVersion.NamespaceKey.NAVIGATION_LINK_REL) + getTitle(field));
        link.setHref(field.getValue().textValue());
        link.setType(ODataLinkType.ENTITY_NAVIGATION.toString());
        linked.getNavigationLinks().add(link);

        toRemove.add(setInline(field.getKey(), suffix, tree, codec, link));
      } else if (field.getValue().isArray()) {
        for (final Iterator<JsonNode> itor = field.getValue().elements(); itor.hasNext();) {
          final JsonNode node = itor.next();

          final LinkImpl link = new LinkImpl();
          link.setTitle(getTitle(field));
          link.setRel(version.getNamespace(ODataServiceVersion.NamespaceKey.NAVIGATION_LINK_REL) + getTitle(field));
          link.setHref(node.asText());
          link.setType(ODataLinkType.ENTITY_SET_NAVIGATION.toString());
          linked.getNavigationLinks().add(link);
          toRemove.add(setInline(field.getKey(), Constants.JSON_BIND_LINK_SUFFIX, tree, codec, link));
        }
      }
      toRemove.add(field.getKey());
    }
  }

  private Map.Entry<ODataPropertyType, EdmTypeInfo> guessPropertyType(final JsonNode node) {
    ODataPropertyType type;
    String typeExpression = null;

    if (node.isValueNode() || node.isNull()) {
      type = ODataPropertyType.PRIMITIVE;
      typeExpression = guessPrimitiveTypeKind(node).getFullQualifiedName().toString();
    } else if (node.isArray()) {
      type = ODataPropertyType.COLLECTION;
      if (node.has(0) && node.get(0).isValueNode()) {
        typeExpression = "Collection(" + guessPrimitiveTypeKind(node.get(0)) + ')';
      }
    } else if (node.isObject()) {
      if (node.has(Constants.ATTR_TYPE)) {
        type = ODataPropertyType.PRIMITIVE;
        typeExpression = "Edm.Geography" + node.get(Constants.ATTR_TYPE).asText();
      } else {
        type = ODataPropertyType.COMPLEX;
      }
    } else {
      type = ODataPropertyType.EMPTY;
    }

    final EdmTypeInfo typeInfo = typeExpression == null ? null :
        new EdmTypeInfo.Builder().setTypeExpression(typeExpression).build();
    return new SimpleEntry<ODataPropertyType, EdmTypeInfo>(type, typeInfo);
  }

  private EdmPrimitiveTypeKind guessPrimitiveTypeKind(final JsonNode node) {
    return node.isShort()      ? EdmPrimitiveTypeKind.Int16   :
           node.isInt()        ? EdmPrimitiveTypeKind.Int32   :
           node.isLong()       ? EdmPrimitiveTypeKind.Int64   :
           node.isBoolean()    ? EdmPrimitiveTypeKind.Boolean :
           node.isFloat()      ? EdmPrimitiveTypeKind.Single  :
           node.isDouble()     ? EdmPrimitiveTypeKind.Double  :
           node.isBigDecimal() ? EdmPrimitiveTypeKind.Decimal :
                                 EdmPrimitiveTypeKind.String;
  }

  protected void populate(final Annotatable annotatable, final List<Property> properties,
          final ObjectNode tree, final ObjectCodec codec)
          throws IOException, EdmPrimitiveTypeException {

    String type = null;
    Annotation annotation = null;
    for (final Iterator<Map.Entry<String, JsonNode>> itor = tree.fields(); itor.hasNext();) {
      final Map.Entry<String, JsonNode> field = itor.next();
      final Matcher customAnnotation = CUSTOM_ANNOTATION.matcher(field.getKey());

      if (field.getKey().charAt(0) == '@') {
        final Annotation entityAnnot = new AnnotationImpl();
        entityAnnot.setTerm(field.getKey().substring(1));

        value(entityAnnot, field.getValue(), codec);
        if (annotatable != null) {
          annotatable.getAnnotations().add(entityAnnot);
        }
      } else if (type == null && field.getKey().endsWith(getJSONAnnotation(jsonType))) {
        type = field.getValue().asText();
      } else if (annotation == null && customAnnotation.matches() && !"odata".equals(customAnnotation.group(2))) {
        annotation = new AnnotationImpl();
        annotation.setTerm(customAnnotation.group(2) + "." + customAnnotation.group(3));
        value(annotation, field.getValue(), codec);
      } else {
        final PropertyImpl property = new PropertyImpl();
        property.setName(field.getKey());
        property.setType(type == null
                ? null
                : new EdmTypeInfo.Builder().setTypeExpression(type).build().internal());
        type = null;

        value(property, field.getValue(), codec);
        properties.add(property);

        if (annotation != null) {
          property.getAnnotations().add(annotation);
          annotation = null;
        }
      }
    }
  }

  private Object fromPrimitive(final JsonNode node, final EdmTypeInfo typeInfo) throws EdmPrimitiveTypeException {
    return node.isNull() ? null
            : typeInfo == null ? node.asText()
            : typeInfo.getPrimitiveTypeKind().isGeospatial()
            ? getGeoDeserializer().deserialize(node, typeInfo)
            : ((EdmPrimitiveType) typeInfo.getType())
            .valueOfString(node.asText(), true, null,
                    Constants.DEFAULT_PRECISION, Constants.DEFAULT_SCALE, true,
                    ((EdmPrimitiveType) typeInfo.getType()).getDefaultType());
  }

  private Object fromComplex(final ObjectNode node, final ObjectCodec codec)
          throws IOException, EdmPrimitiveTypeException {

    if (version.compareTo(ODataServiceVersion.V40) < 0) {
      final List<Property> properties = new ArrayList<Property>();
      populate(null, properties, node, codec);
      return properties;
    } else {
      final LinkedComplexValue linkComplexValue = new LinkedComplexValueImpl();
      final Set<String> toRemove = new HashSet<String>();
      for (final Iterator<Map.Entry<String, JsonNode>> itor = node.fields(); itor.hasNext();) {
        final Map.Entry<String, JsonNode> field = itor.next();

        links(field, linkComplexValue, toRemove, node, codec);
      }
      node.remove(toRemove);

      populate(linkComplexValue, linkComplexValue.getValue(), node, codec);
      return linkComplexValue;
    }
  }

  private void fromCollection(final Valuable valuable, final Iterator<JsonNode> nodeItor, final EdmTypeInfo typeInfo,
          final ObjectCodec codec) throws IOException, EdmPrimitiveTypeException {

    final List<Object> values = new ArrayList<Object>();
    ValueType valueType = ValueType.COLLECTION_PRIMITIVE;

    final EdmTypeInfo type = typeInfo == null ? null
            : new EdmTypeInfo.Builder().setTypeExpression(typeInfo.getFullQualifiedName().toString()).build();

    while (nodeItor.hasNext()) {
      final JsonNode child = nodeItor.next();

      if (child.isValueNode()) {
        if (typeInfo == null || typeInfo.isPrimitiveType()) {
          final Object value = fromPrimitive(child, type);
          valueType = value instanceof Geospatial ? ValueType.COLLECTION_GEOSPATIAL : ValueType.COLLECTION_PRIMITIVE;
          values.add(value);
        } else {
          valueType = ValueType.COLLECTION_ENUM;
          values.add(child.asText());
        }
      } else if (child.isContainerNode()) {
        if (child.has(jsonType)) {
          ((ObjectNode) child).remove(jsonType);
        }
        final Object value = fromComplex((ObjectNode) child, codec);
        valueType = value instanceof LinkedComplexValue ? ValueType.COLLECTION_LINKED_COMPLEX
                : ValueType.COLLECTION_COMPLEX;
        values.add(value);
      }
    }
    valuable.setValue(valueType, values);
  }

  protected void value(final Valuable valuable, final JsonNode node, final ObjectCodec codec)
          throws IOException, EdmPrimitiveTypeException {
    
    EdmTypeInfo typeInfo = StringUtils.isBlank(valuable.getType()) ? null
            : new EdmTypeInfo.Builder().setTypeExpression(valuable.getType()).build();

    final Map.Entry<ODataPropertyType, EdmTypeInfo> guessed = guessPropertyType(node);
    if (typeInfo == null) {
      typeInfo = guessed.getValue();
    }

    final ODataPropertyType propType = typeInfo == null ? guessed.getKey()
            : typeInfo.isCollection() ? ODataPropertyType.COLLECTION
            : typeInfo.isPrimitiveType() ? ODataPropertyType.PRIMITIVE
            : node.isValueNode() ? ODataPropertyType.ENUM : ODataPropertyType.COMPLEX;

    switch (propType) {
      case COLLECTION:
        fromCollection(valuable, node.elements(), typeInfo, codec);
        break;

      case COMPLEX:
        if (node.has(jsonType)) {
          valuable.setType(node.get(jsonType).asText());
          ((ObjectNode) node).remove(jsonType);
        }
        final Object value = fromComplex((ObjectNode) node, codec);
        valuable.setValue(value instanceof LinkedComplexValue ? ValueType.LINKED_COMPLEX : ValueType.COMPLEX, value);
        break;

      case ENUM:
        valuable.setValue(ValueType.ENUM, node.asText());
        break;

      case PRIMITIVE:
        if (valuable.getType() == null && typeInfo != null) {
          valuable.setType(typeInfo.getFullQualifiedName().toString());
        }
        final Object primitiveValue = fromPrimitive(node, typeInfo);
        valuable.setValue(primitiveValue instanceof Geospatial ? ValueType.GEOSPATIAL : ValueType.PRIMITIVE,
                primitiveValue);
        break;

      case EMPTY:
      default:
        valuable.setValue(ValueType.PRIMITIVE, StringUtils.EMPTY);
    }
  }

  @Override
  public ResWrap<EntitySet> toEntitySet(final InputStream input) throws ODataDeserializerException {
    try {
      parser = new JsonFactory(new ObjectMapper()).createParser(input);
      return new JsonEntitySetDeserializer(version, serverMode).doDeserialize(parser);
    } catch (final IOException e) {
      throw new ODataDeserializerException(e);
    }
  }

  @Override
  public ResWrap<Entity> toEntity(final InputStream input) throws ODataDeserializerException {
    try {
      parser = new JsonFactory(new ObjectMapper()).createParser(input);
      return new JsonEntityDeserializer(version, serverMode).doDeserialize(parser);
    } catch (final IOException e) {
      throw new ODataDeserializerException(e);
    }
  }

  @Override
  public ResWrap<Property> toProperty(final InputStream input) throws ODataDeserializerException {
    try {
      parser = new JsonFactory(new ObjectMapper()).createParser(input);
      return new JsonPropertyDeserializer(version, serverMode).doDeserialize(parser);
    } catch (final IOException e) {
      throw new ODataDeserializerException(e);
    }
  }

  @Override
  public ODataError toError(final InputStream input) throws ODataDeserializerException {
    try {
      parser = new JsonFactory(new ObjectMapper()).createParser(input);
      return new JsonODataErrorDeserializer(version, serverMode).doDeserialize(parser);
    } catch (final IOException e) {
      throw new ODataDeserializerException(e);
    }
  }
}
