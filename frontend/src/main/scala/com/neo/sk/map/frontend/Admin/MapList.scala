package com.neo.sk.map.frontend.Admin

import com.neo.sk.map.frontend.Company.LookMap
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
  * Created by mengmengda on 2017/5/10.
  */
object MapList {

  val reviewedMapListUrl =AdminRoute.noreviewMapList
  val noreviewMapListUrl=AdminRoute.reviewedMapList
  val bodyDom=div().render

  def deleteUrl(id: Long) = AdminRoute.deleteCompany(id)
  def passMapUrl(id: Long) = AdminRoute.passMap(id)
  def notPassMapUrl(id: Long) = AdminRoute.notPassMap(id)

  val listBody = tbody().render

  val mapListBtn= button(*.`type` := "button", *.cls := "btn btn-default")("平台地图列表").render
  val notMapListBtn= button(*.`type` := "button", *.cls := "btn btn-default")("待审核地图列表").render

  mapListBtn.onclick={
    e:MouseEvent=>
      e.preventDefault()
      getMapList()
  }

  notMapListBtn.onclick={
    e:MouseEvent=>
      e.preventDefault()
      getNotMapList()
  }



  private def getMapList() :Any= {
    bodyDom.textContent=""
    bodyDom.appendChild(tableDom)
    Http.getAndParse[MapInfoRsp](reviewedMapListUrl).foreach {
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


  private def getNotMapList() :Any= {
    bodyDom.textContent=""
    bodyDom.appendChild(tableDom)
    Http.getAndParse[MapInfoRsp](noreviewMapListUrl).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println("获取待处理地图列表成功")
            handleNotList(rsp.data)
          case x =>
            println(s"获取待处理地图列表失败$x")
            JsFunc.alert(s"获取待处理地图列表失败$x")
        }
      case Left(e) =>
        println(s"获取待处理地图列表出错$e")
        JsFunc.alert(s"获取待处理地图列表出错$e")
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
          td(if (item.state == 1) "正常" else "已禁止"),
          td(if (item.review == 0) "等待审核" else {
            if(item.review==1) "审核通过" else "审核未通过"
          }),
          td(
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault();lookmap(item)})("查看")
            )
          )
        ).render
      )
    }
  }

  private def handleNotList(list: List[MapInfo]) = {
    listBody.textContent=""
    list.zipWithIndex foreach { case (item: MapInfo, key: Int) =>
      listBody.appendChild(
        tr(*.id := (s"questionList-item-$key"))(
          th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
          td(item.mapName),
          td(Shortcut.formatyyyyMMddHHmm(item.createTime.toInt)),
          td(if (item.state == 1) "正常" else "已禁止"),
          td(if (item.review == 0) "等待审核" else {
            if(item.review==1) "审核通过" else "审核未通过"
          }),
          td(
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault();lookmap(item)})("查看")
            ),
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault();passMap(item.id)})("通过")
            ),
            div(*.id := (s"questionList-operation-$key"))(
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault() ;notPassMap(item.id)})("不通过")
            )
          )
        ).render
      )
    }
  }


  def lookmap(item:MapInfo)={
    println("lookmap********")
    bodyDom.textContent=""
    bodyDom.appendChild(new LookMap(item).render())
  }


  def notPassMap(id: Int) = {
    Http.getAndParse[CommonRsp](notPassMapUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"禁止通过${id}号地图成功！")
            JsFunc.alert(s"禁止通过${id}号地图成功！")
            getNotMapList()
          case x =>
            println(s"禁止通过${id}号地图失败！")
            JsFunc.alert(s"禁止通过${id}号地图失败！")
        }
      case Left(e) =>
        println(s"禁止通过${id}号地图出错！")
        JsFunc.alert(s"禁止通过${id}号地图出错！")
    }
  }
  def passMap(id: Int) = {
    Http.getAndParse[CommonRsp](passMapUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"通过${id}号地图成功！")
            JsFunc.alert(s"通过${id}号地图成功！")
            getNotMapList()
          case x =>
            println(s"通过${id}号地图失败！")
            JsFunc.alert(s"通过${id}号地图失败！")
        }
      case Left(e) =>
        println(s"通过${id}号地图出错！")
        JsFunc.alert(s"通过${id}号地图出错！")
    }
  }

 val  tableDom=div( table(*.cls := "table table-bordered")(
   caption("地图列表"),
   thead(
     tr(
       th("Id"),
       th("地图名称"),
       th("创建时间"),
       th("状态"),
       th("审核状态"),
       th("操作")
     )
   ),
   listBody
 )).render


  def render(): Div = {
    getMapList()
    div(*.cls := "question-list")(
      mapListBtn,
      notMapListBtn,
      bodyDom
    ).render
  }



}
