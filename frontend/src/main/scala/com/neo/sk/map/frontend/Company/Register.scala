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
  * Created by ZM on 2017/3/21.
  */
object Register {
  val RegisterSubmitUrl=CompanyRoute.registerSubmit
  val LoginUrl=CompanyRoute.login


  val accountBox = input(*.`type`:="email", *.placeholder := "账号",*.cls:="form-control").render
  val passwordBox = input(*.`type` := "password", *.placeholder := "密码",*.cls:="form-control").render
  val confirmBox = input(*.`type` := "password", *.placeholder := "确认密码",*.cls:="form-control").render
  val companyNameBox=input(*.`type` := "text", *.placeholder := "公司名称",*.cls:="form-control").render
  val registerButton=button(*.cls:="btn btn-default")("注册").render

  accountBox.onchange={e:Event=>
    e.preventDefault()
    val accoutStr=accountBox.value
    val re="^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$".r
    if(re.findAllIn(accoutStr).length==0){
      dom.window.alert(s"请输入正确邮箱格式")
    }
  }

  confirmBox.onchange={ e:Event=>
    e.preventDefault()
    val pasStr=passwordBox.value
    val confirmStr=confirmBox.value
    if(pasStr!=confirmStr){
      dom.window.alert(s"两次输入密码不同")
    }
  }


  registerButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    val account = accountBox.value.toString
    val password = passwordBox.value.toString
    val confirm=confirmBox.value.toString
    val companyName=companyNameBox.value.toString
    val re1="^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$".r
    val accnum=re1.findAllIn(account).length
    val re2="^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$".r
    if(account==""||password==""||confirm==""||companyName==""||accnum==0){
      dom.window.alert(s"表单格式不正确")
    }else{
      if(password!=confirm){
        dom.window.alert(s"两次输入密码不一致")
      }else{
        val bodyStr = RegisterReq(companyName,account,password).asJson.noSpaces
        Http.postJsonAndParse[CommonRsp](RegisterSubmitUrl, bodyStr).foreach{
          case Right(rsp) =>
            if(rsp.errCode == 0){
              println(s"service login submit success.")
              dom.window.alert(s"注册成功，去登陆")
              Shortcut.redirect(LoginUrl)
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
  }

  passwordBox.onkeypress = { e: KeyboardEvent =>
    if(e.charCode == KeyCode.Enter){
      e.preventDefault()
      registerButton.click()
    }
  }

  def render():Div={
    div(*.cls:="container")(
      div(*.cls:="row",*.marginTop:="200px")(
        div(*.cls:="col-md-6 col-md-offset-3")(
          form(*.cls:="form-horizontal")(
            div(*.cls:="form-group",*.textAlign.center)(
              h1("企业用户注册")
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("邮箱"),
              div(*.cls:="col-sm-10")(accountBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("密码"),
              div(*.cls:="col-sm-10")(passwordBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("确认密码"),
              div(*.cls:="col-sm-10")(confirmBox)
            ),
            div(*.cls:="form-group")(
              label(*.cls:="col-sm-2 control-label")("公司名称"),
              div(*.cls:="col-sm-10")(companyNameBox)
            ),
            div(*.cls:="form-group")(
              div(*.cls:="col-sm-offset-2 col-sm-10")(
                registerButton
              )
            )
          )
        )
      )
    ).render
  }
}
