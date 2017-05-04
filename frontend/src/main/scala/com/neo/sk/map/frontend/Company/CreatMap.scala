package com.neo.sk.map.frontend.Company


import com.neo.sk.map.frontend.Routes.CompanyRoute
import com.neo.sk.map.ptcl.{CommonRsp, CreateMapReq}
import com.neo.sk.map.frontend.utils.{Http, JsFunc}
import com.neo.sk.map.ptcl._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import org.scalajs.dom.html
import io.circe.generic.auto._
import io.circe.syntax._

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mengmengda on 2017/4/20.
  */

object CreatMap {

  val addMapUrl=CompanyRoute.createMap
  val addMoadl=div().render
  val questionList=div().render
  val imgBox = img(
    *.height := "80px", *.width := "80px",
    *.src := ""
  ).render
  val imgInput = new ImageUpload(imgBox).render()


  val mapNameBox=input(*.`type`:="text" ,*.id:="serviceQuestion",*.cls:="form-control").render
  val mapBox= input(*.`type`:="text" ,*.id:="serviceAnswer",*.cls:="form-control").render

  def show = {
    modalDom.setAttribute("style","display:block;background-color:rgba(0, 0, 0, 0.54)")
  }

  def hide = {
    modalDom.setAttribute("style","display:none")
    mapNameBox.value=""
    imgBox.setAttribute("src","")
  }

  val cancelButton = button(*.cls := "btn btn-secondary")("取消").render
  cancelButton.onclick = {e:MouseEvent =>
    e.preventDefault()
    hide
  }
  val confirmBtn = button(*.cls := "btn btn-success",*.float:="right")("确定").render
  confirmBtn.onclick ={e:MouseEvent=>
    e.preventDefault()
    handleAdd()
  }

  private def renderQuestionList() = {
    questionList.textContent= ""
    questionList.appendChild(
      MapList.render()
    )
  }



  private def handleAdd()={
    val mapName=mapNameBox.value
    //val map=mapBox.value
    val map=imgBox.getAttribute("src")

    if(mapName==""||map==""){
      dom.window.alert("内容不可为空")
    }else{
      val bodyStr=CreateMapReq(mapName,map).asJson.noSpaces
      Http.postJsonAndParse[CommonRsp](addMapUrl,bodyStr).foreach{
        case Right(rsp)=>
          rsp.errCode match{
            case 0=>
              println("添加地图成功")
              JsFunc.alert("添加地图成功")
              //addMoadl.textContent=""
              hide
              renderQuestionList()
            case x=>
              println(s"添加地图失败$x")
              JsFunc.alert(s"添加地图失败$x")
          }
        case Left(e)=>
          println(s"添加地图出错$e")
          JsFunc.alert(s"添加地图出错$e")
      }
    }
  }

  val modalDom = div(*.cls := "modal",*.overflow:="scroll")(
    div(*.cls := "form")(
      div(*.cls := "panel panel-default",*.width:="50%",*.marginTop:="100px",*.marginLeft:="25%")(
        div(*.cls := "panel-heading")(
          h3(*.cls := "panel-title")("添加地图")
        ),
        div(*.cls := "panel-body")(
          div()(
            form(*.cls:="form-horizontal",*.width:="60%",*.marginLeft:="20%")(
              div(*.cls:="form-group")(
                label(*.`for`:="serviceQuestion")("地图名称"),
                mapNameBox
              ),
              div(*.cls:="form-group")(
                label(*.`for`:="serviceAnswer")("地图文件"),
                //mapBox
                imgInput
              ),
              cancelButton,confirmBtn
            )
          )
        )
      )
    )

  ).render


  def render():Div={
    renderQuestionList()
    div(*.cls:="question-container")(

      modalDom.render,
      button(*.cls:="btn btn-default",*.`type`:="button",*.onclick:={e:MouseEvent=>e.preventDefault();

        show
      })("添加地图"),
      questionList
    ).render
  }
}
