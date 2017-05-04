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

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/4/20.
  */

object MapList {

  val mapListUrl =CompanyRoute.mapList
  val mapListUpdateUrl = CompanyRoute.updateMap

  def deleteUrl(id: Long) = CompanyRoute.deleteMap(id)

  val listBody = tbody().render

  private def getMapList() = {
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

  private def handleList(list: List[MapInfo]) = {
    listBody.textContent=""
    list.zipWithIndex foreach { case (item: MapInfo, key: Int) =>
      listBody.appendChild(
        tr(*.id := (s"questionList-item-$key"))(
          th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
          td(item.mapName),
          td(Shortcut.formatyyyyMMddHHmm(item.createTime.toInt)),
          td(
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); changeListItem(key, item) }
              )("修改"),
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault(); deleteQuestion(key) })("删除")
            )
          )
        ).render
      )
    }
  }


  private def changeListItem(key: Int, item: MapInfo): Unit = {

    val serviceListItem = document.getElementById(s"questionList-item-$key")
    listBody.replaceChild(
      tr(*.id := (s"questionList-item-$key"))(
        th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
        td(input(*.id := (s"questionList-item-question-$key"), *.value := item.mapName)),
        td(Shortcut.formatyyyyMMddHHmm(item.createTime)),
        td(
          div(*.id := (s"questionList-operation-$key"))(
            button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); resetOperation(key, item) })("取消"),
            button(*.`type` := "button", *.cls := "btn btn-success", *.onclick := { e: MouseEvent => e.preventDefault(); handleChange(key, item) })("确认")
          )
        )
      ).render
      , serviceListItem)
  }

  private def handleChange(key: Int, item: MapInfo): Unit = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val question = document.getElementById(s"questionList-item-question-$key").asInstanceOf[html.Input].value.toString
    val answer = document.getElementById(s"questionList-item-answer-$key").asInstanceOf[html.Input].value.toString


    val newItem = MapInfo(id.toInt, question,answer, item.createTime)

    val bodyStr = UpdateMapReq(id.toInt,question,answer).asJson.noSpaces
    console.log(bodyStr)
    Http.postJsonAndParse[CommonRsp](mapListUpdateUrl, bodyStr).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"更新${id}号地图成功")
            JsFunc.alert(s"更新${id}号地图成功")
            resetOperation(key, newItem)
          case x =>
            println(s"更新${id}号地图失败$x")
            JsFunc.alert(s"更新${id}号地图失败$x")

        }
      case Left(e) =>
        println(s"更新${id}号地图出错$e")
        JsFunc.alert(s"更新${id}号地图出错$e")
    }
  }


  private def resetOperation(key: Int, item: MapInfo): Unit = {
    val questionListItem = document.getElementById(s"questionList-item-$key")
    listBody.replaceChild(
      tr(*.id := (s"questionList-item-$key"))(
        th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
        td(item.mapName),
        td(Shortcut.formatyyyyMMddHHmm(item.createTime)),
        td(
          div(*.id := (s"questionList-operation-$key"))(
            button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); changeListItem(key, item) }
            )("修改"),
            button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault(); deleteQuestion(key) })("删除"),
            button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault(); })("预览")
          )
        )
      ).render
      , questionListItem)
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
            listBody.removeChild(th)
          case x =>
            println(s"删除${id}号地图失败！")
            JsFunc.alert(s"删除${id}号地图失败！")
        }
      case Left(e) =>
        println(s"删除${id}号地图出错！")
        JsFunc.alert(s"删除${id}号地图出错！")
    }
  }



  def render(): Div = {
    getMapList()
    div(*.cls := "question-list")(
      table(*.cls := "table table-bordered")(
        caption("地图列表"),
        thead(
          tr(
            th("Id"),
            th("地图名称"),
            th("创建时间"),
            th("操作")
          )
        ),
        listBody
      )
    ).render
  }
}
