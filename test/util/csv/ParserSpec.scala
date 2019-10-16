package util.csv

import org.scalatest.{FunSpec, Matchers}

class ParserSpec extends FunSpec with Matchers {
  describe("Parser") {
    describe("parseString") {
      it("parses basic csv payload") {
        val csvPayload =
          """abc,123,false
            |def,456,true
            |""".stripMargin.trim

        val result = new Parser().parseString(csvPayload, false)
        result shouldBe List(
          List("abc", "123", "false"),
          List("def", "456", "true")
        )
      }

      it("parses basic csv payload that includes header") {
        val csvPayload =
          """title,id,is
            |abc,123,false
            |def,456,true
            |""".stripMargin.trim

        val result = new Parser().parseString(csvPayload, true)
        result shouldBe List(
          List("abc", "123", "false"),
          List("def", "456", "true")
        )
      }
    }
  }

}
