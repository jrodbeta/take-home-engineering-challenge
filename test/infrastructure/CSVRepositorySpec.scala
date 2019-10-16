package infrastructure

import model.foodtruck.{Approved, FoodTruck, Location}
import org.scalatest.{FunSpec, Matchers}
import org.scalatestplus.mockito.MockitoSugar
import util.csv.Parser
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when

class CSVRepositorySpec extends FunSpec with Matchers with MockitoSugar {

  describe("CSVRepository") {
    describe("getFoodTrucks") {
      it("should return trucks when valid csv is provided") {
        val id = 123
        val applicant = "Jose"
        val status = "APPROVED"
        val latitude = 12L
        val longitude = 34L

        val csv = List(
          List[String](
            id.toString, applicant, "", "", "", "",
            "", "", "", "", "", status,
            "", "", "", latitude.toString, longitude.toString, "",
            "", "", "", "", "", ""
          )
        )

        val source = mock[FileSource]
        val parser = mock[Parser]

        when(parser.parseString(any(), any())).thenReturn(csv)

        val result = new CSVRepository(source, parser).getFoodTrucks()

        result shouldBe Right(
          List(
            FoodTruck(
              id,
              applicant,
              Approved,
              Location(
                latitude,
                longitude
              )
            )
          )
        )
      }
    }

  }

}
