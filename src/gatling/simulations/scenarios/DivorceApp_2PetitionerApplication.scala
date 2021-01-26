package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, Common}

import scala.concurrent.duration._

object DivorceApp_2PetitionerApplication {

  val BaseURL = Environment.baseURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val AboutYourMarriage = {

    //Generate random petitioner and respondent names, and set separation date
    exec {
      session =>
        session
          .set("petitionerFirstName", "Perf " + Common.randomString(5))
          .set("petitionerLastName", "Petitioner" + Common.randomString(5))
          .set("respondentFirstName", "Perf " + Common.randomString(5))
          .set("respondentLastName", "Respondent" + Common.randomString(5))
          .set("separationDay", Common.getDay())
          .set("separationMonth", Common.getMonth())
          .set("separationYear", Common.getSeparationYear())
    }

    .group("DivorceApp_070_DetailsSubmit") {
      exec(http("Details")
        .post(BaseURL + "/about-your-marriage/details")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("divorceWho", "wife")
        .formParam("hidden-marriageIsSameSexCouple", "No")
        .formParam("submit", "Continue")
        .check(substring("When did you get married")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_080_DateOfMarriageSubmit") {
      exec(http("Date Of Marriage")
        .post(BaseURL + "/about-your-marriage/date-of-marriage-certificate")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriageDateDay", Common.getDay())
        .formParam("marriageDateMonth", Common.getMonth())
        .formParam("marriageDateYear", Common.getMarriageYear())
        .formParam("submit", "Continue")
        .check(substring("Did you marry your wife in the UK")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_090_InTheUKSubmit") {
      exec(http("In The UK")
        .post(BaseURL + "/about-your-marriage/in-the-uk")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriedInUk", "Yes")
        .formParam("submit", "Continue")
        .check(substring("Check if you can get a divorce in England and Wales")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_100_HabitualResidenceSubmit") {
      exec(http("Habitual Residence")
        .post(BaseURL + "/jurisdiction/habitual-residence")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("jurisdictionPetitionerResidence", "Yes")
        .formParam("jurisdictionRespondentResidence", "Yes")
        .formParam("submit", "Continue")
        .check(substring("You can use English or Welsh courts to get a divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_110_InterstitialSubmit") {
      exec(http("Interstitial")
        .post(BaseURL + "/jurisdiction/interstitial")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("jurisdictionConfidentLegal", "Yes")
        .formParam("submit", "Continue")
        .check(substring("Do you need your address kept private")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_120_ConfidentialSubmit") {
      exec(http("Confidential")
        .post(BaseURL + "/petitioner-respondent/confidential")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerContactDetailsConfidential", "share")
        .formParam("submit", "Continue")
        .check(substring("Enter your current names")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_130_NamesSubmit") {
      exec(http("Names")
        .post(BaseURL + "/petitioner-respondent/names")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerFirstName", "${petitionerFirstName}")
        .formParam("petitionerLastName", "${petitionerLastName}")
        .formParam("respondentFirstName", "${respondentFirstName}")
        .formParam("respondentLastName", "${respondentLastName}")
        .formParam("submit", "Continue")
        .check(substring("How are your names displayed on the marriage certificate")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_140_NamesOnCertSubmit") {
      exec(http("Names On Certificate")
        .post(BaseURL + "/petitioner-respondent/names-on-certificate")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("marriagePetitionerName", "${petitionerFirstName} ${petitionerLastName}")
        .formParam("marriageRespondentName", "${respondentFirstName} ${respondentLastName}")
        .formParam("submit", "Continue")
        .check(substring("Have you changed your name since you got married")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_150_ChangedNameSubmit") {
      exec(http("Changed Name")
        .post(BaseURL + "/petitioner-respondent/changed-name")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerNameDifferentToMarriageCertificate", "No")
        .formParam("petitionerNameChangedHowOtherDetails", "")
        .formParam("submit", "Continue")
        .check(substring("How the court will contact you")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_160_ContactDetailsSubmit") {
      exec(http("Contact Details")
        .post(BaseURL + "/petitioner-respondent/contact-details")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("hidden-petitionerConsent", "No")
        .formParam("petitionerConsent", "Yes")
        .formParam("petitionerPhoneNumber", "")
        .formParam("submit", "Continue")
        .check(substring("What is your home address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_170_AddressPostcodeSubmit") {
      exec(http("Address Postcode")
        .post(BaseURL + "/petitioner-respondent/address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("postcode", "HA8 7ST")
        .formParam("addressType", "postcode")
        .formParam("searchPostcode", "true")
        .formParam("addressConfirmed", "false")
        .check(substring("Pick an address"))
        .check(regex("<option value=0 >1, CANONS COURT, STONEGROVE, EDGWARE, HA8 7ST</option>")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_180_AddressChooseSubmit") {
      exec(http("Address Choose")
        .post(BaseURL + "/petitioner-respondent/address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("selectAddressIndex", "0")
        .formParam("selectAddress", "true")
        .formParam("searchPostcode", "false")
        .formParam("addressConfirmed", "false")
        .formParam("postcode", "HA8 7ST")
        .check(substring("Address Line 0")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_190_AddressSubmit") {
      exec(http("Address")
        .post(BaseURL + "/petitioner-respondent/address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("addressLine0", "1, STONEGROVE")
        .formParam("addressLine1", "CANONS COURT")
        .formParam("addressLine2", "EDGWARE")
        .formParam("addressLine3", "HA8 7ST")
        .formParam("addressType", "postcode")
        .formParam("addressConfirmed", "true")
        .formParam("postcode", "HA8 7ST")
        .check(substring("Do you want your divorce papers sent to this address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_200_UseHomeAddressSubmit") {
      exec(http("Use Home Address")
        .post(BaseURL + "/petitioner-respondent/petitioner-correspondence/use-home-address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("petitionerCorrespondenceUseHomeAddress", "Yes")
        .formParam("submit", "Continue")
        .check(substring("live at the same address")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_210_LiveTogetherSubmit") {
      exec(http("Live Together")
        .post(BaseURL + "/petitioner-respondent/live-together")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("livingArrangementsLiveTogether", "Yes")
        .formParam("submit", "Continue")
        .check(substring("divorce papers should be sent")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_220_UseHomeAddressSubmit") {
      exec(http("Use Home Address")
        .post(BaseURL + "/petitioner-respondent/respondent-correspondence/use-home-address")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("respondentCorrespondenceUseHomeAddress", "Yes")
        .formParam("submit", "Continue")
        .check(substring("Choose a reason for your divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_230_ReasonSubmit") {
      exec(http("Reason")
        .post(BaseURL + "/about-divorce/reason-for-divorce/reason")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("reasonForDivorce", "separation-5-years")
        .formParam("submit", "Continue")
        .check(substring("When you separated")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_240_SeparationDatesSubmit") {
      exec(http("Separation Dates")
        .post(BaseURL + "/about-divorce/reason-for-divorce/separation-dates")
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
        .check(substring("Have you lived apart for the entire time since you separated")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_250_LivedApartSinceSubmit") {
      exec(http("Lived Apart Since")
        .post(BaseURL + "/about-divorce/reason-for-divorce/separation/lived-apart-since")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("livedApartEntireTime", "Yes")
        .formParam("submit", "Continue")
        .check(substring("Other court cases related to your marriage")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_260_LegalProceedingsSubmit") {
      exec(http("Legal Proceedings")
        .post(BaseURL + "/about-divorce/legal-proceedings")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("legalProceedings", "No")
        .formParam("legalProceedingsDetails", "")
        .formParam("submit", "Continue")
        .check(substring("Dividing your money and property")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_270_FinancialArrangementsSubmit") {
      exec(http("Financial Arrangements")
        .post(BaseURL + "/about-divorce/financial/arrangements")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("financialOrder", "No")
        .formParam("submit", "Continue")
        .check(substring("Do you want to apply to claim your divorce costs")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_280_ClaimCostsSubmit") {
      exec(http("Claim Costs")
        .post(BaseURL + "/about-divorce/claim-costs")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("claimsCosts", "No")
        .formParam("submit", "Continue")
        .check(substring("Upload your documents")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_290_DocumentUpload") {
      exec(http("Document Upload")
        .post(BaseURL + "/petitioner-respondent/marriage-certificate-upload?js=true&_csrf=${csrf}")
        .headers(CommonHeader)
        .headers(PostHeader)
        //do something here to upload document!
        .check(substring(""""status:"OK"""")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_300_DocumentUploadSubmit") {
      exec(http("Document Upload")
        .post(BaseURL + "/petitioner-respondent/marriage-certificate-upload")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("submit", "Continue")
        .check(substring("Check your answers|Equality and diversity questions")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_310_CheckYourAnswers") {
      exec(http("Check Your Answers")
        .get(BaseURL + "/check-your-answers")
        .headers(CommonHeader)
        .check(substring("Check your answers")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_320_CheckYourAnswersSubmit") {
      exec(http("Check Your Answers")
        .post(BaseURL + "/check-your-answers")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("confirmPrayer", "Yes")
        .formParam("submit", "Submit and pay")
        .check(substring("The application fee is")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_330_ProceedToPayment") {
      exec(http("Proceed To Payment")
        .post(BaseURL + "/pay/online")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("submit", "Continue")
        .check(substring("Enter card details")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_340_EnterCardDetails") {

      exec(http("Check Card")
        .post("https://www.payments.service.gov.uk/check_card/ps8al5n2vsg4ublqr7pt77on7q")
        .headers(CommonHeader)
        .headers(PostHeader)
        .header("sec-fetch-dest", "empty")
        .header("sec-fetch-mode", "cors")
        .formParam("cardNo", "4444333322221111"))

      .exec(http("Card Details")
        .post("https://www.payments.service.gov.uk/card_details/ps8al5n2vsg4ublqr7pt77on7q")
        .headers(CommonHeader)
        .headers(PostHeader)
        /*
        chargeId: ps8al5n2vsg4ublqr7pt77on7q
        csrfToken: yHTilbxL-Bmiwku7qifwN3Z6XiMA2qlv952g
        cardNo: 4444333322221111
        expiryMonth: 01
        expiryYear: 22
        cardholderName: Mr Perf Test
        cvc: 123
        addressCountry: GB
        addressLine1: 1 Perf Test Road
        addressLine2:
        addressCity: Perf Test Town
        addressPostcode: HA8 7ST
        email: kljdlfjhkjdshfd@dsfljldhjsfjkhs.com
         */
        .check(substring("Confirm your payment")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("DivorceApp_340_ConfirmPayment") {
      exec(http("Confirm Payment")
        .post("https://www.payments.service.gov.uk/card_details/ps8al5n2vsg4ublqr7pt77on7q/confirm")
        .headers(CommonHeader)
        .headers(PostHeader)
        /*
        csrfToken: m5hg25le-sLSH2N1VioMELqPOSyA0ZS35780
        chargeId: ps8al5n2vsg4ublqr7pt77on7q
         */
        /* APPLICATION NUMBER:

              <strong class="govuk-body-reference-number" aria-label="1 6 1 1 6 8 5 9 6 8 5 2 2 1 9 0">
        1611 &#8208; 6859 &#8208; 6852 &#8208; 2190
      </strong>


         */
        .check(substring("Application complete")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

  }


}
