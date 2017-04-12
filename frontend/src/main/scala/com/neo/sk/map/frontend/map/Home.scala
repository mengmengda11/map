package com.neo.sk.map.frontend.map

import com.neo.sk.map.frontend.Routes.MapRoute
import com.neo.sk.map.frontend.utils.{Http, JsFunc}
import com.neo.sk.map.frontend.{Component, Counter}
import com.neo.sk.map.ptcl._
import org.scalajs.dom.html._
import org.scalajs.dom._
import io.circe.generic.auto._
import io.circe.syntax._

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/4/12.
  */
object Home extends Component[Div]{

  import scalatags.JsDom.short._
  val textUrl=MapRoute.text
  val title = h1(*.textAlign := "center")("map")
  val content = p(*.textAlign := "center")("map")

  val counter = new Counter("hello", 10)

  val inputDom = input(*.placeholder := "counter name").render
  val changeNameButton = button("text").render
  changeNameButton.onclick = { e: MouseEvent =>
    val bodyStr=Text("00","00").asJson.noSpaces
    Http.postJsonAndParse[TextRsp](textUrl,bodyStr).foreach{
      case Right(rsp)=>
        rsp.errCode match{
          case 0=>
            println("成功")
            JsFunc.alert(rsp.data.toString)
          case x=>
            println(s"失败$x")
            JsFunc.alert(s"失败$x")
        }
      case Left(e)=>
        println(s"出错$e")
        JsFunc.alert(s"出错$e")
    }
  }


  override def render(): Div = {
    println("render HelloPage")
    div(
      title,
      content,
      inputDom,
      changeNameButton,
      counter.render()
    ).render
  }

}
