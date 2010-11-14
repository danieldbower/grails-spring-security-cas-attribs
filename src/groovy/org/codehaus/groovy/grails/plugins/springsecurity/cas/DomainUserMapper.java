package org.codehaus.groovy.grails.plugins.springsecurity.cas;

import org.jasig.cas.client.authentication.AttributePrincipal;

/**
 * The User mapper is implemented to abstract out the 
 * Domain model instantiation logic from the security model
 * @author Daniel Bower
 */
public interface DomainUserMapper {
	/**
	 * Create and save a new domain user when the user has not previously visited the app.
	 * <br>You could choose to throw a UsernameNotFoundException if you would rather the
	 * user not be created.  This should drop the user to the App's login form with an error message 
	 */
	Object newUser(String username, AttributePrincipal principal);
	
	/**
	 * Tell the security mechanism where to find your user profiles
	 */
	Object findUserByUsername(String username);
}
