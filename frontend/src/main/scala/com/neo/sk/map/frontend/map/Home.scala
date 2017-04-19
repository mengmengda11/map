package com.neo.sk.map.frontend.map

import com.neo.sk.map.frontend.Routes.MapRoute
import com.neo.sk.map.frontend.utils.{Http, JsFunc}
import com.neo.sk.map.frontend.{Component, Counter}
import com.neo.sk.map.ptcl._
import org.scalajs.dom.html
import org.scalajs.dom.html._
import org.scalajs.dom._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import org.scalajs.dom.raw.{SVGElement, SVGRect}
import org.scalajs.dom.svg.SVG

import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/4/12.
  */
object Home extends Component[Div]{

  import scalatags.JsDom.short._
  val textUrl=MapRoute.text
  val title = h1(*.id:="title",*.textAlign := "center")("map")
  val content = p(*.textAlign := "center")("map")

  val counter = new Counter("hello", 10)

  val svg = iframe(*.id:="svg")(*.src:="/hw1701a/static/img/simple.svg",*.width:="100%",*.height:="600px").render
  val inputDom = input(*.placeholder := "counter name").render
  val changeNameButton = button("text").render
  var xlength:Int=0
  var ylength:Int=0
  var x1:Int=0
  var y1:Int=0
  var width:Int=0
  var height:Int=0
  var startx:Int=0
  var starty:Int=0
  var endx:Int=0
  var endy:Int=0

  def setSvgAttribute(evt:MouseEvent)={
    if(startx==0&&starty==0){
      startx=evt.clientX.toInt
      starty=evt.clientY.toInt
    }else{
      endx=evt.clientX.toInt
      endy=evt.clientY.toInt
    }
    println("startx="+startx+"starty"+starty+"endx"+endx+"endy"+endy)
  }

  dom.window.setTimeout(()=>{
    xlength=dom.document.getElementById("svg").scrollWidth
    ylength=dom.document.getElementById("svg").scrollHeight
    val paths = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementById("path")
    val background = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementById("background").asInstanceOf[SVG]
    background.onclick = {
      e:MouseEvent =>
        setSvgAttribute(e)
        e.preventDefault()
    }
    x1 = paths.getAttribute("x").toInt
    y1=paths.getAttribute("y").toInt
     width=paths.getAttribute("width").toInt
     height=paths.getAttribute("height").toInt
    println("background"+background)
    println("x1="+x1+"+y1="+y1+"xlength="+xlength++"ylength="+ylength+"width="+width++"height="+height)
  },2000)


  def drawPath(data:List[Info])={
   val length=data.length
    for(a<- 0 to length-2){
      val start=data(a)
      val end=data(a+1)
      //创建div
      val top=start.x*10+86
     val line=div(*.width:="10px",*.height:="10px",*.left:=s"${start.y*10}px",*.top:=s"${top}px",*.position.absolute,*.visibility.visible,*.backgroundColor:="#cdcdcd").render
     document.body.appendChild(line)
    }
  }


  changeNameButton.onclick = { e: MouseEvent =>
    val bodyStr=Text(xlength,ylength,x1,y1,width,height,startx,starty,endx,endy).asJson.noSpaces
    Http.postJsonAndParse[TextRsp](textUrl,bodyStr).foreach{
      case Right(rsp)=>
        rsp.errCode match{
          case 0=>
            println("成功")
            JsFunc.alert(rsp.data.toString)
            drawPath(rsp.data)
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
      svg,
      changeNameButton
    ).render
  }

}
