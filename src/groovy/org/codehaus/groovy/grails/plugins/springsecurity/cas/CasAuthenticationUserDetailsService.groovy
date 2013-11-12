package org.codehaus.groovy.grails.plugins.springsecurity.cas

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.jasig.cas.client.validation.Assertion
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService
import org.springframework.security.cas.userdetails.GrantedAuthorityFromAssertionAttributesUserDetailsService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails

/**
 *  Allows the Authorities to be brought in from the CAS Assertion
 * @author daniel.d.bower
 */
class CasAuthenticationUserDetailsService extends
	AbstractCasAssertionUserDetailsService {

	/**
	 * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least one role, so
	 * we give a user with no granted roles this one which gets past that restriction but
	 * doesn't grant anything.
	 */
	private static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]

	/**
	 * Should we check for and pull apart Authorities that have been concatenated together? - seems to happen in the cas client lib
	 */
	Boolean CONCATENATED_AUTHORITIES_CHECK = true

	private GrantedAuthorityFromAssertionAttributesUserDetailsService grantedAuthoritiesService
	/**
	 * Dependency injection for Getting Authorities from CAS
	 * Provide the attribute name/s that will contain role information from cas
	 */
	void setAuthorityAttribNamesFromCas(authorityAttribNamesFromCas){
		grantedAuthoritiesService = new GrantedAuthorityFromAssertionAttributesUserDetailsService(
			authorityAttribNamesFromCas as String[])
	}

	/** Dependency injection for creating and finding Users **/
	DomainUserMapperService userMapper

	/** Dependency injection for creating userDetails objects **/
	UserDetailsFromDomainClassFactory userDetailsFromDomainClassFactory

	@Override
	protected UserDetails loadUserDetails(Assertion casAssert) {
		//look up user profile in database
		def user = userMapper.findUserByUsername(casAssert.getPrincipal().getName())

		//Create the user profile if it does not already exist
		if(!user){
			user = userMapper.newUser(casAssert.getPrincipal().getName(), casAssert.principal)
		}

		//authorities
		def casUser = grantedAuthoritiesService.loadUserDetails(casAssert)
		def casAuthorities = []

		casUser.authorities.each{ authBundle ->

			//check to see if some authorities were concatenated together
			if(CONCATENATED_AUTHORITIES_CHECK && authBundle.authority.matches(/^\[.*\]$/)){
				def concattedAuths = authBundle.authority
				//strip off braces
				concattedAuths = concattedAuths[1..(concattedAuths.size()-2)]

				//remove commas and add authorities
				concattedAuths.tokenize(",").each{
					casAuthorities.add(new GrantedAuthorityImpl(it.stripIndent()))
				}

			}else{
				casAuthorities.add(authBundle)
			}

		}

		if (!casAuthorities) casAuthorities=NO_ROLES

		return userDetailsFromDomainClassFactory.createUserDetails(user, casAuthorities)
	}

}
