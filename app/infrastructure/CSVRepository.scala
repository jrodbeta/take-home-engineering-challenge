package infrastructure

import model.foodtruck.{Approved, Expired, FoodTruck, Inactive, Issued, Location, OnHold, Requested, Status, Suspend}
import util.csv.Parser

import scala.util.{Failure, Success, Try}

class CSVRepository(fileSource: FileSource, parser: Parser) {
  val ExpectedCols = 24

  private object ColumnIds {
    // Starting at 0 because we're computer scientists
    val Id = 0
    val Applicant = 1
    val Status = 11
    val Latitude = 15
    val Longitude = 16
  }

  def getFoodTrucks(): Either[List[RepositoryError], List[FoodTruck]] = {
    val payload = fileSource.getPayload()
    val rawRecords = parser.parseString(payload, true)

    val foodTrucksOrErrors = for {
      record <- rawRecords
    } yield {
      if (record.length != ExpectedCols) {

        // If our columns are off this won't work
        Left(CSVError("Invalid # of cols"))
      } else {

        // Try to parse out our CSV options.
        Try {
          FoodTruck(
            id = record(ColumnIds.Id).toInt,
            applicant = record(ColumnIds.Applicant),
            status = toStatus(record(ColumnIds.Status)),
            Location(
              latitude = record(ColumnIds.Latitude).toLong,
              longitude = record(ColumnIds.Longitude).toLong
            )
          )
        } match {
          case Success(foodTruck) => Right(foodTruck)
          case Failure(exception) =>
            Left(CSVError(s"Converting record threw exception: ${exception.getMessage}"))
        }
      }
    }

    val (errors, foodTrucks) = foodTrucksOrErrors.partition(_.isLeft)

    if(errors.nonEmpty) {
      Left(errors.iterator.map(_.left.toSeq).toList.flatten)
    } else {
      Right(foodTrucks.iterator.map(_.toSeq).toList.flatten)
    }
  }

  private def toStatus(status: String): Status = {
    status match {
      case "APPROVED" => Approved
      case "EXPIRED" => Expired
      case "INACTIVE" => Inactive
      case "ISSUED" => Issued
      case "ONHOLD" => OnHold
      case "REQUESTED" => Requested
      case "SUSPEND" => Suspend
    }
  }
}
