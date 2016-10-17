package com.blackducksoftware.integration.hub.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.restlet.Context;
import org.restlet.resource.ClientResource;

import com.blackducksoftware.integration.exception.EncryptionException;
import com.blackducksoftware.integration.hub.exception.BDRestException;
import com.blackducksoftware.integration.hub.global.HubProxyInfo;
import com.blackducksoftware.integration.hub.global.HubServerConfig;

public class CredentialsRestConnection extends RestConnection {

	public CredentialsRestConnection(final HubServerConfig hubServerConfig)
			throws IllegalArgumentException, URISyntaxException, BDRestException, EncryptionException {
		super();
		setBaseUrl(hubServerConfig.getHubUrl().toString());
		final HubProxyInfo proxyInfo = hubServerConfig.getProxyInfo();
		if (proxyInfo.shouldUseProxyForUrl(hubServerConfig.getHubUrl())) {
			setProxyProperties(proxyInfo);
		}
		setTimeout(hubServerConfig.getTimeout());
		final String username = hubServerConfig.getGlobalCredentials().getUsername();
		String password = hubServerConfig.getGlobalCredentials().getEncryptedPassword();
		if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
			password = hubServerConfig.getGlobalCredentials().getDecryptedPassword();
			setCookies(username, password);
		}
	}

	@Override
	public ClientResource createClientResource(final Context context, final String providedUrl)
			throws URISyntaxException {
		return new ClientResource(context, new URI(providedUrl));
	}
}
