package com.neo.sk.utils

import javax.xml.soap.Node

import org.slf4j.LoggerFactory


/**
  * Created by zm on 2017/4/13.
  */
class NodeX(xx:Int,yx:Int,parentNodex:NodeX) {
  private val log = LoggerFactory.getLogger(this.getClass)
  log.debug("new NodeX")
  var x:Int=xx//x坐标
  var y:Int=yx//y坐标
  var parentNode:NodeX=parentNodex  //父类节点
  var g:Int=0  //当前点到起点的移动耗费
  var h:Int=0  //当前点到终点的移动耗费，即曼哈顿距离|x1-x2|+|y1-y2|(忽略障碍物)
  var f:Int =0 //f=g+h

  def getX={
    x
  }

  def setX(x1:Int)={
    x=x1
  }

  def getY={
    y
  }

  def setY(y1:Int)={
    y=y1
  }

  def getParentNode()={
    parentNode
  }

  def setParentNode(parentNode1:NodeX)={
    parentNode=parentNode1
  }

  def getG={
    g
  }

  def setG(g1:Int)={
    g=g1
  }

  def getH={
    g
  }

  def setH(h1:Int)={
    h=h1
  }

  def getF={
    f
  }

  def setf(f1:Int)={
    f=f1
  }



}
