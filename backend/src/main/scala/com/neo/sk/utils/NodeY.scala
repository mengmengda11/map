package com.neo.sk.utils

/**
  * Created by zm on 2017/4/14.
  */
 class NodeY(xx:Int,yy:Int) {
  val x=xx
  val y=yy
  var F:Int=0
  var G:Int=0
  var H:Int=0
 var parent:NodeY=null
  def calcF()={
    F=G+H
  }



}
