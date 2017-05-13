package com.neo.sk.map.frontend.Admin
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
  * Created by mengmengda on 2017/5/6.
  */

object CompanyList {

  val companyListUrl =AdminRoute.companyList

  def deleteUrl(id: Long) = AdminRoute.deleteCompany(id)
  def unableUrl(id: Long) = AdminRoute.unableCompany(id)
  def ableUrl(id: Long) = AdminRoute.ableCompany(id)

  val listBody = tbody().render

  private def getMapList() :Any= {
    Http.getAndParse[CompanyInfoRsp](companyListUrl).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println("获取商户列表成功")
            handleList(rsp.data)
          case x =>
            println(s"获取商户列表失败$x")
            JsFunc.alert(s"获取商户列表失败$x")
        }
      case Left(e) =>
        println(s"获取商户列表出错$e")
        JsFunc.alert(s"获取商户列表出错$e")
    }
  }




  private def handleList(list: List[CompanyInfo]) = {
    listBody.textContent=""
    list.zipWithIndex foreach { case (item: CompanyInfo, key: Int) =>
      listBody.appendChild(
        tr(*.id := (s"questionList-item-$key"))(
          th(*.id := (s"questionList-item-id-$key"), *.scoped := "row")(item.id),
          td(item.companyName),
          td(Shortcut.formatyyyyMMddHHmm(item.createTime.toInt)),
          td(if (item.state == 1) "正常" else "已禁止"),
          td(
            div(*.id := (s"questionList-operation-$key"))(
              if (item.state == 1){
                button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); unableCompany(key) }
                )("禁止")
              }else{
                button(*.`type` := "button", *.cls := "btn btn-primary", *.onclick := { e: MouseEvent => e.preventDefault(); ableCompany(key) }
                )("恢复")
              },
              button(*.`type` := "button", *.cls := "btn btn-danger", *.onclick := { e: MouseEvent => e.preventDefault(); deleteQuestion(key) })("删除")
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

  private def unableCompany(key: Int) = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val th = document.getElementById(s"questionList-item-$key")
    Http.getAndParse[CommonRsp](unableUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"禁止${id}号商家成功！")
            JsFunc.alert(s"禁止${id}号商家成功！")
            getMapList()
          case x =>
            println(s"禁止${id}号商家失败！")
            JsFunc.alert(s"禁止${id}号商家失败！")
        }
      case Left(e) =>
        println(s"禁止${id}号商家出错！")
        JsFunc.alert(s"禁止${id}号商家出错！")
    }
  }
  private def ableCompany(key: Int) = {
    val id = document.getElementById(s"questionList-item-id-$key").textContent.toLong
    val th = document.getElementById(s"questionList-item-$key")
    Http.getAndParse[CommonRsp](ableUrl(id)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"恢复${id}号商家成功！")
            JsFunc.alert(s"恢复${id}号商家成功！")
            getMapList()
          case x =>
            println(s"恢复${id}号商家失败！")
            JsFunc.alert(s"恢复${id}号商家失败！")
        }
      case Left(e) =>
        println(s"恢复${id}号商家出错！")
        JsFunc.alert(s"恢复${id}号商家出错！")
    }
  }


  def render(): Div = {
    getMapList()
    div(*.cls := "question-list")(
      table(*.cls := "table table-bordered")(
        caption("商户列表"),
        thead(
          tr(
            th("Id"),
            th("商家名称"),
            th("创建时间"),
            th("状态"),
            th("操作")
          )
        ),
        listBody
      )
    ).render
  }
}
