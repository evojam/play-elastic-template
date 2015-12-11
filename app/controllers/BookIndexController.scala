package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future.successful

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import org.joda.time.DateTime

import com.evojam.play.elastic4s.client.ElasticSearchClient
import com.google.inject.Inject
import com.google.inject.name.Named
import com.sksamuel.elastic4s.IndexType

import model.Book

class BookIndexController @Inject()(elastic: ElasticSearchClient, @Named("books") books: IndexType)
  extends Controller {

  def index() = Action.async(parse.json[Book]) { implicit request =>
    elastic.index(books, request.body.isbn, request.body)
      .map {
        case true => Ok(Json.toJson(request.body))
        case false => NotFound
      }
  }

  def remove(id: String) = Action.async {
    elastic.remove(books, id).map {
      case true => NoContent
      case false => NotFound
    }
  }

  def update(id: String) = Action.async(parse.json[Book]) { implicit request =>
    elastic.update(books, id, request.body).map {
      case true => Ok
      case false => NotFound
    }
  }

  def bulkInsert() = Action.async { implicit request =>
    elastic.bulkInsert(books, bulkDocuments).map(_.hasFailures).map {
      case true => BadRequest
      case false => Created
    }
  }

  val bulkDocuments = List(
    Book(
      "978-0486298238",
      "Meditations",
      "Marcus Aurelius",
      "One of the world's most famous and influential books, Meditations, by the Roman emperor Marcus Aurelius" +
      "(A.D. 121–180), incorporates the stoic precepts he used to cope with his life as a warrior " +
      "and administrator of an empire.",
      "Dover Publications",
      DateTime.parse("1997-07-11").toDateTimeISO,
      Set("Philosophy", "Classics", "Nonfiction")),
    Book(
      "978-0451524935",
      "1984",
      "George Orwell",
      "The year 1984 has come and gone, but George Orwell's prophetic, nightmarish vision in 1949 of" +
      "the world we were becoming is timelier than ever.",
      "Signet Classics",
      DateTime.parse("1950-07-01").toDateTimeISO,
      Set("Politics", "Fiction")),
    Book(
      "978-0547928227",
      "The Hobbit",
      "J.R.R. Tolkien",
      "Bilbo Baggins is a hobbit who enjoys a comfortable, unambitious life, rarely traveling any farther than" +
      "his pantry or cellar. But his contentment is disturbed when the wizard Gandalf and a company of dwarves" +
      "arrive on his doorstep one day to whisk him away on an adventure. They have launched a plot to raid the" +
      "treasure hoard guarded by Smaug the Magnificent, a large and very dangerous dragon. Bilbo reluctantly joins" +
      "their quest, unaware that on his journey to the Lonely Mountain he will encounter both a magic ring and a" +
      "frightening creature known as Gollum.",
      "Houghton Mifflin Harcourt",
      DateTime.parse("2012-11-18").toDateTimeISO,
      Set("Fantasy", "Fiction")
    ))
}