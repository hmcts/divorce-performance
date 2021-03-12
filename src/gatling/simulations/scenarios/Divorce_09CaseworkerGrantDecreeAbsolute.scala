package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Common, Environment}

import scala.concurrent.duration._

object Divorce_09CaseworkerGrantDecreeAbsolute {

  val DivorceAPIURL = Environment.divorceAPIURL
  val IdamAPIURL = Environment.idamAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val GrantDA = {

    /*
    The following call is made using the Divorce APIs, rather than interacting with CCD.
    They progress the case, updating it as a caseworker would ordinarily do manually through CCD.
     */

    group("Div09CW_010_AddGrantDAEvent") {
      exec(http("Add Grant DA Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/GrantDecreeAbsolute")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{
                           |    "DecreeAbsoluteGranted": [
                           |      "Yes"
                           |    ],
                           |    "D8PetitionerFirstName": "${petitionerFirstName}",
                           |    "D8PetitionerLastName": "${petitionerLastName}",
                           |    "D8RespondentFirstName": "${respondentFirstName}",
                           |    "D8RespondentLastName": "${respondentLastName}"
                           |  }""".stripMargin)).asJson
        .check(jsonPath("$.state").is("DivorceGranted")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

}
