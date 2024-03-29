package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, Common}

import scala.concurrent.duration._

object Divorce_07CaseworkerMakeEligibleForDA {

  val DivorceAPIURL = Environment.divorceAPIURL
  val IdamAPIURL = Environment.idamAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val MakeEligibleForDA = {

    /*
    The following calls are made using the Divorce APIs, rather than interacting with CCD.
    They progress the case, updating it as a caseworker would ordinarily do manually through CCD.
     */

    //set the court hearing date
    exec(_.set("courtHearingDay", Common.getDay()))
    .exec(_.set("courtHearingMonth", Common.getMonth()))
    .exec(_.set("courtHearingYear", Common.getCourtHearingYear()))

    .group("Div07CW_010_AddLinkedWithBulkCaseEvent") {
      exec(http("Add Linked With Bulk Case Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/linkBulkCaseReference")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div07CW_020_AddUpdateCaseWithCourtHearingEvent") {
      exec(http("Add Update Case With Court Hearing Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/updateBulkCaseHearingDetails")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{
                           |    "CourtName": "nottingham",
                           |    "DateAndTimeOfHearing": [
                           |      {
                           |        "id": null,
                           |        "value": {
                           |          "DateOfHearing": "${courtHearingYear}-${courtHearingMonth}-${courtHearingDay}",
                           |          "TimeOfHearing": null
                           |        }
                           |      }
                           |    ]
                           |  }""".stripMargin)).asJson
        .check(jsonPath("$.case_data.CourtName").is("nottingham"))
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    /*
      The following two calls manually amend the case to signify that the court hearing has now taken place
      A DN granted date is added, as well as the judge's name
     */

    .group("Div07CW_030_AddDNGrantedDate") {
      exec(http("Add DN Granted Date")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/amendCase")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"DecreeNisiGrantedDate":"${courtHearingYear}-${courtHearingMonth}-${courtHearingDay}"}""")).asJson
        .check(jsonPath("$.case_data.DecreeNisiGrantedDate").is("${courtHearingYear}-${courtHearingMonth}-${courtHearingDay}"))
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div07CW_040_AddPronouncementJudge") {
      exec(http("Add Pronouncement Judge")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/amendCase")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"PronouncementJudge":"Mr Perf Judge"}""")).asJson
        .check(jsonPath("$.case_data.PronouncementJudge").is("Mr Perf Judge"))
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div07CW_050_AddDNPronouncedBulkEvent") {
      exec(http("Add DN Pronounced Bulk Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/dnPronouncedBulk")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("DNPronounced")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div07CW_060_AddEligibleForDAEvent") {
      exec(http("Add Eligible For DA Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/MakeEligibleForDA_Petitioner")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("AwaitingDecreeAbsolute")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

}
