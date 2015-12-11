package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import com.evojam.play.elastic4s.client.ElasticSearchClient
import com.google.inject.Inject
import com.google.inject.name.Named
import com.sksamuel.elastic4s.{IndexType, SearchDefinition, ElasticDsl}

import model.Book

class BookSearchController @Inject()(elastic: ElasticSearchClient, @Named("books") books: IndexType)
  extends Controller {

  object Query extends ElasticDsl {
    def authorQuery(fullName: String): SearchDefinition =
      search in books query termQuery("author", fullName)
  }

  def search() = Action.async {
    elastic.search(Query.authorQuery("marcus"))
      .collect[Book]
      .map(_.map(_.title))
      .map(Json.toJson(_))
      .map(Ok(_))
  }

}
