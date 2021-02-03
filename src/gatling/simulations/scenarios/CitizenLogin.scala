package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}

import scala.concurrent.duration._

object CitizenLogin {

  val IdamURL = Environment.idamURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader


  // allows the login code to be re-used for the different divorce front-ends (Petitioner, Respondent, DN, DA),
  // by passing through the BaseURL required and name of the app
  def NFDLogin(BaseURL: String, appName: String) = {

    group(s"Divorce_000_${appName}_Login") {

      exec(http(s"Login as ${appName}")
        .post(IdamURL + "/login?client_id=divorce&response_type=code&redirect_uri=" + BaseURL + "/authenticated&ui_locales=en&state=${state}")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("username", "${emailAddress}")
        .formParam("password", "${password}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        .check(CsrfCheck.save)
        .check(regex("What language do you want us to use|Enter these details from the letter")))

    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

}
