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
package org.apache.olingo.client.core.edm.xml.v4.annotation;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.edm.xml.v4.annotation.AnnotationExpression;
import org.apache.olingo.client.api.edm.xml.v4.annotation.Apply;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ApplyDeserializer.class)
public class ApplyImpl extends AbstractAnnotatableDynamicAnnotationExpression implements Apply {

  private static final long serialVersionUID = 4358398303405059879L;

  private String function;

  private final List<AnnotationExpression> parameters = new ArrayList<AnnotationExpression>();

  @Override
  public String getFunction() {
    return function;
  }

  public void setFunction(final String function) {
    this.function = function;
  }

  @Override
  public List<AnnotationExpression> getParameters() {
    return parameters;
  }

}
