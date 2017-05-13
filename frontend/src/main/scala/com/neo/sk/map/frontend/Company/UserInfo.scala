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

object UserInfo {

  val mapListUrl =CompanyRoute.mapList
  def getUserInfoUrl(id:Int)=CompanyRoute.getUserInfo(id)

  val bodyDom=div().render

  val maplistBody = tbody().render
  val userListBody=tbody().render
  val maplistTable=div(
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
        maplistBody
      )
    ).render
  ).render

  val userlistTable=div(
    div(*.cls := "question-list")(
      table(*.cls := "table table-bordered")(
        caption("用户行为列表"),
        thead(
          tr(
            th("搜索地点"),
            th("搜索数量")
          )
        ),
        userListBody
      )
    ).render
  ).render


  private def getMapList():Any = {
    bodyDom.textContent=""
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
    maplistBody.textContent=""
    bodyDom.appendChild(maplistTable)
    list.zipWithIndex foreach { case (item: MapInfo, key: Int) =>
      maplistBody.appendChild(
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
              button(*.`type` := "button", *.cls := "btn btn-success", *.onclick := { e: MouseEvent => e.preventDefault();getUserInfo(item.id) })("查看统计")
            )
          )
        ).render
      )
    }
  }


  private def getUserInfo(mapId:Int):Any = {
    Http.getAndParse[userInfoRsp](getUserInfoUrl(mapId)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println("获取用户信息成功")
            handleInfo(rsp.date)
          case x =>
            println(s"获取用户信息失败$x")
            JsFunc.alert(s"获取用户信息失败$x")
        }
      case Left(e) =>
        println(s"获取用户信息出错$e")
        JsFunc.alert(s"获取用户信息出错$e")
    }
  }

  def handleInfo(date:List[userInfo])={
    bodyDom.textContent=""
    bodyDom.appendChild(userlistTable)
    date.zipWithIndex foreach { case (item:userInfo, key: Int) =>
      userListBody.appendChild(
        tr(*.id := (s"questionList-item-$key"))(
          td(item.roomName),
          td(item.userNumber)
        ).render
      )
    }

  }



  def render(): Div = {
    getMapList()
    div(bodyDom).render
  }
}
