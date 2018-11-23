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

import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmElement;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmException;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.server.api.edm.provider.ComplexType;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.apache.olingo.server.api.edm.provider.EntityType;
import org.apache.olingo.server.api.edm.provider.NavigationProperty;
import org.apache.olingo.server.api.edm.provider.Property;
import org.apache.olingo.server.api.edm.provider.PropertyRef;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EdmEntityTypeImplTest {

  private EdmEntityType baseType;

  private EdmEntityType typeWithBaseType;

  private EdmEntityType typeWithComplexKey;

  @Before
  public void setupTypes() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName baseName = new FullQualifiedName("namespace", "BaseTypeName");
    EntityType baseType = new EntityType();
    baseType.setName(baseName.getName());
    List<Property> properties = new ArrayList<Property>();
    properties.add(new Property().setName("Id").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    properties.add(new Property().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    baseType.setProperties(properties);
    List<PropertyRef> key = new ArrayList<PropertyRef>();
    key.add(new PropertyRef().setPropertyName("Id"));
    baseType.setKey(key);
    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    navigationProperties.add(new NavigationProperty().setName("nav1"));
    baseType.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(baseName)).thenReturn(baseType);

    this.baseType = EdmEntityTypeImpl.getInstance(edm, baseName, baseType);

    FullQualifiedName typeName = new FullQualifiedName("namespace", "typeName");
    EntityType type = new EntityType();
    type.setName(typeName.getName());
    type.setBaseType(baseName);
    List<Property> typeProperties = new ArrayList<Property>();
    typeProperties.add(new Property().setName("address").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    typeProperties.add(new Property().setName("email").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    type.setProperties(typeProperties);
    List<NavigationProperty> typeNavigationProperties = new ArrayList<NavigationProperty>();
    typeNavigationProperties.add(new NavigationProperty().setName("nav2"));
    type.setNavigationProperties(typeNavigationProperties);
    when(provider.getEntityType(typeName)).thenReturn(type);

    typeWithBaseType = EdmEntityTypeImpl.getInstance(edm, typeName, type);

    FullQualifiedName typeWithComplexKeyName = new FullQualifiedName("namespace", "typeName");
    EntityType typeWithComplexKeyProvider = new EntityType();
    typeWithComplexKeyProvider.setName(typeWithComplexKeyName.getName());
    List<Property> typeWithComplexKeyProperties = new ArrayList<Property>();
    typeWithComplexKeyProperties.add(new Property().setName("Id").setType(
        EdmPrimitiveTypeKind.String.getFullQualifiedName()));

    List<Property> complexTypeProperties = new ArrayList<Property>();
    complexTypeProperties.add(new Property().setName("ComplexPropName").setType(
        EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    FullQualifiedName complexTypeName = new FullQualifiedName("namespace", "complexTypeName");
    when(provider.getComplexType(complexTypeName)).thenReturn(
        new ComplexType().setName("complexTypeName").setProperties(complexTypeProperties));

    typeWithComplexKeyProperties.add(new Property().setName("Comp").setType(complexTypeName));
    typeWithComplexKeyProvider.setProperties(typeWithComplexKeyProperties);
    List<PropertyRef> keyForTypeWithComplexKey = new ArrayList<PropertyRef>();
    keyForTypeWithComplexKey.add(new PropertyRef().setPropertyName("Id"));
    keyForTypeWithComplexKey.add(new PropertyRef().setPropertyName("ComplexPropName").setAlias("alias").setPath(
        "Comp/ComplexPropName"));
    typeWithComplexKeyProvider.setKey(keyForTypeWithComplexKey);
    when(provider.getEntityType(typeWithComplexKeyName)).thenReturn(typeWithComplexKeyProvider);

    typeWithComplexKey = EdmEntityTypeImpl.getInstance(edm, typeWithComplexKeyName, typeWithComplexKeyProvider);
  }
  
  @Test
  public void testAbstractBaseTypeWithoutKey() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName baseName = new FullQualifiedName("namespace", "BaseTypeName");
    EntityType baseType = new EntityType();
    baseType.setName(baseName.getName());
    List<Property> properties = new ArrayList<Property>();
    properties.add(new Property().setName("Id").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    properties.add(new Property().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    baseType.setProperties(properties);
    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    navigationProperties.add(new NavigationProperty().setName("nav1"));
    baseType.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(baseName)).thenReturn(baseType);
    baseType.setAbstract(true);
    EdmEntityType edmAbstarctBaseType = EdmEntityTypeImpl.getInstance(edm, baseName, baseType);
    
    assertEquals(2, edmAbstarctBaseType.getPropertyNames().size());
    assertEquals("Id", edmAbstarctBaseType.getPropertyNames().get(0));
    assertEquals("Name", edmAbstarctBaseType.getPropertyNames().get(1));
    
    FullQualifiedName typeName = new FullQualifiedName("namespace", "typeName");
    EntityType type = new EntityType();
    type.setName(typeName.getName());
    type.setBaseType(baseName);
    List<Property> typeProperties = new ArrayList<Property>();
    typeProperties.add(new Property().setName("address").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    typeProperties.add(new Property().setName("email").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    type.setProperties(typeProperties);
    List<PropertyRef> key = new ArrayList<PropertyRef>();
    key.add(new PropertyRef().setPropertyName("email"));
    type.setKey(key);
    List<NavigationProperty> typeNavigationProperties = new ArrayList<NavigationProperty>();
    typeNavigationProperties.add(new NavigationProperty().setName("nav2"));
    type.setNavigationProperties(typeNavigationProperties);
    when(provider.getEntityType(typeName)).thenReturn(type);

    EdmEntityType edmType = EdmEntityTypeImpl.getInstance(edm, typeName, type);
    
    assertNotNull(edmType.getBaseType());
    assertEquals(2, edmAbstarctBaseType.getPropertyNames().size());
    
    assertEquals(1, edmType.getKeyPropertyRefs().size());
    assertEquals("email", edmType.getKeyPredicateNames().get(0));
    
    assertEquals(4, edmType.getPropertyNames().size());
    assertEquals("Id", edmType.getPropertyNames().get(0));
    assertEquals("Name", edmType.getPropertyNames().get(1));
    assertEquals("address", edmType.getPropertyNames().get(2));
    assertEquals("email", edmType.getPropertyNames().get(3));
    
    assertEquals(2, edmType.getNavigationPropertyNames().size());
    assertEquals("nav1", edmType.getNavigationPropertyNames().get(0));
    assertEquals("nav2", edmType.getNavigationPropertyNames().get(1));
  }
  
  @Test
  public void testAbstractBaseTypeWithtKey() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    FullQualifiedName baseName = new FullQualifiedName("namespace", "BaseTypeName");
    EntityType baseType = new EntityType();
    baseType.setName(baseName.getName());
    List<Property> properties = new ArrayList<Property>();
    properties.add(new Property().setName("Id").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    properties.add(new Property().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    baseType.setProperties(properties);
    List<PropertyRef> key = new ArrayList<PropertyRef>();
    key.add(new PropertyRef().setPropertyName("Id"));
    baseType.setKey(key);
    List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
    navigationProperties.add(new NavigationProperty().setName("nav1"));
    baseType.setNavigationProperties(navigationProperties);
    when(provider.getEntityType(baseName)).thenReturn(baseType);
    baseType.setAbstract(true);
    EdmEntityType edmAbstarctBaseType = EdmEntityTypeImpl.getInstance(edm, baseName, baseType);
    
    FullQualifiedName typeName = new FullQualifiedName("namespace", "typeName");
    EntityType type = new EntityType();
    type.setName(typeName.getName());
    type.setBaseType(baseName);
    List<Property> typeProperties = new ArrayList<Property>();
    typeProperties.add(new Property().setName("address").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    typeProperties.add(new Property().setName("email").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()));
    type.setProperties(typeProperties);
    List<NavigationProperty> typeNavigationProperties = new ArrayList<NavigationProperty>();
    typeNavigationProperties.add(new NavigationProperty().setName("nav2"));
    type.setNavigationProperties(typeNavigationProperties);
    when(provider.getEntityType(typeName)).thenReturn(type);
    EdmEntityType edmType = EdmEntityTypeImpl.getInstance(edm, typeName, type);
    
    assertNotNull(edmType.getBaseType());
    assertEquals(2, edmAbstarctBaseType.getPropertyNames().size());
    
    assertEquals(1, edmType.getKeyPropertyRefs().size());
    assertEquals("Id", edmType.getKeyPredicateNames().get(0));
    
    assertEquals(4, edmType.getPropertyNames().size());
    assertEquals("Id", edmType.getPropertyNames().get(0));
    assertEquals("Name", edmType.getPropertyNames().get(1));
    assertEquals("address", edmType.getPropertyNames().get(2));
    assertEquals("email", edmType.getPropertyNames().get(3));
    
    assertEquals(2, edmType.getNavigationPropertyNames().size());
    assertEquals("nav1", edmType.getNavigationPropertyNames().get(0));
    assertEquals("nav2", edmType.getNavigationPropertyNames().get(1));
  }
  
  @Test
  public void hasStream() {
    assertFalse(typeWithBaseType.hasStream());
  }

  @Test
  public void complexKeyWithAlias() {
    List<String> keyPredicateNames = typeWithComplexKey.getKeyPredicateNames();
    assertEquals(2, keyPredicateNames.size());
    assertEquals("Id", keyPredicateNames.get(0));
    assertEquals("alias", keyPredicateNames.get(1));

    EdmKeyPropertyRef keyPropertyRef = typeWithComplexKey.getKeyPropertyRef("Id");
    assertNotNull(keyPropertyRef);
    assertEquals("Id", keyPropertyRef.getKeyPropertyName());
    assertNull(keyPropertyRef.getAlias());
    EdmProperty keyProperty = keyPropertyRef.getProperty();
    assertNotNull(keyProperty);
    assertEquals(typeWithComplexKey.getProperty("Id"), keyProperty);

    keyPropertyRef = typeWithComplexKey.getKeyPropertyRef("alias");
    assertNotNull(keyPropertyRef);
    assertEquals("ComplexPropName", keyPropertyRef.getKeyPropertyName());
    assertEquals("alias", keyPropertyRef.getAlias());
    assertEquals("Comp/ComplexPropName", keyPropertyRef.getPath());

    keyProperty = keyPropertyRef.getProperty();
    assertNotNull(keyProperty);
    EdmElement complexProperty = typeWithComplexKey.getProperty("Comp");
    EdmComplexType complexType = (EdmComplexType) complexProperty.getType();
    assertNotNull(complexType);
    assertEquals(complexType.getProperty("ComplexPropName"), keyProperty);
  }

  @Test
  public void keyBehaviour() {
    List<String> keyPredicateNames = baseType.getKeyPredicateNames();
    assertEquals(1, keyPredicateNames.size());
    assertEquals("Id", keyPredicateNames.get(0));

    EdmKeyPropertyRef keyPropertyRef = baseType.getKeyPropertyRef("Id");
    assertNotNull(keyPropertyRef);
    assertEquals("Id", keyPropertyRef.getKeyPropertyName());
    assertNull(keyPropertyRef.getAlias());

    EdmProperty keyProperty = keyPropertyRef.getProperty();
    assertNotNull(keyProperty);
    assertEquals(baseType.getProperty("Id"), keyProperty);

    List<EdmKeyPropertyRef> keyPropertyRefs = baseType.getKeyPropertyRefs();
    assertNotNull(keyPropertyRefs);
    assertEquals(1, keyPropertyRefs.size());
    assertEquals("Id", keyPropertyRefs.get(0).getKeyPropertyName());
  }

  @Test
  public void keyBehaviourWithBasetype() {
    List<String> keyPredicateNames = typeWithBaseType.getKeyPredicateNames();
    assertEquals(1, keyPredicateNames.size());
    assertEquals("Id", keyPredicateNames.get(0));

    EdmKeyPropertyRef keyPropertyRef = typeWithBaseType.getKeyPropertyRef("Id");
    assertNotNull(keyPropertyRef);
    assertEquals("Id", keyPropertyRef.getKeyPropertyName());
    assertNull(keyPropertyRef.getAlias());

    List<EdmKeyPropertyRef> keyPropertyRefs = typeWithBaseType.getKeyPropertyRefs();
    assertNotNull(keyPropertyRefs);
    assertEquals(1, keyPropertyRefs.size());
    assertEquals("Id", keyPropertyRefs.get(0).getKeyPropertyName());
    assertTrue(keyPropertyRefs == typeWithBaseType.getKeyPropertyRefs());
  }

  @Test
  public void getBaseType() {
    assertNull(baseType.getBaseType());
    assertNotNull(typeWithBaseType.getBaseType());
  }

  @Test
  public void propertiesBehaviour() {
    List<String> propertyNames = baseType.getPropertyNames();
    assertEquals(2, propertyNames.size());
    assertEquals("Id", baseType.getProperty("Id").getName());
    assertEquals("Name", baseType.getProperty("Name").getName());
  }

  @Test
  public void propertiesBehaviourWithBaseType() {
    List<String> propertyNames = typeWithBaseType.getPropertyNames();
    assertEquals(4, propertyNames.size());
    assertEquals("Id", typeWithBaseType.getProperty("Id").getName());
    assertEquals("Name", typeWithBaseType.getProperty("Name").getName());
    assertEquals("address", typeWithBaseType.getProperty("address").getName());
    assertEquals("email", typeWithBaseType.getProperty("email").getName());
  }

  @Test
  public void navigationPropertiesBehaviour() {
    List<String> navigationPropertyNames = baseType.getNavigationPropertyNames();
    assertEquals(1, navigationPropertyNames.size());
    assertEquals("nav1", baseType.getProperty("nav1").getName());
  }

  @Test
  public void navigationPropertiesBehaviourWithBaseType() {
    List<String> navigationPropertyNames = typeWithBaseType.getNavigationPropertyNames();
    assertEquals(2, navigationPropertyNames.size());
    assertEquals("nav1", typeWithBaseType.getProperty("nav1").getName());
    assertEquals("nav2", typeWithBaseType.getProperty("nav2").getName());
  }

  @Test
  public void propertyCaching() {
    EdmElement property = typeWithBaseType.getProperty("Id");
    assertTrue(property == typeWithBaseType.getProperty("Id"));

    property = typeWithBaseType.getProperty("address");
    assertTrue(property == typeWithBaseType.getProperty("address"));

    property = typeWithBaseType.getProperty("nav1");
    assertTrue(property == typeWithBaseType.getProperty("nav1"));

    property = typeWithBaseType.getProperty("nav2");
    assertTrue(property == typeWithBaseType.getProperty("nav2"));
  }

  @Test
  public void abstractTypeDoesNotNeedKey() {
    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    EntityType entityType = new EntityType().setName("n").setAbstract(true);
    EdmEntityTypeImpl.getInstance(edm, new FullQualifiedName("n", "n"), entityType);
  }

  @Test(expected = EdmException.class)
  public void invalidBaseType() {
    EdmProviderImpl edm = mock(EdmProviderImpl.class);
    EntityType entityType = new EntityType().setName("n").setBaseType(new FullQualifiedName("wrong", "wrong"));
    EdmEntityTypeImpl instance = EdmEntityTypeImpl.getInstance(edm, new FullQualifiedName("n", "n"), entityType);
    instance.getBaseType();
  }

  @Test
  public void abstractTypeWithAbstractBaseTypeDoesNotNeedKey() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);
    FullQualifiedName baseName = new FullQualifiedName("n", "base");
    when(provider.getEntityType(baseName)).thenReturn(new EntityType().setName("base").setAbstract(true));
    EntityType entityType = new EntityType().setName("n").setAbstract(true).setBaseType(baseName);
    EdmEntityTypeImpl.getInstance(edm, new FullQualifiedName("n", "n"), entityType);
  }

}
