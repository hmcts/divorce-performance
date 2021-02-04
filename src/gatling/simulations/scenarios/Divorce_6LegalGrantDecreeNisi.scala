package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

import scala.concurrent.duration._

object Divorce_6LegalGrantDecreeNisi {

  val DivorceAPIURL = Environment.divorceAPIURL
  val IdamAPIURL = Environment.idamAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val GrantDN =

    /*
    The following call is made using the Divorce API, rather than interacting with CCD.
    It progresses the case, updating it as a legal advisor (e.g. judge) would ordinarily do manually through CCD.
     */

    group("Div6Legal_010_AddDecisionEvent") {
      exec(http("Add Decision Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/grantDnMakeDecision")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"DecreeNisiGranted": "Yes",
                "D8DivorceCostsClaim": "NO",
                "DivorceCostsOptionDN": ""}""")).asJson //might be null
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
