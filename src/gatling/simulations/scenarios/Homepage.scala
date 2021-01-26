package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}
import scala.concurrent.duration._

object Homepage {

  val BaseURL = Environment.baseURL
  val IdamURL = Environment.idamURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader

  val NFDHomepage =

    group("Divorce_001_HomePage") {

      exec(http("Load Homepage")
        .get(BaseURL + "/")
        .headers(CommonHeader)
        .header("sec-fetch-site", "none")
        .check(CsrfCheck.save)
        .check(regex("state=([0-9a-z-]+)&").saveAs("state"))
        .check(substring("Sign in or create an account")))

    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}