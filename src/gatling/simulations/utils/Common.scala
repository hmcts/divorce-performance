package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

object Common {

  val rnd = new Random()
  val now = LocalDate.now()
  val patternDay = DateTimeFormatter.ofPattern("dd")
  val patternMonth = DateTimeFormatter.ofPattern("MM")
  val patternYear = DateTimeFormatter.ofPattern("yyyy")
  val patternDate = DateTimeFormatter.ofPattern("yyyyMMdd")

  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }

  def getDate(): String = {
    now.format(patternDate)
  }

  def getDay(): String = {
    (1 + rnd.nextInt(28)).toString.format(patternDay)
  }

  def getMonth(): String = {
    (1 + rnd.nextInt(12)).toString.format(patternMonth)
  }

  //15-39 years ago
  def getMarriageYear(): String = {
    now.minusYears(15 + rnd.nextInt(40)).format(patternYear)
  }

  //6-14 years ago
  def getSeparationYear(): String = {
    now.minusYears(6 + rnd.nextInt(9)).format(patternYear)
  }

  def getPostcode(): String = {
    randomString(2).toUpperCase() + rnd.nextInt(10).toString + " " + rnd.nextInt(10).toString + randomString(2).toUpperCase()
  }

}