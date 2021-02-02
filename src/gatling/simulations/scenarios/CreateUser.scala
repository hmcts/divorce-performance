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

  //takes an userType e.g. petitioner/respondent, to create unique users for each user
  def CreateCitizen(userType: String) = {
    feed(newUserFeeder)
      .group("Divorce_000_CreateCitizen") {
        exec(http("CreateCitizen")
          .post(IdamAPIURL + "/testing-support/accounts")
          .body(ElFileBody("bodies/CreateUserTemplate.json")).asJson
          .check(status.is(201)))
      }

    //storing each username/password in the session to be used later (for re-logging in and deleting accounts)
    .exec {
      session =>
        session
          .set(s"${userType}EmailAddress", session("emailAddress").as[String])
          .set(s"${userType}Password", session("password").as[String])
    }

      .exec {
        session =>
          println(s"${userType} Email: " + session(s"${userType}EmailAddress").as[String])
          println(s"${userType} Password: " + session(s"${userType}Password").as[String])
          println(s"${userType} Role: " + session("role").as[String])
          session
      }
  }

}
