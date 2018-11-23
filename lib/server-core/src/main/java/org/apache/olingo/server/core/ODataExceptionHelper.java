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

import java.util.Locale;

import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataServerError;
import org.apache.olingo.server.api.ODataTranslatedException;
import org.apache.olingo.server.api.ODataTranslatedException.ODataErrorMessage;
import org.apache.olingo.server.api.batch.exception.BatchDeserializerException;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.core.uri.parser.UriParserException;
import org.apache.olingo.server.core.uri.parser.UriParserSemanticException;
import org.apache.olingo.server.core.uri.parser.UriParserSyntaxException;
import org.apache.olingo.server.core.uri.validator.UriValidationException;

public class ODataExceptionHelper {

  public static ODataServerError createServerErrorObject(UriValidationException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserSemanticException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if (UriParserSemanticException.MessageKeys.RESOURCE_NOT_FOUND.equals(e.getMessageKey())
        || UriParserSemanticException.MessageKeys.FUNCTION_NOT_FOUND.equals(e.getMessageKey())
        || UriParserSemanticException.MessageKeys.PROPERTY_NOT_IN_TYPE.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.NOT_FOUND.getStatusCode());
    } else {
      serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    }
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserSyntaxException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(
        UriParserSyntaxException.MessageKeys.WRONG_VALUE_FOR_SYSTEM_QUERY_OPTION_FORMAT.equals(e.getMessageKey()) ?
            HttpStatusCode.NOT_ACCEPTABLE.getStatusCode() :
            HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(UriParserException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(ContentNegotiatorException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.NOT_ACCEPTABLE.getStatusCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(ODataHandlerException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    if (ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.PROCESSOR_NOT_IMPLEMENTED.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.NOT_IMPLEMENTED.getStatusCode());
    } else if (ODataHandlerException.MessageKeys.ODATA_VERSION_NOT_SUPPORTED.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.INVALID_HTTP_METHOD.equals(e.getMessageKey())
        || ODataHandlerException.MessageKeys.AMBIGUOUS_XHTTP_METHOD.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    } else if (ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED.equals(e.getMessageKey())) {
      serverError.setStatusCode(HttpStatusCode.METHOD_NOT_ALLOWED.getStatusCode());
    }

    return serverError;
  }

  public static ODataServerError createServerErrorObject(SerializerException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }
  
  public static ODataServerError createServerErrorObject(BatchDeserializerException e, Locale requestedLocale) {
    ODataServerError serverError = basicTranslatedError(e, requestedLocale);
    serverError.setStatusCode(HttpStatusCode.BAD_REQUEST.getStatusCode());
    return serverError;
  }
  
  public static ODataServerError createServerErrorObject(ODataTranslatedException e, Locale requestedLocale) {
    return basicTranslatedError(e, requestedLocale);
  }

  public static ODataServerError createServerErrorObject(ODataApplicationException e) {
    ODataServerError serverError = basicServerError(e);
    serverError.setStatusCode(e.getStatusCode());
    serverError.setLocale(e.getLocale());
    serverError.setCode(e.getODataErrorCode());
    return serverError;
  }

  public static ODataServerError createServerErrorObject(Exception e) {
    ODataServerError serverError = basicServerError(e);
    serverError.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    serverError.setLocale(Locale.ENGLISH);
    return serverError;
  }

  private static ODataServerError basicServerError(Exception e) {
    ODataServerError serverError = new ODataServerError().setException(e).setMessage(e.getMessage());
    return serverError;
  }

  private static ODataServerError basicTranslatedError(ODataTranslatedException e, Locale requestedLocale) {
    ODataServerError serverError = basicServerError(e);
    ODataErrorMessage translatedMessage = e.getTranslatedMessage(requestedLocale);
    serverError.setMessage(translatedMessage.getMessage());
    serverError.setLocale(translatedMessage.getLocale());
    serverError.setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode());
    return serverError;
  }
}
