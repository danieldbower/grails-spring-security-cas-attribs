package org.codehaus.groovy.grails.plugins.springsecurity.cas

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class DefaultUserDetailsFromDomainClassFactory implements
		UserDetailsFromDomainClassFactory {

	/**
	 * When using cas, the password attribute of the User object means nothing.
	 */
	private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";

	@Override
	UserDetails createUserDetails(Object domainClass,
			Collection<GrantedAuthority> authorities) {

		def conf = SpringSecurityUtils.securityConfig

		String usernamePropertyName = conf.userLookup.usernamePropertyName
		String username = domainClass."$usernamePropertyName"

		new GrailsUser(username, NON_EXISTENT_PASSWORD_VALUE,
				true, true, true, true, authorities, domainClass.id)
	}
}
