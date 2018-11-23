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

import java.util.LinkedList;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmAction;
import org.apache.olingo.commons.api.edm.EdmActionImport;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmFunction;
import org.apache.olingo.commons.api.edm.EdmFunctionImport;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.EdmReturnType;
import org.apache.olingo.commons.api.edm.constants.ODataServiceVersion;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.format.ODataFormat;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.core.edm.primitivetype.EdmPrimitiveTypeFactory;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ODataServerError;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.batch.exception.BatchDeserializerException;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.processor.ActionComplexCollectionProcessor;
import org.apache.olingo.server.api.processor.ActionComplexProcessor;
import org.apache.olingo.server.api.processor.ActionEntityCollectionProcessor;
import org.apache.olingo.server.api.processor.ActionEntityProcessor;
import org.apache.olingo.server.api.processor.ActionPrimitiveCollectionProcessor;
import org.apache.olingo.server.api.processor.ActionPrimitiveProcessor;
import org.apache.olingo.server.api.processor.BatchProcessor;
import org.apache.olingo.server.api.processor.ComplexCollectionProcessor;
import org.apache.olingo.server.api.processor.ComplexProcessor;
import org.apache.olingo.server.api.processor.CountComplexCollectionProcessor;
import org.apache.olingo.server.api.processor.CountEntityCollectionProcessor;
import org.apache.olingo.server.api.processor.CountPrimitiveCollectionProcessor;
import org.apache.olingo.server.api.processor.DefaultProcessor;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.processor.ErrorProcessor;
import org.apache.olingo.server.api.processor.MediaEntityProcessor;
import org.apache.olingo.server.api.processor.MetadataProcessor;
import org.apache.olingo.server.api.processor.PrimitiveCollectionProcessor;
import org.apache.olingo.server.api.processor.PrimitiveProcessor;
import org.apache.olingo.server.api.processor.PrimitiveValueProcessor;
import org.apache.olingo.server.api.processor.Processor;
import org.apache.olingo.server.api.processor.ReferenceCollectionProcessor;
import org.apache.olingo.server.api.processor.ReferenceProcessor;
import org.apache.olingo.server.api.processor.ServiceDocumentProcessor;
import org.apache.olingo.server.api.serializer.CustomContentTypeSupport;
import org.apache.olingo.server.api.serializer.RepresentationType;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceAction;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourcePartTyped;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.UriResourceProperty;
import org.apache.olingo.server.core.batchhandler.BatchHandler;
import org.apache.olingo.server.core.uri.parser.Parser;
import org.apache.olingo.server.core.uri.parser.UriParserException;
import org.apache.olingo.server.core.uri.parser.UriParserSemanticException;
import org.apache.olingo.server.core.uri.parser.UriParserSyntaxException;
import org.apache.olingo.server.core.uri.validator.UriValidationException;
import org.apache.olingo.server.core.uri.validator.UriValidator;

public class ODataHandler {

  private final OData odata;
  private final ServiceMetadata serviceMetadata;
  private List<Processor> processors = new LinkedList<Processor>();
  private CustomContentTypeSupport customContentTypeSupport = null;

  private UriInfo uriInfo;

  public ODataHandler(final OData server, final ServiceMetadata serviceMetadata) {
    odata = server;
    this.serviceMetadata = serviceMetadata;

    register(new DefaultProcessor());
    register(new DefaultRedirectProcessor());
  }

  public ODataResponse process(final ODataRequest request) {
    ODataResponse response = new ODataResponse();
    try {

      processInternal(request, response);

    } catch (final UriValidationException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (final UriParserSemanticException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (final UriParserSyntaxException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (final UriParserException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (ContentNegotiatorException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (SerializerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (BatchDeserializerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (DeserializerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (ODataHandlerException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e, null);
      handleException(request, response, serverError);
    } catch (ODataApplicationException e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
      handleException(request, response, serverError);
    } catch (Exception e) {
      ODataServerError serverError = ODataExceptionHelper.createServerErrorObject(e);
      handleException(request, response, serverError);
    }
    return response;
  }

  private void processInternal(final ODataRequest request, final ODataResponse response)
          throws ODataHandlerException, UriParserException, UriValidationException, ContentNegotiatorException,
          ODataApplicationException, SerializerException, DeserializerException {
    validateODataVersion(request, response);

    uriInfo = new Parser().parseUri(request.getRawODataPath(), request.getRawQueryPath(), null,
            serviceMetadata.getEdm());

    final HttpMethod method = request.getMethod();
    new UriValidator().validate(uriInfo, method);

    switch (uriInfo.getKind()) {
      case metadata:
        if (method == HttpMethod.GET) {
          final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, customContentTypeSupport, RepresentationType.METADATA);
          selectProcessor(MetadataProcessor.class)
                  .readMetadata(request, response, uriInfo, requestedContentType);
        } else {
          throw new ODataHandlerException("HttpMethod " + method + " not allowed for metadata document",
                  ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
        }
        break;

      case service:
        if (method == HttpMethod.GET) {
          if ("".equals(request.getRawODataPath())) {
            selectProcessor(RedirectProcessor.class).redirect(request, response);
          } else {
            final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                    request, customContentTypeSupport, RepresentationType.SERVICE);

            selectProcessor(ServiceDocumentProcessor.class)
                    .readServiceDocument(request, response, uriInfo, requestedContentType);
          }
        } else {
          throw new ODataHandlerException("HttpMethod " + method + " not allowed for service document",
                  ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
        }
        break;

      case resource:
        handleResourceDispatching(request, response);
        break;

      case batch:
        if (method == HttpMethod.POST) {
          final BatchProcessor bp = selectProcessor(BatchProcessor.class);
          final BatchHandler handler = new BatchHandler(this, bp);
          handler.process(request, response, true);
        } else {
          throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                  ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
        }
        break;

      default:
        throw new ODataHandlerException("not implemented",
                ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }

  public void handleException(final ODataRequest request, final ODataResponse response,
                              final ODataServerError serverError) {

    ErrorProcessor exceptionProcessor;
    try {
      exceptionProcessor = selectProcessor(ErrorProcessor.class);
    } catch (ODataHandlerException e) {
      // This cannot happen since there is always an ExceptionProcessor registered.
      exceptionProcessor = new DefaultProcessor();
    }
    ContentType requestedContentType;
    try {
      requestedContentType = ContentNegotiator.doContentNegotiation(
              uriInfo == null ? null : uriInfo.getFormatOption(), request, customContentTypeSupport,
              RepresentationType.ERROR);
    } catch (final ContentNegotiatorException e) {
      requestedContentType = ODataFormat.JSON.getContentType(ODataServiceVersion.V40);
    }
    exceptionProcessor.processError(request, response, serverError, requestedContentType);
  }

  private void handleResourceDispatching(final ODataRequest request, final ODataResponse response)
          throws ODataHandlerException, ContentNegotiatorException, ODataApplicationException,
          SerializerException, DeserializerException {

    final int lastPathSegmentIndex = uriInfo.getUriResourceParts().size() - 1;
    final UriResource lastPathSegment = uriInfo.getUriResourceParts().get(lastPathSegmentIndex);

    switch (lastPathSegment.getKind()) {
      case action:
        handleActionDispatching(request, response, (UriResourceAction) lastPathSegment);
        break;

      case function:
        handleFunctionDispatching(request, response, (UriResourceFunction) lastPathSegment);
        break;

      case entitySet:
      case navigationProperty:
        handleEntityDispatching(request, response, (UriResourcePartTyped) lastPathSegment);
        break;

      case count:
        handleCountDispatching(request, response, lastPathSegmentIndex);
        break;

      case primitiveProperty:
        handlePrimitivePropertyDispatching(request, response, false,
                ((UriResourceProperty) lastPathSegment).isCollection());
        break;

      case complexProperty:
        handleComplexPropertyDispatching(request, response, false,
                ((UriResourceProperty) lastPathSegment).isCollection());
        break;

      case value:
        handleValueDispatching(request, response, lastPathSegmentIndex);
        break;

      case ref:
        handleReferenceDispatching(request, response, lastPathSegmentIndex);
        break;

      default:
        throw new ODataHandlerException("not implemented",
                ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }

  private void handleFunctionDispatching(final ODataRequest request, final ODataResponse response,
                                         final UriResourceFunction uriResourceFunction)
          throws ODataHandlerException, SerializerException, ContentNegotiatorException,
          ODataApplicationException, DeserializerException {
    final HttpMethod method = request.getMethod();
    if(method != HttpMethod.GET) {
      throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
              ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
    }

    EdmFunctionImport functionImport = uriResourceFunction.getFunctionImport();
    // could be null for bound functions
    if(functionImport == null) {
      throw new ODataHandlerException("Bound functions are not implemented yet",
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }

    List<EdmFunction> unboundFunctions = functionImport.getUnboundFunctions();
    if(unboundFunctions == null || unboundFunctions.isEmpty()) {
      throw new ODataHandlerException("No unbound function defined for function import",
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
    EdmReturnType returnType = unboundFunctions.get(0).getReturnType();
    handleOperationDispatching(request, response, false, returnType);
  }

  private void handleActionDispatching(final ODataRequest request, final ODataResponse response,
                                       final UriResourceAction uriResourceAction)
          throws ODataHandlerException, SerializerException, ContentNegotiatorException,
          ODataApplicationException, DeserializerException {

    final HttpMethod method = request.getMethod();
    if(request.getMethod() != HttpMethod.POST) {
      throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
              ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
    }

    EdmActionImport actionImport = uriResourceAction.getActionImport();
    // could be null for bound actions
    if(actionImport == null) {
      throw new ODataHandlerException("Bound actions are not implemented yet",
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }

    EdmAction unboundActions = actionImport.getUnboundAction();
    if(unboundActions == null) {
      throw new ODataHandlerException("No unbound function defined for function import",
              ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
    EdmReturnType returnType = unboundActions.getReturnType();
    handleOperationDispatching(request, response, true, returnType);
  }


  private void handleOperationDispatching(final ODataRequest request, final ODataResponse response,
                                          final boolean isAction, final EdmReturnType edmReturnTypeKind)
          throws ODataHandlerException, SerializerException, ContentNegotiatorException,
          ODataApplicationException, DeserializerException {

    switch (edmReturnTypeKind.getType().getKind()) {
      case ENTITY:
        handleEntityDispatching(request, response, edmReturnTypeKind.isCollection(), false, isAction);
        break;
      case PRIMITIVE:
        handlePrimitivePropertyDispatching(request, response, isAction, edmReturnTypeKind.isCollection());
        break;
      case COMPLEX:
        handleComplexPropertyDispatching(request, response, isAction, edmReturnTypeKind.isCollection());
        break;
      default:
        throw new ODataHandlerException("not implemented",
                ODataHandlerException.MessageKeys.FUNCTIONALITY_NOT_IMPLEMENTED);
    }
  }


  private void handleReferenceDispatching(final ODataRequest request, final ODataResponse response,
                                          final int lastPathSegmentIndex)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {
    final HttpMethod method = request.getMethod();
    if (((UriResourcePartTyped) uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1)).isCollection()) {
      if (method == HttpMethod.GET) {
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.COLLECTION_REFERENCE);
        selectProcessor(ReferenceCollectionProcessor.class)
                .readReferenceCollection(request, response, uriInfo, responseFormat);
      } else if (method == HttpMethod.POST) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        checkContentTypeSupport(requestFormat, RepresentationType.REFERENCE);
        selectProcessor(ReferenceProcessor.class)
                .createReference(request, response, uriInfo, requestFormat);
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    } else {
      if (method == HttpMethod.GET) {
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.REFERENCE);
        selectProcessor(ReferenceProcessor.class).readReference(request, response, uriInfo, responseFormat);
      } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        checkContentTypeSupport(requestFormat, RepresentationType.REFERENCE);
        selectProcessor(ReferenceProcessor.class)
                .updateReference(request, response, uriInfo, requestFormat);
      } else if (method == HttpMethod.DELETE) {
        selectProcessor(ReferenceProcessor.class)
                .deleteReference(request, response, uriInfo);
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    }
  }

  private void handleValueDispatching(final ODataRequest request, final ODataResponse response,
                                      final int lastPathSegmentIndex)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {
    final HttpMethod method = request.getMethod();
    final UriResource resource = uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1);
    if (resource instanceof UriResourceProperty) {
      final RepresentationType valueRepresentationType =
              ((UriResourceProperty) resource).getType() ==
                      EdmPrimitiveTypeFactory.getInstance(EdmPrimitiveTypeKind.Binary) ?
                      RepresentationType.BINARY : RepresentationType.VALUE;
      if (method == HttpMethod.GET) {
        final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, valueRepresentationType);

        selectProcessor(PrimitiveValueProcessor.class)
                .readPrimitiveValue(request, response, uriInfo, requestedContentType);
      } else if (method == HttpMethod.PUT) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        checkContentTypeSupport(requestFormat, valueRepresentationType);
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, valueRepresentationType);
        selectProcessor(PrimitiveValueProcessor.class)
                .updatePrimitive(request, response, uriInfo, requestFormat, responseFormat);
      } else if (method == HttpMethod.DELETE) {
        selectProcessor(PrimitiveValueProcessor.class).deletePrimitive(request, response, uriInfo);
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    } else {
      if (method == HttpMethod.GET) {
        final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.MEDIA);
        selectProcessor(MediaEntityProcessor.class)
                .readMediaEntity(request, response, uriInfo, requestedContentType);
      } else if (method == HttpMethod.PUT) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.ENTITY);
        selectProcessor(MediaEntityProcessor.class)
                .updateMediaEntity(request, response, uriInfo, requestFormat, responseFormat);
      } else if (method == HttpMethod.DELETE) {
        selectProcessor(MediaEntityProcessor.class).deleteEntity(request, response, uriInfo);
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    }
  }

  private void handleComplexPropertyDispatching(final ODataRequest request, final ODataResponse response,
                                                final boolean isAction, final boolean isCollection)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {

    final HttpMethod method = request.getMethod();
    final RepresentationType complexRepresentationType = isCollection ?
            RepresentationType.COLLECTION_COMPLEX : RepresentationType.COMPLEX;
    if (method == HttpMethod.GET) {
      final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, complexRepresentationType);
      if (complexRepresentationType == RepresentationType.COMPLEX) {
        selectProcessor(ComplexProcessor.class)
                .readComplex(request, response, uriInfo, requestedContentType);
      } else {
        selectProcessor(ComplexCollectionProcessor.class)
                .readComplexCollection(request, response, uriInfo, requestedContentType);
      }
    } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
      final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
      checkContentTypeSupport(requestFormat, complexRepresentationType);
      final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, complexRepresentationType);
      if (complexRepresentationType == RepresentationType.COMPLEX) {
        selectProcessor(ComplexProcessor.class)
                .updateComplex(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        selectProcessor(ComplexCollectionProcessor.class)
                .updateComplexCollection(request, response, uriInfo, requestFormat, responseFormat);
      }
    } else if (method == HttpMethod.POST && isAction) {
      final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
      checkContentTypeSupport(requestFormat, complexRepresentationType);
      final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, complexRepresentationType);
      if (complexRepresentationType == RepresentationType.COMPLEX) {
        selectProcessor(ActionComplexProcessor.class)
                .processActionComplex(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        selectProcessor(ActionComplexCollectionProcessor.class)
                .processActionComplexCollection(request, response, uriInfo, requestFormat, responseFormat);
      }
    } else if (method == HttpMethod.DELETE) {
      if (complexRepresentationType == RepresentationType.COMPLEX) {
        selectProcessor(ComplexProcessor.class).deleteComplex(request, response, uriInfo);
      } else {
        selectProcessor(ComplexCollectionProcessor.class).deleteComplexCollection(request, response, uriInfo);
      }
    } else {
      throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
              ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
    }
  }

  private void handlePrimitivePropertyDispatching(final ODataRequest request, final ODataResponse response,
                                                  boolean isAction, final boolean isCollection)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {

    final HttpMethod method = request.getMethod();
    final RepresentationType representationType = isCollection ?
            RepresentationType.COLLECTION_PRIMITIVE : RepresentationType.PRIMITIVE;
    if (method == HttpMethod.GET) {
      final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, representationType);
      if (representationType == RepresentationType.PRIMITIVE) {
        selectProcessor(PrimitiveProcessor.class).readPrimitive(request, response, uriInfo, requestedContentType);
      } else {
        selectProcessor(PrimitiveCollectionProcessor.class)
                .readPrimitiveCollection(request, response, uriInfo, requestedContentType);
      }
    } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
      final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
      checkContentTypeSupport(requestFormat, representationType);
      final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, representationType);
      if (representationType == RepresentationType.PRIMITIVE) {
        selectProcessor(PrimitiveProcessor.class)
                .updatePrimitive(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        selectProcessor(PrimitiveCollectionProcessor.class)
                .updatePrimitiveCollection(request, response, uriInfo, requestFormat, responseFormat);
      }
    } else if (method == HttpMethod.DELETE) {
      if (representationType == RepresentationType.PRIMITIVE) {
        selectProcessor(PrimitiveProcessor.class).deletePrimitive(request, response, uriInfo);
      } else {
        selectProcessor(PrimitiveCollectionProcessor.class).deletePrimitiveCollection(request, response, uriInfo);
      }
    } else if (method == HttpMethod.POST && isAction) {
      final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
      checkContentTypeSupport(requestFormat, representationType);
      final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
              request, customContentTypeSupport, representationType);
      if (representationType == RepresentationType.PRIMITIVE) {
        selectProcessor(ActionPrimitiveProcessor.class)
                .processActionPrimitive(request, response, uriInfo, requestFormat, responseFormat);
      } else {
        selectProcessor(ActionPrimitiveCollectionProcessor.class)
                .processActionPrimitiveCollection(request, response, uriInfo, requestFormat, responseFormat);
      }
    } else {
      throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
              ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
    }
  }

  private void handleCountDispatching(final ODataRequest request, final ODataResponse response,
                                      final int lastPathSegmentIndex)
          throws ODataApplicationException, SerializerException, ODataHandlerException {

    final HttpMethod method = request.getMethod();
    if (method == HttpMethod.GET) {
      final UriResource resource = uriInfo.getUriResourceParts().get(lastPathSegmentIndex - 1);
      if (resource instanceof UriResourceEntitySet || resource instanceof UriResourceNavigation) {
        selectProcessor(CountEntityCollectionProcessor.class)
                .countEntityCollection(request, response, uriInfo);
      } else if (resource instanceof UriResourcePrimitiveProperty) {
        selectProcessor(CountPrimitiveCollectionProcessor.class)
                .countPrimitiveCollection(request, response, uriInfo);
      } else {
        selectProcessor(CountComplexCollectionProcessor.class)
                .countComplexCollection(request, response, uriInfo);
      }
    } else {
      throw new ODataHandlerException("HTTP method " + method + " is not allowed for count.",
              ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
    }
  }

  private void handleEntityDispatching(final ODataRequest request, final ODataResponse response,
                                       final UriResourcePartTyped uriResourcePart)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {
    handleEntityDispatching(request, response, uriResourcePart.isCollection(), isMedia(uriResourcePart), false);
  }

  private void handleEntityDispatching(final ODataRequest request, final ODataResponse response,
                                       final boolean isCollection, final boolean isMedia, boolean isAction)
          throws ContentNegotiatorException, ODataApplicationException, SerializerException, ODataHandlerException,
          DeserializerException {

    final HttpMethod method = request.getMethod();
    if (isCollection) {
      if (method == HttpMethod.GET) {
        final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.COLLECTION_ENTITY);

        selectProcessor(EntityCollectionProcessor.class)
                .readEntityCollection(request, response, uriInfo, requestedContentType);
      } else if (method == HttpMethod.POST) {
          final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        if (isMedia) {
          final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, customContentTypeSupport, RepresentationType.ENTITY);
          selectProcessor(MediaEntityProcessor.class)
                  .createMediaEntity(request, response, uriInfo, requestFormat, responseFormat);
        } else if(isAction) {
          checkContentTypeSupport(requestFormat, RepresentationType.ENTITY);
          final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, customContentTypeSupport, RepresentationType.ENTITY);
          selectProcessor(ActionEntityCollectionProcessor.class)
                  .processActionEntityCollection(request, response, uriInfo, requestFormat, responseFormat);
        } else {
          checkContentTypeSupport(requestFormat, RepresentationType.ENTITY);
          final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                  request, customContentTypeSupport, RepresentationType.ENTITY);
          selectProcessor(EntityProcessor.class)
                  .createEntity(request, response, uriInfo, requestFormat, responseFormat);
        }
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    } else {
      if (method == HttpMethod.GET) {
        final ContentType requestedContentType = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.ENTITY);

        selectProcessor(EntityProcessor.class).readEntity(request, response, uriInfo, requestedContentType);
      } else if (method == HttpMethod.PUT || method == HttpMethod.PATCH) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        checkContentTypeSupport(requestFormat, RepresentationType.ENTITY);
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.ENTITY);
        selectProcessor(EntityProcessor.class).updateEntity(request, response, uriInfo, requestFormat, responseFormat);
      } else if (method == HttpMethod.POST && isAction) {
        final ContentType requestFormat = ContentType.parse(request.getHeader(HttpHeader.CONTENT_TYPE));
        checkContentTypeSupport(requestFormat, RepresentationType.ENTITY);
        final ContentType responseFormat = ContentNegotiator.doContentNegotiation(uriInfo.getFormatOption(),
                request, customContentTypeSupport, RepresentationType.ENTITY);
        selectProcessor(ActionEntityProcessor.class).processActionEntity(
                request, response, uriInfo, requestFormat, responseFormat);
      } else if (method == HttpMethod.DELETE) {
        selectProcessor(isMedia ? MediaEntityProcessor.class : EntityProcessor.class)
                .deleteEntity(request, response, uriInfo);
      } else {
        throw new ODataHandlerException("HTTP method " + method + " is not allowed.",
                ODataHandlerException.MessageKeys.HTTP_METHOD_NOT_ALLOWED, method.toString());
      }
    }
  }

  private void checkContentTypeSupport(ContentType requestFormat, RepresentationType representationType)
          throws ODataHandlerException, ContentNegotiatorException {
    if (!ContentNegotiator.isSupported(requestFormat, customContentTypeSupport, representationType)) {
      final String contentTypeString = requestFormat.toContentTypeString();
      throw new ODataHandlerException("ContentType " + contentTypeString + " is not supported.",
              ODataHandlerException.MessageKeys.UNSUPPORTED_CONTENT_TYPE, contentTypeString);
    }
  }

  private void validateODataVersion(final ODataRequest request, final ODataResponse response)
          throws ODataHandlerException {
    final String maxVersion = request.getHeader(HttpHeader.ODATA_MAX_VERSION);
    response.setHeader(HttpHeader.ODATA_VERSION, ODataServiceVersion.V40.toString());

    if (maxVersion != null) {
      if (ODataServiceVersion.isBiggerThan(ODataServiceVersion.V40.toString(), maxVersion)) {
        throw new ODataHandlerException("ODataVersion not supported: " + maxVersion,
                ODataHandlerException.MessageKeys.ODATA_VERSION_NOT_SUPPORTED, maxVersion);
      }
    }
  }

  private boolean isMedia(final UriResource pathSegment) {
    return pathSegment instanceof UriResourceEntitySet
            && ((UriResourceEntitySet) pathSegment).getEntityType().hasStream()
            || pathSegment instanceof UriResourceNavigation
            && ((EdmEntityType) ((UriResourceNavigation) pathSegment).getType()).hasStream();
  }

  private <T extends Processor> T selectProcessor(final Class<T> cls) throws ODataHandlerException {
    for (final Processor processor : processors) {
      if (cls.isAssignableFrom(processor.getClass())) {
        processor.init(odata, serviceMetadata);
        return cls.cast(processor);
      }
    }
    throw new ODataHandlerException("Processor: " + cls.getSimpleName() + " not registered.",
            ODataHandlerException.MessageKeys.PROCESSOR_NOT_IMPLEMENTED, cls.getSimpleName());
  }

  public void register(final Processor processor) {
    processors.add(0, processor);
  }

  public void register(final CustomContentTypeSupport customContentTypeSupport) {
    this.customContentTypeSupport = customContentTypeSupport;
  }
}
