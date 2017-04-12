package com.neo.sk.map.service

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.neo.sk.map.service.BaseService
import com.neo.sk.utils.{CirceSupport, SessionSupport}
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by zm on 2017/4/12.
  */
trait AuthService extends BaseService with SessionSupport with CirceSupport{

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val sessionTimeOut = 24l * 60 * 60 * 1000
  private[this] val userSessionTimeOut = 24l * 60 * 60 * 1000

  object SessionKey{
    val accountType = "account_type"
    val accountId = "account_id"
    val account = "account"
    val timestamp = "time_stamp"

    val headImg = "head_img"
    val nickName = "nick_name"
  }

  object UserSessionKey{
    //    val userType = "user_type"
    val userState = "user_state"
    val userSessionId = "user_session_id"
    val userServiceId = "user_service_id"
    val userTimestamp = "user_time_stamp"
    val userRandomMark = "user_random_mark"
    val userRemoteIp = "user_remote_ip"
  }

  def loggingAction: Directive[Tuple1[RequestContext]] = extractRequestContext.map{ ctx =>
    log.info(s"Access uri ${ctx.request.uri} from ip ${ctx.request.uri.authority.host.address()}.")
    ctx
  }

  def dealFutureResult(f: => Future[Route]) = onComplete(f){
    case Success(route) => route
    //    case Failure(x: DeserializationException) =>
    //      reject(ValidationRejection(x.getMessage, Some(x)))
    case Failure(e) =>
      e.printStackTrace()
      log.warn(s"internal error: ${e.getMessage}")
      complete("internalErr")
  }






}
