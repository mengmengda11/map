package com.neo.sk.map.service

/**
  * Created by mengmengda on 2017/5/6.
  */
import java.nio.file.Paths

import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.StatusCodes
import com.neo.sk.map.service.SessionBase.UserSession
import com.neo.sk.map.ptcl
import com.neo.sk.map.models.dao._
import com.neo.sk.map.models.SlickTables
import com.neo.sk.map.ptcl._

import scala.concurrent.Future
import java.io.File
import java.nio.file.Paths

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart}
import akka.stream.scaladsl.FileIO
import io.circe.generic.auto._
import com.neo.sk.map.common._
import com.neo.sk.map.ptcl._
import com.neo.sk.utils.FileUtil
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}


trait AdminService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701a.http.AdminService")

  private val loginSubmit = (path( "loginSubmit") & pathEndOrSingleSlash & post){
    entity(as[Either[Error, ptcl.AdminConfirm]]){
      case Right(l) =>
        dealFutureResult {
          AdminDao.getAdminByEmail(l.email).map{adminOption=>
            if(adminOption.isDefined){
              val admin = adminOption.get
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



  private val companyList = (path("companyList") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      dealFutureResult(
        AdminDao.getAllCompany.map{ seq =>
          val data = seq.map(q =>
            CompanyInfo(q.id, q.name,q.createTime,q.state)
          ).toList
          complete(CompanyInfoRsp(data))
        }.recover{
          case t =>
            t.printStackTrace()
            log.error(s"get robot questions error.")
            complete(CommonRsp(1000002, "internal error."))
        }
      )
    }
  }

  private val noreviewMapList = (path("noreviewMapList") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      dealFutureResult(
        MapDao.notReMap.map{ seq =>
          val data = seq.map(q =>
            MapInfo(q.id,q.companyid,q.mapname,q.des,q.map,q.mapPic,q.createtime,q.state,q.review)
          ).toList
          complete(MapInfoRsp(data))
        }.recover{
          case t =>
            t.printStackTrace()
            log.error(s"get not reviewed map error.")
            complete(CommonRsp(1000002, "internal error."))
        }
      )
    }
  }

  private val reviewedMapList = (path("reviewedMapList") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      dealFutureResult(
        MapDao.reMap.map{ seq =>
          val data = seq.map(q =>
            MapInfo(q.id,q.companyid,q.mapname,q.des,q.map,q.mapPic,q.createtime,q.state,q.review)
          ).toList
          complete(MapInfoRsp(data))
        }.recover{
          case t =>
            t.printStackTrace()
            log.error(s"get reviewed map error.")
            complete(CommonRsp(1000002, "internal error."))
        }
      )
    }
  }

  private val getMapByCompany = (path("getMapByCompany") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case companyId =>
          dealFutureResult(
            MapDao.getMapByCompany(companyId.toInt).map{ seq =>
              val data = seq.map(q =>
                MapInfo(q.id,q.companyid,q.mapname,q.des,q.map,q.mapPic,q.createtime,q.state,q.review)
              ).toList
              complete(MapInfoRsp(data))
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"get map by company  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val passMap = (path("passMap") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case mapId =>
          dealFutureResult(
            MapDao.passMap(mapId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"pass map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val notPassMap = (path("notPassMap") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case mapId =>
          dealFutureResult(
            MapDao.notPassMap(mapId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"not pass map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val deleteCompany = (path("deleteCompany") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case companyId =>
          dealFutureResult(
            CompanyDao.deleteCompany(companyId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"delete company  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val unableCompany = (path("unableCompany") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case companyId =>
          dealFutureResult(
            CompanyDao.unableCompany(companyId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"unable company  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val ableCompany = (path("ableCompany") & get & pathEndOrSingleSlash){
    AdminAction{ admin =>
      parameters(
        'id.as[Long]
      ){
        case companyId =>
          dealFutureResult(
            CompanyDao.ableCompany(companyId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"able company  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }



  private val getMap = (path("getMap") & get & pathEndOrSingleSlash){
    loggingAction{ _ =>
      parameter(
        'path.as[String]
      ){
        case path =>
          val f = new File(path)
          if(f.exists()){
            val contentType = FileUtil.imageContentType(path).getOrElse(ContentTypes.`application/octet-stream`)
            val responseEntity = HttpEntity(
              contentType,
              f.length(),
              FileIO.fromFile(f, chunkSize = 262144)
            )
            complete(responseEntity)
          }else{
            log.warn(s"con not find [$path] in ${f.getAbsolutePath}")
            complete(CommonRsp(1000010, "get map file error."))
          }
      }
    }

  }


  private val passwordUpdate = (path("updatePwd") & post & pathEndOrSingleSlash){
    AdminAction{ admin =>
      entity(as[Either[Error, AdminUpdatePwdReq]]){
        case Right(info) =>
          dealFutureResult(
            AdminDao.getAdminById(admin.id.toInt).flatMap{
              case Some(adminInfo) =>
                if(info.oldPwd == adminInfo.password){
                  AdminDao.updatePsw(admin.id.toInt, info.newPwd).map{ _ =>
                    complete(CommonRsp())
                  }.recover{
                    case t =>
                      t.printStackTrace()
                      log.error(s"admin update password error.")
                      complete(s"internal error")
                  }
                }else{
                  log.info(s"old password is wrong.")
                  Future(complete(s"old paw error"))
                }
              case _ =>
                log.debug(s"admin not exist.")
                Future(complete(s"admin not exists"))
            }
          )
        case Left(e) =>
          complete(s"post json error")
      }
    }
  }












  val adminRoutes = pathPrefix("admin"){
    log.debug("start***")
    (path("home") | path("login")& get) {
      getFromResource("html/admin.html")
    } ~ loginSubmit~logout~deleteCompany~getMap~unableCompany~ableCompany~companyList~
      passwordUpdate~getMapByCompany~reviewedMapList~noreviewMapList~passMap~notPassMap

  }
}