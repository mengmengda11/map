package com.neo.sk.map.service

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{Materializer, OverflowStrategy}
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor

/**
  * User: Taoz
  * Date: 8/26/2016
  * Time: 10:27 PM
  */
trait HttpService extends ResourceService
                          with TestService
                          with MapService
                          with CompanyService
                          with CounterService {


//  implicit val system: ActorSystem
//
//  implicit val executor: ExecutionContextExecutor
//
//  implicit val materializer: Materializer
//
//  implicit val timeout: Timeout


  val routes: Route =
    pathPrefix("hw1701a") {
      resourceRoutes ~ testRoute ~ counterRouter~mapRoutes~companyRoutes
    }




}
