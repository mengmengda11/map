package com.neo.sk.map.service

import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.Directives._
import com.neo.sk.map.ptcl._
import com.neo.sk.utils._
import akka.http.scaladsl.server.Directives._
import io.circe.Error
import org.slf4j.LoggerFactory
import Array._

import scala.collection.mutable.ListBuffer


/**
  * Created by zm on 2017/4/12.
  */
trait MapService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._
  private val log = LoggerFactory.getLogger(this.getClass)



  def exists(endArray:Array[Array[Int]],startx:Int,starty:Int,endx:Int,endy:Int):Boolean={
   if(endArray(startx)(starty)==1||endArray(endx)(endy)==1){
     return false
   }
    return true
  }


  def toArray(ylength:Int,xlength:Int,pathList:List[PathInfo]):Array[Array[Int]]={
    var startArray=ofDim[Int](xlength,ylength)
    //初始化(1为障碍物，0为道路)
    for(i<-0 to xlength-1){
      for(j<- 0 to ylength-1){
        startArray(i)(j)=1
      }
    }
    for(k<- 0 to pathList.length-1){
      for(a<-pathList(k).x/10 to pathList(k).x/10+pathList(k).width/10-1){
        for(b<- pathList(k).y/10 to pathList(k).y/10+pathList(k).height/10-1){
          startArray(a)(b)=0
        }
      }
    }

    return startArray

  }




  private val text=(path("text") & post & pathEndOrSingleSlash){
        entity(as[Either[Error,Text]]){
          case Right(info) =>
            log.info(s"get question update data: $info")
            val startx=info.starty/10.toInt
            val starty=info.startx/10.toInt
            val endx=info.endy/10.toInt
            val endy=info.endx/10.toInt


            val endArray=toArray(info.xlength/10,info.ylength/10,info.path)
            val startNode: NewAstar.Node = new NewAstar.Node(startx,starty)
            val endNode: NewAstar.Node = new NewAstar.Node(endx,endy)
            if(exists(endArray,startx,starty,endx,endy)==false){
              complete(CommonRsp(1001001, "please select correct start and end point"))
            }

            val endend=NewAstar.main(endArray,startNode,endNode)
            val xyList:ListBuffer[Info]=new ListBuffer[Info]
            for(i<-0 to info.ylength/10-1){
              for(j<- 0 to info.xlength/10-1){
                if(endend(i)(j)==6){
                 val a=Info(i,j)
                  xyList.append(a)
                }
              }
            }
            log.debug("listlength="+xyList.length)


            complete(TextRsp(0,"ok",xyList.toList))
          case Left(e) =>
            complete("error")
        }

      }





  val mapRoutes = pathPrefix("map") {
    log.debug("start***")
    (path("home")|path("path")  & get) {
      getFromResource("html/map.html")
    }~text
  }
}
