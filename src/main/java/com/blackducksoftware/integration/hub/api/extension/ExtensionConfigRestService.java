/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
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
 *******************************************************************************/
package com.blackducksoftware.integration.hub.api.extension;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;

import org.restlet.data.Method;

import com.blackducksoftware.integration.hub.api.HubItemRestService;
import com.blackducksoftware.integration.hub.api.HubRequest;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ExtensionConfigRestService extends HubItemRestService<ConfigurationItem> {
    private static final Type TYPE_TOKEN_ITEM = new TypeToken<ConfigurationItem>() {
    }.getType();

    private static final Type TYPE_TOKEN_LIST = new TypeToken<List<ConfigurationItem>>() {
    }.getType();

    public ExtensionConfigRestService(final RestConnection restConnection) {
        super(restConnection, TYPE_TOKEN_ITEM, TYPE_TOKEN_LIST);
    }

    public List<ConfigurationItem> getGlobalOptions(final String globalConfigUrl)
            throws IOException, URISyntaxException, BDRestException {
        final HubRequest itemRequest = new HubRequest(getRestConnection());
        itemRequest.setUrl(globalConfigUrl);
        itemRequest.setMethod(Method.GET);
        itemRequest.setLimit(100);

        final JsonObject jsonObject = itemRequest.executeForResponseJson();
        final List<ConfigurationItem> allItems = getAll(jsonObject, itemRequest);
        return allItems;
    }

    public List<ConfigurationItem> getCurrentUserOptions(final String currentUserConfigUrl)
            throws IOException, URISyntaxException, BDRestException {
        final HubRequest itemRequest = new HubRequest(getRestConnection());
        itemRequest.setUrl(currentUserConfigUrl);
        itemRequest.setMethod(Method.GET);
        itemRequest.setLimit(100);

        final JsonObject jsonObject = itemRequest.executeForResponseJson();
        final List<ConfigurationItem> allItems = getAll(jsonObject, itemRequest);
        return allItems;
    }

    public List<ConfigurationItem> getUserConfiguration(final String userConfigUrl)
            throws IOException, URISyntaxException, BDRestException {
        final HubRequest itemRequest = new HubRequest(getRestConnection());
        itemRequest.setUrl(userConfigUrl);
        itemRequest.setMethod(Method.GET);
        itemRequest.setLimit(100);

        final JsonObject jsonObject = itemRequest.executeForResponseJson();
        final List<ConfigurationItem> allItems = getAll(jsonObject, itemRequest);
        return allItems;
    }

}
