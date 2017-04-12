package com.neo.sk.map.service

import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.Directives._
import com.neo.sk.map.ptcl._
import com.neo.sk.utils.{Dijkstra, HttpUtil}
import akka.http.scaladsl.server.Directives._

import io.circe.Error
import org.slf4j.LoggerFactory


/**
  * Created by zm on 2017/4/12.
  */
trait MapService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._
  private val log = LoggerFactory.getLogger(this.getClass)

  private val text=(path("text") & post & pathEndOrSingleSlash){
      entity(as[Either[Error,Text]]){
        case Right(info) =>
          log.info(s"get question update data: $info")
          val INF=Integer.MAX_VALUE
          log.debug("1")
          val Nodes=Array('0','1','2','3')
          log.debug("2")
          val matrix=Array(Array(0,1,2,1),Array(INF,0,INF,INF),Array(INF,3,0,1),Array(INF,1,1,0))
          log.debug("3")
          val dist:Array[Int]=Array[Int](Nodes.length)
          log.debug("4")
          val dijkstra=new Dijkstra(Nodes,matrix)
          log.debug("5")
          dijkstra.dijkstra(2,1)
          val num=dijkstra.dijkstra(2,1)
          log.debug("6")
          log.debug(num+"")
          complete(TextRsp(0,"ok",num))
        case Left(e) =>
          complete("error")
      }

  }





  val mapRoutes = pathPrefix("map") {
    log.debug("start***")
    (path("home")  & get) {
      getFromResource("html/map.html")
    }~text
  }
}
