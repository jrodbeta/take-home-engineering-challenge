package service

import model.foodtruck.{FoodTruck, Location}
import org.apache.lucene.document.{Document, NumericDocValuesField, StoredField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig}
import org.apache.lucene.search.{IndexSearcher, MatchAllDocsQuery, ScoreDoc, Sort}
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy
import org.locationtech.spatial4j.context.SpatialContext
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree
import org.apache.lucene.store.ByteBuffersDirectory
import org.locationtech.spatial4j.distance.DistanceUtils
import org.locationtech.spatial4j.shape.Point

/**
 * In memory geospatial search using an in memory lucene
 * datastore under the hood.
 *
 * I'm not super experienced with geospatial searches so a lot
 * of this is really from lucene's example:
 * https://github.com/apache/lucene-solr/blob/master/lucene/spatial-extras/src/test/org/apache/lucene/spatial/SpatialExample.java
 *
 * @param foodTrucks Food trucks to index
 */
class SearchService(foodTrucks: List[FoodTruck]) {
  /**
   * MaxLevels at 11 gives us "sub-meter" precision which seems
   * suitable for the given problem (Food trucks can be right next to each other)
   */
  private val MaxLevels = 11

  private val ctx = SpatialContext.GEO
  private val grid = new GeohashPrefixTree(ctx, MaxLevels)
  private val strategy = new RecursivePrefixTreeStrategy(grid, "GeoTree")
  // Heap based directory buffer (in memory indexing)
  private val directory = new ByteBuffersDirectory()
  private val foodTrucksById: Map[Int, FoodTruck] =
    foodTrucks.map(f => (f.id, f)).toMap

  {
    val indexWriter =
      new IndexWriter(directory, new IndexWriterConfig())

    foodTrucks.foreach( foodTruck => {
      indexWriter.addDocument(
        newDocument(foodTruck)
      )
    })
    indexWriter.close()
  }

  /**
   * Create lucene document from domain entity FoodTruck
   * @param foodTruck
   * @return Document to put in index
   */
  private def newDocument(foodTruck: FoodTruck): Document = {
    val doc = new Document
    doc.add(new StoredField("id", foodTruck.id))
    doc.add(new NumericDocValuesField("id", foodTruck.id))


    val shape = toPoint(foodTruck.location)
    strategy.createIndexableFields(shape)
        .foreach(doc.add)

    doc.add(new StoredField(strategy.getFieldName, s"${shape.getX} ${shape.getY}"))

    doc
  }

  /**
   * Convert domain location object to spatial point
   * @param location
   * @return
   */
  private def toPoint(location: Location): Point = {
    // Long is X, Lat is Y
    ctx.getShapeFactory.pointXY(
      location.longitude,
      location.latitude
    )
  }

  /**
   * Returns list of foodtrucks sorted by nearest to
   * location provided.
   * @param location Point of reference to start search from
   * @param maxResults Max # of results to return
   * @return
   */
  def search(location: Location, maxResults: Int): List[FoodTruck] = {
    val indexReader = DirectoryReader.open(directory)
    val indexSearcher = new IndexSearcher(indexReader)

    val point = toPoint(location)
    val source = strategy.makeDistanceValueSource(point, DistanceUtils.DEG_TO_KM)

    val sort = new Sort(source.getSortField(false)).rewrite(indexSearcher)

    val docs = indexSearcher.search(new MatchAllDocsQuery(), maxResults, sort)

    docs.scoreDocs.toList.iterator.map((doc: ScoreDoc) => {
      val id = indexSearcher.doc(doc.doc).getField("id")
      foodTrucksById(id.numericValue().intValue())
    }).toList
  }

}
