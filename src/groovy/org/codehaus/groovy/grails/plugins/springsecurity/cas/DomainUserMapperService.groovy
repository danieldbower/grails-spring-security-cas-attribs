package org.codehaus.groovy.grails.plugins.springsecurity.cas

import java.lang.reflect.Constructor

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.jasig.cas.client.authentication.AttributePrincipal
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * The User mapper is implemented to abstract out the
 * Domain model instantiation logic from the security model
 * @author daniel.d.bower
 */
class DomainUserMapperService {

	GrailsApplication grailsApplication
	
	static transactional = true
	
	/**
	 * Create and save a new domain user when the user has not previously visited the app. 
	 */
	Object newUser(String username, AttributePrincipal principal){
		Class<?> UserClass = getUserClass()
		
		def userModel
		
		//map actual values to properties:
		Constructor<?> constructor
		try{
			// see if implemented a constructor which accepts an AttributePrincipal
			constructor = getUserClass().getConstructor([AttributePrincipal] as Class[])
			userModel = constructor.newInstance(principal)
		}catch(NoSuchMethodException nsme){
			//if they use the same names in cas as in the user model
			constructor = getUserClass().getConstructor([Map] as Class[])
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
	 * Where to find user profiles
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
