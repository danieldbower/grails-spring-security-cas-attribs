//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//

ant.copy(file:"${pluginBasedir}/src/groovy/org/codehaus/groovy/grails/plugins/springsecurity/cas/samples/DomainUserMapperService.txt", tofile:"${basedir}/grails-app/services/DomainUserMapperService.groovy")

println """
*******************************************************
* You've installed the Spring Security Cas Attribs plugin.
*
* You MUST edit:
* ${basedir}/grails-app/services/DomainUserMapperService.groovy 
* with correct values, or your app will not compile
* You will need to tell it what properties to map from 
* CAS, as well as the Class of your User Profiles.
*
*******************************************************
"""
