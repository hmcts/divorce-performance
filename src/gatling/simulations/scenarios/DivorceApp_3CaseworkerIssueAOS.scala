package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

import scala.concurrent.duration._

object DivorceApp_3CaseworkerIssueAOS {

  val DivorceAPIURL = Environment.divorceAPIURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val IssueAOS =

    group("Div3CW_010_AddIssueEvent") {
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

    .group("Div3CW_020_AddIssueAOSEvent") {
      exec(http("Add Issue AOS Event")
        .post(DivorceAPIURL + "/casemaintenance/version/1/updateCase/${appId}/issueAos")
        .header("Authorization", "${authToken}")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .body(StringBody("{}")).asJson
        .check(jsonPath("$.state").is("AosAwaiting")))
    }

      .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
