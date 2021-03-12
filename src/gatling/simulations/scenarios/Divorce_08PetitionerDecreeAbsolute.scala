package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CsrfCheck, Environment}

import scala.concurrent.duration._

object Divorce_08PetitionerDecreeAbsolute {

  val DecreeAbsoluteURL = Environment.decreeAbsoluteURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val DecreeAbsolute =

    /*
    Petitioner DA questions
     */

    group("Div08PetDA_010_ProgressBar") {
      exec(http("Progress Bar")
        .post(DecreeAbsoluteURL + "/progress-bar/petitioner")
        .headers(CommonHeader)
        .headers(PostHeader)
        .check(CsrfCheck.save)
        .check(substring("Do you want to finalise your divorce")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

    .group("Div08PetDA_020_ContinueWithDivorceSubmit") {
      exec(http("Continue With Divorce")
        .post(DecreeAbsoluteURL + "/continue-with-divorce")
        .headers(CommonHeader)
        .headers(PostHeader)
        .formParam("_csrf", "${csrf}")
        .formParam("applyForDecreeAbsolute", "yes")
        .check(substring("Your application for Decree Absolute is being processed")))
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)

}
