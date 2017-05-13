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
                   startx:Int,
                   starty:Int,
                   endx:Int,
                   endy:Int,
                   mapid:Int,
                   companyId:Int,
                   roomName:String
                 )
  case class TextRsp(errCode:Int, msg:String, data:List[Info])
  case class Info(x:Int, y:Int)
  case class PathInfo(x:Int,y:Int,width:Int,height:Int)


  //company
  case class RegisterReq(
                           companyName: String,
                           email: String,
                           password:String
                         ) extends Request
  case class CompanyConfirm(email: String, psw: String) extends Request
  case class CreateMapReq(
                                     mapName: String,
                                     des:String,
                                     map: String,
                                     mapPic:String
                                   ) extends Request
  case class UpdateMapReq(
                                     id: Int,
                                     mapName: String,
                                     des:String,
                                     map: String,
                                     mapPic:String
                                   ) extends Request

  case class MapInfo(
                            id: Int,
                            companyId:Int,
                            mapName: String,
                            des:String,
                            map: String,
                            mapPic:String,
                            createTime:Long,
                            state:Int,
                            review:Int
                          )
  case class MapInfoRsp(data: List[MapInfo], errCode: Int = 0, msg: String = "ok") extends Response
  case class ImageUploadRsp(filePath: String, errCode: Int = 0, msg: String = "ok") extends Response
  case class CompanyUpdatePwdReq(oldPwd: String, newPwd: String) extends Request
  case class SvgInfoRsp(data: String, errCode: Int = 0, msg: String = "ok") extends Response
  case class userInfo(
                     roomName:String,
                     userNumber:Int
                     )
  case class userInfoRsp(date:List[userInfo],errCode:Int=0,msg:String="ok")extends Response
//admin
case class AdminConfirm(email: String, psw: String) extends Request

  case class CompanyInfo(
                      id: Int,
                      companyName: String,
                      createTime:Long,
                      state:Int
                    )
  case class CompanyInfoRsp(data: List[CompanyInfo], errCode: Int = 0, msg: String = "ok") extends Response
  case class AdminUpdatePwdReq(oldPwd: String, newPwd: String) extends Request





}
