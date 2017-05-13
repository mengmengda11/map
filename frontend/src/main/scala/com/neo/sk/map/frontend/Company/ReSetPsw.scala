package com.neo.sk.map.frontend.Company
import com.neo.sk.map.frontend.Routes._
import com.neo.sk.map.frontend.utils.{Http, Shortcut}
import com.neo.sk.map.ptcl._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw._

import scalatags.JsDom.short._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import scala.util.matching.Regex

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/3/22.
  */
object ReSetPsw{
  val ResetSubmitUrl=CompanyRoute.resetPas


  val oldPasswordBox = input(*.`type` := "password", *.placeholder := "旧密码",*.cls:="form-control").render
  val newPasswordBox = input(*.`type` := "password", *.placeholder := "新密码",*.cls:="form-control").render
  val anewPasswordBox = input(*.`type` := "password", *.placeholder := "确认密码",*.cls:="form-control").render
  val resetButton=button(*.cls:="btn btn-default")("确定").render


  resetButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    val oldPassword = oldPasswordBox.value.toString
    val newPassword = newPasswordBox.value.toString
    val anewPassword=anewPasswordBox.value.toString
    if(oldPassword==""||newPassword==""||anewPassword==""){
      dom.window.alert(s"表单不可为空")
    }else{
      if(newPassword!=anewPassword){
        dom.window.alert(s"两次输入密码不一致")
      }else{
        val bodyStr = CompanyUpdatePwdReq(oldPassword,newPassword).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](ResetSubmitUrl, bodyStr).foreach{
          case Right(rsp) =>
            if(rsp.errCode == 0){
              println(s"service reset submit success.")
              dom.window.alert(s"修改密码成功")
             // CreatMap.render()
            }else{
              println(s"service reset submit failed, error: $rsp")
              dom.window.alert(s"error: $rsp")
            }
          case Left(e) =>
            println(s"service reset submit error: $e")
            dom.window.alert(s"error: $e")
        }
      }
    }
  }


  def render():Div={
    div(*.cls:="container")(
      div(*.cls:="row")(
        div(*.cls:="col-md-6 ")(
          form(*.cls:="form-horizontal")(
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("旧密码"),
              div(*.cls:="col-sm-10")(oldPasswordBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("新密码"),
              div(*.cls:="col-sm-10")(newPasswordBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("确认密码"),
              div(*.cls:="col-sm-10")(anewPasswordBox)
            ),
            div(*.cls:="form-group")(
              div(*.cls:="col-sm-offset-2 col-sm-10")(
                resetButton
              )
            )
          )
        )
      )
    ).render
  }
}
