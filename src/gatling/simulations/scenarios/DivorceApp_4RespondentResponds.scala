package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}

import scala.concurrent.duration._

object DivorceApp_4RespondentResponds {

  val RespondentURL = Environment.respondentURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val ScreeningQuestions =

    group("Div4Resp_010_PinSubmit") {
      exec(http("Respond with PIN")
        .post(RespondentURL + "/respond-with-pin")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("referenceNumber", "1612265151712558")
        .formParam("securityAccessCode", "SXEWT37M")
        .check(substring("Respond to your")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_020_Respond") {
      exec(http("Respond")
        .post(RespondentURL + "/respond")
        .headers(CommonHeader)
        .headers(PostHeader)
        .check(CsrfCheck.save)
        .check(substring("Divorce application (petition)")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_030_ReviewApplication") {
      exec(http("Review Application")
        .post(RespondentURL + "/review-application")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("respConfirmReadPetition", "Yes")
        .check(CsrfCheck.save)
        .check(substring("What language do you want us to use")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_040_LanguagePrefSubmit") {
      exec(http("Language Preference")
        .post(RespondentURL + "/language-preference")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("languagePreferenceWelsh", "No")
        .check(CsrfCheck.save)
        .check(substring("How do you want to respond")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_050_ChooseAResponseSubmit") {
      exec(http("Choose A Response")
        .post(RespondentURL + "/choose-a-response")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("response", "proceed")
        .check(CsrfCheck.save)
        .check(substring("Do you intend to ask the court to delay")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_060_DelayTheDecreeSubmit") {
      exec(http("Delay The Decree")
        .post(RespondentURL + "/delay-the-decree")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("respConsiderFinancialSituation", "No")
        .check(CsrfCheck.save)
        .check(substring("Do you agree that the courts")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_070_JurisdictionSubmit") {
      exec(http("Jurisdiction")
        .post(RespondentURL + "/jurisdiction")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("jurisdiction.agree", "Yes")
        .formParam("jurisdiction.reason", "")
        .formParam("jurisdiction.country", "")
        .check(CsrfCheck.save)
        .check(substring("Other legal proceedings")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_080_LegalProceedingsSubmit") {
      exec(http("Legal Proceedings")
        .post(RespondentURL + "/legal-proceedings")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("legalProceedings.exists", "No")
        .formParam("legalProceedings.details", "")
        .check(CsrfCheck.save)
        .check(substring("How the court will contact you")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_090_ContactDetailsSubmit") {
      exec(http("Contact Details")
        .post(RespondentURL + "/contact-details")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("contactDetails.consent", "Yes")
        .formParam("contactDetails.telephone", "")
        .check(CsrfCheck.save)
        .check(regex("Check your answers|Equality and diversity questions")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_100_CheckYourAnswers") {
      exec(http("Check Your Answers")
        .get(RespondentURL + "/check-your-answers")
        .headers(CommonHeader)
        .check(CsrfCheck.save)
        .check(substring("Check your answers")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div4Resp_110_CheckYourAnswersSubmit") {
      exec(http("Check Your Answers")
        .post(RespondentURL + "/check-your-answers")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("respStatementOfTruth", "Yes")
        .check(substring("Response sent")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
