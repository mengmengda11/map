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
    //val openList:List //开启列表
    //val closeList:List  //关闭列表
    var openList:ListBuffer[NodeX]
    var closeList:ListBuffer[NodeX]
    val  COST_STRAIGHT=10
    val COST_DIAGIONAL=14
    var row:Int
    var column:Int
    val loop = new Breaks

   def AStar(mapx:Array[Array[Int]],rowx:Int,columnx:Int) ={
       row=rowx
       column=columnx
       //openList=Nil
       //closeList=Nil

   }
  //查找坐标（-1：错误，0：没找到，1：找到了）
  def search(x1:Int,y1:Int,x2:Int,y2:Int):Int={
    if(x1<0||x1>=row||x2<0||x2>=row||y1<0||y1>=column||y2<0||y2>=column){
      return -1
    }
    val sNode=new NodeX(x1,x2,null)
    val eNode=new NodeX(x2,y2,null)
    openList.append(sNode)
    var resultList:ListBuffer[NodeX]=searchN(sNode,eNode)
    if(resultList.size==0){
      return 0
    }
    for(i<- 0 to resultList.size-2){
      map(resultList(i).getX)(resultList(i).getY)=(-1)
    }
    return 1

  }
  //查找核心算法
  def searchN(sNode:NodeX,eNode:NodeX): ListBuffer[NodeX] ={
    var resultList:ListBuffer[NodeX]=null
    var node:NodeX=null
    while (openList.size>0){
      //取出开启列表中最低F值，即第一个存储的值的F为最低的
      node=openList(0)
      //判断是否找到目标点
      if(node.getX==eNode.getX&&node.getY==eNode.getY){
        loop.break()
      }
      //上
      if((node.getY-1)>=0){
        checkPath(node.getX,node.getY-1,node,eNode,COST_STRAIGHT)
      }
      //下
      if(node.getY+1<column){
        checkPath(node.getX,node.getY+1,node,eNode,COST_STRAIGHT)
      }
      //左
      if(node.getX-1>=0){
        checkPath(node.getX-1,node.getY,node,eNode,COST_STRAIGHT)
      }
      //右
      if(node.getX+1<row){
        checkPath(node.getX+1,node.getY,node,eNode,COST_STRAIGHT)
      }
      //从开启列表中删除
      //添加到关闭列表中
      closeList.append(openList.remove(0))
      //****开启列表中排序，把F值最低的放到最底端
      openList.sortBy()

    }
    getPath(resultList,node)
    return resultList
  }




  //查询此路是否能走通
  def checkPath(x2:Int,y2:Int,parentNode1:NodeX,eNode:NodeX,cost:Int): Boolean ={
    val node=new NodeX(x2,y2,parentNode1)
    if(x2==3&&y2==4){
      log.debug("")
    }
    //查找地图中是否能通过
    if(map(x2)(y2)==0&&(x2!=eNode.getX||y2!=eNode.getY)){
      closeList.append(node)
      return false
    }

    //查找关闭列表中是否存在
    if(isListContains(closeList,x2,y2)!=1){
      return false
    }

    //查找开启列表中是否存在
    var index=(-1)
    if((index=isListContains(openList,x2,y2))!=(-1)){
      node.setParentNode(parentNode1)
      countG(node,eNode,cost)
      countF(node)
      openList(index)=node
    }else{
      node.setParentNode(parentNode1)
      count(node,eNode,cost)
      openList.append(node)
    }

    return true


  }

  //集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
  def isListContains(list:ListBuffer[NodeX],x3:Int,y3:Int): Int ={
    for(i<- 0 to list.size){
      val node=list(i)
      if(node.getX==x3&&node.getY==y3){
        return  i
      }
    }
    return -1
  }

  //从终点往返回到起点
  def getPath(resultList:ListBuffer[NodeX],node:NodeX){
    if(node.getParentNode()!=null){
      getPath(resultList,node.getParentNode())
    }
    resultList.append(node)
  }

  //计算G,H,F值
  def count(node:NodeX,eNode:NodeX,cost:Int){
    countG(node,eNode,cost)
    countH(node,eNode)
    countF(eNode)
  }

  //计算G值
  def countG(node:NodeX,eNode:NodeX,cost:Int){
    if(node.getParentNode()==null){
      node.setG(cost)
    }else{
      node.setG(node.getParentNode().getG+cost)
    }
  }

  def countH(node:NodeX,eNode:NodeX){
    node.setf(Math.abs(node.getX-eNode.getX)+Math.abs(node.getY-eNode.getY))
  }

  def countF(node:NodeX){
    node.setf(node.getG+node.getF)
  }





}
