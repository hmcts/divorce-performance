package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{CsrfCheck, Environment}

import scala.concurrent.duration._

object Divorce_10PetitionerDownloadDA {

  val DecreeAbsoluteURL = Environment.decreeAbsoluteURL

  val MinThinkTime = Environment.minThinkTime
  val MaxThinkTime = Environment.maxThinkTime

  val CommonHeader = Environment.commonHeader
  val PostHeader = Environment.postHeader

  val DownloadDA = {

    /*
    Petitioner download Decree Absolute PDF
     */

    group("Div10PetDA_010_DownloadDAPDF") {
      exec(http("Download DA PDF")
        .get(DecreeAbsoluteURL + "/document-download/decreeAbsolute${appId}.pdf")
        .headers(CommonHeader)
        .check(bodyString.transform(_.size > 60000).is(true))) //Check PDF is at least 60kb in size
    }

    .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}
