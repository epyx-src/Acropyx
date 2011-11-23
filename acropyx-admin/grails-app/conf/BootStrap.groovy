import grails.plugin.multitenant.core.util.TenantUtils
import ch.acropyx.Competition
import ch.acropyx.Judge
import ch.acropyx.Manoeuvre
import ch.acropyx.MarkDefinition
import ch.acropyx.Pilot
import ch.acropyx.Role
import ch.acropyx.Run
import ch.acropyx.Team
import ch.acropyx.User
import ch.acropyx.UserRole

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

class BootStrap {

	def springSecurityService
    
    def createDefaultUsers(servletContext) {
        // Initialize security (users and roles)
        def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
        def eventRole = new Role(authority: 'ROLE_EVENT').save(flush: true)
        def judgeRole = new Role(authority: 'ROLE_JUDGE').save(flush: true)
        def audienceRole = new Role(authority: 'ROLE_AUDIENCE').save(flush: true)

        String defaultPassword = springSecurityService.encodePassword('123')
        
        def admin = new User(username: 'admin', enabled: true, password: defaultPassword)
        admin.save(flush: true)
        UserRole.create(admin, adminRole, true)
        
        def event = new User(username: 'event', enabled: true, password: defaultPassword)
        event.save(flush: true)
        UserRole.create(event, eventRole, true)
	}
        
    def createDefaultMarkDefinitionsAndManoeuvres(servletContext) {
        // Populate database with default values
        if (MarkDefinition.list().size == 0) {
            MarkDefinition.importFromStream(servletContext.getResourceAsStream("markDefinitions.csv"))
        }
        if (Manoeuvre.list().size == 0) {
            Manoeuvre.importFromStream(servletContext.getResourceAsStream("manoeuvres.csv"))
        }
    }
            
	def init = { servletContext ->
        createDefaultUsers(servletContext)
        
        CH.config.tenant.domainTenantMap.each { key, value ->
            TenantUtils.doWithTenant(value) {
                createDefaultMarkDefinitionsAndManoeuvres(servletContext)
            }
        }
        
        environments {
            development {
                // doWithTenant closure only works if inside environments.devlopment closure
                TenantUtils.doWithTenant(1) {
                    if (Competition.list().size() == 0) {
                        println "### Populate dev database"
                        
                        new Judge(name:"Gilles", username:"", role:Judge.Role.FAI).save()
                        new Judge(name:"Chrigel", username:"", role:Judge.Role.FAI).save()
                        new Judge(name:"Lady Gaga", username:"", role:Judge.Role.VIP).save()
                        new Judge(name:"Prince Charles", username:"", role:Judge.Role.VIP).save(flush:true)
                        
                        def sky2011_Solo = new Competition(code: "Sky2011-Solo", name: "Sky Water Show 2011 Solo", type:Competition.Type.Solo).save(flush:true)
                        sky2011_Solo.addDefaultCoefficients()
                        sky2011_Solo.addAllJudges()
                        def sky2011_Synchro = new Competition(code: "Sky2011-Synchro", name: "Sky Water Show 2011 Synchro", type:Competition.Type.Synchro).save(flush:true)
                        sky2011_Synchro.addDefaultCoefficients()
                        sky2011_Synchro.addAllJudges()
                                            
                        new Run(competition:sky2011_Solo, name:"Qualification").save()
                        new Run(competition:sky2011_Solo, name:"1").save()
                        new Run(competition:sky2011_Solo, name:"2").save()
                        new Run(competition:sky2011_Solo, name:"Final").save(flush:true)
                        new Run(competition:sky2011_Synchro, name:"Qualification").save()
                        new Run(competition:sky2011_Synchro, name:"1").save()
                        new Run(competition:sky2011_Synchro, name:"2").save()
                        new Run(competition:sky2011_Synchro, name:"Final").save(flush:true)
                        
                        def david = new Pilot(name:"David", country:"che", job:"", selection:"", glider:"", sponsor:"", bestResult:"").save()
                        def vincent = new Pilot(name:"Vincent", country:"fra", job:"", selection:"", glider:"", sponsor:"", bestResult:"").save()
                        new Pilot(name:"Cédric", country:"che", job:"", selection:"", glider:"", sponsor:"", bestResult:"").save()
                        new Pilot(name:"Yann", country:"che", job:"", selection:"", glider:"", sponsor:"", bestResult:"").save()
                        new Pilot(name:"Stefan", country:"che", job:"", selection:"", glider:"", sponsor:"", bestResult:"").save(flush:true)
                        
                        new Team(name: "Acropyx", pilots: [david, vincent], bestResult:"").save(flush:true)
                    }
                }
            }
        }
	}    
}
