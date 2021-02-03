package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, CsrfCheck}

import scala.concurrent.duration._

object DivorceApp_1PetitionerScreening {

  val PetitionerURL = Environment.petitionerURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val ScreeningQuestions =

    group("Div1PetScreen_010_LanguagePrefSubmit") {
      exec(http("Language Preference")
        .post(PetitionerURL + "/screening-questions/language-preference")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("languagePreferenceWelsh", "No")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Has your marriage broken down")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div1PetScreen_020_MarriageBrokenDownSubmit") {
      exec(http("Marriage Broken")
        .post(PetitionerURL + "/screening-questions/has-marriage-broken")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("screenHasMarriageBroken", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Do you have an address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div1PetScreen_030_RespondentAddressSubmit") {
      exec(http("Respondent Address")
        .post(PetitionerURL + "/screening-questions/respondent-address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("screenHasRespondentAddress", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Do you have your marriage certificate")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div1PetScreen_040_MarriageCertificateSubmit") {
      exec(http("Marriage Certificate")
        .post(PetitionerURL + "/screening-questions/marriage-certificate")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("screenHasMarriageCert", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Settling your finances")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div1PetScreen_050_FinancialRemedySubmit") {
      exec(http("Financial Remedy")
        .post(PetitionerURL + "/screening-questions/financial-remedy")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Do you want help paying your fee")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div1PetScreen_060_NeedHelpSubmit") {
      exec(http("Need Help")
        .post(PetitionerURL + "/pay/help/need-help")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("helpWithFeesNeedHelp", "No")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Who are you divorcing")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
