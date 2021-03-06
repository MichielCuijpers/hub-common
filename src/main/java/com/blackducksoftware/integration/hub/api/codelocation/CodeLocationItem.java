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
package com.blackducksoftware.integration.hub.api.codelocation;

import java.util.Date;

import com.blackducksoftware.integration.hub.api.item.HubItem;
import com.blackducksoftware.integration.hub.meta.MetaInformation;

public class CodeLocationItem extends HubItem {
    private final CodeLocationTypeEnum type;

    private final String url;

    private final String mappedProjectVersion;

    private final Date createdAt;

    private final Date updatedAt;

    public CodeLocationItem(final MetaInformation meta, final CodeLocationTypeEnum type, final String url,
            final String mappedProjectVersion, final Date createdAt, final Date updatedAt) {
        super(meta);
        this.type = type;
        this.url = url;
        this.mappedProjectVersion = mappedProjectVersion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CodeLocationTypeEnum getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getMappedProjectVersion() {
        return mappedProjectVersion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
        result = prime * result + ((mappedProjectVersion == null) ? 0 : mappedProjectVersion.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeLocationItem other = (CodeLocationItem) obj;
        if (createdAt == null) {
            if (other.createdAt != null) {
                return false;
            }
        } else if (!createdAt.equals(other.createdAt)) {
            return false;
        }
        if (mappedProjectVersion == null) {
            if (other.mappedProjectVersion != null) {
                return false;
            }
        } else if (!mappedProjectVersion.equals(other.mappedProjectVersion)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        if (updatedAt == null) {
            if (other.updatedAt != null) {
                return false;
            }
        } else if (!updatedAt.equals(other.updatedAt)) {
            return false;
        }
        if (url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CodeLocationItem [type=" + type + ", url=" + url + ", mappedProjectVersion=" + mappedProjectVersion
                + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
