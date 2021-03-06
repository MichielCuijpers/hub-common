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
package com.blackducksoftware.integration.hub;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.hub.capabilities.HubCapabilitiesEnum;
import com.blackducksoftware.integration.hub.exception.HubIntegrationException;
import com.blackducksoftware.integration.log.IntLogger;

public abstract class ScanExecutor {
    public static final int DEFAULT_MEMORY = 4096;

    public static enum Result {
        SUCCESS, FAILURE;
    }

    private final String hubUrl;

    private final String hubUsername;

    private final String hubPassword;

    private final List<String> scanTargets;

    private final String buildIdentifier;

    private final HubSupportHelper supportHelper;

    private int scanMemory;

    private IntLogger logger;

    private String project;

    private String version;

    private String workingDirectory;

    private String proxyHost;

    private int proxyPort;

    private List<Pattern> noProxyHosts;

    private String proxyUsername;

    private String proxyPassword;

    private boolean verboseRun;

    private boolean dryRun;

    protected ScanExecutor(final String hubUrl, final String hubUsername, final String hubPassword,
            final List<String> scanTargets, final String buildIdentifier, final HubSupportHelper supportHelper) {
        if (StringUtils.isBlank(hubUrl)) {
            throw new IllegalArgumentException("No Hub URL provided.");
        }
        if (StringUtils.isBlank(hubUsername)) {
            throw new IllegalArgumentException("No Hub username provided.");
        }
        if (StringUtils.isBlank(hubPassword)) {
            throw new IllegalArgumentException("No Hub password provided.");
        }
        if (scanTargets == null || scanTargets.isEmpty()) {
            throw new IllegalArgumentException("No scan targets provided.");
        }
        if (buildIdentifier == null) {
            throw new IllegalArgumentException("No build identifier provided.");
        }
        if (supportHelper == null) {
            throw new IllegalArgumentException("No HubSupportHelper provided.");
        } else if (!supportHelper.isHasBeenChecked()) {
            throw new IllegalArgumentException("The HubSupportHelper has not been checked yet.");
        }
        this.hubUrl = hubUrl;
        this.hubUsername = hubUsername;
        this.hubPassword = hubPassword;
        this.scanTargets = scanTargets;
        this.buildIdentifier = buildIdentifier;
        this.supportHelper = supportHelper;
    }

    public IntLogger getLogger() {
        return logger;
    }

    public void setLogger(final IntLogger logger) {
        this.logger = logger;
    }

    public List<String> getScanTargets() {
        return scanTargets;
    }

    public Integer getScanMemory() {
        return scanMemory;
    }

    public void setScanMemory(final Integer scanMemory) {
        this.scanMemory = scanMemory;
    }

    public String getProject() {
        return project;
    }

    public void setProject(final String project) {
        this.project = project;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(final String version) {
        this.version = version;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(final String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public String getHubUrl() {
        return hubUrl;
    }

    public String getBuildIdentifier() {
        return buildIdentifier;
    }

    public String getHubUsername() {
        return hubUsername;
    }

    public String getHubPassword() {
        return hubPassword;
    }

    public boolean isVerboseRun() {
        return verboseRun;
    }

    public void setVerboseRun(final boolean verboseRun) {
        this.verboseRun = verboseRun;
    }

    public boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(final boolean dryRun) {
        this.dryRun = dryRun;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(final Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public List<Pattern> getNoProxyHosts() {
        return noProxyHosts;
    }

    public void setNoProxyHosts(final List<Pattern> noProxyHosts) {
        this.noProxyHosts = noProxyHosts;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(final String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(final String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    protected boolean isConfiguredCorrectly(final String scanExec, final String oneJarPath, final String javaExec) {
        if (getLogger() == null) {
            System.out.println("Could not find a logger");
            return false;
        }

        if (scanExec == null) {
            getLogger().error("Please provide the Hub scan CLI.");
            return false;
        } else {
            final File scanExecFile = new File(scanExec);
            if (!scanExecFile.exists()) {

                getLogger().error("The Hub scan CLI provided does not exist.");
                return false;
            }
        }

        if (oneJarPath == null) {
            getLogger().error("Please provide the path for the CLI cache.");
            return false;
        }

        if (javaExec == null) {
            getLogger().error("Please provide the java home directory.");
            return false;
        } else {
            final File javaExecFile = new File(javaExec);
            if (!javaExecFile.exists()) {
                getLogger().error("The Java executable provided does not exist at : " + javaExecFile.getAbsolutePath());
                return false;
            }
        }

        if (scanMemory <= 0) {
            getLogger().error("No memory set for the HUB CLI. Will use the default memory, " + DEFAULT_MEMORY);
            setScanMemory(DEFAULT_MEMORY);
        }

        return true;
    }

    public Result setupAndRunScan(final String scanExec, final String oneJarPath, final String javaExec)
            throws HubIntegrationException {
        if (isConfiguredCorrectly(scanExec, oneJarPath, javaExec)) {

            try {

                final URL url = new URL(getHubUrl());
                final List<String> cmd = new ArrayList<>();

                final String javaPath = javaExec;

                getLogger().debug("Using this java installation : " + javaPath);

                cmd.add(javaPath);
                cmd.add("-Done-jar.silent=true");
                cmd.add("-Done-jar.jar.path=" + oneJarPath);

                if (StringUtils.isNotBlank(getProxyHost()) && getProxyPort() != null) {
                    cmd.add("-Dhttp.proxyHost=" + getProxyHost());
                    cmd.add("-Dhttp.proxyPort=" + getProxyPort());

                    if (getNoProxyHosts() != null) {
                        final StringBuilder noProxyHosts = new StringBuilder();

                        for (final Pattern pattern : getNoProxyHosts()) {
                            if (noProxyHosts.length() > 0) {
                                noProxyHosts.append("|");
                            }
                            noProxyHosts.append(pattern.toString());
                        }
                        cmd.add("-Dhttp.nonProxyHosts=" + noProxyHosts.toString());
                    }
                    if (StringUtils.isNotBlank(getProxyUsername()) && StringUtils.isNotBlank(getProxyPassword())) {
                        cmd.add("-Dhttp.proxyUser=" + getProxyUsername());
                        cmd.add("-Dhttp.proxyPassword=" + getProxyPassword());
                    }
                }

                cmd.add("-Xmx" + scanMemory + "m");

                cmd.add("-jar");
                cmd.add(scanExec);
                cmd.add("--scheme");
                cmd.add(url.getProtocol());
                cmd.add("--host");
                cmd.add(url.getHost());
                getLogger().debug("Using this Hub hostname : '" + url.getHost() + "'");
                cmd.add("--username");
                cmd.add(getHubUsername());
                if (!supportHelper.hasCapability(HubCapabilitiesEnum.CLI_PASSWORD_ENVIRONMENT_VARIABLE)) {
                    cmd.add("--password");
                    cmd.add(getHubPassword());
                }

                if (url.getPort() != -1) {
                    cmd.add("--port");
                    cmd.add(Integer.toString(url.getPort()));
                } else {
                    if (url.getDefaultPort() != -1) {
                        cmd.add("--port");
                        cmd.add(Integer.toString(url.getDefaultPort()));
                    } else {
                        getLogger().warn("Could not find a port to use for the Server.");
                    }

                }

                if (isVerboseRun()) {
                    cmd.add("-v");
                }

                final String logDirectoryPath = getLogDirectoryPath();
                cmd.add("--logDir");

                cmd.add(logDirectoryPath);

                if (isDryRun()) {
                    // The dryRunWriteDir is the same as the log directory path
                    // The CLI will create a subdirectory for the json files
                    cmd.add("--dryRunWriteDir");
                    cmd.add(getLogDirectoryPath());
                }

                if (supportHelper.hasCapability(HubCapabilitiesEnum.CLI_STATUS_DIRECTORY_OPTION)) {
                    // Only add the statusWriteDir option if the Hub supports
                    // the statusWriteDir option

                    // The scanStatusDirectoryPath is the same as the log
                    // directory path
                    // The CLI will create a subdirectory for the status files
                    final String scanStatusDirectoryPath = getLogDirectoryPath();

                    cmd.add("--statusWriteDir");

                    cmd.add(scanStatusDirectoryPath);
                }

                if (StringUtils.isNotBlank(getProject()) && StringUtils.isNotBlank(getVersion())) {
                    cmd.add("--project");

                    cmd.add(getProject());

                    cmd.add("--release");

                    cmd.add(getVersion());
                }

                for (final String target : scanTargets) {
                    cmd.add(target);
                }

                return executeScan(cmd, logDirectoryPath);

            } catch (final MalformedURLException e) {
                throw new HubIntegrationException("The server URL provided was not a valid", e);
            } catch (final IOException e) {
                throw new HubIntegrationException(e.getMessage(), e);
            } catch (final InterruptedException e) {
                throw new HubIntegrationException(e.getMessage(), e);
            }
        } else {
            return Result.FAILURE;
        }
    }

    /**
     * Should determine the path to the log directory to pass into the CLI. If
     * the directory does not exist it should be created here.
     *
     */
    protected String getLogDirectoryPath() throws IOException {
        final File logDirectory = getLogDirectory();
        return logDirectory.getCanonicalPath();
    }

    /**
     * Should determine the log directory to pass into the CLI. If the directory
     * does not exist it should be created here.
     *
     */
    protected File getLogDirectory() throws IOException {
        final File logDirectory = new File(new File(getWorkingDirectory(), "HubScanLogs"),
                String.valueOf(getBuildIdentifier()));
        // This log directory should never exist as a new one is created for
        // each Build
        if (!logDirectory.exists() && !logDirectory.mkdirs()) {
            throw new IOException("Could not create the HubScanLogs directory!");
        }

        return logDirectory;
    }

    /**
     * Should determine the path to the scan status directory within the log
     * directory. This should only be used outside of this class to get the path
     * of the status directory
     *
     */
    public String getScanStatusDirectoryPath() throws IOException {
        File scanStatusDirectory = getLogDirectory();
        scanStatusDirectory = new File(scanStatusDirectory, "status");
        return scanStatusDirectory.getCanonicalPath();
    }

    protected abstract Result executeScan(List<String> cmd, String logDirectoryPath)
            throws HubIntegrationException, InterruptedException;

}
