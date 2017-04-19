package com.neo.sk.map

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 3:50 PM
  */
package object ptcl {


  trait Request

  case class Plus(value: Int) extends Request
  case class Minus(value: Int) extends Request



  trait Response{
    val errCode: Int
    val msg: String
  }


  case class CommonRsp( errCode: Int = 0, msg: String = "ok") extends Response

  case class CounterData(value: Int, timestamp: Long)

  case class CounterRsp(
    data: CounterData,
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response

  //map
  case class Text(
                   xlength:Int,
                   ylength:Int,
                   path:List[PathInfo],
                   startx:Int,
                   starty:Int,
                   endx:Int,
                   endy:Int
                 )
  case class TextRsp(errCode:Int, msg:String, data:List[Info])
  case class Info(x:Int, y:Int)
  case class PathInfo(x:Int,y:Int,width:Int,height:Int)


  //company

  case class CompanyConfirm(email: String, psw: String) extends Request








}
