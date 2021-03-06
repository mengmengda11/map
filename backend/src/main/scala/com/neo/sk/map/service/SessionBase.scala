package com.neo.sk.map.service

import akka.http.scaladsl.server.{Directive0, Directive1}
import akka.http.scaladsl.server.directives.BasicDirectives
import com.neo.sk.map.common.AppSettings
import com.neo.sk.utils.SessionSupport
import org.slf4j.LoggerFactory

/**
  * User: Taoz
  * Date: 12/4/2016
  * Time: 7:57 PM
  */

object SessionBase {
  private val logger = LoggerFactory.getLogger(this.getClass)

  val SessionTypeKey = "STKey"

  object UserSessionKey {
    val SESSION_TYPE = "userSession"
    val uid = "uid"
    val loginTime = "loginTime"
  }

  case class UserSession(
    uid: Long,
    loginTime: Long
  ) {
    def toSessionMap = Map(
      SessionTypeKey -> UserSessionKey.SESSION_TYPE,
      UserSessionKey.uid -> uid.toString,
      UserSessionKey.loginTime -> loginTime.toString
    )
  }

  implicit class SessionTransformer(sessionMap: Map[String, String]) {
    def toUserSession: Option[UserSession] = {
      logger.debug(s"toUserSession: change map to session, ${sessionMap.mkString(",")}")
      try {
        if (sessionMap.get(SessionTypeKey).exists(_.equals(UserSessionKey.SESSION_TYPE))) {
          Some(UserSession(
            sessionMap(UserSessionKey.uid).toLong,
            sessionMap(UserSessionKey.loginTime).toLong
          ))
        } else {
          logger.debug("no session type in the session")
          None
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          logger.warn(s"toUserSession: ${e.getMessage}")
          None
      }
    }
  }

}

trait SessionBase extends SessionSupport {

  import SessionBase._

  override val sessionEncoder = SessionSupport.PlaySessionEncoder
  override val sessionConfig = AppSettings.sessionConfig

  protected def setUserSession(userSession: UserSession): Directive0 = setSession(userSession.toSessionMap)

  protected val optionalUserSession: Directive1[Option[UserSession]] = optionalSession.flatMap {
    case Right(sessionMap) => BasicDirectives.provide(sessionMap.toUserSession)
    case Left(error) =>
      logger.debug(error)
      BasicDirectives.provide(None)
  }

}
