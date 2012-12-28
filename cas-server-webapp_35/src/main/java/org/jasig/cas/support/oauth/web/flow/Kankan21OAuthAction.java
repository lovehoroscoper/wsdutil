/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.support.oauth.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.support.oauth.OAuthConfiguration;
import org.jasig.cas.support.oauth.OAuthConstants;
import org.jasig.cas.support.oauth.OAuthUtils;
import org.jasig.cas.support.oauth.authentication.principal.OAuthCredentials;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.web.support.WebUtils;
import org.scribe.up.credential.OAuthCredential;
import org.scribe.up.provider.BaseOAuth10Provider;
import org.scribe.up.provider.BaseOAuthProvider;
import org.scribe.up.provider.OAuthProvider;
import org.scribe.up.session.HttpUserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public final class Kankan21OAuthAction extends AbstractAction {
    
    private static final Logger logger = LoggerFactory.getLogger(OAuthAction.class);
    
    @NotNull
    private OAuthConfiguration configuration;
    
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;
    
    private String oauth10loginUrl = "/" + OAuthConstants.OAUTH10_LOGIN_URL;
    
    @Override
    protected Event doExecute(final RequestContext context) throws Exception {
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        final HttpSession session = request.getSession();
        
        // get provider type
        final String providerType = request.getParameter(OAuthConstants.OAUTH_PROVIDER);
        logger.debug("providerType : {}", providerType);
        
        // it's an authentication
        if (StringUtils.isNotBlank(providerType)) {
        	
        	//1、请求登录：https://api.weibo.com/oauth2/authorize
        	//2、登录后返回 http://www.example.com/response&code=CODE
        	
            // get provider
            final OAuthProvider provider = OAuthUtils
                .getProviderByType(this.configuration.getProviders(), providerType);
            logger.debug("provider : {}", provider);
            
            // 这里面或到code 和         this.verifier-->code ; this.providerType-->GitHubProvider;
            // get credential
            @SuppressWarnings("unchecked")
            final OAuthCredential credential = provider.getCredential(new HttpUserSession(request),
                                                                      request.getParameterMap());
            logger.debug("credential : {}", credential);
            
            // retrieve parameters from web session
            final Service service = (Service) session.getAttribute(OAuthConstants.SERVICE);
            context.getFlowScope().put(OAuthConstants.SERVICE, service);
            restoreRequestAttribute(request, session, OAuthConstants.THEME);
            restoreRequestAttribute(request, session, OAuthConstants.LOCALE);
            restoreRequestAttribute(request, session, OAuthConstants.METHOD);
            
            // create credentials
            final Credentials credentials = new OAuthCredentials(credential);
            
            try {
                WebUtils.putTicketGrantingTicketInRequestScope(context, this.centralAuthenticationService
                    .createTicketGrantingTicket(credentials));
                return success();
            } catch (final TicketException e) {
                return error();
            }
        } else {
            // no authentication : go to login page
            
            // save parameters in web session
            final Service service = (Service) context.getFlowScope().get(OAuthConstants.SERVICE);
            if (service != null) {
                session.setAttribute(OAuthConstants.SERVICE, service);
            }
            saveRequestParameter(request, session, OAuthConstants.THEME);
            saveRequestParameter(request, session, OAuthConstants.LOCALE);
            saveRequestParameter(request, session, OAuthConstants.METHOD);
            
            // for all providers, generate authorization urls
            for (final OAuthProvider provider : this.configuration.getProviders()) {
                final String key = provider.getType() + "Url";
                String authorizationUrl = null;
                // for OAuth 1.0 protocol, delay request_token request by pointing to an intermediate url
                if (provider instanceof BaseOAuth10Provider) {
                    authorizationUrl = OAuthUtils.addParameter(request.getContextPath() + this.oauth10loginUrl,
                                                               OAuthConstants.OAUTH_PROVIDER, provider.getType());
                } else {
                    authorizationUrl = provider.getAuthorizationUrl(new HttpUserSession(session));
                }
                logger.debug("{} -> {}", key, authorizationUrl);
                context.getFlowScope().put(key, authorizationUrl);
            }
        }
        
        return error();
    }
    
    /**
     * Restore an attribute in web session as an attribute in request.
     * 
     * @param request
     * @param session
     * @param name
     */
    private void restoreRequestAttribute(final HttpServletRequest request, final HttpSession session, final String name) {
        final String value = (String) session.getAttribute(name);
        request.setAttribute(name, value);
    }
    
    /**
     * Save a request parameter in the web session.
     * 
     * @param request
     * @param session
     * @param name
     */
    private void saveRequestParameter(final HttpServletRequest request, final HttpSession session, final String name) {
        final String value = request.getParameter(name);
        if (value != null) {
            session.setAttribute(name, value);
        }
    }
    
    public void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
    
    public void setOauth10loginUrl(final String oauth10loginUrl) {
        this.oauth10loginUrl = oauth10loginUrl;
    }
    
    public void setConfiguration(final OAuthConfiguration configuration) {
        this.configuration = configuration;
        for (final OAuthProvider provider : configuration.getProviders()) {
            final BaseOAuthProvider baseProvider = (BaseOAuthProvider) provider;
            // calculate new callback url by adding the OAuth provider type to the login url
            baseProvider.setCallbackUrl(OAuthUtils.addParameter(configuration.getLoginUrl(),
                                                                OAuthConstants.OAUTH_PROVIDER, provider.getType()));
        }
    }
}
