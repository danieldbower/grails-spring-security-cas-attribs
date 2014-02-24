package org.codehaus.groovy.grails.plugins.springsecurity.cas

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Implement this class to turn a domain class into a custom UserDetails Object.
 *
 * The CasAuthenticationUserDetailsService will call this class with the user's
 * domain class object and authorities from CAS.
 *
 * @author daniel.d.bower
 */
interface UserDetailsFromDomainClassFactory {
	UserDetails createUserDetails(domainClass, Collection<GrantedAuthority> authorities)
}
