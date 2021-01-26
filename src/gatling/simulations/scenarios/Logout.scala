package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

object Logout {

  val BaseURL = Environment.baseURL

  val CommonHeader = Environment.commonHeader

  val NFDLogout =

    group("Divorce_999_Logout") {

      exec(http("Logout")
        .get(BaseURL + "/logout")
        .headers(CommonHeader)
        .check(substring("Sign in or create an account")))

    }

}