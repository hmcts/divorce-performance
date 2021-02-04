package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CsrfCheck, Environment}

import scala.concurrent.duration._

object Divorce_5PetitionerDecreeNisi {

  val DecreeNisiURL = Environment.decreeNisiURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val DecreeNisi =

    /*
    Petitioner AOS/DN questions
     */

    group("Div5PetDN_010_ProgressBar") {
      exec(http("Progress Bar")
        .post(DecreeNisiURL + "/progress-bar/petitioner")
        .headers(CommonHeader)
        .headers(PostHeader)
        .check(CsrfCheck.save)
        .check(substring("Acknowledgement of service")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_020_ReviewAOSResponse") {
      exec(http("Review AOS Response")
        .post(DecreeNisiURL + "/review-aos-response")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("reviewAosResponse", "yes")
        .check(CsrfCheck.save)
        .check(substring("Do you want to continue with your divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_030_ContinueWithDivorce") {
      exec(http("Continue With Divorce")
        .post(DecreeNisiURL + "/continue-with-divorce")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("applyForDecreeNisi", "yes")
        .check(CsrfCheck.save)
        .check(substring("Review your application")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_040_ReviewYourPetitionSubmit") {
      exec(http("Review Your Petition")
        .post(DecreeNisiURL + "/review-your-petition")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("changes.hasBeenChanges", "no")
        .formParam("changes.changesDetails", "")
        .formParam("changes.statementOfTruthNoChanges", "yes")
        .check(CsrfCheck.save)
        .check(substring("Have you lived apart since you applied")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_050_LivedApartSubmit") {
      exec(http("Lived Apart")
        .post(DecreeNisiURL + "/lived-apart-since-separation")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("changes.livedApartSinceSeparation", "no")
        .formParam("changes.approximateDatesOfLivingTogetherField", "")
        .check(CsrfCheck.save)
        .check(substring("Do you need to upload any other documents")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_060_ShareCourtDocumentsSubmit") {
      exec(http("Share Court Documents")
        .post(DecreeNisiURL + "/share-court-documents")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("upload", "no")
        .check(CsrfCheck.save)
        .check(substring("Check your answers")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div5PetDN_070_CheckYourAnswersSubmit") {
      exec(http("Check Your Answers")
        .post(DecreeNisiURL + "/check-your-answers")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("statementOfTruth", "yes")
        .check(substring("Application complete")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
