package scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import utils.{Environment, Common}

object CreateUser {

  val IdamAPIURL = Environment.idamAPIURL

  val newUserFeeder = Iterator.continually(Map(
    "emailAddress" -> ("perftest" + Common.getDate() + "@perftest-" + Common.randomString(10) + ".com"),
    "password" -> "Pa55word11",
    "role" -> "citizen"
  ))

  val CreateCitizen =
    feed(newUserFeeder)
      .group("Divorce_000_CreateCitizen") {
        exec(http("CreateCitizen")
          .post(IdamAPIURL + "/testing-support/accounts")
          .body(ElFileBody("bodies/CreateUserTemplate.json")).asJson
          .check(status.is(201)))
      }

      .exec {
        session =>
          println("EMAIL: " + session("emailAddress").as[String])
          println("PASSWORD: " + session("password").as[String])
          println("ROLE: " + session("role").as[String])
          session
      }

}
