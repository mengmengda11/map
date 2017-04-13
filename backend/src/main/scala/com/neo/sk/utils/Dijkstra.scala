package com.neo.sk.utils

import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

/**
  * Created by zm on 2017/4/12.
  */
class Dijkstra(Nodex:Array[Char],Matrixx:Array[Array[Int]]){
  private val log = LoggerFactory.getLogger(this.getClass)


  val INF:Int=Integer.MAX_VALUE
  var Nodes:Array[Char]=Nodex
  var Matrix:Array[Array[Int]]=Matrixx

  def dijkstra(node:Int,end:Int) ={
    log.debug("dijkstra")

    val flag=new Array[Boolean](Nodes.length)
    val distancex=new Array[Int](Nodes.length)
    val way=new Array[Int](Nodes.length)
    val wayend=new Array[Int](Nodes.length)

    //初始化

    for(a<- 0 to Nodes.length-1){
      way(a)=0
    }
    for(a<-0 to Nodes.length-1){
       flag(a)=false
      log.debug("flag"+a+flag(a))
      distancex(a)=Matrix(node)(a)
     // log.debug("distancex"+distancex(a)+"*"+Matrix(node)(a))
    }

    //对顶点node本身进行初始化

    flag(node)=true
    distancex(node)=0

    //遍历Nodes.length-1次，每次找出一个顶点最短路径

    var k=0
    for(i<-1 to Nodes.length-1){
      var min=INF

      //寻找最短路径
      for(j<-0 to Nodes.length-1){
        if(flag(j)==false&&distancex(j)<min){
          k=j
          min=distancex(j)
        }
      }

      flag(k)=true

      //更新Matrix点值
      for(j<-0 to Nodes.length-1){
        var len={
          if(Matrix(k)(j)==INF){
            INF
          }else{
            min+Matrix(k)(j)
          }
        }
        if(flag(j)==false&&len<distancex(j)){
          distancex(j)=len
          way(j)=k
          log.debug("is way"+k)
        }
      }

    }


//    for(i<-0 to Nodes.length-1){
//      log.debug("shortest"+Nodes(node)+"*"+Nodes(i)+"*"+distancex(i))
//    }





//输出最短路径
      var j=end
      while (way(j)!=0){
        log.debug("endway"+way(j))
        j=way(j)
      }


    log.debug("shortest"+Nodes(node)+"*"+Nodes(end)+"*"+distancex(end))
    //最短路径的值
    distancex(end)





  }


}
