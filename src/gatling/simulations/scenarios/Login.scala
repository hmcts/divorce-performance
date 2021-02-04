package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}

import scala.concurrent.duration._

object Login {

  val IdamURL = Environment.idamURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader


  // allows the login code to be re-used for the different divorce front-ends (Petitioner, Respondent, DN, DA),
  // by passing through the BaseURL required and name of the app
  def DivorceLogin(BaseURL: String, appName: String, userType: String) = {

    doIfOrElse(userType.equals("Petitioner") || userType.equals("Respondent")){
      exec {
        session =>
          session
            .set("emailAddress", session(s"${userType}EmailAddress").as[String])
            .set("password", session(s"${userType}Password").as[String])
      }
    } {
      doIfOrElse(userType.equals("Caseworker")) {
        exec(_.set("emailAddress", "divorce_as_caseworker_beta@mailinator.com"))
          .exec(_.set("password", "Testing1234"))
      }
      {
        doIf(userType.equals("Legal")) {
          exec(_.set("emailAddress", "divorce_as_caseworker_la@mailinator.com"))
            .exec(_.set("password", "Testing1234"))
        }
      }
    }

    .group(s"Divorce_000_${appName}_${userType}_Login") {
      exec(http(s"Login to ${appName} as ${userType}")
        .post(IdamURL + "/login?client_id=divorce&response_type=code&redirect_uri=" + BaseURL + "/authenticated&ui_locales=en&state=${state}")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("username", "${emailAddress}")
        .formParam("password", "${password}")
        .formParam("save", "Sign in")
        .formParam("selfRegistrationEnabled", "true")
        .formParam("_csrf", "${csrf}")
        //Not using the CsrfCheck.save function, as not all FE apps return a csrf token when logging in, so making it optional
        .check(regex("""name="_csrf" value="(.+)">""").optional.saveAs("csrf"))
        //Check for homepage text on each FE app (Petitioner, Respondent, Decree Nisi, Decree Absolute), as this code is used for all of them
        .check(regex("What language do you want us to use|Enter these details from the letter|You can continue with your divorce")))
    }

    .exec(getCookieValue(CookieKey("__auth-token").withDomain(BaseURL.replace("https://", "")).withSecure(true).saveAs("authToken")))

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

}
