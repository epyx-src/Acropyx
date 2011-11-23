/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.acropyx

import java.util.Collection

import javax.servlet.ServletContext

import org.springframework.context.ApplicationContext
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication
import org.springframework.web.context.support.WebApplicationContextUtils

class MultiTenantVoter implements AccessDecisionVoter {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return 'MULTI_TENANT' == attribute?.attribute
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        ServletContext servletContext = object.request.getSession().getServletContext()
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext)
        def tenantResolver = ctx.tenantResolver
        def tenantId = tenantResolver.getTenantFromRequest(object.request)

        return ACCESS_ABSTAIN
    }
}
