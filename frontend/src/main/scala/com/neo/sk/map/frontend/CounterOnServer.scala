package com.neo.sk.map.frontend

import com.neo.sk.map.frontend.utils.{Http, JsFunc}
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:28 PM
  */
class CounterOnServer extends Component[Div] {

  import com.neo.sk.map.ptcl
  import com.neo.sk.map.ptcl._
  import io.circe.generic.auto._
  import io.circe.syntax._

  import scala.concurrent.ExecutionContext.Implicits.global
  import scalatags.JsDom.short._

  private var counterValue = 0

  val counterDom = div(
    p("counter: -")
  ).render


  private def changeCount(c: Int) = { event: MouseEvent =>
    event.preventDefault()
    val v = counterValue + c
    setCount(v)
    c match {
      case x if x > 0 => plusServerCounter(x)
      case x if x < 0 => minusServerCounter( -x )
      case _ => //do nothing.
    }
  }


  private def plusServerCounter(v: Int) = {
    assert(v >= 0)
    val url = Routes.CounterRoute.plus
    val data = ptcl.Plus(v).asJson.noSpaces
    Http.postJsonAndParse[ptcl.CounterRsp](url, data).map{
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          if(rsp.data.value == counterValue){
            //do nothing.
          } else {
            println(s"update counter by server to: ${rsp.data.value}")
            setCount(rsp.data.value)
          }
        } else {
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => JsFunc.alert(s"error: $error")
    }
  }


  private def minusServerCounter(v: Int) = {
    assert(v >= 0)
    val url = Routes.CounterRoute.minus
    val data = ptcl.Minus(v).asJson.noSpaces
    Http.postJsonAndParse[ptcl.CounterRsp](url, data).map{
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          if(rsp.data.value == counterValue){
            //do nothing.
          } else {
            println(s"update counter by server to: ${rsp.data.value}")
            setCount(rsp.data.value)
          }
        } else {
          JsFunc.alert(s"error: ${rsp.msg}")
        }
      case Left(error) => JsFunc.alert(s"error: $error")
    }
  }




  private def setCount(c: Int) = {
    counterValue = c
    counterDom.innerHTML = ""
    counterDom.appendChild(
      p("counter: " + counterValue).render
    )
  }

  val plusButton = button(*.onclick := changeCount(1))("plus").render

  val minusButton = button(*.onclick := changeCount(-1))("minus").render



  private def updateCounterByServer() = {
    val url = Routes.CounterRoute.get
    Http.getAndParse[CounterRsp](url).map{
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          val count = rsp.data.value
          setCount(count)
        } else {
          JsFunc.alert(s"some server error happen: ${rsp.msg}")
        }
      case Left(error) =>
        JsFunc.alert(s"some internal error happen: $error")
    }
  }



  override def render(): Div = {
    updateCounterByServer()
    div(
      h3("counter on server"),
      counterDom,
      minusButton,
      plusButton
    ).render
  }

}
