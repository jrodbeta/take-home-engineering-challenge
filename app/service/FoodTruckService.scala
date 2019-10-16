package service

import infrastructure.{CSVRepository, LocalFileSource}
import model.foodtruck.{FoodTruck, Location}
import org.slf4j.LoggerFactory
import util.csv.Parser

/**
 * Singleton to compose interactions between infrastructure
 * and other services.
 */
object FoodTruckService {
  private val MaxResults = 5
  private val logger = LoggerFactory.getLogger(this.getClass)

  private val foodTrucks: List[FoodTruck] =
    new CSVRepository(new LocalFileSource(), new Parser())
    .getFoodTrucks()
    .fold({ errors =>
      logger.error(s"Failed to retrieve food trucks $errors")
      List.empty
    }, identity)

  val searchService = new SearchService(foodTrucks)

  /**
   * Search closest trucks to location provided.
   * @param location lat and longitude to search from
   * @return
   */
  def search(location: Location): List[FoodTruck] = {
    searchService.search(location, MaxResults)
  }

  /**
   * Returns all of the food trucks available
   * @return
   */
  def getAll: List[FoodTruck] = {
    foodTrucks
  }
}
