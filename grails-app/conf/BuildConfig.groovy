grails.project.work.dir = 'target'

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
		mavenLocal()
		mavenCentral()
		mavenRepo "https://repository.jboss.org/nexus/content/repositories/thirdparty-releases"
	}

	dependencies {
		compile 'opensaml:opensaml:1.1b', 'org.apache.santuario:xmlsec:1.5.2'
	}

	plugins {
		compile ':spring-security-cas:1.0.5'

		runtime(":hibernate:$grailsVersion") {
			export = false
		}

		build ':release:2.2.1', ':rest-client-builder:1.0.3', {
			export = false
		}
	}
}
