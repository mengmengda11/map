package com.neo.sk.utils

import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
  * Created by zm on 2017/4/14.
  */
class AStarNew {
  private val log = LoggerFactory.getLogger(this.getClass)
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

  val STEP=10
  val openList=new ListBuffer[NodeY]
  var closeList=new ListBuffer[NodeY]

  def findMinFNodeInOpenList():Int={
    log.debug("findMinFNodeInOpenList")
    //var tempNode:NodeY=openList(0)
    var tempNode:Int=0
    for(i<-0 to openList.length-1){
      log.debug("openlist f")
      if(openList(i).F<openList(tempNode).F){
        //tempNode=openList(i)
        log.debug("tempNode")
        tempNode=i
      }
    }
    log.debug(tempNode+"tempNode")
    return tempNode
  }

  def findNeightborNodes(currentNode:NodeY): ListBuffer[NodeY] ={
    log.debug("findNeightborNodes")
    val arrayList=new ListBuffer[NodeY]
    val topX=currentNode.x
    val topY=currentNode.y-1
    if(canReach(topX,topY)&&(!exists(closeList,topX,topY))){
      arrayList.append(new NodeY(topX,topY))
    }

    val bottomX=currentNode.x
    val bottomY=currentNode.y+1
    if(canReach(bottomX,bottomY)&&(!exists(closeList,bottomX,bottomY))){
      arrayList.append(new NodeY(bottomX,bottomY))
    }

    val leftX=currentNode.x-1
    val leftY=currentNode.y
    if(canReach(leftX,leftY)&&(!exists(closeList,leftX,leftY))){
      arrayList.append(new NodeY(leftX,leftY))
    }

    val rightX=currentNode.x+1
    val rightY=currentNode.y
    if(canReach(rightX,rightY)&&(!exists(closeList,rightX,rightY))){
      arrayList.append(new NodeY(rightX,rightY))
    }

    return arrayList


  }

  def canReach(x:Int,y:Int): Boolean ={
    log.debug("canReach")
    if(x>=0&&x<NODES.length&&y>=0&&y<NODES(0).length){
      return NODES(x)(y)==0
    }
    return false
  }

  def findPath(startNode:NodeY,endNode:NodeY): NodeY ={
    log.debug("findPath")
    openList.append(startNode)

    while (openList.length>0){
      val currentNode=findMinFNodeInOpenList()
      val currentNodeXX=openList(currentNode)
      log.debug("close add")
      closeList.append(openList(currentNode))
      log.debug("open remove"+openList.length)

      openList.remove(currentNode)

      val neighboerNodes=findNeightborNodes(currentNodeXX)
      for(a<- 0 to neighboerNodes.length-1){
        if(exists(openList,neighboerNodes(a))){
          foundPoint(currentNodeXX,neighboerNodes(a))
        }else{
          notFoundPoint(currentNodeXX,endNode,neighboerNodes(a))
        }
      }
      if(find(openList,endNode)!=null){
        return  find(openList,endNode)
      }

    }
    log.debug("openlist*******"+openList.length)
    return find(openList,endNode)
  }

  def foundPoint(tempStart:NodeY,node:NodeY)={
    log.debug("foundPoint")
    val G=calcG(tempStart,node)
    if(G<node.G){
      node.parent=tempStart
      node.G=G
      node.calcF()
    }
  }

  def notFoundPoint(tempStart:NodeY,end:NodeY,node:NodeY)={
    log.debug("notFoundPoint")
    node.parent=tempStart
    node.G=calcG(tempStart,node)
    node.H=calcH(end,node)
    node.calcF()
    openList.append(node)
  }

  def calcG(start:NodeY,node:NodeY): Int ={
    log.debug("calcG")
    val G=STEP
    val parentG={
      if(node.parent!=null){
        node.parent.G
      }else{
        0
      }
    }
    return G+parentG
  }

  def calcH(end:NodeY,node:NodeY): Int ={
    log.debug("calcH")
    val step=Math.abs(node.x-end.x)+Math.abs(node.y-end.y)
    return step*STEP
  }

  def find(nodes:ListBuffer[NodeY],point:NodeY): NodeY ={
    log.debug("find")
    for(i<- 0 to nodes.length-1){
      if(nodes(i).x==point.x){
        return nodes(i)
      }
    }
    return null
  }

  def exists(nodes:ListBuffer[NodeY],x:Int,y:Int):Boolean={
    log.debug("exists")
    for(i<- 0 to nodes.length-1){
      if(nodes(i).x==x&&nodes(i).y==y){
        return  true
      }
    }
    return false
  }

  def exists(nodes:ListBuffer[NodeY],node:NodeY):Boolean={
    log.debug("exists")
    for(i<- 0 to nodes.length-1){
      if(nodes(i).x==node.x&&nodes(i).y==node.y){
        return  true
      }
    }
    return false
  }






}
