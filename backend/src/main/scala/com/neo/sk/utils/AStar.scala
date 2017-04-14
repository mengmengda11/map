package com.neo.sk.utils

import java.util
import javax.xml.soap.Node

import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer
import scala.util.control._

/**
  * Created by zm on 2017/4/13.
  */
 class AStar(mapx:Array[Array[Int]],rowx:Int,columnx:Int) {
  private val log = LoggerFactory.getLogger(this.getClass)
    var map:Array[Array[Int]]=mapx  //地图（1可通过，0不可通过）

    val openList=new ListBuffer[NodeX]
    var closeList=new ListBuffer[NodeX]
    val  COST_STRAIGHT=10
    val COST_DIAGIONAL=14
    val row:Int=rowx
    val column=columnx

    val loop = new Breaks


  //查找坐标（-1：错误，0：没找到，1：找到了）
  def search(x1:Int,y1:Int,x2:Int,y2:Int):Int={
    //输入有错误
    if(x1<0||x1>=row||x2<0||x2>=row||y1<0||y1>=column||y2<0||y2>=column){
      log.debug("输入错误0")
      return -1
    }
    //输入有错误
    if(map(x1)(y1)==0||map(x2)(y2)==0){
      log.debug("输入错误1")
      return -1
    }

    val sNode=new NodeX(x1,x2,null)//起点
    val eNode=new NodeX(x2,y2,null)//终点

    openList.append(sNode)
    log.debug("openList++++++++start")

    val resultList:ListBuffer[NodeX]=searchN(sNode,eNode)
    log.debug("resultList")
    if(resultList.size==0){
      log.debug("没有找到")
      return 0
    }
    for(i<- 0 to resultList.size-1){
      map(resultList(i).getX)(resultList(i).getY)=2
      log.debug("resultList init")
    }
    log.debug("找到")
    return 1

  }
  //查找核心算法
  // return 可走路径
  def searchN(sNode:NodeX,eNode:NodeX): ListBuffer[NodeX] ={
    log.debug("searchN")
    val resultList=new ListBuffer[NodeX]
    var isFind:Boolean=false
    var node:NodeX=null
    while (openList.size>0){
      //取出开启列表中最低F值，即第一个存储的值的F为最低的
      node=openList.head
      log.debug("node==")
      //判断是否找到目标点
      if(node.getX==eNode.getX&&node.getY==eNode.getY){
        isFind=true
        log.debug("找到目标点")
        loop.break()
      }


      //上
      if((node.getY-1)>=0){
        log.debug("上")
        checkPath(node.getX,node.getY-1,node,eNode,COST_STRAIGHT)
      }
      //下
      if(node.getY+1<column){
        log.debug("下")
        checkPath(node.getX,node.getY+1,node,eNode,COST_STRAIGHT)
      }
      //左
      if(node.getX-1>=0){
        log.debug("左")
        checkPath(node.getX-1,node.getY,node,eNode,COST_STRAIGHT)
      }
      //右
      if(node.getX+1<row){
        log.debug("右")
        checkPath(node.getX+1,node.getY,node,eNode,COST_STRAIGHT)
      }
      //左上
      if(node.getX-1>=0&&node.getY-1>=0){
        log.debug("左上")
        checkPath(node.getX-1,node.getY-1,node,eNode,COST_STRAIGHT)
      }
      //左下
      if(node.getX-1>=0&&node.getY+1<column){
        log.debug("左下")
        checkPath(node.getX-1,node.getY+1,node,eNode,COST_STRAIGHT)
      }
      //右上
      if(node.getX+1<row&&node.getY-1>=0){
        log.debug("右上")
        checkPath(node.getX+1,node.getY-1,node,eNode,COST_STRAIGHT)
      }
      //右下
      if(node.getX+1<row&&node.getY+1<column){
        log.debug("右下")
        checkPath(node.getX+1,node.getY+1,node,eNode,COST_STRAIGHT)
      }


      //从开启列表中删除
      //添加到关闭列表中
      log.debug("1openList"+openList.size)
      closeList.append(openList.remove(0))
      log.debug("1openList"+openList.size+"closelist"+closeList.size)
      log.debug("从开启列表中删除添加到关闭列表中")
      //****开启列表中排序，把F值最低的放到最底端
      openList.sortBy(_.getF).reverse
      log.debug("开启列表中排序，把F值最低的放到最底端")

    }
    if(isFind){
      log.debug("node="+node.getParentNode())
      getPath(resultList,node)
    }

    return resultList
  }




  //查询此路是否能走通
  def checkPath(x2:Int,y2:Int,parentNode1:NodeX,eNode:NodeX,cost:Int): Boolean ={
    log.debug("checkPath")
    val node=new NodeX(x2,y2,parentNode1)
    //查找地图中是否能通过
    if(map(x2)(y2)==1){
      closeList.append(node)
      return false
    }

//    if(map(x2)(y2)==0&&(x2!=eNode.getX||y2!=eNode.getY)){
//      closeList.append(node)
//      log.debug("false")
//      return false
//    }

    //查找关闭列表中是否存在
    log.debug("closelist"+closeList.size)
    if(isListContains(closeList,x2,y2)!=(-1)){
      return false
    }

    //查找开启列表中是否存在
    var index=(-1)
    log.debug("openlist"+openList.size)
    index=isListContains(openList,x2,y2)
    if(index!=(-1)){
      //G是否更小，是否跟新G
      if(parentNode1.getG+cost<openList(index).getG){
        node.setParentNode(parentNode1)
        countG(node,eNode,cost)
        countF(node)
        openList(index)=node
      }
    }else{
      //添加到开启列表
      node.setParentNode(parentNode1)
      count(node,eNode,cost)
      openList.append(node)
      log.debug("openlist+++++++"+openList.length)

    }

    return true


  }



  //集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
  def isListContains(list:ListBuffer[NodeX],x3:Int,y3:Int): Int ={
    log.debug("isListContains")
    for(i<- 0 to list.size-1){
      val node=list(i)
      if(node.getX==x3&&node.getY==y3){
        log.debug("returni"+i)
        return  i
      }
    }
    log.debug("return -1")
    return -1
  }

  //从终点往返回到起点
  def getPath(resultList:ListBuffer[NodeX],node:NodeX){
    log.debug("getPath")
    if(node.getParentNode()!=null){
      getPath(resultList,node.getParentNode())
    }
    resultList.append(node)
  }

  //计算G,H,F值
  def count(node:NodeX,eNode:NodeX,cost:Int){
    log.debug("count")
    countG(node,eNode,cost)
    countH(node,eNode)
    countF(eNode)
  }

  //计算G值
  def countG(node:NodeX,eNode:NodeX,cost:Int){
    log.debug("countG")
    if(node.getParentNode()==null){
      node.setG(cost)
    }else{
      node.setG(node.getParentNode().getG+cost)
    }
  }

  def countH(node:NodeX,eNode:NodeX){
    log.debug("countH")
    node.setf(Math.abs(node.getX-eNode.getX)+Math.abs(node.getY-eNode.getY*10))
  }

  def countF(node:NodeX){
    log.debug("countF")
    node.setf(node.getG+node.getF)
  }





}
