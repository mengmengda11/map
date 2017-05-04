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
    val path=mapRoot+"/path"
    val text=mapRoot+"/text"
  }

  object CompanyRoute{
    val  companyRoot=baseUrl+"/company"
    val login=companyRoot+"/login"
    val loginSubmit = companyRoot + "/loginSubmit"
    val register=companyRoot+"/register"
    val registerSubmit=companyRoot+"/registerSubmit"
    val home=companyRoot+"/home"
    val resetPas=companyRoot+"/updatePwd"
    val info=companyRoot+"/info"
    val logout=companyRoot+"/logout"
    val createMap=companyRoot+"/createMap"
    val mapList=companyRoot+"/maplist"
    def deleteMap(id:Long)=companyRoot+s"/deleteMap?id=$id"
    val updateMap=companyRoot+"/updateMap"
    val uploadMap = companyRoot + "/uploadMap"
    def getMap(mapPath: String) = companyRoot + s"/getMap?path=$mapPath"
  }




}
