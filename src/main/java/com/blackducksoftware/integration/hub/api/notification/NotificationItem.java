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
package com.blackducksoftware.integration.hub.api.notification;

import java.util.Date;

import com.blackducksoftware.integration.hub.api.item.HubItem;
import com.blackducksoftware.integration.hub.meta.MetaInformation;

public class NotificationItem extends HubItem {
    public String contentType;

    public NotificationType type;

    public Date createdAt;

    public NotificationItem(final MetaInformation meta) {
        super(meta);
    }

    public String getContentType() {
        return contentType;
    }

    public NotificationType getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public void setType(final NotificationType type) {
        this.type = type;
    }

    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "NotificationItem [contentType=" + contentType + ", type=" + type + ", createdAt=" + createdAt
                + ", Meta=" + getMeta() + "]";
    }

}
