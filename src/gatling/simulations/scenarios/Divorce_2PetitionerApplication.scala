package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Common, Environment, CsrfCheck}

import scala.concurrent.duration._
import scala.util.Random

object Divorce_2PetitionerApplication {

  val PetitionerURL = Environment.petitionerURL
  val PaymentURL = Environment.paymentURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val postcodeFeeder = csv("postcodes.csv").random

  val rnd = new Random()

  val ApplicationQuestions = {

    /*
    Petitioner application questions
     */

    //Generate petitioner and respondent names and separation/marriage dates (these are used multiple times throughout the flow)
    exec {
      session =>
        session
          .set("petitionerFirstName", "Perf " + Common.randomString(5))
          .set("petitionerLastName", "Petitioner" + Common.randomString(5))
          .set("respondentFirstName", "Perf " + Common.randomString(5))
          .set("respondentLastName", "Respondent" + Common.randomString(5))
          .set("MarriageDay", Common.getDay())
          .set("MarriageMonth", Common.getMonth())
          .set("MarriageYear", Common.getMarriageYear())
          .set("separationDay", Common.getDay())
          .set("separationMonth", Common.getMonth())
          .set("separationYear", Common.getSeparationYear())
    }

    .group("Div2PetApp_010_DetailsSubmit") {
      exec(http("Details")
        .post(PetitionerURL + "/about-your-marriage/details")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("divorceWho", "wife")
        .formParam("hidden-marriageIsSameSexCouple", "No")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("When did you get married")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_020_DateOfMarriageSubmit") {
      exec(http("Date Of Marriage")
        .post(PetitionerURL + "/about-your-marriage/date-of-marriage-certificate")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriageDateDay", "${MarriageDay}")
        .formParam("marriageDateMonth", "${MarriageMonth}")
        .formParam("marriageDateYear", "${MarriageYear}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Did you marry your wife in the UK")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_030_InTheUKSubmit") {
      exec(http("In The UK")
        .post(PetitionerURL + "/about-your-marriage/in-the-uk")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriedInUk", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Check if you can get a divorce in England and Wales")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_040_HabitualResidenceSubmit") {
      exec(http("Habitual Residence")
        .post(PetitionerURL + "/jurisdiction/habitual-residence")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("jurisdictionPetitionerResidence", "Yes")
        .formParam("jurisdictionRespondentResidence", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("You can use English or Welsh courts to get a divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_050_InterstitialSubmit") {
      exec(http("Interstitial")
        .post(PetitionerURL + "/jurisdiction/interstitial")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Do you need your address kept private")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_060_ConfidentialSubmit") {
      exec(http("Confidential")
        .post(PetitionerURL + "/petitioner-respondent/confidential")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerContactDetailsConfidential", "share")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Enter your current names")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_070_NamesSubmit") {
      exec(http("Names")
        .post(PetitionerURL + "/petitioner-respondent/names")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerFirstName", "${petitionerFirstName}")
        .formParam("petitionerLastName", "${petitionerLastName}")
        .formParam("respondentFirstName", "${respondentFirstName}")
        .formParam("respondentLastName", "${respondentLastName}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("How are your names displayed on the marriage certificate")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_080_NamesOnCertSubmit") {
      exec(http("Names On Certificate")
        .post(PetitionerURL + "/petitioner-respondent/names-on-certificate")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriagePetitionerName", "${petitionerFirstName} ${petitionerLastName}")
        .formParam("marriageRespondentName", "${respondentFirstName} ${respondentLastName}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Have you changed your name since you got married")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_090_ChangedNameSubmit") {
      exec(http("Changed Name")
        .post(PetitionerURL + "/petitioner-respondent/changed-name")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerNameDifferentToMarriageCertificate", "No")
        .formParam("petitionerNameChangedHowOtherDetails", "")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("How the court will contact you")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_100_ContactDetailsSubmit") {
      exec(http("Contact Details")
        .post(PetitionerURL + "/petitioner-respondent/contact-details")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("hidden-petitionerConsent", "No")
        .formParam("petitionerConsent", "Yes")
        .formParam("petitionerPhoneNumber", "")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("What is your home address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_110_AddressPostcodeSubmit") {
      feed(postcodeFeeder)
        .exec(http("Address Postcode")
          .post(PetitionerURL + "/petitioner-respondent/address")
          .headers(CommonHeader)
          .headers(PostHeader)
          .formParam("_csrf", "${csrf}")
          .formParam("postcode", "${postcode}")
          .formParam("addressType", "postcode")
          .formParam("searchPostcode", "true")
          .formParam("addressConfirmed", "false")
          .check(CsrfCheck.save)
          .check(substring("Pick an address"))
          .check(regex("<option value=([0-9]+) >").findRandom.saveAs("randomAddressIndex")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_120_AddressChooseSubmit") {
      exec(http("Address Choose")
        .post(PetitionerURL + "/petitioner-respondent/address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("selectAddressIndex", "${randomAddressIndex}")
        .formParam("selectAddress", "true")
        .formParam("searchPostcode", "false")
        .formParam("addressConfirmed", "false")
        .formParam("postcode", "${postcode}")
        .check(CsrfCheck.save)
        //capture tuples of address values (key, value) e.g. ("addressLine0", "1 Perf Road"), ("addressLine1", "Perf Town"), ("addressLine2", "PR1 1RF")
        //this allows a dynamic number of address lines to be captured and submitted to the next call
        .check(regex("""<input class="govuk-input" type="text" id="addressLine." name="(.+)" value="(.+)"""").ofType[(String, String)].findAll.saveAs("addressLines")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_130_AddressSubmit") {
      exec(http("Address")
        .post(PetitionerURL + "/petitioner-respondent/address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParamSeq("${addressLines}") //submit the tuple seq captured from the previous call (i.e. all the lines of the address)
        .formParam("addressType", "postcode")
        .formParam("addressConfirmed", "true")
        .formParam("postcode", "${postcode}")
        .check(CsrfCheck.save)
        .check(substring("Do you want your divorce papers sent to this address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_140_UseHomeAddressSubmit") {
      exec(http("Use Home Address")
        .post(PetitionerURL + "/petitioner-respondent/petitioner-correspondence/use-home-address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerCorrespondenceUseHomeAddress", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("live at the same address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_150_LiveTogetherSubmit") {
      exec(http("Live Together")
        .post(PetitionerURL + "/petitioner-respondent/live-together")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("livingArrangementsLiveTogether", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("divorce papers should be sent")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_160_UseHomeAddressSubmit") {
      exec(http("Use Home Address")
        .post(PetitionerURL + "/petitioner-respondent/respondent-correspondence/use-home-address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("respondentCorrespondenceUseHomeAddress", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Choose a reason for your divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_170_ReasonSubmit") {
      exec(http("Reason")
        .post(PetitionerURL + "/about-divorce/reason-for-divorce/reason")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("reasonForDivorce", "separation-5-years")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("When you separated")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_180_SeparationDatesSubmit") {
      exec(http("Separation Dates")
        .post(PetitionerURL + "/about-divorce/reason-for-divorce/separation-dates")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("reasonForDivorceDecisionDay", "${separationDay}")
        .formParam("reasonForDivorceDecisionMonth", "${separationMonth}")
        .formParam("reasonForDivorceDecisionYear", "${separationYear}")
        .formParam("reasonForDivorceLivingApartDay", "${separationDay}")
        .formParam("reasonForDivorceLivingApartMonth", "${separationMonth}")
        .formParam("reasonForDivorceLivingApartYear", "${separationYear}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Have you lived apart for the entire time since you separated")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_190_LivedApartSinceSubmit") {
      exec(http("Lived Apart Since")
        .post(PetitionerURL + "/about-divorce/reason-for-divorce/separation/lived-apart-since")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("livedApartEntireTime", "Yes")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Other court cases related to your marriage")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_200_LegalProceedingsSubmit") {
      exec(http("Legal Proceedings")
        .post(PetitionerURL + "/about-divorce/legal-proceedings")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("legalProceedings", "No")
        .formParam("legalProceedingsDetails", "")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Dividing your money and property")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_210_FinancialArrangementsSubmit") {
      exec(http("Financial Arrangements")
        .post(PetitionerURL + "/about-divorce/financial/arrangements")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("financialOrder", "No")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Do you want to apply to claim your divorce costs")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_220_ClaimCostsSubmit") {
      exec(http("Claim Costs")
        .post(PetitionerURL + "/about-divorce/claim-costs")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("claimsCosts", "No")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(substring("Upload your documents")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_230_DocumentUpload") {
      exec(http("Document Upload")
        .post(PetitionerURL + "/petitioner-respondent/marriage-certificate-upload?js=true&_csrf=${csrf}")
        .header("accept", "application/json")
        .header("accept-encoding", "gzip, deflate, br")
        .header("accept-language", "en-GB,en;q=0.9")
        .header("content-type", "multipart/form-data")
        .header("sec-fetch-dest", "empty")
        .header("sec-fetch-mode", "cors")
        .header("sec-fetch-site", "same-origin")
        .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36")
        .header("x-requested-with", "XMLHttpRequest")
        .bodyPart(RawFileBodyPart("file", "2MB.pdf")
          .fileName("2MB.pdf")
          .transferEncoding("binary"))
        .asMultipartForm
        .check(status.is(200))
        .check(currentLocation.is(PetitionerURL + "/petitioner-respondent/marriage-certificate-upload?js=true&_csrf=${csrf}"))
        .check(regex("status.:.OK")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_240_DocumentUploadSubmit") {
      exec(http("Document Upload")
        .post(PetitionerURL + "/petitioner-respondent/marriage-certificate-upload")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("submit", "Continue")
        .check(CsrfCheck.save)
        .check(regex("Check your answers|Equality and diversity questions")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_250_CheckYourAnswers") {
      exec(http("Check Your Answers")
        .get(PetitionerURL + "/check-your-answers")
        .headers(CommonHeader)
        .check(CsrfCheck.save)
        .check(substring("Check your answers")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_260_CheckYourAnswersSubmit") {
      exec(http("Check Your Answers")
        .post(PetitionerURL + "/check-your-answers")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("confirmPrayer", "Yes")
        .formParam("submit", "Submit and pay")
        .check(CsrfCheck.save)
        .check(substring("The application fee is")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_270_ProceedToPayment") {
      exec(http("Proceed To Payment")
        .post(PetitionerURL + "/pay/online")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("submit", "Continue")
        .check(substring("Enter card details"))
        .check(css("input[name='csrfToken']", "value").saveAs("csrf"))
        .check(css("input[name='chargeId']", "value").saveAs("ChargeId")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_280_CheckCard") {
      exec(http("Check Card")
        .post(PaymentURL + "/check_card/${ChargeId}")
        .headers(CommonHeader)
        .headers(PostHeader)
        .header("sec-fetch-dest", "empty")
        .header("sec-fetch-mode", "cors")
        .formParam("cardNo", "4444333322221111")
        .check(jsonPath("$.accepted").is("true")))

    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_290_CardDetailsSubmit") {
      exec(http("Card Details")
        .post(PaymentURL + "/card_details/${ChargeId}")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("chargeId", "${ChargeId}")
        .formParam("csrfToken", "${csrf}")
        .formParam("cardNo", "4444333322221111")
        .formParam("expiryMonth", Common.getMonth())
        .formParam("expiryYear", "23")
        .formParam("cardholderName", "Perf Tester" + Common.randomString(5))
        .formParam("cvc", (100 + rnd.nextInt(900)).toString())
        .formParam("addressCountry", "GB")
        .formParam("addressLine1", rnd.nextInt(1000).toString + " Perf" + Common.randomString(5) + " Road")
        .formParam("addressLine2", "")
        .formParam("addressCity", "Perf " + Common.randomString(5) + " Town")
        .formParam("addressPostcode", "PR1 1RF") //Common.getPostcode()
        .formParam("email", "probate@perftest" + Common.randomString(8) + ".com")
        .check(regex("Confirm your payment"))
        .check(css("input[name='csrfToken']", "value").saveAs("csrf")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div2PetApp_300_ConfirmPayment") {
      exec(http("Confirm Payment")
        .post(PaymentURL + "/card_details/${ChargeId}/confirm")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("chargeId", "${ChargeId}")
        .formParam("csrfToken", "${csrf}")
        .check(regex(""""govuk-body-reference-number" aria-label="([0-9 ]+)"""").find.transform(str => str.replace(" ", "")).saveAs("appId"))
        .check(substring("Application complete")))
    }

    .exec {
      session =>
        println("APPLICATION ID: " + session("appId").as[String])
        session
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }


}
