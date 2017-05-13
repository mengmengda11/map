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
    def getMap(mapId:Long)=mapRoot+s"/getMap?id=$mapId"
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
    def unableMap(id:Long)=companyRoot+s"/unableMap?id=$id"
    def ableMap(id:Long)=companyRoot+s"/ableMap?id=$id"
    val updateMap=companyRoot+"/updateMap"
    val uploadMap = companyRoot + "/uploadMap"
    def getMap(mapPath: String) = companyRoot + s"/getMap?path=$mapPath"
    def getUserInfo(mapId:Int)=companyRoot+s"/getUserInfo?id=$mapId"
  }

  object AdminRoute{
    val  adminRoot=baseUrl+"/admin"
    val login=adminRoot+"/login"
    val loginSubmit =adminRoot + "/loginSubmit"
    val home=adminRoot+"/home"
    val resetPas=adminRoot+"/updatePwd"
    val info=adminRoot+"/info"
    val logout=adminRoot+"/logout"
    val companyList=adminRoot+"/companyList"
    def deleteCompany(id:Long)=adminRoot+s"/deleteCompany?id=$id"
    def unableCompany(id:Long)=adminRoot+s"/unableCompany?id=$id"
    def ableCompany(id:Long)=adminRoot+s"/ableCompany?id=$id"
    def passMap(id:Long)=adminRoot+s"/passMap?id=$id"
    def notPassMap(id:Long)=adminRoot+s"/notPassMap?id=$id"
    def getMapByCompany(id:Long)=adminRoot+s"/getMapByCompany?id=$id"
    def getMap(mapPath: String) = adminRoot + s"/getMap?path=$mapPath"
    def reviewedMapList=adminRoot+"/reviewedMapList"
    def noreviewMapList=adminRoot+"/noreviewMapList"
  }




}
