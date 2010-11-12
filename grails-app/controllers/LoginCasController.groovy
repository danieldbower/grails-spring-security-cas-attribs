import grails.plugins.springsecurity.Secured;

@Secured(['IS_AUTHENTICATED_REMEMBERED'])
class LoginCasController {

	def index = {
		redirect(uri:"/")
	}
}
