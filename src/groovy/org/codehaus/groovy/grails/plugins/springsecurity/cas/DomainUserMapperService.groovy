package org.codehaus.groovy.grails.plugins.springsecurity.cas

import java.lang.reflect.Constructor

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.jasig.cas.client.authentication.AttributePrincipal
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * The User mapper is implemented to abstract out the
 * Domain model instantiation logic from the security model
 * @author Daniel Bower
 */
class DomainUserMapperService {

	GrailsApplication grailsApplication
	
	static transactional = true
	
    /**
	 * Create and save a new domain user when the user has not previously visited the app.
	 * <br>You could choose to throw a UsernameNotFoundException if you would rather the
	 * user not be created.  This should drop the user to the App's login form with an error message 
	 */
	Object newUser(String username, AttributePrincipal principal){
		def conf = SpringSecurityUtils.securityConfig
		
		Class<?> UserClass = getUserClass()
		
		def userModel
		
		//map actual values to properties:
		if(conf.cas.customUserMapping){
			//if they have implemented a constructor which accepts an AttributePrincipal
			Constructor<?> constructor = getUserClass().getConstructor(AttributePrincipal)
			userModel = constructor.newInstance(principal)
		}else{
			//if they use the same names in cas as in the user model
			Constructor<?> constructor = getUserClass().getConstructor(Map)
			userModel = constructor.newInstance(principal.getAttributes())
		}
		
		try{
			userModel.save()
		}catch(Exception e){
			throw new Exception("Unable to create and save user to the database", e)
		}
		
		return userModel
	}
	
	private Class<?> getUserClass(){
		def conf = SpringSecurityUtils.securityConfig
		String userClassName = conf.userLookup.userDomainClassName
		def dc = grailsApplication.getDomainClass(userClassName)
		if (!dc) {
			throw new RuntimeException("The specified user domain class '$userClassName' is not a domain class")
		}
		
		return dc.clazz
		
	}
	
	/**
	 * Tell the security mechanism where to find your user profiles
	 */
	Object findUserByUsername(String username){
		def conf = SpringSecurityUtils.securityConfig
		
		Class<?> UserClass = getUserClass()
		
		def userModel
		
		UserClass.withTransaction { status ->
			userModel = UserClass.findWhere((conf.userLookup.usernamePropertyName): username)
			
			if (!userModel) {
			log.warn "User not found: $username"
			throw new UsernameNotFoundException('User not found', username)
			}
		}
		
		return userModel
	}
}
