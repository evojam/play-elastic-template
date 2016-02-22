package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.Logger
import play.api.mvc.{Action, Controller}

import com.sksamuel.elastic4s.ElasticDsl

import com.evojam.play.elastic4s.PlayElasticFactory
import com.evojam.play.elastic4s.configuration.ClusterSetup


class ElasticController @Inject() (cs: ClusterSetup, elasticFactory: PlayElasticFactory) extends
    Controller with ElasticDsl {

  private val logger = Logger(getClass)
  private lazy val client = elasticFactory(cs)

  def getStats = Action.async {
    try {
      client execute {
        get cluster stats
      } map (response => Ok(response.toString))
    } catch {
      case e: Exception =>
        logger.error("Error connecting to Elasticsearch", e)
        Future(InternalServerError("Error connecting to Elasticsearch. Is application.conf filled in properly?\n"))
    }
  }

  def createIndex = Action.async {
    client execute {
      create index "library" replicas 0
    } map { _ => Ok("Index created") }
  }
}
