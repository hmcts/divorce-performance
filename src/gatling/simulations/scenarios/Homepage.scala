package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}
import scala.concurrent.duration._

object Homepage {

    val MinThinkTime = Environment.minThinkTime
    val MaxThinkTime = Environment.maxThinkTime

    val CommonHeader = Environment.commonHeader

    // allows the homepage code to be re-used for the different divorce front-ends (Petitioner, Respondent, DN, DA),
    // by passing through the BaseURL required and name of the app
    def NFDHomepage(BaseURL: String, appName: String) = {

      group(s"Divorce_001_${appName}_HomePage") {

        exec(http(s"Load ${appName} Homepage")
          .get(BaseURL + "/")
          .headers(CommonHeader)
          .header("sec-fetch-site", "none")
          .check(CsrfCheck.save)
          .check(regex("state=([0-9a-z-]+)&").saveAs("state"))
          .check(substring("Sign in or create an account")))

      }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    }

}