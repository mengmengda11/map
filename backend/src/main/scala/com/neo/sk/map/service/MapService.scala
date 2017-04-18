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

  def toArray(ylength:Int,xlength:Int,y:Int,x:Int,height:Int,width:Int): Array[Array[Int]]={
    var startArray=ofDim[Int](xlength,ylength)

    for(i<-0 to xlength-1){
      for(j<- 0 to ylength-1){
        startArray(i)(j)=0
      }
    }

    log.debug("x="+x+"width-1="+(width-1))
    log.debug("y="+y+"height-1="+(height-1))
    for(a<- x to x+width-1){
      for(b<- y to y+height-1){
        startArray(a)(b)=1
      }
    }


    return startArray

  }



  private val text=(path("text") & post & pathEndOrSingleSlash){
        entity(as[Either[Error,Text]]){
          case Right(info) =>
            log.info(s"get question update data: $info")
//            val nodeArr=Array(Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 1, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 0, 0, 0, 0, 0, 0),
//                  Array(0, 0, 0, 0, 0, 0, 0, 0, 0))
//            val startNode: NewAstar.Node = new NewAstar.Node(5, 1)
//            val endNode: NewAstar.Node = new NewAstar.Node(5, 5)
//
//            val endArray=NewAstar.main(nodeArr,startNode,endNode)
//
//            log.debug(s"***********")
//            for(i<- 0 to endArray.length-1){
//              for(j<- 0 to endArray(0).length-1){
//                System.out.print(endArray(i)(j) + ", ")
//              }
//              System.out.println
//            }

            val endArray=toArray(info.xlength/10,info.ylength/10,info.x/10,info.y/10,info.width/10,info.height/10)
            val startNode: NewAstar.Node = new NewAstar.Node(info.startx,info.starty)
            val endNode: NewAstar.Node = new NewAstar.Node(info.endx,info.endy)
            val endend=NewAstar.main(endArray,startNode,endNode)
//            for(i<- 0 to endArray.length-1){
//              for(j<- 0 to endArray(0).length-1){
//                System.out.print(endArray(i)(j) + ", ")
//              }
//              System.out.println
//            }

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
