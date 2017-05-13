package com.neo.sk.map.core

import akka.actor.Actor.Receive
import akka.actor.{Actor, Props, ReceiveTimeout, Stash}
import com.neo.sk.map.models.SlickTables
import org.slf4j.LoggerFactory
import com.neo.sk.map.models.dao._
import com.neo.sk.map.models.dao.CompanyDao
import com.neo.sk.utils.SecureUtil

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mengmengda on 2017/2/2.
  */
object AdminActor {

  def props = Props[CompanyActor](new CompanyActor)

  sealed trait ExternalMsg

  //return "Ok" "Exist" "Error"
  case class Register(email: String, name: String, pwd: String) extends ExternalMsg

  //return "Ok" "NotExist" "Wrong"
  case class LoginByEmail(email: String, pwd: String) extends ExternalMsg


  sealed trait InternalMsg

  case class SwitchState(stateName: String, func: Receive, duration: Duration) extends InternalMsg

}

class AdminActor extends Actor with Stash {

  import CompanyActor._

  private[this] val log = LoggerFactory.getLogger(this.getClass)
  private[this] val selfRef = context.self
  private[this] val logPrefix = selfRef.path

  private[this] def switchState(stateName: String, func: Receive, duration: Duration) = {
    log.debug(s"$logPrefix becomes $stateName state.")
    unstashAll()
    context.become(func)
    context.setReceiveTimeout(duration)
  }

  private[this] def switchIdleMsg() = {
    selfRef ! SwitchState("idle", idle(), Duration.Undefined)
  }

  private[this] def terminate(cause: String) = {
    log.info(s"$logPrefix will terminate because $cause.")
    context.stop(selfRef)
  }

  override def receive: Receive = idle()

  def idle(): Receive = {
    case msg@LoginByEmail(email, pwd) =>
      log.debug(s"$logPrefix I got msg: $msg")
      val peer = sender()
      val getAdminFuture = AdminDao.getAdminByEmail(email)

      getAdminFuture.foreach{
        adminOption =>
          if (adminOption.isEmpty){
            peer ! "NotExist"
          } else {
            val admin = adminOption.get
            if (admin.password == pwd){
              peer ! "Ok"
            } else {
              peer ! "Wrong"
            }
          }
          switchIdleMsg()
      }

      getAdminFuture.onFailure {
        case e =>
          log.debug(s"$logPrefix getAdminFuture failed: $e")
          selfRef ! SwitchState("idle", idle(), Duration.Undefined)
          peer ! "Error"
      }

      switchState("busy", busy(), Duration.Undefined)


  }



  def busy(): Receive = {
    case SwitchState(stateName: String, func: Receive, duration: Duration) =>
      switchState(stateName, func, duration)

    case msg =>
      log.debug(s"$logPrefix i'm busy, stash $msg.")
      stash()
  }

}
