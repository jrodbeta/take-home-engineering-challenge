package infrastructure

import scala.io.Source

/**
 * FileSource trait that allows us to inject and mock.
 * Only encapsulates a few simple commands for our
 * POC
 */
trait FileSource {
  def getPayload(): String
}

class FileSourceImpl extends FileSource {
  override def getPayload(): String = {
    Source.fromFile("Mobile_Food_Facility_Permit.csv").toList.mkString
  }
}
