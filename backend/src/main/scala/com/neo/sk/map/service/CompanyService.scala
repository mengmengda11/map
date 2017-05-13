package com.neo.sk.map.service

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
import com.neo.sk.map.common._
import com.neo.sk.map.ptcl._
import com.neo.sk.utils.FileUtil
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}


/**
  * Created by mengmengda on 2017/4/19.
  */
trait CompanyService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._

  private val log = LoggerFactory.getLogger("com.neo.sk.hw1701a.http.AdminService")

  private val loginSubmit = (path( "loginSubmit") & pathEndOrSingleSlash & post){
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
  private val registerSubmit = (path("registerSubmit") & post & pathEndOrSingleSlash){
    loggingAction{ ctx =>
      entity(as[Either[Error,RegisterReq]]){
        case Right(info) =>
          dealFutureResult(
            CompanyDao.isCompanyEmailExist(info.email).flatMap{
              case true =>
                Future(complete("email is exists"))
              case false =>
                val timestamp = System.currentTimeMillis()
                val company = SlickTables.rCompany(
                  -1,
                  info.companyName,
                  info.password,
                  info.email,
                  1,
                  timestamp.toInt
                )
                CompanyDao.createCompany(company).map{ _ =>
                  complete(CommonRsp())
                }.recover{
                  case t =>
                    t.printStackTrace()
                    log.error(s"add company error.")
                    complete(CommonRsp(1002003, "Pattern error."))
                }
            }
          )

        case Left(e) =>
          complete(CommonRsp(1002003, "Pattern error."))
      }
    }
  }



  private val logout = (path("logout") & get & pathEndOrSingleSlash) {
    invalidateSession {
      redirect("/hw1701a/company/login", StatusCodes.SeeOther)
    }
  }

  private val createMap = (path("createMap") & post & pathEndOrSingleSlash){
    CompanyAction{ company =>
      entity(as[Either[Error, CreateMapReq]]){
        case Right(info) =>
          val q = SlickTables.rMaps(-1,company.id.toInt,info.map,System.currentTimeMillis(),info.mapName,1,info.mapPic,0,info.des)
          dealFutureResult(
            MapDao.create(q).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"create map error.")
                complete(CommonRsp(1000000, "internal error."))
            }
          )
        case Left(e) =>
          complete(CommonRsp(1000001, "post json parse error."))
      }
    }
  }

  private val mapList = (path("maplist") & get & pathEndOrSingleSlash){
    CompanyAction{ company =>
      dealFutureResult(
        MapDao.getMap(company.id.toInt).map{ seq =>
          val data = seq.map(q =>
            MapInfo(q.id, q.companyid,q.mapname, q.des,q.map, q.mapPic,q.createtime,q.state,q.review)
          ).toList
          complete(MapInfoRsp(data))
        }.recover{
          case t =>
            t.printStackTrace()
            log.error(s"get robot questions error.")
            complete(CommonRsp(1000002, "internal error."))
        }
      )
    }
  }

  private val getAmap = (path("getAmap") & get & pathEndOrSingleSlash){
    CompanyAction{ company =>
      parameters(
        'id.as[Long]
      ){
        case mapId =>
          dealFutureResult(
            MapDao.getMapById(mapId.toInt).map{ seq =>
              val data = seq.map(q =>
                MapInfo(q.id,q.companyid, q.mapname, q.map,q.des,q.mapPic, q.createtime,q.state,q.review)
              ).toList
              complete(MapInfoRsp(data))
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"delete map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }


  private val deleteMap = (path("deleteMap") & get & pathEndOrSingleSlash){
    CompanyAction{ company =>
      parameters(
        'id.as[Long]
      ){
        case questionId =>
          dealFutureResult(
            MapDao.delete(questionId.toInt, company.id.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"delete map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }


  private val unableMap = (path("unableMap") & get & pathEndOrSingleSlash){
    CompanyAction{ company =>
      parameters(
        'id.as[Long]
      ){
        case mapId =>
          dealFutureResult(
            MapDao.unableMap(mapId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"unable map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val ableMap = (path("ableMap") & get & pathEndOrSingleSlash){
    CompanyAction{ company =>
      parameters(
        'id.as[Long]
      ){
        case mapId =>
          dealFutureResult(
            MapDao.ableMap(mapId.toInt).map{ _ =>
              complete(CommonRsp())
            }.recover{
              case t =>
                t.printStackTrace()
                log.error(s"able map  error.")
                complete(CommonRsp(1000004, "internal error."))
            }
          )
      }
    }
  }

  private val updateMap = (path("updateMap") & post & pathEndOrSingleSlash){
    CompanyAction{ company =>
      entity(as[Either[Error,UpdateMapReq]]){
        case Right(info) =>
          log.info(s"get mapupdate data: $info")
          dealFutureResult(
            MapDao.getMapByCompanyId(info.id).flatMap{
              case Some(companyId) =>
                if(companyId == company.id){
                  MapDao.update(company.id.toInt, info.id, info.mapName,info.des, info.map,info.mapPic).map{ _ =>
                    complete(CommonRsp())
                  }.recover{
                    case t =>
                      t.printStackTrace()
                      log.error(s"update map error.")
                      complete(CommonRsp(1000005, "internal error."))
                  }
                }else{
                  Future(complete(CommonRsp(1000006, "map not belong to this company")))
                }
              case _ =>
                Future(complete(CommonRsp(1000007, "map not exist")))
            }
          )
        case Left(e) =>
          complete(CommonRsp(1000008, "post json parse error."))
      }
    }
  }

//  private val uploadMap = (path("uploadMap") & post ){
//    CompanyAction{ company =>
//      entity(as[Multipart.FormData]){ formData =>
//       fileUpload("fileUpload"){
//          case (fileInfo, fileStream) =>
//            log.info(s"fileName === ${fileInfo.fileName}")
//            val path = Paths.get(System.getProperty("java.io.tmpdir")) resolve fileInfo.fileName
//            val sink = FileIO.toPath(path)
//            val writeResult = fileStream.runWith(sink)
//            onSuccess(writeResult){result =>
//              result.status match {
//                case Success(_) =>
//                  val file = new File(path.toUri)
//                  val dirPath = System.getProperty("user.dir") + AppSettings.imageUpload
//                  val dir = new File(dirPath)
//                  if(!dir.exists()){
//                    dir.mkdir()
//                  }
//                  val destFile = new File(dirPath + System.currentTimeMillis() + fileInfo.fileName)
//                  FileUtil.copyFile(file, destFile)
//                  val destFilePath = destFile.getAbsolutePath
//                  log.info(s"dest file path === $destFilePath")
//                  complete(ImageUploadRsp(destFilePath))
//                case Failure(e) =>
//                  log.error(s"upload img error: $e")
//                  complete(CommonRsp(1000009, "upload file error."))
//              }
//            }
//        }
//      }
//    }
//  }

  private val uploadMap = (path("uploadMap") & post ){
    CompanyAction{ company =>
      entity(as[Multipart.FormData]){ formData =>
        fileUpload("fileUpload"){
          case (fileInfo, fileStream) =>
            log.info(s"fileName === ${fileInfo.fileName}")
                  log.info(s"dest file path === $fileInfo.fileName")
                  val path="/hw1701a/static/img/"+fileInfo.fileName
                  complete(ImageUploadRsp(fileInfo.fileName))
        }
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
    CompanyAction{ company =>
      entity(as[Either[Error, CompanyUpdatePwdReq]]){
        case Right(info) =>
          dealFutureResult(
            CompanyDao.getCompanyById(company.id.toInt).flatMap{
              case Some(companyInfo) =>
                if(info.oldPwd == companyInfo.password){
                  CompanyDao.updatePsw(company.id.toInt, info.newPwd).map{ _ =>
                    complete(CommonRsp())
                  }.recover{
                    case t =>
                      t.printStackTrace()
                      log.error(s"company update password error.")
                      complete(s"internal error")
                  }
                }else{
                  log.info(s"old password is wrong.")
                  Future(complete(s"old paw error"))
                }
              case _ =>
                log.debug(s"company not exist.")
                Future(complete(s"company not exists"))
            }
          )
        case Left(e) =>
          complete(s"post json error")
      }
    }
  }

  private val getUserInfo=(path("getUserInfo") &get & pathEndOrSingleSlash) {
    CompanyAction{ company =>
    parameters(
      'id.as[Long]
    ) {
      case mapId =>
        dealFutureResult(
          UserDao.getUserByMap(mapId.toInt).map { seq =>
            val data = seq.map{r=>
              val room=r._1
              val userNumber=r._2
              userInfo(room,userNumber)
            }.toList.sortBy(_.userNumber)
            complete(userInfoRsp(data))
          }.recover {
            case t =>
              t.printStackTrace()
              log.error(s"get map by id  error.")
              complete(CommonRsp(1000004, "internal error."))
          }
        )
    }
  }
  }













  val companyRoutes = pathPrefix("company"){
    log.debug("start***")
    (path("home") | path("login")| path("register")& get) {
      getFromResource("html/company.html")
    } ~ loginSubmit~logout~createMap~mapList~deleteMap~updateMap~uploadMap~getMap~registerSubmit~passwordUpdate~
    unableMap~ableMap~getUserInfo

  }
}
