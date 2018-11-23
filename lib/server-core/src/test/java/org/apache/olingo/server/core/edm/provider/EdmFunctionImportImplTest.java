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

import org.apache.olingo.commons.api.edm.EdmEntityContainer;
import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.edm.EdmFunctionImport;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import org.apache.olingo.server.api.edm.provider.EdmProvider;
import org.apache.olingo.server.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.server.api.edm.provider.Function;
import org.apache.olingo.server.api.edm.provider.FunctionImport;
import org.apache.olingo.server.api.edm.provider.Parameter;
import org.apache.olingo.server.api.edm.provider.ReturnType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EdmFunctionImportImplTest {

  @Test
  public void functionImport() throws Exception {
    EdmProvider provider = mock(EdmProvider.class);
    EdmProviderImpl edm = new EdmProviderImpl(provider);

    final FullQualifiedName functionName = new FullQualifiedName("ns", "function");
    final Function functionProvider = new Function()
        .setName(functionName.getName())
        .setParameters(Collections.<Parameter> emptyList())
        .setBound(false)
        .setComposable(false)
        .setReturnType(new ReturnType().setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName()));
    when(provider.getFunctions(functionName)).thenReturn(Arrays.asList(functionProvider));

    final FullQualifiedName containerName = new FullQualifiedName("ns", "container");
    final EntityContainerInfo containerInfo = new EntityContainerInfo().setContainerName(containerName);
    when(provider.getEntityContainerInfo(containerName)).thenReturn(containerInfo);
    final EdmEntityContainer entityContainer = new EdmEntityContainerImpl(edm, provider, containerInfo);

    final String functionImportName = "functionImport";
    final FunctionImport functionImportProvider = new FunctionImport()
        .setName(functionImportName)
        .setFunction(functionName)
        .setIncludeInServiceDocument(true);
    when(provider.getFunctionImport(containerName, functionImportName)).thenReturn(functionImportProvider);

    final EdmFunctionImport functionImport = new EdmFunctionImportImpl(edm, entityContainer, functionImportProvider);
    assertEquals(functionImportName, entityContainer.getFunctionImport(functionImportName).getName());
    assertEquals("functionImport", functionImport.getName());
    final EdmFunction function = functionImport.getUnboundFunction(Collections.<String> emptyList());
    assertEquals(functionName.getNamespace(), function.getNamespace());
    assertEquals(functionName.getName(), function.getName());
    assertFalse(function.isBound());
    assertFalse(function.isComposable());
    assertEquals(EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Boolean),
        function.getReturnType().getType());
    assertEquals(entityContainer, functionImport.getEntityContainer());
    assertNull(functionImport.getReturnedEntitySet());
  }
}
