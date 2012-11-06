import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.cas.CasAuthenticationUserDetailsService
import org.codehaus.groovy.grails.plugins.springsecurity.cas.DomainUserMapperService
import org.jasig.cas.client.proxy.Cas20ProxyRetriever
import org.jasig.cas.client.validation.Saml11TicketValidator

class SpringSecurityCasAttribsGrailsPlugin {
    // the plugin version
    String version = '1.1.0'
	String grailsVersion = '1.2.3 > *'
    // the other plugins this plugin depends on
    def dependsOn = ['springSecurityCas': '1.0 > *']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Daniel Bower"
    def authorEmail = "daniel.d.bower@gmail.com"
    def title = "Spring Security Cas with Cas Attribs"
    def description = '''\\
Allows Grails to obtain authorities directly from CAS.  
Also allows grails to create the user's profile from attributes in CAS in the app if they do not already exist.  
At this time, ignores local role table.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/spring-security-cas-attribs"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)

		def conf = SpringSecurityUtils.securityConfig

		if(conf.cas.userAttribsFromCas){
			casTicketValidator(Saml11TicketValidator, conf.cas.serverUrlPrefix) {
				renew = conf.cas.sendRenew // false
			}
			
			domainUserMapperService(DomainUserMapperService)

			/*
			 * replacement authenticationUserDetailsService - overwrites the one in  spring-security-core
			 */
			authenticationUserDetailsService(CasAuthenticationUserDetailsService){
				authorityAttribNamesFromCas = conf.cas.authorityAttribNamesFromCas
				userMapper = ref('domainUserMapperService')
			}
		}

    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
