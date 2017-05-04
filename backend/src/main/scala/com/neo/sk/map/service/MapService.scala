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

   if(endArray(startx)(starty)==1) {
     log.debug("起点不对")
     return false
   }else{
     if( endArray(endx)(endy)==1){
       return false
     }

   }

    log.debug("对啊对啊")
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
          for(a<-pathList(k).x/5 to pathList(k).x/5+pathList(k).width/5-1){
            for(b<- pathList(k).y/5 to pathList(k).y/5+pathList(k).height/5-1){
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
            val startx=info.starty/5
            val starty=info.startx/5
            val endx=info.endy/5
            val endy=info.endx/5


            //val endArray=toArray(info.xlength/5,info.ylength/5,info.path)
            val endArray=Draw.imgMtr("D:\\newmap.jpg")
//            log.debug("++++++++++++++++++++++")
//            for(i<-0 to endArray.length-1){
//              for(j<- 0 to endArray(0).length-1){
//                System.out.print(endArray(i)(j))
//              }
//              System.out.println
//            }
            val startNode: NewAstar.Node = new NewAstar.Node(startx,starty)
            val endNode: NewAstar.Node = new NewAstar.Node(endx,endy)
            if(exists(endArray,startx,starty,endx,endy)==false){
              complete(CommonRsp(1001001, "please select correct start and end point"))
            }else{
              val endend=NewAstar.main(endArray,startNode,endNode)
              val xyList:ListBuffer[Info]=new ListBuffer[Info]
              log.debug("********************")
              for(i<-0 to endend.length-1){
                for(j<- 0 to endend(0).length-1){
                  System.out.print(endend(i)(j))
                  if(endend(i)(j)==6){
                    val a=Info(i,j)
                    xyList.append(a)
                  }
                }
                System.out.println
              }
              log.debug("listlength="+xyList.length)


              complete(TextRsp(0,"ok",xyList.toList))
            }
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
