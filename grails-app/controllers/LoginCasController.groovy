import grails.plugins.springsecurity.Secured;

/**
 * Simple controller that will redirect user to CAS, and then back to the home page.
 * @author Daniel Bower
 */
@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class LoginCasController {

	def index = {
		redirect(uri:"/")
	}
}
