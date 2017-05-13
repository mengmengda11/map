package com.neo.sk.map.frontend.Admin
import com.neo.sk.map.frontend.Routes._
import com.neo.sk.map.frontend.utils.Shortcut
import org.scalajs.dom
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by mengmengda on 2017/5/6.
  */
object Home {

  val AdminHomeUrl = AdminRoute.home
  val AdminLogoutUrl = AdminRoute.logout

  val containerBody = div( CompanyList.render()).render

  private def changeContainer(key: Long) = {
    containerBody.textContent= ""
    key match {
      case 0 =>
        containerBody.appendChild(
          CompanyList.render()
        )
      case 1 =>
        containerBody.appendChild(
          ReSetPsw.render()
        )
      case 2 =>
        containerBody.appendChild(
          MapList.render()
        )
      case 3 =>
        Shortcut.redirect(AdminLogoutUrl)

    }
  }

  def render(): Div = {
    div(
      div(*.cls:="container")(
        div(*.cls:="row")(
          div(*.cls:="navbar navbar-inverse navbar-fixed-top")(
            div(*.cls:="collapse navbar-collapse")(
              ul(*.cls:="nav navbar-nav",*.marginLeft:="80px")(
                li(*.cls:="navbar-brand")(
                  div(*.cls := "sidebar-nav", *.onclick := { e: MouseEvent => e.preventDefault(); changeContainer(0) })(
                    div(*.cls := "sidebar-title")("商家列表")
                  )
                ),
                li(*.cls:="navbar-brand")(
                  div(*.cls := "sidebar-nav", *.onclick := { e: MouseEvent => e.preventDefault(); changeContainer(1) })(
                    div(*.cls := "sidebar-title")("重置密码")
                  )
                ),
                li(*.cls:="navbar-brand")(
                  div(*.cls := "sidebar-nav", *.onclick := { e: MouseEvent => e.preventDefault(); changeContainer(2) })(
                    div(*.cls := "sidebar-title")("地图列表")
                  )
                ),
                li(*.cls:="navbar-brand")(
                  div(*.cls := "sidebar-nav", *.onclick := { e: MouseEvent => e.preventDefault(); changeContainer(3) })(
                    div(*.cls := "sidebar-title")("退出")
                  )
                )
              )
            )



          )
        ),
        div(*.cls:="row",*.marginTop:="70px")(
          containerBody
        )
      )

    ).render
  }
}
