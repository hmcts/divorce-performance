package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation
import scenarios._
import utils.Environment

class Divorce_Pipeline extends Simulation {

  val httpProtocol = Environment.HttpProtocol
    .doNotTrackHeader("1")
    .inferHtmlResources()
    .silentResources

  val NFDSimulation = scenario( "NFDSimulation")

    .exitBlockOnFail {

      /* PETITIONER CREATES A NEW APPLICATION */
      exec(
        CreateUser.CreateCitizen("Petitioner"),
        Homepage.NFDHomepage(Environment.petitionerURL, "Petitioner"),
        CitizenLogin.NFDLogin(Environment.petitionerURL, "Petitioner"),
        DivorceApp_1PetitionerScreening.ScreeningQuestions,
        DivorceApp_2PetitionerApplication.ApplicationQuestions,
        CitizenLogout.NFDLogout(Environment. petitionerURL, "Petitioner"))
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* CASEWORKER UPDATES THE APPLICATION (ISSUE AOS)*/
      .exec(
        CaseworkerLogin.CWLogin, //Login to the petitioner frontend with a caseworker (CW) just to get a CW authToken
        DivorceApp_3CaseworkerIssueAOS.IssueAOS
      )
      /*
      /* RESPONDENT RESPONDS TO THE APPLICATION */
      .exec(
        CreateUser.CreateCitizen("Respondent"),
        Homepage.NFDHomepage(Environment.respondentURL, "Respondent"),
        CitizenLogin.NFDLogin(Environment.respondentURL, "Respondent"),
        CitizenLogout.NFDLogout(Environment.respondentURL, "Respondent"))
      .exec(flushHttpCache)
      .exec(flushCookieJar)
 */

    }
    //delete the petitioner and respondent accounts
    /*.doIf("${PetitionerEmailAddress.exists()}") {
      exec(DeleteUser.DeleteCitizen("${PetitionerEmailAddress}"))
    }
    .doIf("${RespondentEmailAddress.exists()}") {
      exec(DeleteUser.DeleteCitizen("${RespondentEmailAddress}"))
    }

     */

  setUp(
    NFDSimulation.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(100))

}
