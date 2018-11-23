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
package org.apache.olingo.server.tecsvc.provider;

import org.apache.olingo.commons.api.ODataException;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.server.api.edm.provider.ComplexType;
import org.apache.olingo.server.api.edm.provider.NavigationProperty;
import org.apache.olingo.server.api.edm.provider.Property;

import java.util.Arrays;

public class ComplexTypeProvider {

  public static final FullQualifiedName nameCTAllPrim = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTAllPrim");
  public static final FullQualifiedName nameCTBase = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTBase");
  public static final FullQualifiedName nameCTBasePrimCompNav = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTBasePrimCompNav");
  public static final FullQualifiedName nameCTCollAllPrim = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTCollAllPrim");
  public static final FullQualifiedName nameCTCompCollComp = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTCompCollComp");
  public static final FullQualifiedName nameCTCompComp = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTCompComp");
  public static final FullQualifiedName nameCTCompNav = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTCompNav");

  public static final FullQualifiedName nameCTMixPrimCollComp = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTMixPrimCollComp");
  public static final FullQualifiedName nameCTNavFiveProp = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTNavFiveProp");
  public static final FullQualifiedName nameCTPrim = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTPrim");
  public static final FullQualifiedName nameCTPrimComp = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTPrimComp");
  public static final FullQualifiedName nameCTTwoBase = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTTwoBase");
  public static final FullQualifiedName nameCTTwoBasePrimCompNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "CTTwoBasePrimCompNav");
  public static final FullQualifiedName nameCTTwoPrim = new FullQualifiedName(SchemaProvider.NAMESPACE, "CTTwoPrim");
  public static final FullQualifiedName nameCTMixEnumDef = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "CTMixEnumDef");

  public ComplexType getComplexType(final FullQualifiedName complexTypeName) throws ODataException {

    if (complexTypeName.equals(nameCTPrim)) {
      return new ComplexType()
          .setName("CTPrim")
          .setProperties(Arrays.asList(PropertyProvider.propertyInt16));

    } else if (complexTypeName.equals(nameCTAllPrim)) {
      return new ComplexType()
          .setName("CTAllPrim")
          .setProperties(
              Arrays.asList(PropertyProvider.propertyString, PropertyProvider.propertyBinary,
                  PropertyProvider.propertyBoolean, PropertyProvider.propertyByte, PropertyProvider.propertyDate,
                  PropertyProvider.propertyDateTimeOffset, PropertyProvider.propertyDecimal,
                  PropertyProvider.propertySingle, PropertyProvider.propertyDouble, PropertyProvider.propertyDuration,
                  PropertyProvider.propertyGuid, PropertyProvider.propertyInt16, PropertyProvider.propertyInt32,
                  PropertyProvider.propertyInt64, PropertyProvider.propertySByte, PropertyProvider.propertyTimeOfDay
                  /* TODO add propertyStream */));

    } else if (complexTypeName.equals(nameCTCollAllPrim)) {
      return new ComplexType()
          .setName("CTCollAllPrim")
          .setProperties(
              Arrays.asList(
                  PropertyProvider.collPropertyString, PropertyProvider.collPropertyBoolean,
                  PropertyProvider.collPropertyByte, PropertyProvider.collPropertySByte,
                  PropertyProvider.collPropertyInt16, PropertyProvider.collPropertyInt32,
                  PropertyProvider.collPropertyInt64, PropertyProvider.collPropertySingle,
                  PropertyProvider.collPropertyDouble, PropertyProvider.collPropertyDecimal,
                  PropertyProvider.collPropertyBinary, PropertyProvider.collPropertyDate,
                  PropertyProvider.collPropertyDateTimeOffset, PropertyProvider.collPropertyDuration,
                  PropertyProvider.collPropertyGuid, PropertyProvider.collPropertyTimeOfDay
                  /* TODO add collectionPropertyStream */));

    } else if (complexTypeName.equals(nameCTTwoPrim)) {
      return new ComplexType()
          .setName("CTTwoPrim")
          .setProperties(Arrays.asList(PropertyProvider.propertyInt16_NotNullable,
                                        PropertyProvider.propertyString_NotNullable));

    } else if (complexTypeName.equals(nameCTCompNav)) {
      return new ComplexType()
          .setName("CTCompNav")
          .setProperties(Arrays.asList(PropertyProvider.propertyString,
              PropertyProvider.propertyComp_CTNavFiveProp));

    } else if (complexTypeName.equals(nameCTMixPrimCollComp)) {
      return new ComplexType()
          .setName("CTMixPrimCollComp")
          .setProperties(
              Arrays.asList(PropertyProvider.propertyInt16, PropertyProvider.collPropertyString,
                  PropertyProvider.propertyComp_CTTwoPrim, PropertyProvider.collPropertyComp_CTTwoPrim));

    } else if (complexTypeName.equals(nameCTBase)) {
      return new ComplexType()
          .setName("CTBase")
          .setBaseType(nameCTTwoPrim)
          .setProperties(Arrays.asList(
              new Property()
                  .setName("AdditionalPropString")
                  .setType(new FullQualifiedName("Edm", "String"))));

    } else if (complexTypeName.equals(nameCTTwoBase)) {
      return new ComplexType()
          .setName("CTTwoBase")
          .setBaseType(nameCTBase);

    } else if (complexTypeName.equals(nameCTCompComp)) {
      return new ComplexType()
          .setName("CTCompComp")
          .setProperties(Arrays.asList(PropertyProvider.propertyComp_CTTwoPrim));

    } else if (complexTypeName.equals(nameCTCompCollComp)) {
      return new ComplexType()
          .setName("CTCompCollComp")
          .setProperties(Arrays.asList(PropertyProvider.collPropertyComp_CTTwoPrim));

    } else if (complexTypeName.equals(nameCTPrimComp)) {
      return new ComplexType()
          .setName("CTPrimComp")
          .setProperties(Arrays.asList(PropertyProvider.propertyInt16, PropertyProvider.propertyComp_CTAllPrim));

    } else if (complexTypeName.equals(nameCTNavFiveProp)) {
      return new ComplexType()
          .setName("CTNavFiveProp")
          .setProperties(Arrays.asList(PropertyProvider.propertyInt16))
          .setNavigationProperties((Arrays.asList(
              PropertyProvider.collectionNavPropertyETTwoKeyNavOne_ETTwoKeyNav,
              PropertyProvider.collectionNavPropertyETTwoKeyNavMany_ETTwoKeyNav,
              new NavigationProperty()
                  .setName("NavPropertyETMediaOne")
                  .setType(EntityTypeProvider.nameETMedia),
              new NavigationProperty()
                  .setName("NavPropertyETMediaMany")
                  .setType(EntityTypeProvider.nameETMedia).setCollection(true)
              )));

    } else if (complexTypeName.equals(nameCTBasePrimCompNav)) {
      return new ComplexType()
          .setName("CTBasePrimCompNav")
          .setBaseType(nameCTPrimComp)
          .setNavigationProperties(Arrays.asList(
              PropertyProvider.collectionNavPropertyETTwoKeyNavMany_ETTwoKeyNav,
              PropertyProvider.collectionNavPropertyETTwoKeyNavOne_ETTwoKeyNav,
              PropertyProvider.navPropertyETKeyNavOne_ETKeyNav,
              PropertyProvider.collectionNavPropertyETKeyNavMany_ETKeyNav));

    } else if (complexTypeName.equals(nameCTTwoBasePrimCompNav)) {
      return new ComplexType()
          .setName("CTTwoBasePrimCompNav")
          .setBaseType(nameCTBasePrimCompNav);

    } else if (complexTypeName.equals(nameCTMixEnumDef)) {
      return new ComplexType()
          .setName(nameCTMixEnumDef.getName())
          .setProperties(Arrays.asList(
              PropertyProvider.propertyEnumString_ENString,
              PropertyProvider.collPropertyEnumString_ENString,
              PropertyProvider.propertyTypeDefinition_TDString,
              PropertyProvider.collPropertyTypeDefinition_TDString));
    }

    return null;
  }
}
