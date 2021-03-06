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
package com.blackducksoftware.integration.hub.api.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;

import com.blackducksoftware.integration.hub.exception.UnexpectedHubResponseException;
import com.blackducksoftware.integration.hub.meta.MetaInformation;
import com.blackducksoftware.integration.hub.meta.MetaLink;

/**
 * An Item (project, notification, etc.) as returned from the Hub.
 *
 * @author sbillings
 *
 */
public class HubItem {
    private final MetaInformation _meta;

    public HubItem(final MetaInformation meta) {
        this._meta = meta;
    }

    public MetaInformation getMeta() {
        return _meta;
    }

    public String getLink(final String linkRel) throws UnexpectedHubResponseException {
        final List<String> links = getLinks(linkRel);
        if (links.size() != 1) {
            final String combinedLinks = StringUtils.join(links, ", ");
            throw new UnexpectedHubResponseException(
                    String.format("1 link was expected, but found %s: %s", links.size(), combinedLinks));
        }

        return links.get(0);
    }

    public List<String> getLinks(final String linkRel) {
        final List<String> links = new ArrayList<>();
        if (linksExist()) {
            for (final MetaLink link : getLinks()) {
                if (isRequestedLink(linkRel, link)) {
                    links.add(link.getHref());
                }
            }
        }
        return links;
    }

    private List<MetaLink> getLinks() {
        return getMeta().getLinks();
    }

    private boolean isRequestedLink(final String linkRel, final MetaLink link) {
        return link.getRel().equalsIgnoreCase(linkRel);
    }

    private boolean linksExist() {
        return getMeta() != null && getLinks() != null && !getLinks().isEmpty();
    }

    public DateTime getDateTime(final String time) {
        if (StringUtils.isBlank(time)) {
            return null;
        }
        try {
            return DateTime.parse(time);
        } catch (final IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, RecursiveToStringStyle.JSON_STYLE);
    }

}
