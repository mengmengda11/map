package com.neo.sk.map.frontend.Admin

import com.neo.sk.map.frontend.Routes._
import com.neo.sk.map.frontend.utils.{Http, Shortcut}
import com.neo.sk.map.ptcl._
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{KeyboardEvent, MouseEvent}

import scalatags.JsDom.short._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mengmengda on 2017/4/20.
  */
object Login {
  val LoginSubmitUrl=AdminRoute.loginSubmit
  val HomeUrl=AdminRoute.home

  val accountBox = input(*.`type` := "text", *.placeholder := "账号",*.cls:="form-control").render
  val passwordBox = input(*.`type` := "password", *.placeholder := "密码",*.cls:="form-control").render
  val loginButton = button(*.cls:="btn btn-default",*.marginLeft:="50px")("登录").render
  val registerButton=button(*.cls:="btn btn-default",*.marginLeft:="80px")("注册").render


  loginButton.onclick = { e: MouseEvent =>
    e.preventDefault()

    val account = accountBox.value
    val password = passwordBox.value
    val bodyStr = CompanyConfirm(account, password).asJson.noSpaces
    if(account==""||password==""){
      dom.window.alert(s"用户名密码不可为空！")
    }else{
      Http.postJsonAndParse[CommonRsp](LoginSubmitUrl, bodyStr).foreach{
        case Right(rsp) =>
          if(rsp.errCode == 0){
            println(s"service login submit success.")
            Shortcut.redirect(HomeUrl)
          }else{
            println(s"service login submit failed, error: $rsp")
            dom.window.alert(s"error: $rsp")
          }
        case Left(e) =>
          println(s"service login submit error: $e")
          dom.window.alert(s"error: $e")
      }
    }

  }

  passwordBox.onkeypress = { e: KeyboardEvent =>
    if(e.charCode == KeyCode.Enter){
      e.preventDefault()
      loginButton.click()
    }
  }





  def render():Div={
    div(*.cls:="container")(

      div(*.cls:="row",*.marginTop:="200px")(
        div(*.cls:="col-md-4 col-md-offset-4")(
          form(*.cls:="form-horizontal")(
            div(*.cls:="form-group",*.textAlign.center)(
              h1("管理员登录")
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("账号"),
              div(*.cls:="col-sm-10")(accountBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("密码"),
              div(*.cls:="col-sm-10")(passwordBox)
            ),
            div(*.cls:="form-group")(
              div(*.cls:="col-sm-offset-2 col-sm-10")(
                loginButton
              )
            )
          )
        )
      )
    ).render
  }
}

