package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CsrfCheck, Environment}

import scala.concurrent.duration._

object Divorce_8PetitionerDownloadDA {

  val DecreeAbsoluteURL = Environment.decreeAbsoluteURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val DownloadDA = {

    /*
    Petitioner download Decree Absolute PDF
     */

    group("Div8PetDA_010_DownloadDAPDFSubmit") {
      exec(http("Download DA PDF")
        .get(DecreeAbsoluteURL + "/document-download/certificateOfEntitlement${DaPdfCode}.pdf")
        .headers(CommonHeader)
        .check(bodyString.transform(_.size > 40000).is(true))) //Check PDF is at least 40kb in size
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}
