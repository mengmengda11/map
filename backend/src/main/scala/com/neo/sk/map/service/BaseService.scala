package com.neo.sk.map.service

import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import akka.util.Timeout
import com.neo.sk.utils.CirceSupport

import scala.concurrent.ExecutionContextExecutor

/**
  * Created by zm on 2017/4/12.
  */

import scala.concurrent.ExecutionContextExecutor
/**
  * Created by mengmengda on 2017/2/9.
  */
trait BaseService extends CirceSupport{

  implicit val system: ActorSystem

  implicit val executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  implicit val timeout: Timeout

  val companyActor: ActorRef


}
