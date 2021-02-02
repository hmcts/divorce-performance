package utils

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object Environment {

  val petitionerURL = "https://petitioner-frontend-aks.aat.platform.hmcts.net"
  val respondentURL = "https://respond-divorce-aks.aat.platform.hmcts.net"
  val decreeNisiURL = "https://decree-nisi-aks.aat.platform.hmcts.net"
  val decreeAbsoluteURL = "https://decree-absolute-aks.aat.platform.hmcts.net"

  val idamURL = "https://idam-web-public.aat.platform.hmcts.net"
  val idamAPIURL = "https://idam-api.aat.platform.hmcts.net"

  val paymentURL = "https://www.payments.service.gov.uk"

  val minThinkTime = 1
  val maxThinkTime = 1

  val HttpProtocol = http

  val commonHeader = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "en-GB,en;q=0.9",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "same-origin",
    "sec-fetch-user" -> "?1",
    "upgrade-insecure-requests" -> "1",
    "user-agent" -> "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36"
  )

  val postHeader = Map(
    "content-type" -> "application/x-www-form-urlencoded"
  )

}
