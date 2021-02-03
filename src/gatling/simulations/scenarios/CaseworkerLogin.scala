package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CsrfCheck, Environment}

import scala.concurrent.duration._

object CaseworkerLogin {

  val IdamURL = Environment.idamURL
  val BaseURL = Environment.petitionerURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val CWLogin = {

    group("Divorce_001_Petitioner_HomePage") {

      exec(http("Load Petitioner Homepage")
        .get(BaseURL + "/")
        .headers(CommonHeader)
        .header("sec-fetch-site", "none")
        .check(CsrfCheck.save)
        .check(regex("state=([0-9a-z-]+)&").saveAs("state"))
        .check(substring("Sign in or create an account")))

    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Divorce_000_Casworker_Login") {

      exec(http("Login as Caseworker")
        .post(IdamURL + "/login?client_id=divorce&response_type=code&redirect_uri=" + BaseURL + "/authenticated&ui_locales=en&state=${state}")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("username", "divorce_as_caseworker_beta@mailinator.com")
        .formParam("password", "Testing1234")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        .check(CsrfCheck.save)
        .check(regex("What language do you want us to use")))

    }

    .exec(getCookieValue(CookieKey("__auth-token").withDomain("petitioner-frontend-aks.aat.platform.hmcts.net").withSecure(true).saveAs("authToken")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

    .exec {
      session =>
        println(session)
        println("Auth Token: " + session("authToken").as[String])
        session
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
