package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.scenario.Simulation
import scenarios._
import utils.Environment
import scala.concurrent.duration._

class Divorce_NewApplication extends Simulation {

  val httpProtocol = Environment.HttpProtocol
    .doNotTrackHeader("1")
    .inferHtmlResources()
    .silentResources

  val numberOfPipelineUsers = 10

  val DivorceSimulation = scenario( "DivorceSimulation")

    //This scenario covers an end to end Divorce application

    .exitBlockOnFail {

      exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 1. PETITIONER ANSWERS SCREENING QUESTIONS */
      /* 2. PETITIONER CREATES A NEW APPLICATION */
      .exec(
        CreateUser.CreateCitizen("Petitioner"),
        Homepage.DivorceHomepage(Environment.petitionerURL, "PetitionerFE"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Petitioner"),
        Divorce_01PetitionerScreening.ScreeningQuestions,
        Divorce_02PetitionerApplication.ApplicationQuestions,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 3. CASEWORKER UPDATES THE APPLICATION (ISSUE AOS)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "PetitionerFE"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Caseworker"), //Login to petitioner frontend with a caseworker (CW) just to get a CW authToken
        Divorce_03CaseworkerIssueAOS.IssueAOS,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 4. RESPONDENT RESPONDS TO THE APPLICATION */
      .exec(
        CreateUser.CreateCitizen("Respondent"),
        Homepage.DivorceHomepage(Environment.respondentURL, "RespondentFE"),
        Login.DivorceLogin(Environment.respondentURL, "RespondentFE", "Respondent"),
        Divorce_04RespondentResponds.Response,
        Logout.DivorceLogout(Environment.respondentURL, "RespondentFE"))
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 5. PETITIONER COMPLETES DECREE NISI (DN) QUESTIONS */
      .exec(
        Homepage.DivorceHomepage(Environment.decreeNisiURL, "DecreeNisiFE"),
        Login.DivorceLogin(Environment.decreeNisiURL, "DecreeNisiFE", "Petitioner"),
        Divorce_05PetitionerDecreeNisi.DecreeNisi,
        Logout.DivorceLogout(Environment.decreeNisiURL, "DecreeNisiFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 6. LEGAL ADVISOR (JUDGE) UPDATES THE APPLICATION (GRANT DN)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "PetitionerFE"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Legal"), //Login to petitioner frontend with a legal advisor just to get an authToken
        Divorce_06LegalGrantDecreeNisi.GrantDN,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 7. CASEWORKER UPDATES THE APPLICATION (MAKE ELIGIBLE FOR DA)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "PetitionerFE"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Caseworker"), //Login to petitioner frontend with a caseworker (CW) just to get a CW authToken
        Divorce_07CaseworkerMakeEligibleForDA.MakeEligibleForDA,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 8. PETITIONER COMPLETES DECREE ABSOLUTE (DA) QUESTIONS */
      .exec(
        Homepage.DivorceHomepage(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE"),
        Login.DivorceLogin(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE", "Petitioner"),
        Divorce_08PetitionerDecreeAbsolute.DecreeAbsolute,
        Logout.DivorceLogout(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 9. CASEWORKER UPDATES THE APPLICATION (GRANT DECREE ABSOLUTE)*/
      .exec(
        Homepage.DivorceHomepage(Environment.petitionerURL, "PetitionerFE"),
        Login.DivorceLogin(Environment.petitionerURL, "PetitionerFE", "Caseworker"), //Login to petitioner frontend with a caseworker (CW) just to get a CW authToken
        Divorce_09CaseworkerGrantDecreeAbsolute.GrantDA,
        Logout.DivorceLogout(Environment. petitionerURL, "PetitionerFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

      /* 10. PETITIONER DOWNLOADS DECREE ABSOLUTE (DA) PDF */
      .exec(
        Homepage.DivorceHomepage(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE"),
        Login.DivorceLogin(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE", "Petitioner"),
        Divorce_10PetitionerDownloadDA.DownloadDA,
        Logout.DivorceLogout(Environment.decreeAbsoluteURL, "DecreeAbsoluteFE")
      )
      .exec(flushHttpCache)
      .exec(flushCookieJar)

    }

    //delete the petitioner and respondent accounts
    .doIf("${PetitionerEmailAddress.exists()}") {
      exec(DeleteUser.DeleteCitizen("${PetitionerEmailAddress}"))
    }
    .doIf("${RespondentEmailAddress.exists()}") {
      exec(DeleteUser.DeleteCitizen("${RespondentEmailAddress}"))
    }

    .exec {
      session =>
        println(session)
        session
    }

  setUp(
    DivorceSimulation.inject(rampUsers(numberOfPipelineUsers) during (5 minutes))
  ).protocols(httpProtocol)
  .assertions(
    //ensure at least 95% of attempted transactions have passed
    global.successfulRequests.percent.gte(95),
    //ensure that at least half of the users complete the journey end to end
    details("Div10PetDA_010_DownloadDAPDF").successfulRequests.count.gte(numberOfPipelineUsers / 2)
  )


}
