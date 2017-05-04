package com.neo.sk.map.frontend

import com.neo.sk.map.frontend.Company.Login
import com.neo.sk.map.frontend.map._
import org.scalajs.dom
import org.scalajs.dom.html.Heading

import scala.scalajs.js

/**
  * User: Taoz
  * Date: 12/13/2016
  * Time: 8:09 PM
  */
object Hub extends js.JSApp {


  import scalatags.JsDom.short._

  val p1 = """/""".r

  val basePath =
    Option(dom.document.getElementById("basePath"))
      .map(_.innerHTML).getOrElse("/hw1701a/")

  def elementIdHtml(id: String): Option[String] = Option(dom.document.getElementById(id)).map(_.innerHTML)


  @scala.scalajs.js.annotation.JSExport
  override def main(): Unit = {

    println(s"fake basePath: ${elementIdHtml("basePath")}")
    println(s"fake pathName: ${elementIdHtml("pathname")}")
    println(s"fake urlSearch: ${elementIdHtml("urlSearch")}")

    println(s"current href: ${dom.window.location.href}")
    println(s"current search: ${dom.window.location.search}")
    println(s"current pathname: ${dom.window.location.pathname}")


    val path =
      elementIdHtml("pathname").getOrElse(dom.window.location.pathname)


    println(s"full path: $path")

    val paths = if (path.startsWith(basePath)) {
      p1.split(path.substring(basePath.length))
    } else {
      Array[String]()
    }

    println(s"valid paths: ${paths.mkString("[", ";", "]")}")

    val page = if (paths.length < 1) {
      println(s"paths.length error: $path")
      todo(s"paths.length error: $path")
    } else {

      //JsFunc.alert(s"here is Hub. full href: ${dom.document.location.href}")
      paths(0) match {
        case "counter" => paths(1) match {
          case "helloPage" => HelloPage.render()
          case "hiPage" => HiPage.render()
          case x => todo(s"counter match error: ${paths.mkString("/")}")
        }
        case "map" => paths(1) match {
          case "home" => Home.render()
          case "path" => Path.render()
          case x => todo(s"map match error: ${paths.mkString("/")}")
        }
        case "company" => paths(1) match {
          case "login" => Login.render()
          case "home" => Company.Home.render()
          case x => todo(s"map match error: ${paths.mkString("/")}")
        }
        case "admin" => paths(1) match {
          case "home" => todo(paths(1))
          case "login" => todo(paths(1))
          case x => todo(s"admin match error: ${paths.mkString("/")}")
        }
        case "zttests" => paths(1) match {
          case "business" => paths(2) match {
            case "dashboard" => todo(paths(2))
            case "selector" => todo(paths(2))
            case x => todo(s"zttests match error: ${paths.mkString("/")}")
          }
          case x => todo(s"match error: ${paths.mkString("/")}")
        }
        case "frontTest" => paths(1) match {
          case "helloPage" => HelloPage.render()
          case "hiPage" => HiPage.render()
          case x => todo(s"frontTest match error: ${paths.mkString("/")}")
        }

        case x => todo(s"paths(0) match error: ${paths(0)}")
      }
    }

    dom.document.body.appendChild(page)

  }


  def todo(title: String): Heading = h1(
    s"TO DO PAGE: $title"
  ).render


}
