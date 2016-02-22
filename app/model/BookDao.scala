package model

import javax.inject.{Named, Inject}

import scala.concurrent.{ExecutionContext, Future}

import com.sksamuel.elastic4s.{IndexAndType, ElasticDsl}

import com.evojam.play.elastic4s.configuration.ClusterSetup
import com.evojam.play.elastic4s.{PlayElasticFactory, PlayElasticJsonSupport}

class BookDao @Inject()(cs: ClusterSetup, elasticFactory: PlayElasticFactory, @Named("book") indexAndType: IndexAndType)
    extends ElasticDsl with PlayElasticJsonSupport {

  private[this] lazy val client = elasticFactory(cs)
  def getBookById(bookId: String)(implicit ec: ExecutionContext): Future[Option[Book]] = client execute {
    get id bookId from indexAndType
  } map (_.as[Book])
  // the above .as[Book] conversion is available as an extension method
  // provided by PlayElasticJsonSupport
  
  def indexBook(bookId: String, book: Book) = client execute {
    index into indexAndType source book id bookId
  }
  // original elastic4s .source(doc) expects a DocumentSource or T : Indexable.
  // PlayElasticJsonSupport provides Indexable[T] for any T with Json.Writes[T] available.

  def bulkIndex(books: Iterable[Book]) = client execute {
    bulk {
      books map (book => index into indexAndType source book)
    }
  }

  def searchByQueryString(q: String)(implicit ec: ExecutionContext) = client execute {
    search in indexAndType query queryStringQuery(q)
  } map (_.as[Book])
  // the .as[T] conversion is available in elastic4s for any T with HitAs[T] instance available.
  // PlayElasticJsonSupport automatically derives HitAs[T] based on Json.Reads[T].


}
