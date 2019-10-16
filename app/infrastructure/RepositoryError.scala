package infrastructure


sealed trait RepositoryError {
  val message: String
}
case class CSVError(message: String) extends RepositoryError
