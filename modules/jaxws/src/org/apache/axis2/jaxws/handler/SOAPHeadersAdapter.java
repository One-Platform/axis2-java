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

package org.apache.axis2.jaxws.handler;

import org.apache.axis2.jaxws.Constants;
import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.axis2.jaxws.core.MessageContext;
import org.apache.axis2.jaxws.message.Block;
import org.apache.axis2.jaxws.message.Message;
import org.apache.axis2.jaxws.message.factory.XMLStringBlockFactory;
import org.apache.axis2.jaxws.registry.FactoryRegistry;
import org.apache.axis2.jaxws.utility.JavaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The JAX-WS exposes soap header properties whose value is Map<QName, List<String>>.  The
 * QName is the name of the header and List<String> are the xml values of the headers for qname.
 * 
 * The JAX-WS MessageContext stores soap headers in an Axiom tree object located on the JAX-WS 
 * Message.
 * 
 * This class, SOAPHeadersAdapter, is an adapter between the Map<QName, List<String>> interface needed
 * by the properties and the actual implementation.  All useful function is delegated through the MessageContext, so 
 * that we only have one copy of the soap header information.  
 * 
 * To use this class, invoke the install method.  This will create an SOAPHeaderAdapter (if necessary) and install it
 * on to provide the JAX-WS soap headers property.  (See BaseMessageContext.)
 */
public class SOAPHeadersAdapter implements Map<QName, List<String>> {

    private static final Log log = LogFactory.getLog(SOAPHeadersAdapter.class);
    
    private MessageContext mc;     // MessageContext which provides the backing implementation of Attachments
    private boolean isOutbound;    // IsOutbound
    private String property;
    
    /**
     * Add the AttachmentAdapter as the property for the inbound and/or
     * outbound attachment property
     * @param mc MessageContext
     */
    public static void install(MessageContext mc) {
        
        Message m = mc.getMessage();
        
        if (m == null) {
            // Probably a unit test, can't continue.
            return;
        }
        
        boolean isOutbound = mc.isOutbound();
       
        if (log.isTraceEnabled()) {
            log.trace("Installing SOAPHeadersAdapter: " +
                      JavaUtils.callStackToString());
        }
        
        String property = (isOutbound) ? 
                Constants.JAXWS_OUTBOUND_SOAP_HEADERS :
                    Constants.JAXWS_INBOUND_SOAP_HEADERS;
        
        // See if there is an existing map
        Object map = mc.getProperty(property);

        // Reuse existing Adapter
        if (map instanceof SOAPHeadersAdapter) {
            if (log.isDebugEnabled()) {
                log.debug("A SOAPHeadersAdapter is already installed.  Reusing the existing one.");
            }
            return;
        } 
      
        // Create a new AttachmentsAdapter and set it on the property 
        SOAPHeadersAdapter sha = 
            new SOAPHeadersAdapter(mc, 
                                   isOutbound);

        if (map != null) {
            // Copy the existing Map contents to this new adapter
            sha.putAll((Map<QName, List<String>>) map);
        }
        mc.setProperty(property, sha);
    }

    /**
     * The backing storage of the Attachments is the JAX-WS MessageContext.
     * Intentionally private, use install(MessageContext)
     */
    private SOAPHeadersAdapter(MessageContext mc, 
                               boolean isOutbound) {
        this.mc = mc;
        this.isOutbound = isOutbound;
        this.property = (isOutbound) ? 
                    Constants.JAXWS_OUTBOUND_SOAP_HEADERS :
                    Constants.JAXWS_INBOUND_SOAP_HEADERS;
        
        if (log.isDebugEnabled()) {
            log.debug("Init SOAPHeadersAdapter for " + property);
        }
    }
    

    public void clear() {
        // Throw unsupported operation exception per Map javadoc
        // for any method that is not supported.
        throw new UnsupportedOperationException();
    }
    
    public boolean containsKey(Object key) {
        Set<QName> keys = this.keySet();
        return keys.contains(key);
    }
    
    public boolean containsValue(Object value) {
        Set<QName> keys = this.keySet();
        for(QName key: keys) {
            List<String> tryValue = get(key);

            if (tryValue == value ||
                value.equals(tryValue)) {
                return true;
            }
        }
        return false;
    }
    
    public Set<Entry<QName, List<String>>> entrySet() {
        Map<QName, List<String>> tempMap = new HashMap<QName, List<String>>();
        tempMap.putAll(this);
        return tempMap.entrySet();
    }
    
    public List<String> get(Object _key) {
        try {
            if (!(keySet().contains(_key))) {
                return null;
            }
            QName key = (QName) _key;
            Message m = mc.getMessage();
            List<Block> blocks = m.getHeaderBlocks(key.getNamespaceURI(), 
                                                   key.getLocalPart(),
                                                   null,
                                                   getXMLStringBlockFactory(),
                                                   null);
            if (blocks == null || blocks.size() == 0) {
                return null;
            }
            
            // Get the strings from the blocks
            ArrayList<String> xmlStrings = new ArrayList<String>();
            for (int i=0; i<blocks.size(); i++) {
                Block block = blocks.get(i);
                String value = (block == null) ? null : (String) block.getBusinessObject(false);
                xmlStrings.add(value);
            }
            
            return xmlStrings;
        } catch (Throwable t) {
            throw ExceptionFactory.makeWebServiceException(t);
        }
    }
    
    public boolean isEmpty() {
        return this.keySet().isEmpty();
    }
    
    public Set<QName> keySet() {
        Message m = mc.getMessage();
        return m.getHeaderQNames();
    }
    
    public List<String> put(QName key, List<String> values) {
        Message m = mc.getMessage();
        if (log.isDebugEnabled()) {
            log.debug("put(" + key + " , " + values + ")");
        }
        // Get the old value
        List<String> old = get(key);
        
        if (values != null) {
            if (old != null) {
                // Replace the existing header blocks
                m.removeHeaderBlock(key.getNamespaceURI(), key.getLocalPart());
            }
            for (int i=0; i < values.size(); i++) {
                String value = values.get(i);
                Block block = getXMLStringBlockFactory().createFrom(value, null, key);
                m.appendHeaderBlock(key.getNamespaceURI(), key.getLocalPart(), block);
            }
        }
        
        return old;
        
    }
    
    public void putAll(Map<? extends QName, ? extends List<String>> t) {
        for(Entry<? extends QName, ? extends List<String>> entry: t.entrySet()) {
            QName key = entry.getKey();
            List<String> value = entry.getValue();  
            put(key, value);
        }
    }
    
    public List<String> remove(Object key) {
        // Throw unsupported operation exception per Map javadoc
        // for any method that is not supported.
        throw new UnsupportedOperationException();
    }
    
    public int size() {
        return this.keySet().size();
    }
    
    public Collection<List<String>> values() {
        Map<QName, List<String>> tempMap = new HashMap<QName, List<String>>();
        tempMap.putAll(this);
        return tempMap.values();
    }
    
    private XMLStringBlockFactory getXMLStringBlockFactory() {
        return (XMLStringBlockFactory)
            FactoryRegistry.getFactory(XMLStringBlockFactory.class);
    }

}
