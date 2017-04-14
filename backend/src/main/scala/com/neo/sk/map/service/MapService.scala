package com.neo.sk.map.service

import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Credentials`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.Directives._
import com.neo.sk.map.ptcl._
import com.neo.sk.utils._
import akka.http.scaladsl.server.Directives._
import io.circe.Error
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer


/**
  * Created by zm on 2017/4/12.
  */
trait MapService extends AuthService{
  import io.circe.generic.auto._
  import io.circe._
  private val log = LoggerFactory.getLogger(this.getClass)

//  private val text=(path("text") & post & pathEndOrSingleSlash){
//      entity(as[Either[Error,Text]]){
//        case Right(info) =>
//          log.info(s"get question update data: $info")
//          val INF=Integer.MAX_VALUE
//          log.debug("1")
//          val Nodes=Array('0','1','2','3','4','5')
//          log.debug("2")
//          val matrix=Array(
//            Array(0,6,3,INF,INF,INF),
//            Array(6,0,2,5,INF,INF),
//            Array(3,2,0,3,4,INF),
//            Array(INF,5,3,0,2,3),
//            Array(INF,INF,4,2,0,5),
//            Array(INF,INF,INF,3,5,0)
//          )
//          log.debug("3")
//          val dist:Array[Int]=Array[Int](Nodes.length)
//          log.debug("4")
//          val dijkstra=new Dijkstra(Nodes,matrix)
//          log.debug("5")
//          dijkstra.dijkstra(0,1)
//          val num=dijkstra.dijkstra(0,1)
//          log.debug("6")
//          log.debug(num+"")
//          complete(TextRsp(0,"ok",num))
//        case Left(e) =>
//          complete("error")
//      }
//
//  }

//
//  private val text=(path("text") & post & pathEndOrSingleSlash){
//    entity(as[Either[Error,Text]]){
//      case Right(info) =>
//        log.info(s"get question update data: $info")
//
//        log.debug("1")
//        val map=Array(
//          Array(1,1,1,1,0,1,1,1,1,1),
//          Array(1,1,1,1,0,1,1,1,1,1),
//          Array(1,1,1,1,0,1,1,0,0,1),
//          Array(1,1,1,1,0,1,0,0,0,1),
//          Array(1,1,1,1,0,1,1,0,0,0),
//          Array(1,1,1,1,1,1,1,1,1,1)
//        )
//        log.debug("2")
//        val aStar=new AStar(map,6,10)
//        log.debug("3")
//        val flag=aStar.search(4,0,3,9)
//        log.debug("flag"+flag)
//       if(flag==(-1)){
//         log.debug("传输数据有误")
//       }else if(flag==0){
//         log.debug("没有找到")
//       }else{
//         for(x<- 0 to 6-1){
//           for(y<- 0 to 10-1){
//             if(map(x)(y)==1){
//               log.debug(" ")
//             }else if(map(x)(y)==0){
//               log.debug("=")
//             }else if(map(x)(y)==(-1)){
//               log.debug("*")
//             }
//           }
//           log.debug("")
//         }
//       }
//        complete(TextRsp(0,"ok",1))
//      case Left(e) =>
//        complete("error")
//    }
//
//  }

  def exists(nodes:ListBuffer[NodeY],x:Int,y:Int):Boolean={
    for(i<- 0 to nodes.length-1){
      if(nodes(i).x==x&&nodes(i).y==y){
        return  true
      }
    }
    return false
  }



  private val text=(path("text") & post & pathEndOrSingleSlash){
        entity(as[Either[Error,Text]]){
          case Right(info) =>
            log.info(s"get question update data: $info")

            val NODES=Array(
              Array(0,0,0,0,0,0,0,0,0),
              Array(0,0,0,0,0,0,0,0,0),
              Array(0,0,0,0,0,0,0,0,0),
              Array(0,0,0,1,0,0,0,0,0),
              Array(0,0,0,1,0,0,0,0,0),
              Array(0,0,0,1,0,0,0,0,0),
              Array(0,0,0,1,0,0,0,0,0),
              Array(0,0,0,0,0,0,0,0,0),
              Array(0,0,0,0,0,0,0,0,0)
            )
            log.debug("1")
            val startNode=new NodeY(5,1)
            val endNode=new NodeY(5,5)
            log.debug("2")
            var parent=new AStarNew().findPath(startNode,endNode)
            log.debug("3*********")
//            for(i<- 0 to NODES.length-1){
//              for(j<- 0 to NODES(0).length-1){
//                log.debug(NODES(i)(j)+",")
//              }
//              log.debug("")
//            }

            log.debug("4**********")
            val arrayList=new ListBuffer[NodeY]
            while (parent!=null){
              arrayList.append(new NodeY(parent.x,parent.y))
              parent=parent.parent
            }
            log.debug("\n")
            log.debug("5***********")

            for(i<- 0 to NODES.length-1){
              for(j<- 0 to NODES(0).length-1){

                if(exists(arrayList,i,j)){
                  log.debug("@,")
                }else{
                  log.debug(NODES(i)(j)+",")
                }
              }
              log.debug("")
            }

            complete(TextRsp(0,"ok",1))
          case Left(e) =>
            complete("error")
        }

      }





  val mapRoutes = pathPrefix("map") {
    log.debug("start***")
    (path("home")  & get) {
      getFromResource("html/map.html")
    }~text
  }
}
