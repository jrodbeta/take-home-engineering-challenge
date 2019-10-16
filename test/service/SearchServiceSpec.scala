package service

import model.foodtruck.{Approved, FoodTruck, Location}
import org.scalatest.{FunSpec, Matchers}

class SearchServiceSpec extends FunSpec with Matchers {
  describe("SearchService") {
    describe("search") {
      it("works for basic case because first time using lucene") {
        val foodTrucks = List(
          FoodTruck(
            123,
            "jose",
            Approved,
            Location(1, 1)
          )
        )

        val service = new SearchService(foodTrucks)
        val results = service.search(
          Location(1, 1),
          maxResults = 10
        )


      }

      it("returns closest results of max results") {

        // Create 3 trucks,
        // two closer to 1,1
        // then return expected max 2
        val expectedTruck1 = FoodTruck(
          12,
          "jose",
          Approved,
          Location(1, 1)
        )

        val expectedTruck2 =
          FoodTruck(
            34,
            "kyle",
            Approved,
            Location(2, 1)
          )

        val foodTrucks = List(
          expectedTruck1,
          expectedTruck2
          ,
            FoodTruck(
            56,
            "stan",
            Approved,
            Location(3, 1)
          )
        )

        val service = new SearchService(foodTrucks)
        val results = service.search(
          Location(1, 1),
          maxResults = 2
        )

        results shouldBe List(expectedTruck1, expectedTruck2)
      }
    }
  }

}
