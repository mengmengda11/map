package com.neo.sk.map.frontend

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:49 PM
  */
object Routes {


  val baseUrl = "/hw1701a"

  object CounterRoute{
    val counterRoot = baseUrl + "/counter"
    val get = counterRoot + "/get"
    val plus = counterRoot + "/plus"
    val minus = counterRoot + "/minus"
  }

  object MapRoute{
    val mapRoot = baseUrl + "/map"
    val home = mapRoot + "/home"
    val text=mapRoot+"/text"
  }




}
