package com.neo.sk.map.service

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directive, RequestContext}
import akka.http.scaladsl.server
import com.neo.sk.utils.SessionSupport
import com.neo.sk.map.common.AppSettings
import com.neo.sk.map.models.dao._
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.util.Timeout
import com.neo.sk.map.models._
import com.neo.sk.map.ptcl
import com.neo.sk.map.service.SessionBase.UserSession
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

/**
  * Created by mengmengda on 2017/2/9.
  */
trait AuthService extends BaseService with SessionBase{
  import com.neo.sk.map.ptcl
  import io.circe.generic.auto._

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701a.http.AuthService")

  private val sessionTimeOut = 24 * 60 * 60 * 1000


  object SessionKey {
    val userId = "uid"
    val name = "username"
    val timestamp = "loginTime"
  }


  def loggingAction: Directive[Tuple1[RequestContext]] = extractRequestContext.map { ctx =>
    log.info(s"Access uri: ${ctx.request.uri} from ip ${ctx.request.uri.authority.host.address}.")
    ctx
  }


  def dealFutureResult(future: ⇒ Future[server.Route]) = onComplete(future) {
    case Success(route) =>
      route
    case Failure(e) =>
      e.printStackTrace()
      log.warn("internal error: {}", e.getMessage)
      complete(ptcl.CommonRsp(100000, "Internal error."))
  }

  def CompanyAction(f: Company => server.Route) = loggingAction { ctx =>
    val companyUrl = "/hw1701a/company/login"
    optionalSession{
      case Right(session) =>
        try{
          val uId = session(SessionKey.userId).toLong
          val name = session(SessionKey.name)
          val timestamp = session(SessionKey.timestamp).toLong
          log.debug("",uId,name)
          if(System.currentTimeMillis() - timestamp > sessionTimeOut) {
            log.info("Login failed fot TimeOut.")
            redirect(companyUrl, StatusCodes.SeeOther)
          }
          else {
            dealFutureResult {
              CompanyDao.isCompanyExists(uId.toInt).map {
                case true =>
                  f(Company(uId, name, timestamp))

                case false =>
                  redirect(companyUrl, StatusCodes.SeeOther)
              }
            }
          }
        }catch {
          case ex: Exception =>
            log.info(s"Not Login Yet, ex: $ex")
            redirect(companyUrl, StatusCodes.SeeOther)
        }

      case _ =>
        log.debug("no session...")
        redirect(companyUrl, StatusCodes.SeeOther)
    }
  }


}
