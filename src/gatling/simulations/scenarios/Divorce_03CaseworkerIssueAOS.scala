package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

import scala.concurrent.duration._

object Divorce_03CaseworkerIssueAOS {

  val DivorceAPIURL = Environment.divorceAPIURL
  val IdamAPIURL = Environment.idamAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val IssueAOS =

    /*
    The following calls are made using the Divorce APIs, rather than interacting with CCD.
    They progress the case, updating it as a caseworker would ordinarily do manually through CCD.
     */

    group("Div03CW_010_AddIssueEvent") {
      exec(http("Add Issue Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/issueFromSubmitted")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("""{"D8caseReference": "EZ12D91234",
                "D8MarriageDate": "${MarriageYear}-${MarriageMonth}-${MarriageDay}",
                "D8MarriagePetitionerName": "${petitionerFirstName} ${petitionerLastName}",
                "D8MarriagePlaceOfMarriage": "Perf Test",
                "D8MarriageRespondentName": "${respondentFirstName} ${respondentLastName}"}""")).asJson
        .check(jsonPath("$.state").is("Issued")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div03CW_020_AddIssueAOSEvent") {
      exec(http("Add Issue AOS Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/issueAos")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.case_data.AosLetterHolderId").saveAs("letterHolderId"))
        .check(jsonPath("$.state").is("AosAwaiting")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div03CW_030_RetrievePIN") {
      exec(http("Retrieve PIN")
        .get(IdamAPIURL + "/testing-support/accounts/pin/${letterHolderId}")
        .check(bodyString.saveAs("pin")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
