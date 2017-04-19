package com.neo.sk.map.service

import java.text.SimpleDateFormat
import java.util.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import com.neo.sk.map.service.SessionBase.UserSession
import com.neo.sk.map.models.dao._

import scala.concurrent.Future
import com.neo.sk.map.ptcl._
import com.github.nscala_time.time.Imports._
import com.neo.sk.map.core._
import com.neo.sk.map.ptcl
import org.slf4j.LoggerFactory
import akka.pattern.ask


/**
  * Created by mengmengda on 2017/4/19.
  */
trait CompanyService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701a.http.AdminService")

  private val loginSubmit = (path("rsf" / "loginSubmit") & pathEndOrSingleSlash & post){
    entity(as[Either[Error, ptcl.CompanyConfirm]]){
      case Right(l) =>
        dealFutureResult {
          CompanyDao.getCompanyByEmail(l.email).map{companyOption=>
            if(companyOption.isDefined){
              val admin = companyOption.get
              if (admin.password == l.psw){
                val session=UserSession(admin.id,System.currentTimeMillis
                (),admin.name)
                setUserSession(session) {
                  complete(CommonRsp())
                }
                //complete(CommonRsp())
              } else {
                complete(CommonRsp(1002005, "Password wrong."))
              }
            }else{
              complete(CommonRsp(1002004, "Email not exist."))
            }
          }
        }
      case Left(error) =>
        log.warn(s"some error: $error")
        complete(CommonRsp(1002003, "Pattern error."))
    }
  }

  private val logout = (path("logout") & get & pathEndOrSingleSlash) {
    invalidateSession {
      redirect("/hw1701a/admin/login", StatusCodes.SeeOther)
    }
  }










  val companyRoutes = pathPrefix("company"){
    log.debug("start***")
    (path("home") | path("login")|path("addProduct")|path("productDetailPage")|path("editProductPage")|path("allUserComment")|path("upload")|path("uploadEdit")& get) {
      getFromResource("html/admin.html")
    } ~ loginSubmit~logout

  }
}
