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
package org.apache.olingo.server.core.edm.provider;

import org.apache.olingo.commons.api.edm.EdmException;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.constants.EdmTypeKind;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.apache.olingo.server.api.edm.provider.EntityType;
import org.apache.olingo.server.api.edm.provider.NavigationProperty;
import org.apache.olingo.server.api.edm.provider.PropertyRef;
import org.apache.olingo.server.api.edm.provider.ReferentialConstraint;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EdmNavigationPropertyImplTest {

  @Test
  public void navigationProperty() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    final FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    EntityType entityTypeProvider = new EntityType();
    entityTypeProvider.setKey(Collections.<PropertyRef> emptyList());
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    NavigationProperty propertyProvider = new NavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, entityTypeName, propertyProvider);
    assertFalse(property.isCollection());
    assertFalse(property.isNullable());
    EdmType type = property.getType();
    assertEquals(EdmTypeKind.ENTITY, type.getKind());
    assertEquals("ns", type.getNamespace());
    assertEquals("entity", type.getName());
    assertNull(property.getReferencingPropertyName("referencedPropertyName"));
    assertNull(property.getPartner());

    // Test caching
    EdmType cachedType = property.getType();
    assertTrue(type == cachedType);
  }

  @Test
  public void navigationPropertyWithReferntialConstraint() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    final FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    EntityType entityTypeProvider = new EntityType();
    entityTypeProvider.setKey(Collections.<PropertyRef> emptyList());
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    NavigationProperty propertyProvider = new NavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    List<ReferentialConstraint> referentialConstraints = new ArrayList<ReferentialConstraint>();
    referentialConstraints.add(new ReferentialConstraint().setProperty("property").setReferencedProperty(
        "referencedProperty"));
    propertyProvider.setReferentialConstraints(referentialConstraints);
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, entityTypeName, propertyProvider);
    assertEquals("property", property.getReferencingPropertyName("referencedProperty"));
    assertNull(property.getReferencingPropertyName("wrong"));
  }

  @Test
  public void navigationPropertyWithPartner() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    final FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    EntityType entityTypeProvider = new EntityType();
    entityTypeProvider.setKey(Collections.<PropertyRef> emptyList());

    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    navigationProperties.add(new NavigationProperty().setName("partnerName").setType(entityTypeName));
    entityTypeProvider.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    NavigationProperty propertyProvider = new NavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    propertyProvider.setPartner("partnerName");
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, entityTypeName, propertyProvider);
    EdmNavigationProperty partner = property.getPartner();
    assertNotNull(partner);

    // Caching
    assertTrue(partner == property.getPartner());
  }

  @Test(expected = EdmException.class)
  public void navigationPropertyWithNonexistentPartner() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    final FullQualifiedName entityTypeName = new FullQualifiedName("ns", "entity");
    EntityType entityTypeProvider = new EntityType();
    entityTypeProvider.setKey(Collections.<PropertyRef> emptyList());

    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    navigationProperties.add(new NavigationProperty().setName("partnerName").setType(entityTypeName));
    entityTypeProvider.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(entityTypeName)).thenReturn(entityTypeProvider);
    NavigationProperty propertyProvider = new NavigationProperty();
    propertyProvider.setType(entityTypeName);
    propertyProvider.setNullable(false);
    propertyProvider.setPartner("wrong");
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, entityTypeName, propertyProvider);
    property.getPartner();
  }

  @Test(expected = EdmException.class)
  public void navigationPropertyWithNonExistentType() throws Exception {
    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    NavigationProperty propertyProvider = new NavigationProperty();
    EdmNavigationProperty property = new EdmNavigationPropertyImpl(edm, null, propertyProvider);
    property.getType();
  }
}