package model

import play.api.libs.json.Json

import org.joda.time.DateTime

case class Book(isbn: String, title: String, author: String, blurb: String, publisher: String,
  publishDate: DateTime, genres: Set[String]) {

  require(isbn != null, "id cannot be null")
  require(title != null, "title cannot be null")
  require(author != null, "author cannot be null")
  require(blurb != null, "blurb cannot be null")
  require(publishDate != null, "publishDate cannot be null")
  require(genres != null, "genres cannot be null")

}

object Book {
  implicit val format = Json.format[Book]
}
