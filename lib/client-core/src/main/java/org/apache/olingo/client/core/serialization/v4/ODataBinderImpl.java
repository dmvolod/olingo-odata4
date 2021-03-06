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
package org.apache.olingo.client.core.serialization.v4;

import java.net.URI;
import java.util.List;

import org.apache.olingo.client.api.data.ServiceDocument;
import org.apache.olingo.client.api.data.ServiceDocumentItem;
import org.apache.olingo.client.api.serialization.v4.ODataBinder;
import org.apache.olingo.client.api.v4.EdmEnabledODataClient;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.serialization.AbstractODataBinder;
import org.apache.olingo.client.core.uri.URIUtils;
import org.apache.olingo.commons.api.data.Annotatable;
import org.apache.olingo.commons.api.data.Annotation;
import org.apache.olingo.commons.api.data.DeletedEntity;
import org.apache.olingo.commons.api.data.Delta;
import org.apache.olingo.commons.api.data.DeltaLink;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntitySet;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Linked;
import org.apache.olingo.commons.api.data.LinkedComplexValue;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ResWrap;
import org.apache.olingo.commons.api.data.Valuable;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.domain.CommonODataEntity;
import org.apache.olingo.commons.api.domain.CommonODataEntitySet;
import org.apache.olingo.commons.api.domain.CommonODataProperty;
import org.apache.olingo.commons.api.domain.ODataCollectionValue;
import org.apache.olingo.commons.api.domain.ODataInlineEntity;
import org.apache.olingo.commons.api.domain.ODataInlineEntitySet;
import org.apache.olingo.commons.api.domain.ODataLinked;
import org.apache.olingo.commons.api.domain.ODataServiceDocument;
import org.apache.olingo.commons.api.domain.ODataValue;
import org.apache.olingo.commons.api.domain.v4.ODataAnnotatable;
import org.apache.olingo.commons.api.domain.v4.ODataAnnotation;
import org.apache.olingo.commons.api.domain.v4.ODataDeletedEntity.Reason;
import org.apache.olingo.commons.api.domain.v4.ODataDelta;
import org.apache.olingo.commons.api.domain.v4.ODataDeltaLink;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;
import org.apache.olingo.commons.api.domain.v4.ODataEntitySet;
import org.apache.olingo.commons.api.domain.v4.ODataLink;
import org.apache.olingo.commons.api.domain.v4.ODataLinkedComplexValue;
import org.apache.olingo.commons.api.domain.v4.ODataProperty;
import org.apache.olingo.commons.api.domain.v4.ODataValuable;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmElement;
import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmTerm;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.geo.Geospatial;
import org.apache.olingo.commons.core.data.AnnotationImpl;
import org.apache.olingo.commons.core.data.LinkedComplexValueImpl;
import org.apache.olingo.commons.core.data.PropertyImpl;
import org.apache.olingo.commons.core.domain.v4.ODataAnnotationImpl;
import org.apache.olingo.commons.core.domain.v4.ODataDeletedEntityImpl;
import org.apache.olingo.commons.core.domain.v4.ODataDeltaLinkImpl;
import org.apache.olingo.commons.core.domain.v4.ODataPropertyImpl;
import org.apache.olingo.commons.core.edm.EdmTypeInfo;
import org.apache.olingo.commons.core.serialization.ContextURLParser;

public class ODataBinderImpl extends AbstractODataBinder implements ODataBinder {

  public ODataBinderImpl(final ODataClient client) {
    super(client);
  }

  @Override
  public boolean add(final CommonODataEntity entity, final CommonODataProperty property) {
    return ((ODataEntity) entity).getProperties().add((ODataProperty) property);
  }

  @Override
  protected boolean add(final CommonODataEntitySet entitySet, final CommonODataEntity entity) {
    return ((ODataEntitySet) entitySet).getEntities().add((ODataEntity) entity);
  }

  @Override
  public ODataServiceDocument getODataServiceDocument(final ServiceDocument resource) {
    final ODataServiceDocument serviceDocument = super.getODataServiceDocument(resource);

    for (ServiceDocumentItem functionImport : resource.getFunctionImports()) {
      serviceDocument.getFunctionImports().put(
              functionImport.getName() == null ? functionImport.getUrl() : functionImport.getName(),
              URIUtils.getURI(resource.getBaseURI(), functionImport.getUrl()));
    }
    for (ServiceDocumentItem singleton : resource.getSingletons()) {
      serviceDocument.getSingletons().put(
              singleton.getName() == null ? singleton.getUrl() : singleton.getName(),
              URIUtils.getURI(resource.getBaseURI(), singleton.getUrl()));
    }
    for (ServiceDocumentItem sdoc : resource.getRelatedServiceDocuments()) {
      serviceDocument.getRelatedServiceDocuments().put(
              sdoc.getName() == null ? sdoc.getUrl() : sdoc.getName(),
              URIUtils.getURI(resource.getBaseURI(), sdoc.getUrl()));
    }

    return serviceDocument;
  }

  private void updateValuable(final Valuable propertyResource, final ODataValuable odataValuable) {
    final Object propertyValue = getValue(odataValuable.getValue());
    if (odataValuable.hasPrimitiveValue()) {
      propertyResource.setType(odataValuable.getPrimitiveValue().getTypeName());
      propertyResource.setValue(
              propertyValue instanceof Geospatial ? ValueType.GEOSPATIAL : ValueType.PRIMITIVE,
              propertyValue);
    } else if (odataValuable.hasEnumValue()) {
      propertyResource.setType(odataValuable.getEnumValue().getTypeName());
      propertyResource.setValue(ValueType.ENUM, propertyValue);
    } else if (odataValuable.hasComplexValue()) {
      propertyResource.setType(odataValuable.getComplexValue().getTypeName());
      propertyResource.setValue(
              propertyValue instanceof LinkedComplexValue ? ValueType.LINKED_COMPLEX : ValueType.COMPLEX,
              propertyValue);
    } else if (odataValuable.hasCollectionValue()) {
      final ODataCollectionValue<org.apache.olingo.commons.api.domain.v4.ODataValue> collectionValue =
              odataValuable.getCollectionValue();
      propertyResource.setType(collectionValue.getTypeName());
      final org.apache.olingo.commons.api.domain.v4.ODataValue value =
              collectionValue.iterator().hasNext() ? collectionValue.iterator().next() : null;
      ValueType valueType = ValueType.COLLECTION_PRIMITIVE;
      if (value == null) {
        valueType = ValueType.COLLECTION_PRIMITIVE;
      } else if (value.isPrimitive()) {
        valueType = value.asPrimitive().toValue() instanceof Geospatial
                ? ValueType.COLLECTION_GEOSPATIAL : ValueType.COLLECTION_PRIMITIVE;
      } else if (value.isEnum()) {
        valueType = ValueType.COLLECTION_ENUM;
      } else if (value.isLinkedComplex()) {
        valueType = ValueType.COLLECTION_LINKED_COMPLEX;
      } else if (value.isComplex()) {
        valueType = ValueType.COLLECTION_COMPLEX;
      }
      propertyResource.setValue(valueType, propertyValue);
    }
  }

  private void annotations(final ODataAnnotatable odataAnnotatable, final Annotatable annotatable) {
    for (ODataAnnotation odataAnnotation : odataAnnotatable.getAnnotations()) {
      final Annotation annotation = new AnnotationImpl();

      annotation.setTerm(odataAnnotation.getTerm());
      annotation.setType(odataAnnotation.getValue().getTypeName());
      updateValuable(annotation, odataAnnotation);

      annotatable.getAnnotations().add(annotation);
    }
  }

  @Override
  public EntitySet getEntitySet(final CommonODataEntitySet odataEntitySet) {
    final EntitySet entitySet = super.getEntitySet(odataEntitySet);
    entitySet.setDeltaLink(((ODataEntitySet) odataEntitySet).getDeltaLink());
    annotations((ODataEntitySet) odataEntitySet, entitySet);
    return entitySet;
  }

  @Override
  protected void links(final ODataLinked odataLinked, final Linked linked) {
    super.links(odataLinked, linked);

    for (Link link : linked.getNavigationLinks()) {
      final org.apache.olingo.commons.api.domain.ODataLink odataLink = odataLinked.getNavigationLink(link.getTitle());
      if (!(odataLink instanceof ODataInlineEntity) && !(odataLink instanceof ODataInlineEntitySet)) {
        annotations((ODataLink) odataLink, link);
      }
    }
  }

  @Override
  public Entity getEntity(final CommonODataEntity odataEntity) {
    final Entity entity = super.getEntity(odataEntity);
    entity.setId(((ODataEntity) odataEntity).getId());
    annotations((ODataEntity) odataEntity, entity);
    return entity;
  }

  @Override
  public Property getProperty(final CommonODataProperty property) {
    final ODataProperty _property = (ODataProperty) property;

    final Property propertyResource = new PropertyImpl();
    propertyResource.setName(_property.getName());
    updateValuable(propertyResource, _property);
    annotations(_property, propertyResource);

    return propertyResource;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Object getValue(final ODataValue value) {
    Object valueResource;
    if (value instanceof org.apache.olingo.commons.api.domain.v4.ODataValue
            && ((org.apache.olingo.commons.api.domain.v4.ODataValue) value).isEnum()) {

      valueResource =
              ((org.apache.olingo.commons.api.domain.v4.ODataValue) value).asEnum().getValue();
    } else {
      valueResource = super.getValue(value);

      if (value instanceof org.apache.olingo.commons.api.domain.v4.ODataValue
              && ((org.apache.olingo.commons.api.domain.v4.ODataValue) value).isLinkedComplex()) {

        final LinkedComplexValue lcValueResource = new LinkedComplexValueImpl();
        lcValueResource.getValue().addAll((List<Property>) valueResource);

        final ODataLinkedComplexValue linked =
                ((org.apache.olingo.commons.api.domain.v4.ODataValue) value).asLinkedComplex();
        annotations(linked, lcValueResource);
        links(linked, lcValueResource);

        valueResource = lcValueResource;
      }
    }
    return valueResource;
  }

  private void odataAnnotations(final Annotatable annotatable, final ODataAnnotatable odataAnnotatable) {
    for (Annotation annotation : annotatable.getAnnotations()) {
      FullQualifiedName fqn = null;
      if (client instanceof EdmEnabledODataClient) {
        final EdmTerm term = ((EdmEnabledODataClient) client).getCachedEdm().
                getTerm(new FullQualifiedName(annotation.getTerm()));
        if (term != null) {
          fqn = term.getType().getFullQualifiedName();
        }
      }

      if (fqn == null && annotation.getType() != null) {
        final EdmTypeInfo typeInfo = new EdmTypeInfo.Builder().setTypeExpression(annotation.getType()).build();
        if (typeInfo.isPrimitiveType()) {
          fqn = typeInfo.getPrimitiveTypeKind().getFullQualifiedName();
        }
      }

      final ODataAnnotation odataAnnotation = new ODataAnnotationImpl(annotation.getTerm(),
              (org.apache.olingo.commons.api.domain.v4.ODataValue) getODataValue(fqn, annotation, null, null));
      odataAnnotatable.getAnnotations().add(odataAnnotation);
    }
  }

  @Override
  public ODataEntitySet getODataEntitySet(final ResWrap<EntitySet> resource) {
    final ODataEntitySet entitySet = (ODataEntitySet) super.getODataEntitySet(resource);

    if (resource.getPayload().getDeltaLink() != null) {
      final URI base = resource.getContextURL() == null
              ? resource.getPayload().getBaseURI()
              : ContextURLParser.parse(resource.getContextURL()).getServiceRoot();
      entitySet.setDeltaLink(URIUtils.getURI(base, resource.getPayload().getDeltaLink()));
    }
    odataAnnotations(resource.getPayload(), entitySet);

    return entitySet;
  }

  @Override
  protected void odataNavigationLinks(final EdmType edmType,
          final Linked linked, final ODataLinked odataLinked, final String metadataETag, final URI base) {

    super.odataNavigationLinks(edmType, linked, odataLinked, metadataETag, base);
    for (org.apache.olingo.commons.api.domain.ODataLink link : odataLinked.getNavigationLinks()) {
      if (!(link instanceof ODataInlineEntity) && !(link instanceof ODataInlineEntitySet)) {
        odataAnnotations(linked.getNavigationLink(link.getName()), (ODataAnnotatable) link);
      }
    }
  }

  @Override
  public ODataEntity getODataEntity(final ResWrap<Entity> resource) {
    final ODataEntity entity = (ODataEntity) super.getODataEntity(resource);

    entity.setId(resource.getPayload().getId());
    odataAnnotations(resource.getPayload(), entity);

    return entity;
  }

  @Override
  public ODataProperty getODataProperty(final ResWrap<Property> resource) {
    final Property payload = resource.getPayload();
    final EdmTypeInfo typeInfo = buildTypeInfo(ContextURLParser.parse(resource.getContextURL()),
            resource.getMetadataETag(), payload.getName(), payload.getType());

    final ODataProperty property = new ODataPropertyImpl(payload.getName(),
            getODataValue(typeInfo == null ? null : typeInfo.getFullQualifiedName(),
                    payload, resource.getContextURL(), resource.getMetadataETag()));
    odataAnnotations(payload, property);

    return property;
  }

  @Override
  protected ODataProperty getODataProperty(final EdmType type, final Property resource) {
    final EdmTypeInfo typeInfo = buildTypeInfo(type == null ? null : type.getFullQualifiedName(), resource.getType());

    final ODataProperty property = new ODataPropertyImpl(resource.getName(),
            getODataValue(typeInfo == null ? null : typeInfo.getFullQualifiedName(),
                    resource, null, null));
    odataAnnotations(resource, property);

    return property;
  }

  @Override
  protected ODataValue getODataValue(final FullQualifiedName type,
          final Valuable valuable, final URI contextURL, final String metadataETag) {

    // fixes enum values treated as primitive when no type information is available
    if (client instanceof EdmEnabledODataClient && type != null) {
      final EdmEnumType edmType = ((EdmEnabledODataClient) client).getEdm(metadataETag).getEnumType(type);
      if (valuable.isPrimitive() && edmType != null) {
        valuable.setValue(ValueType.ENUM, valuable.asPrimitive());
      }
    }

    ODataValue value;
    if (valuable.isEnum()) {
      value = ((ODataClient) client).getObjectFactory().newEnumValue(type == null ? null : type.toString(),
              valuable.asEnum().toString());
    } else if (valuable.isLinkedComplex()) {
      final ODataLinkedComplexValue lcValue =
              ((ODataClient) client).getObjectFactory().newLinkedComplexValue(type == null ? null : type.toString());

      EdmComplexType edmType = null;
      if (client instanceof EdmEnabledODataClient && type != null) {
        edmType = ((EdmEnabledODataClient) client).getEdm(metadataETag).getComplexType(type);
      }

      for (Property property : valuable.asLinkedComplex().getValue()) {
        EdmType edmPropertyType = null;
        if (edmType != null) {
          final EdmElement edmProp = edmType.getProperty(property.getName());
          if (edmProp != null) {
            edmPropertyType = edmProp.getType();
          }
        }
        lcValue.add(getODataProperty(edmPropertyType, property));
      }

      odataNavigationLinks(edmType, valuable.asLinkedComplex(), lcValue, metadataETag, contextURL);
      odataAnnotations(valuable.asLinkedComplex(), lcValue);

      value = lcValue;
    } else {
      value = super.getODataValue(type, valuable, contextURL, metadataETag);
    }

    return value;
  }

  @Override
  public ODataDelta getODataDelta(final ResWrap<Delta> resource) {
    final URI base = resource.getContextURL() == null
            ? resource.getPayload().getBaseURI()
            : ContextURLParser.parse(resource.getContextURL()).getServiceRoot();

    final URI next = resource.getPayload().getNext();

    final ODataDelta delta = next == null
            ? ((ODataClient) client).getObjectFactory().newDelta()
            : ((ODataClient) client).getObjectFactory().newDelta(URIUtils.getURI(base, next.toASCIIString()));

    if (resource.getPayload().getCount() != null) {
      delta.setCount(resource.getPayload().getCount());
    }

    if (resource.getPayload().getDeltaLink() != null) {
      delta.setDeltaLink(URIUtils.getURI(base, resource.getPayload().getDeltaLink()));
    }

    for (Entity entityResource : resource.getPayload().getEntities()) {
      add(delta, getODataEntity(
              new ResWrap<Entity>(resource.getContextURL(), resource.getMetadataETag(), entityResource)));
    }
    for (DeletedEntity deletedEntity : resource.getPayload().getDeletedEntities()) {
      final ODataDeletedEntityImpl impl = new ODataDeletedEntityImpl();
      impl.setId(URIUtils.getURI(base, deletedEntity.getId()));
      impl.setReason(Reason.valueOf(deletedEntity.getReason().name()));

      delta.getDeletedEntities().add(impl);
    }

    odataAnnotations(resource.getPayload(), delta);

    for (DeltaLink link : resource.getPayload().getAddedLinks()) {
      final ODataDeltaLink impl = new ODataDeltaLinkImpl();
      impl.setRelationship(link.getRelationship());
      impl.setSource(URIUtils.getURI(base, link.getSource()));
      impl.setTarget(URIUtils.getURI(base, link.getTarget()));

      odataAnnotations(link, impl);

      delta.getAddedLinks().add(impl);
    }
    for (DeltaLink link : resource.getPayload().getDeletedLinks()) {
      final ODataDeltaLink impl = new ODataDeltaLinkImpl();
      impl.setRelationship(link.getRelationship());
      impl.setSource(URIUtils.getURI(base, link.getSource()));
      impl.setTarget(URIUtils.getURI(base, link.getTarget()));

      odataAnnotations(link, impl);

      delta.getDeletedLinks().add(impl);
    }

    return delta;
  }
}
