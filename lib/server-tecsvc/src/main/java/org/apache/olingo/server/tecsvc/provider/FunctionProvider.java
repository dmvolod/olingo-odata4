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
import org.apache.olingo.server.api.edm.provider.Function;
import org.apache.olingo.server.api.edm.provider.Parameter;
import org.apache.olingo.server.api.edm.provider.ReturnType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionProvider {

  // Bound Functions
  public static final FullQualifiedName nameBFCCollCTPrimCompRTESAllPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCollCTPrimCompRTESAllPrim");

  public static final FullQualifiedName nameBFCCollStringRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCollStringRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCCTPrimCompRTESBaseTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCTPrimCompRTESBaseTwoKeyNav");

  public static final FullQualifiedName nameBFCCTPrimCompRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCTPrimCompRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCCTPrimCompRTESTwoKeyNavParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCTPrimCompRTESTwoKeyNavParam");

  public static final FullQualifiedName nameBFCCTPrimCompRTETTwoKeyNavParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCCTPrimCompRTETTwoKeyNavParam");

  public static final FullQualifiedName nameBFCESAllPrimRTCTAllPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESAllPrimRTCTAllPrim");

  public static final FullQualifiedName nameBFCESBaseTwoKeyNavRTESBaseTwoKey =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESBaseTwoKeyNavRTESBaseTwoKey");

  public static final FullQualifiedName nameBFCESKeyNavRTETKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESKeyNavRTETKeyNav");

  public static final FullQualifiedName nameBFCESKeyNavRTETKeyNavParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESKeyNavRTETKeyNavParam");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTCollCTTwoPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTCollCTTwoPrim");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTCollString =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTCollString");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTCTTwoPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTCTTwoPrim");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTString =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTString");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTStringParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTStringParam");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTTwoKeyNav");

  public static final FullQualifiedName nameBFCETBaseTwoKeyNavRTESBaseTwoKey =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETBaseTwoKeyNavRTESBaseTwoKey");

  public static final FullQualifiedName nameBFCETBaseTwoKeyNavRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETBaseTwoKeyNavRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCETBaseTwoKeyNavRTETTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETBaseTwoKeyNavRTETTwoKeyNav");

  public static final FullQualifiedName nameBFCETKeyNavRTETKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETKeyNavRTETKeyNav");

  public static final FullQualifiedName nameBFCETTwoKeyNavRTCTTwoPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETTwoKeyNavRTCTTwoPrim");

  public static final FullQualifiedName nameBFCETTwoKeyNavRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETTwoKeyNavRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCETTwoKeyNavRTETTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCETTwoKeyNavRTETTwoKeyNav");

  public static final FullQualifiedName nameBFCSINavRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCSINavRTESTwoKeyNav");

  public static final FullQualifiedName nameBFCStringRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFCStringRTESTwoKeyNav");

  public static final FullQualifiedName nameBFESTwoKeyNavRTESTwoKeyNav =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "BFESTwoKeyNavRTESTwoKeyNav");

  // Unbound Functions
  public static final FullQualifiedName nameUFCRTCollCTTwoPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTCollCTTwoPrim");
  public static final FullQualifiedName nameUFCRTCollCTTwoPrimParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTCollCTTwoPrimParam");
  public static final FullQualifiedName nameUFCRTCollString = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "UFCRTCollString");
  public static final FullQualifiedName nameUFCRTCollStringTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTCollStringTwoParam");
  public static final FullQualifiedName nameUFCRTCTAllPrimTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTCTAllPrimTwoParam");
  public static final FullQualifiedName nameUFCRTCTTwoPrim = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "UFCRTCTTwoPrim");
  public static final FullQualifiedName nameUFCRTCTTwoPrimParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTCTTwoPrimParam");
  public static final FullQualifiedName nameUFCRTESMixPrimCollCompTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTESMixPrimCollCompTwoParam");
  public static final FullQualifiedName nameUFCRTESTwoKeyNavParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTESTwoKeyNavParam");
  public static final FullQualifiedName nameUFCRTETAllPrimTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTETAllPrimTwoParam");
  public static final FullQualifiedName nameUFCRTETKeyNav = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "UFCRTETKeyNav");
  public static final FullQualifiedName nameUFCRTETMedia = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "UFCRTETMedia");

  public static final FullQualifiedName nameUFCRTETTwoKeyNavParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTETTwoKeyNavParam");

  public static final FullQualifiedName nameUFCRTETTwoKeyNavParamCTTwoPrim =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTETTwoKeyNavParamCTTwoPrim");

  public static final FullQualifiedName nameUFCRTString =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTString");

  public static final FullQualifiedName nameUFCRTStringTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFCRTStringTwoParam");

  public static final FullQualifiedName nameUFNRTESMixPrimCollCompTwoParam =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFNRTESMixPrimCollCompTwoParam");
  public static final FullQualifiedName nameUFNRTInt16 =
      new FullQualifiedName(SchemaProvider.NAMESPACE, "UFNRTInt16");

  public static final FullQualifiedName nameUFNRTCollCTNavFiveProp = new FullQualifiedName(SchemaProvider.NAMESPACE,
      "UFNRTCollCTNavFiveProp");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTCTNavFiveProp = new FullQualifiedName(
      SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTCTNavFiveProp");

  public static final FullQualifiedName nameBFCESTwoKeyNavRTCollCTNavFiveProp = new FullQualifiedName(
      SchemaProvider.NAMESPACE, "BFCESTwoKeyNavRTCollCTNavFiveProp");

  public List<Function> getFunctions(final FullQualifiedName functionName) throws ODataException {

    if (functionName.equals(nameUFNRTInt16)) {
      return Arrays.asList(
          new Function()
              .setName("UFNRTInt16")
              .setParameters(new ArrayList<Parameter>())
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameInt16))
          );

    } else if (functionName.equals(nameUFCRTETKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTETKeyNav")
              .setParameters(new ArrayList<Parameter>())
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETKeyNav).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTETTwoKeyNavParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTETTwoKeyNavParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false)
              )
          );

    } else if (functionName.equals(nameUFCRTETTwoKeyNavParamCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTETTwoKeyNavParamCTTwoPrim")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterCTTwoPrim").setType(ComplexTypeProvider.nameCTTwoPrim)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false)
              )
          );

    } else if (functionName.equals(nameUFCRTStringTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTStringTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter()
                      .setName("ParameterInt16")
                      .setType(PropertyProvider.nameInt16)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setNullable(false)),
          new Function()
              .setName("UFCRTStringTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter()
                      .setName("ParameterString")
                      .setType(PropertyProvider.nameString)
                      .setNullable(false),
                  new Parameter()
                      .setName("ParameterInt16")
                      .setType(PropertyProvider.nameInt16)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(new ReturnType().setType(PropertyProvider.nameString).setNullable(false))

          );

    } else if (functionName.equals(nameUFCRTESTwoKeyNavParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTESTwoKeyNavParam")
              .setParameters(Arrays.asList(
                  new Parameter()
                      .setName("ParameterInt16")
                      .setType(PropertyProvider.nameInt16)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTString)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTString")

              .setComposable(true)
              .setParameters(new ArrayList<Parameter>())
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setNullable(false)
              )
          );

    } else if (functionName.equals(nameUFCRTCollStringTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCollStringTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(false),
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTCollString)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCollString")
              .setParameters(new ArrayList<Parameter>())
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTCTAllPrimTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCTAllPrimTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(false),
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTAllPrim).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTCTTwoPrimParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCTTwoPrimParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false),
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(true)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setNullable(false))
          );
    } else if (functionName.equals(nameUFCRTCollCTTwoPrimParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCollCTTwoPrimParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false),
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(true)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCTTwoPrim")
              .setParameters(new ArrayList<Parameter>())
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTCollCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTCollCTTwoPrim")
              .setComposable(true)
              .setParameters(new ArrayList<Parameter>())
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTETMedia)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTETMedia")
              .setParameters(new ArrayList<Parameter>())
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETMedia).setNullable(false))
          );

    } else if (functionName.equals(nameUFNRTESMixPrimCollCompTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFNRTESMixPrimCollCompTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(false),
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)))
              .setComposable(false)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETMixPrimCollComp).setCollection(true)
                      .setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTETAllPrimTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTETAllPrimTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(false),
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETAllPrim).setNullable(false))
          );

    } else if (functionName.equals(nameUFCRTESMixPrimCollCompTwoParam)) {
      return Arrays.asList(
          new Function()
              .setName("UFCRTESMixPrimCollCompTwoParam")
              .setParameters(Arrays.asList(
                  new Parameter().setName("ParameterString").setType(PropertyProvider.nameString).setNullable(false),
                  new Parameter().setName("ParameterInt16").setType(PropertyProvider.nameInt16).setNullable(false)
                  ))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETMixPrimCollComp).setCollection(true)
                      .setNullable(false))
          );

    } else if (functionName.equals(nameUFNRTCollCTNavFiveProp)) {
      return Arrays.asList(
          new Function()
              .setName("UFNRTCollCTNavFiveProp")
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTNavFiveProp).setCollection(true))
          );
    } else if (functionName.equals(nameBFCESTwoKeyNavRTESTwoKeyNav)) {
      return Arrays
          .asList(
              new Function()
                  .setName("BFCESTwoKeyNavRTESTwoKeyNav")
                  .setBound(true)
                  .setParameters(
                      Arrays.asList(
                          new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                              .setCollection(true).setNullable(false)))
                  .setComposable(true)
                  .setReturnType(
                      new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true)
                          .setNullable(false)),

              new Function()
                  .setName("BFCESTwoKeyNavRTESTwoKeyNav")
                  .setBound(true)
                  .setParameters(
                      Arrays.asList(
                          new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                              .setCollection(true).setNullable(false),
                          new Parameter().setName("ParameterString").setType(PropertyProvider.nameString)
                              .setCollection(false).setNullable(false)))
                  .setComposable(true)
                  .setReturnType(
                      new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true)
                          .setNullable(false)),
              new Function()
                  .setName("BFCESTwoKeyNavRTESTwoKeyNav")
                  .setBound(true)
                  .setParameters(
                      Arrays.asList(
                          new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETKeyNav)
                              .setCollection(true).setNullable(false)))
                  .setComposable(true)
                  .setReturnType(
                      new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true)
                          .setNullable(false)),
              new Function()
                  .setName("BFCESTwoKeyNavRTESTwoKeyNav")
                  .setBound(true)
                  .setParameters(
                      Arrays.asList(new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETKeyNav)
                          .setCollection(true).setNullable(false),
                          new Parameter().setName("ParameterString").setType(PropertyProvider.nameString)
                              .setCollection(false).setNullable(false)))
                  .setComposable(true)
                  .setReturnType(
                      new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true)
                          .setNullable(false))
          );

    } else if (functionName.equals(nameBFCStringRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function().setName("BFCStringRTESTwoKeyNav")
              .setBound(true)
              .setParameters(Arrays.asList(
                  new Parameter().setName("BindingParam").setType(PropertyProvider.nameString).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETBaseTwoKeyNavRTETTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETBaseTwoKeyNavRTETTwoKeyNav")
              .setBound(true)
              .setParameters(Arrays.asList(
                  new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETBaseTwoKeyNav)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false)
              )
          );

    } else if (functionName.equals(nameBFCESBaseTwoKeyNavRTESBaseTwoKey)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESBaseTwoKeyNavRTESBaseTwoKey")
              .setBound(true)
              .setParameters(Arrays.asList(
                  new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETBaseTwoKeyNav)
                      .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETBaseTwoKeyNav).setCollection(true)
                      .setNullable(false))
          );

    } else if (functionName.equals(nameBFCESAllPrimRTCTAllPrim)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESAllPrimRTCTAllPrim")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETAllPrim)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTAllPrim).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESTwoKeyNavRTCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTCTTwoPrim")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESTwoKeyNavRTCollCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTCollCTTwoPrim")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESTwoKeyNavRTString)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTString")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESTwoKeyNavRTCollString)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTCollString")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETTwoKeyNavRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETTwoKeyNavRTESTwoKeyNav")
              .setBound(true)
              .setParameters(Arrays.asList(
                  new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                      .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETBaseTwoKeyNavRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETBaseTwoKeyNavRTESTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETBaseTwoKeyNav)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCSINavRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCSINavRTESTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(
                          false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETBaseTwoKeyNavRTESBaseTwoKey)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETBaseTwoKeyNavRTESBaseTwoKey")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETBaseTwoKeyNav)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETBaseTwoKeyNav).setCollection(true).setNullable(
                      false))
          );

    } else if (functionName.equals(nameBFCCollStringRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCollStringRTESTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(PropertyProvider.nameString).setCollection(true)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCCTPrimCompRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCTPrimCompRTESTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(ComplexTypeProvider.nameCTPrimComp).setNullable(
                          false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCCTPrimCompRTESBaseTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCTPrimCompRTESBaseTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(ComplexTypeProvider.nameCTPrimComp).setNullable(
                          false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETBaseTwoKeyNav).setCollection(true).setNullable(
                      false))
          );

    } else if (functionName.equals(nameBFCCollCTPrimCompRTESAllPrim)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCollCTPrimCompRTESAllPrim")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(ComplexTypeProvider.nameCTPrimComp)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETAllPrim).setCollection(true).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESTwoKeyNavRTTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESKeyNavRTETKeyNav)) {
      return Arrays
          .asList(
          new Function()
              .setName("BFCESKeyNavRTETKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETKeyNav).setCollection(
                          true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETKeyNav).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETKeyNavRTETKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETKeyNavRTETKeyNav")
              .setBound(true)
              .setParameters(Arrays.asList(
                  new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETKeyNav).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETKeyNav).setNullable(false))
          );
    } else if (functionName.equals(nameBFESTwoKeyNavRTESTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFESTwoKeyNavRTESTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))

          );

    } else if (functionName.equals(nameBFCETTwoKeyNavRTETTwoKeyNav)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETTwoKeyNavRTETTwoKeyNav")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(
                          false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false))
          );

    } else if (functionName.equals(nameBFCETTwoKeyNavRTCTTwoPrim)) {
      return Arrays.asList(
          new Function()
              .setName("BFCETTwoKeyNavRTCTTwoPrim")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(
                          false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTTwoPrim).setNullable(false))
          );
    } else if (functionName.equals(nameBFCESTwoKeyNavRTCTNavFiveProp)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTCTNavFiveProp")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTNavFiveProp).setNullable(false))
          );
    } else if (functionName.equals(nameBFCESTwoKeyNavRTCollCTNavFiveProp)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTCollCTNavFiveProp")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(ComplexTypeProvider.nameCTNavFiveProp).setCollection(true)
                      .setNullable(false))
          );
    } else if (functionName.equals(nameBFCESTwoKeyNavRTStringParam)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESTwoKeyNavRTStringParam")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETTwoKeyNav)
                          .setCollection(true).setNullable(false),
                      new Parameter().setName("ParameterComplex").setType(ComplexTypeProvider.nameCTTwoPrim)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(PropertyProvider.nameString).setNullable(false))
          );

    } else if (functionName.equals(nameBFCESKeyNavRTETKeyNavParam)) {
      return Arrays.asList(
          new Function()
              .setName("BFCESKeyNavRTETKeyNavParam")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(EntityTypeProvider.nameETKeyNav).setCollection(
                          true).setNullable(false),
                      new Parameter().setName("ParameterString").setType(PropertyProvider.nameString)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETKeyNav).setNullable(false))
          );
    } else if (functionName.equals(nameBFCCTPrimCompRTETTwoKeyNavParam)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCTPrimCompRTETTwoKeyNavParam")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(ComplexTypeProvider.nameCTPrimComp).setNullable(
                          false),
                      new Parameter().setName("ParameterString").setType(PropertyProvider.nameString)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(new ReturnType()
                  .setType(EntityTypeProvider.nameETTwoKeyNav).setNullable(false)
              )
          );
    } else if (functionName.equals(nameBFCCTPrimCompRTESTwoKeyNavParam)) {
      return Arrays.asList(
          new Function()
              .setName("BFCCTPrimCompRTESTwoKeyNavParam")
              .setBound(true)
              .setParameters(
                  Arrays.asList(
                      new Parameter().setName("BindingParam").setType(ComplexTypeProvider.nameCTPrimComp).setNullable(
                          false),
                      new Parameter().setName("ParameterString").setType(PropertyProvider.nameString)
                          .setNullable(false)))
              .setComposable(true)
              .setReturnType(
                  new ReturnType().setType(EntityTypeProvider.nameETTwoKeyNav).setCollection(true).setNullable(false))
          );
    }

    return null;
  }

}
