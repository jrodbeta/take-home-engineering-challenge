package controllers

import javax.inject._
import model.foodtruck.{FoodTruck, Location}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import service.FoodTruckService

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class FoodTruckController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Returns all Food Trucks or trucks closest to provided latitude and longitude
   */
  def index(lat: Option[Long], long: Option[Long]) = Action { implicit request: Request[AnyContent] =>
    (lat, long) match {
      case (Some(lat), Some(long)) =>
        Ok(serializeFoodTrucks(
          FoodTruckService.search(Location(latitude = lat, longitude = long))
        ))
      case (None, None) =>
        Ok(serializeFoodTrucks(
          FoodTruckService.getAll
        ))
      case _ =>
        BadRequest(Json.obj("message" -> "Both long and lat are required when searching by location"))

    }
  }

  private def serializeFoodTrucks(foodTrucks: List[FoodTruck]): JsObject = {
    Json.obj(
      "food-trucks" -> Json.arr(
        foodTrucks.map { truck =>
          Json.obj(
            "id" -> truck.id,
            "applicant" -> truck.applicant,
            "status" -> truck.status.toString.toUpperCase,
            "latitude" -> truck.location.latitude,
            "longitude" -> truck.location.longitude
          )
        }
      )
    )
  }
}
