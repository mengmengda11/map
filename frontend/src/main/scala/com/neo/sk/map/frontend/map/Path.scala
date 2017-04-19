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
//import org.w3c.dom.Element

import scala.collection.mutable.ListBuffer
import scalatags.JsDom.short._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by zm on 2017/4/12.
  */
object Path extends Component[Div]{

  import scalatags.JsDom.short._
  val textUrl=MapRoute.text
  val title = h1(*.id:="title",*.textAlign := "center")("map")
  val content = p(*.textAlign := "center")("map")
  val startDiv=div().render
  val endDiv=div().render
  var lineList:ListBuffer[Div]=new ListBuffer[Div]

  val counter = new Counter("hello", 10)

  val svg = iframe(*.id:="svg")(*.src:="/hw1701a/static/img/map.svg",*.width:="100%",*.height:="600px").render
  val inputDom = input(*.placeholder := "counter name").render
  val changeNameButton = button("开始规划").render
  val restartButton = button("重新选择起始点").render

  var xlength:Int=0
  var ylength:Int=0
  val pathList:ListBuffer[PathInfo]=new ListBuffer[PathInfo]
  var startx:Int=0
  var starty:Int=0
  var endx:Int=0
  var endy:Int=0

  def setSvgAttribute(evt:MouseEvent)={
    if(startx==0&&starty==0){
      startx=evt.clientX.toInt
      starty=evt.clientY.toInt
      val startDom=div(*.width:="10px",*.height:="10px",*.left:=s"${startx}px",*.top:=s"${starty+86}px",*.position.absolute,*.visibility.visible,*.backgroundColor:="#ff0000").render
      startDiv.appendChild(startDom)
      document.body.appendChild(startDiv)
    }else if(endx==0&&endy==0){
      endx=evt.clientX.toInt
      endy=evt.clientY.toInt
      val endDom=div(*.width:="10px",*.height:="10px",*.left:=s"${endx}px",*.top:=s"${endy+86}px",*.position.absolute,*.visibility.visible,*.backgroundColor:="#ff0000").render
      endDiv.appendChild(endDom)
      document.body.appendChild(endDiv)
    }
    println("startx="+startx+"starty"+starty+"endx"+endx+"endy"+endy)
  }

  dom.window.setTimeout(()=>{
    xlength=dom.document.getElementById("svg").scrollWidth
    ylength=dom.document.getElementById("svg").scrollHeight
    val paths = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("path")
    val background = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementById("background").asInstanceOf[SVG]


    println("pathslength="+paths.length)
    println("path0"+paths(0))
    println("background="+background)
    for(i<- 0 to paths.length-1){
      val pathE=paths(i).asInstanceOf[dom.Element]
      val pathSvg=paths(i).asInstanceOf[SVG]
      val x=pathE.getAttribute("x").toInt
      val y=pathE.getAttribute("y").toInt
      val width=pathE.getAttribute("width").toInt
      val height=pathE.getAttribute("height").toInt
      pathList.append(PathInfo(y,x,height,width))
      pathSvg.onclick = {
        e:MouseEvent =>
          setSvgAttribute(e)
          e.preventDefault()
      }
    }

  },2000)


  def drawPath(data:List[Info])={
    val length=data.length
    for(a<- 0 to length-2){
      val start=data(a)
      val end=data(a+1)
      //创建div
      val top=start.x*10+86
      val line=div(*.width:="10px",*.height:="10px",*.left:=s"${start.y*10}px",*.top:=s"${top}px",*.position.absolute,*.visibility.visible,*.backgroundColor:="#cdcdcd").render
      lineList.append(line)
      //document.body.appendChild(line)
    }
    lineList.map{line=>
      document.body.appendChild(line)
    }

  }


  changeNameButton.onclick = { e: MouseEvent =>
    val bodyStr=Text(xlength,ylength,pathList.toList,startx,starty,endx,endy).asJson.noSpaces
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

  restartButton.onclick = { e: MouseEvent =>
    startx=0
    starty=0
    endx=0
    endy=0
    startDiv.textContent= ""
    endDiv.textContent=""
    lineList.map{line=>
      document.body.removeChild(line)
    }
    val lineListNew:ListBuffer[Div]=new ListBuffer[Div]
    lineList=lineListNew


  }




  override def render(): Div = {
    println("render HelloPage")
    div(
      title,
      svg,
      changeNameButton,
      restartButton
    ).render
  }

}
