package util.csv

import org.apache.commons.csv.{CSVFormat, CSVParser, CSVRecord}

import scala.jdk.CollectionConverters._

/**
 * CSV can get pretty complicated so going to outsource this to
 * a well known open library and abstract for my needs here.
 */
class Parser {

  /**
   * Parses csv payload a multi-dimensional list
   * @param payload CSV payload to be parsed
   * @param skipHeader Set to true to skip first line of headers
   * @return
   */
  def parseString(payload: String, skipHeader: Boolean): List[List[String]] = {
    val records = CSVParser.parse(payload, CSVFormat.DEFAULT)
      .getRecords
      .asScala
      .toList
      .map(_.iterator().asScala.toList)

    if(skipHeader) records.tail
    else records
  }
}
