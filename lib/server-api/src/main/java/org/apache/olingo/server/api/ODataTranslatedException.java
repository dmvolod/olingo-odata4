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
package org.apache.olingo.server.api;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.MissingFormatArgumentException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.olingo.commons.api.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract superclass of all translatable server exceptions.
 */
public abstract class ODataTranslatedException extends ODataException {

  private static final long serialVersionUID = -1210541002198287561L;
  private static final Logger LOG = LoggerFactory.getLogger(ODataTranslatedException.class);
  private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  protected static final String DEFAULT_SERVER_BUNDLE_NAME = "server-core-exceptions-i18n";

  /** Key for the exception text in the resource bundle. */
  public static interface MessageKey {
    /** Gets this key. */
    public String getKey();
  }

  private MessageKey messageKey;
  private Object[] parameters;

  protected ODataTranslatedException(String developmentMessage, MessageKey messageKey, String... parameters) {
    super(developmentMessage);
    this.messageKey = messageKey;
    this.parameters = parameters;
  }

  protected ODataTranslatedException(String developmentMessage, Throwable cause, MessageKey messageKey,
      String... parameters) {
    super(developmentMessage, cause);
    this.messageKey = messageKey;
    this.parameters = parameters;
  }

  @Override
  public String getLocalizedMessage() {
    return getTranslatedMessage(DEFAULT_LOCALE).getMessage();
  }

  @Override
  public String toString() {
    return getMessage();
  }

  /** Gets the message key. */
  public MessageKey getMessageKey() {
    return messageKey;
  }

  /**
   * Gets the translated message text for a given locale (or the default locale if not available),
   * returning the developer message text if none is found.
   * @param locale the preferred {@link Locale}
   * @return the error message
   */
  public ODataErrorMessage getTranslatedMessage(final Locale locale) {
    if (messageKey == null) {
      return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
    }
    ResourceBundle bundle = createResourceBundle(locale);
    if (bundle == null) {
      return new ODataErrorMessage(getMessage(), DEFAULT_LOCALE);
    }

    return buildMessage(bundle, locale);
  }

  /**
   * <p>Gets the name of the {@link ResourceBundle} containing the exception texts.</p>
   * <p>The key for an exception text is the concatenation of the exception-class name and
   * the {@link MessageKey}, separated by a dot.</p>
   * @return the name of the resource bundle
   */
  protected abstract String getBundleName();

  private ResourceBundle createResourceBundle(final Locale locale) {
    try {
      return ResourceBundle.getBundle(getBundleName(), locale == null ? DEFAULT_LOCALE : locale);
    } catch (final MissingResourceException e) {
      LOG.error(e.getMessage(), e);
      return null;
    }
  }

  private ODataErrorMessage buildMessage(ResourceBundle bundle, Locale locale) {
    String message = null;

    try {
      message = bundle.getString(getClass().getSimpleName() + '.' + messageKey.getKey());
      StringBuilder builder = new StringBuilder();
      Formatter f = new Formatter(builder, locale);
      f.format(message, parameters);
      f.close();
      Locale usedLocale = bundle.getLocale();
      if (Locale.ROOT.equals(usedLocale)) {
        usedLocale = DEFAULT_LOCALE;
      }
      return new ODataErrorMessage(builder.toString(), usedLocale);
    } catch (MissingResourceException e) {
      return new ODataErrorMessage("Missing message for key '" + messageKey.getKey() + "'!", DEFAULT_LOCALE);
    } catch (MissingFormatArgumentException e) {
      return new ODataErrorMessage("Missing replacement for place holder in message '" + message +
          "' for following arguments '" + Arrays.toString(parameters) + "'!", DEFAULT_LOCALE);
    }
  }

  /** Error message text and {@link Locale} used for it. */
  public class ODataErrorMessage {
    String message;
    Locale locale;

    public ODataErrorMessage(final String message, final Locale usedLocale) {
      this.message = message;
      this.locale = usedLocale;
    }

    /** Gets the message text. */
    public String getMessage() {
      return message;
    }

    /** Gets the {@link Locale} used for this message. */
    public Locale getLocale() {
      return locale;
    }
  }
}
