import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.cas.CasAuthenticationUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.cas.DefaultUserDetailsFromDomainClassFactory
import org.codehaus.groovy.grails.plugins.springsecurity.cas.DomainUserMapperService
import org.jasig.cas.client.validation.Saml11TicketValidator

class SpringSecurityCasAttribsGrailsPlugin {
	String version = '1.1.1'
	String grailsVersion = '1.2.3 > *'
	def dependsOn = ['springSecurityCas': '1.0 > *']
	def pluginExcludes = [
		'web-app/**'
	]
	def author = "Daniel Bower"
	def authorEmail = "daniel.d.bower@gmail.com"
	def title = "Spring Security Cas with Cas Attribs"
	def description = '''\
Allows Grails to obtain authorities directly from CAS.
Also allows Grails to create the user's profile from attributes in CAS in the app if they do not already exist.
At this time, ignores local role table.
'''

	def documentation = "http://grails.org/plugin/spring-security-cas-attribs"

	def doWithSpring = {

		def conf = SpringSecurityUtils.securityConfig

		if(conf.cas.userAttribsFromCas){
			casTicketValidator(Saml11TicketValidator, conf.cas.serverUrlPrefix) {
				renew = conf.cas.sendRenew // false
			}

			domainUserMapperService(DomainUserMapperService)

			userDetailsFromDomainClassFactory(DefaultUserDetailsFromDomainClassFactory)

			/*
			 * replacement authenticationUserDetailsService - overwrites the one in  spring-security-core
			 */
			authenticationUserDetailsService(CasAuthenticationUserDetailsService){
				authorityAttribNamesFromCas = conf.cas.authorityAttribNamesFromCas
				userMapper = ref('domainUserMapperService')
				userDetailsFromDomainClassFactory = ref('userDetailsFromDomainClassFactory')
			}
		}

	}
}
