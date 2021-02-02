package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.Environment

object DeleteUser {

  val IdamAPIURL = Environment.idamAPIURL

  def DeleteCitizen (emailAddressToDelete: String)= {

    group("Divorce_000_DeleteCitizen") {
      exec(http("DeleteCitizen")
        .delete(IdamAPIURL + s"/testing-support/accounts/${emailAddressToDelete}")
        .check(status.is(204)))
    }

  }
}
