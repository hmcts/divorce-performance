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

  val DivorceSimulation = scenario( "DivorceSimulation")

    //This scenario covers an end to end Divorce application
    
    .exitBlockOnFail {

      /* 1. PETITIONER ANSWERS SCREENING QUESTIONS */
      /* 2. PETITIONER CREATES A NEW APPLICATION */
      exec(
        CreateUser.CreateCitizen("Petitioner"),
        Homepage.DivorceHomepage(Environment.petitionerURL, "Petitioner"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Petitioner"),
        Divorce_1PetitionerScreening.ScreeningQuestions,
        Divorce_2PetitionerApplication.ApplicationQuestions,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 3. CASEWORKER UPDATES THE APPLICATION (ISSUE AOS)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "Caseworker"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Caseworker"), //Login to petitioner frontend with a caseworker (CW) just to get a CW authToken
        Divorce_3CaseworkerIssueAOS.IssueAOS,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 4. RESPONDENT RESPONDS TO THE APPLICATION */
      .exec(
        CreateUser.CreateCitizen("Respondent"),
        Homepage.DivorceHomepage(Environment.respondentURL, "Respondent"),
        Login.DivorceLogin(Environment.respondentURL, "RespondentFE", "Respondent"),
        Divorce_4RespondentResponds.Response,
        Logout.DivorceLogout(Environment.respondentURL, "RespondentFE"))
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 5. PETITIONER COMPLETES DECREE NISI (DN) QUESTIONS */
      exec(
        Homepage.DivorceHomepage(Environment.decreeNisiURL, "Petitioner"),
        Login.DivorceLogin(Environment.decreeNisiURL, "DecreeNisiFE", "Petitioner"),
        Divorce_5PetitionerDecreeNisi.DecreeNisi,
        Logout.DivorceLogout(Environment.decreeNisiURL, "DecreeNisiFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 6. LEGAL ADVISOR (JUDGE) UPDATES THE APPLICATION (GRANT DN)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "Legal"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Legal"), //Login to petitioner frontend with a legal advisor just to get an authToken
        Divorce_6LegalGrantDecreeNisi.GrantDN,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 7. CASEWORKER UPDATES THE APPLICATION (GRANT DA)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "Caseworker"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Caseworker"), //Login to petitioner frontend with a caseworker (CW) just to get a CW authToken
        Divorce_7CaseworkerGrantDecreeAbsolute.GrantDA,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

    }
    .exec {
      session =>
        println(session)
        session
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
    DivorceSimulation.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(100))

}
