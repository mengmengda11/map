package com.neo.sk.map.frontend.Company

import com.neo.sk.map.frontend.Routes._
import com.neo.sk.map.frontend.utils.{Http, JsFunc, Shortcut}
import com.neo.sk.map.ptcl._
import org.scalajs.dom._
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import org.scalajs.dom.html
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/4/20.
  */

object MapList {

  val mapListUrl =CompanyRoute.mapList
  val mapListUpdateUrl = CompanyRoute.updateMap

  def deleteUrl(id: Long) = CompanyRoute.deleteMap(id)
  def unableUrl(id: Long) = CompanyRoute.unableMap(id)
  def ableUrl(id: Long) = CompanyRoute.ableMap(id)
  val bodyDom=div().render

  val listBody = tbody().render
  val listTable=div(
    div(*.cls := "question-list")(
      table(*.cls := "table table-bordered")(
        caption("地图列表"),
        thead(
          tr(
            th("Id"),
            th("地图名称"),
            th("创建时间"),
            th("地图状态"),
            th("审核状况"),
            th("操作")
          )
        ),
        listBody
      )
    ).render
  ).render

  val editMapUrl=CompanyRoute.updateMap
  val editMoadl=div().render
  val imgBox = img(
    *.height := "80px", *.width := "80px",
    *.src := ""
  ).render
  val imgInput = new ImageUpload(imgBox).render()
  val imageBox = img(
    *.height := "80px", *.width := "80px",
    *.src := ""
  ).render
  val imageInput = new ImageUpload(imageBox).render()
  val mapNameBox=input(*.`type`:="text" ,*.id:="serviceQuestion",*.cls:="form-control").render
  val desBox=input(*.`type`:="text" ,*.id:="des",*.cls:="form-control").render
  val mapBox= input(*.`type`:="text" ,*.id:="serviceAnswer",*.cls:="form-control").render
  var mapId=0


  def show(item:MapInfo) = {
    modalDom.setAttribute("style","display:block;background-color:rgba(0, 0, 0, 0.54)")
    mapId=item.id
    mapNameBox.value=item.mapName
    desBox.value=item.des
    imgBox.setAttribute("src",item.map)
    imageBox.setAttribute("src",item.mapPic)
    println("mapId"+mapId)
  }

  def hide = {
    modalDom.setAttribute("style","display:none")
    mapNameBox.value=""
    mapBox.value=""
    desBox.value=""
    mapId=0
    imgBox.setAttribute("src","")
    imageBox.setAttribute("src","")
  }

  val cancelButton = button(*.cls := "btn btn-secondary")("取消").render
  cancelButton.onclick = {e:MouseEvent =>
    e.preventDefault()
    hide
  }
  val confirmBtn = button(*.cls := "btn btn-success",*.float:="right")("确定").render
  confirmBtn.onclick ={e:MouseEvent=>
    e.preventDefault()
    handleEdit()
  }

  private def handleEdit()={
    val mapName=mapNameBox.value
    val des=desBox.value
    //val map=mapBox.value
    val map=imgBox.getAttribute("src")
    val mapPic=imageBox.getAttribute("src")

    if(mapName==""||map==""||mapPic==""||des==""){
      dom.window.alert("内容不可为空")
    }else{
      val bodyStr=UpdateMapReq(mapId,mapName,des,map,mapPic).asJson.noSpaces
      Http.postJsonAndParse[CommonRsp](editMapUrl,bodyStr).foreach{
        case Right(rsp)=>
          rsp.errCode match{
            case 0=>
              println("修改地图成功")
              JsFunc.alert("修改地图成功")
              //addMoadl.textContent=""
              hide
              getMapList()
            case x=>
              println(s"修改地图失败$x")
              JsFunc.alert(s"修改地图失败$x")
          }
        case Left(e)=>
          println(s"修改地图出错$e")
          JsFunc.alert(s"修改地图出错$e")
      }
    }
  }

  val modalDom = div(*.cls := "modal",*.overflow:="scroll")(
    div(*.cls := "form")(
      div(*.cls := "panel panel-default",*.width:="50%",*.marginTop:="100px",*.marginLeft:="25%")(
        div(*.cls := "panel-heading")(
          h3(*.cls := "panel-title")("修改地图")
        ),
        div(*.cls := "panel-body")(
          div()(
            form(*.cls:="form-horizontal",*.width:="60%",*.marginLeft:="20%")(
              div(*.cls:="form-group")(
                label(*.`for`:="serviceQuestion")("地图名称"),
                mapNameBox
              ),
              div(*.cls:="form-group")(
                label(*.`for`:="serviceQuestion")("地图描述"),
                desBox
              ),
              div(*.cls:="form-group")(
                label(*.`for`:="serviceAnswer")("地图文件"),
                //mapBox
                imgInput
              ),
              div(*.cls:="form-group")(
                label(*.`for`:="serviceAnswer")("地图二值图"),
                //mapBox
                imageInput
              ),
              cancelButton,confirmBtn
            )
          )
        )
      )
    )

  ).render



  //以上为修改地图

  private def getMapList():Any = {
    bodyDom.textContent=""
    bodyDom.appendChild(
      div(listTable,
        modalDom).render
    )
    Http.getAndParse[MapInfoRsp](mapListUrl).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println("获取地图列表成功")
            handleList(rsp.data)
          case x =>
            println(s"获取地图列表失败$x")
            JsFunc.alert(s"获取地图列表失败$x")
        }
      case Left(e) =>
        println(s"获取地图列表出错$e")
        JsFunc.alert(s"获取地图列表出错$e")
    }
  }

  def lookmap(item:MapInfo)={
    bodyDom.textContent=""
    bodyDom.appendChild(new LookMap(item).render())
  }

  private def handleList(list: List[MapInfo]) = {
    listBody.textContent=""
    list.zipWithIndex foreach { case (item: MapInfo, key: Int) =>
      listBody.appendChild(
        tr(*.id := (s"questionList-item-$key"))(
          th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
          td(item.mapName),
          td(Shortcut.formatyyyyMMddHHmm(item.createTime.toInt)),
          td(if (item.state == 1) "正常" else "已禁止"),
          td(if (item.review == 1) "审核通过" else {
            if(item.review==0) "审核中"  else  "审核未通过"
          }),
          td(
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault();show(item) }
              )("修改"),
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault(); deleteQuestion(key) })("删除"),
              if (item.state == 1){
                button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); unableMap(key) }
                )("下架")
              }else{
                button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); ableMap(key) }
                )("上架")
              },
              button(*.`type` := "button", *.cls := "btn btn-success", *.onclick := { e: MouseEvent => e.preventDefault();lookmap(item) })("查看")
            )
          )
        ).render
      )
    }
  }



  private def deleteQuestion(key: Int) = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val th = document.getElementById(s"questionList-item-$key")
    Http.getAndParse[CommonRsp](deleteUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"删除${id}号地图成功！")
            JsFunc.alert(s"删除${id}号地图成功！")
            getMapList()
          case x =>
            println(s"删除${id}号地图失败！")
            JsFunc.alert(s"删除${id}号地图失败！")
        }
      case Left(e) =>
        println(s"删除${id}号地图出错！")
        JsFunc.alert(s"删除${id}号地图出错！")
    }
  }
  private def unableMap(key: Int) = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val th = document.getElementById(s"questionList-item-$key")
    Http.getAndParse[CommonRsp](unableUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"下架${id}号地图成功！")
            JsFunc.alert(s"下架${id}号地图成功！")
            getMapList()
          case x =>
            println(s"下架${id}号地图失败！")
            JsFunc.alert(s"下架${id}号地图失败！")
        }
      case Left(e) =>
        println(s"下架${id}号地图出错！")
        JsFunc.alert(s"下架${id}号地图出错！")
    }
  }

  private def ableMap(key: Int) = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val th = document.getElementById(s"questionList-item-$key")
    Http.getAndParse[CommonRsp](ableUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"上架${id}号地图成功！")
            JsFunc.alert(s"上架${id}号地图成功！")
            getMapList()
          case x =>
            println(s"上架${id}号地图失败！")
            JsFunc.alert(s"上架${id}号地图失败！")
        }
      case Left(e) =>
        println(s"上架${id}号地图出错！")
        JsFunc.alert(s"上架${id}号地图出错！")
    }
  }


  def render(): Div = {
    getMapList()
    div(bodyDom).render
//    div(listTable,
//      modalDom).render
  }
}
