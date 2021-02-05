package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

object Logout {

  val CommonHeader = Environment.commonHeader

  // allows the logout code to be re-used for the different divorce front-ends (Petitioner, Respondent, DN, DA),
  // by passing through the BaseURL required and name of the app
  def DivorceLogout(BaseURL: String, appName: String) = {

    doIfOrElse(appName.equals("PetitionerFE")) {
      exec(_.set("suffix", "sign-out"))
      } {
      doIfOrElse(appName.equals("RespondentFE")) {
        exec(_.set("suffix", "end"))
      } {
        doIf(appName.equals("DecreeNisiFE") || appName.equals("DecreeAbsoluteFE")) {
          exec(_.set("suffix", "exit"))
        }
      }
    }

    .group(s"Divorce_999_${appName}_Logout") {
      exec(http(s"Logout from ${appName}")
        .get(BaseURL + "/${suffix}")
        .headers(CommonHeader)
        .check(regex("Sign in or create an account|You(?:.+)ve been signed out")))
    }

  }

}