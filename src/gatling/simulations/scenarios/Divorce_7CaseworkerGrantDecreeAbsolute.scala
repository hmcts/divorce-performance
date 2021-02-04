package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

import scala.concurrent.duration._

object Divorce_7CaseworkerGrantDecreeAbsolute {

  val DivorceAPIURL = Environment.divorceAPIURL
  val IdamAPIURL = Environment.idamAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val GrantDA = {

    /*
    The following calls are made using the Divorce APIs, rather than interacting with CCD.
    They progress the case, updating it as a caseworker would ordinarily do manually through CCD.
     */

    group("Div7CW_010_AddLinkedWithBulkCaseEvent") {
      exec(http("Add Linked With Bulk Case Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/linkBulkCaseReference")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div7CW_020_AddUpdateCaseWithCourtHearingEvent") {
      exec(http("Add Update Case With Court Hearing Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/updateBulkCaseHearingDetails")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"DecreeNisiGranted": "Yes",
                "CourtName": "nottingham",
                "DateAndTimeOfHearing": "[{"id": "",
                                           "value": {"DateOfHearing": "2020-01-01",
                                                     "TimeOfHearing": ""}
                                                    }
                                          }]"}""")).asJson
        .check(jsonPath("$.case_data.CourtName").is("nottingham"))
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    /*
      The following two calls manually amend the case to signify that the court hearing has now taken place
      A DN granted date is added, as well as the judge's name
     */

    .group("Div7CW_030_AddDNGrantedDate") {
      exec(http("Add DN Granted Date")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/amendCase")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"DecreeNisiGrantedDate":"2020-01-01"}""")).asJson
        .check(jsonPath("$.case_data.DecreeNisiGrantedDate").is("2020-01-01"))
        .check(jsonPath("$.state").is("AwaitingPronouncement")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div7CW_040_AddPronouncementJudge") {
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

    .group("Div7CW_050_AddDNPronouncedBulkEvent") {
      exec(http("Add DN Pronounced Bulk Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/dnPronouncedBulk")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("DNPronounced")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div7CW_060_AddEligibleForDAEvent") {
      exec(http("Add Eligible For DA Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/MakeEligibleForDA_Petitioner")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("AwaitingDecreeAbsolute")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div7CW_070_AddDAGrantedEvent") {
      exec(http("Add DA Granted Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/daGranted")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("DivorceGranted")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }

}
