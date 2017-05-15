package com.neo.sk.map.frontend.map

import com.neo.sk.map.frontend.Routes.MapRoute
import com.neo.sk.map.frontend.utils.{Http, JsFunc, Shortcut}
import com.neo.sk.map.frontend.{Component, Counter}
import com.neo.sk.map.ptcl._
import org.scalajs.dom.html
import org.scalajs.dom.html._
import org.scalajs.dom._
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import org.scalajs.dom.raw.{SVGElement, SVGRect, SVGTextElement}
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
  def getMapUrl(id: Long) = MapRoute.getMap(id)
  val bodyDom=div().render
  val textUrl=MapRoute.text
  val title = h1(*.id:="title",*.textAlign := "center")("map")
  val content = p(*.textAlign := "center")("map")
  val startDiv=div().render
  val endDiv=div().render
  val roomDiv=div().render
  var lineList:ListBuffer[Div]=new ListBuffer[Div]
  val paramStr =
    Option(dom.document.getElementById("urlSearch"))
      .map(_.innerHTML).getOrElse(dom.window.location.search).split("=",2)

  val params=Shortcut.getUrlParams
  val mapId=params("id")
  val companyId=params("company")

  val counter = new Counter("hello", 10)

  def getSvg(svg:String):Node= {

    val svg = iframe(*.id := "svg")(*.src := "/hw1701a/static/img/newmap.svg", *.width := "100%", *.height := "600px").render
    val inputDom = input(*.placeholder := "counter name").render
    val startBox = input(*.`type` := "text").render
    val endBox = input(*.`type` := "text").render
    val changeNameButton = button("开始规划").render
    val restartButton = button("重新选择起始点").render

    var xlength: Int = 0
    var ylength: Int = 0
    val pathList: ListBuffer[PathInfo] = new ListBuffer[PathInfo]
    var startx: Int = 0
    var starty: Int = 0
    var endx: Int = 0
    var endy: Int = 0
    var endRoomName:String = ""

    def setSvgAttribute(evt: MouseEvent) = {
      if (startx == 0 && starty == 0) {
        startx = evt.clientX.toInt
        starty = evt.clientY.toInt
        val startDom = div(*.width := "10px", *.height := "10px", *.left := s"${startx}px", *.top := s"${starty + 72}px", *.position.absolute, *.visibility.visible, *.backgroundColor := "#ff0000").render
        startDiv.appendChild(startDom)
        document.body.appendChild(startDiv)
      } else if (endx == 0 && endy == 0) {
        endx = evt.clientX.toInt
        endy = evt.clientY.toInt
        val endDom = div(*.width := "10px", *.height := "10px", *.left := s"${endx}px", *.top := s"${endy + 72}px", *.position.absolute, *.visibility.visible, *.backgroundColor := "#ff0000").render
        endDiv.appendChild(endDom)
        document.body.appendChild(endDiv)
      }
      println("startx=" + startx + "starty" + starty + "endx" + endx + "endy" + endy)
    }

    def setHouse(evt: MouseEvent) = {
      println("x=" + evt.clientX + "y=" + evt.clientY)
      if (startx == 0 && starty == 0) {
        val househome = evt.target.asInstanceOf[dom.Element]
        println("househome" + househome)
        val housename = househome.getAttribute("name")
        val doorx = househome.getAttribute("doorx")
        val doory = househome.getAttribute("doory")
        startBox.value = housename
        startx = doorx.toInt
        starty = doory.toInt
        val startDom = img(*.width := "10px", *.height := "20px", *.left := s"${evt.clientX}px", *.top := s"${evt.clientY + 72}px", *.position.absolute, *.visibility.visible, *.src := "/hw1701a/static/img/weizhi.jpg").render
        startDiv.appendChild(startDom)
        document.body.appendChild(startDiv)
      } else if (endx == 0 && endy == 0) {
        val househome = evt.target.asInstanceOf[dom.Element]
        val housename = househome.getAttribute("name")
        val doorx = househome.getAttribute("doorx")
        val doory = househome.getAttribute("doory")
        endBox.value = housename
        endx = doorx.toInt
        endy = doory.toInt
        endRoomName=housename.toString
        val endDom = img(*.width := "10px", *.height := "20px", *.left := s"${evt.clientX}px", *.top := s"${evt.clientY + 72}px", *.position.absolute, *.visibility.visible, *.src := "/hw1701a/static/img/weizhi.jpg").render
        endDiv.appendChild(endDom)
        document.body.appendChild(endDiv)
      }
      println("startx=" + startx + "starty" + starty + "endx" + endx + "endy" + endy)
    }



    dom.window.setTimeout(() => {
      xlength = dom.document.getElementById("svg").scrollWidth
      ylength = dom.document.getElementById("svg").scrollHeight
      val paths = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("path")
      val outside = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("outside").asInstanceOf[SVG]
      val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")

      for (i <- 0 to houses.length - 1) {
        val houseSvg = houses(i).asInstanceOf[SVG]
        houseSvg.onclick = {
          e: MouseEvent =>
            e.preventDefault()
            setHouse(e)
        }
      }
      outside.onclick = {
        e: MouseEvent =>
          e.preventDefault()
          println("x=" + e.clientX + "y=" + e.clientY)
      }


      println("pathslength=" + paths.length)
      println("path0" + paths(0))
      println("outside=" + outside)
      for (i <- 0 to paths.length - 1) {
        val pathE = paths(i).asInstanceOf[dom.Element]
        val pathSvg = paths(i).asInstanceOf[SVG]
        val x = pathE.getAttribute("x").toInt
        val y = pathE.getAttribute("y").toInt
        val width = pathE.getAttribute("width").toInt
        val height = pathE.getAttribute("height").toInt
        pathList.append(PathInfo(y, x, height, width))
        pathSvg.onclick = {
          e: MouseEvent =>
            //setSvgAttribute(e)
            e.preventDefault()
        }
      }

    }, 2000)


    def drawPath(data: List[Info]) = {
      val length = data.length
      for (a <- 0 to length - 2) {
        val start = data(a)
        val end = data(a + 1)
        //创建div
        val top = start.x * 5 + 72
        val line = div(*.width := "5px", *.height := "5px", *.left := s"${start.y * 5}px", *.top := s"${top}px", *.position.absolute, *.visibility.visible, *.backgroundColor := "#FF6A6A").render
        lineList.append(line)
      }
      lineList.map { line =>
        document.body.appendChild(line)
      }

    }


    changeNameButton.onclick = { e: MouseEvent =>
      if (startx * starty == 0 || endx * endy == 0) {
        if (startBox.value == "" || endBox.value == "") {
          JsFunc.alert("起始点不可为空")
        } else {
          val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")
          for (i <- 0 to houses.length - 1) {
            val houseSvg = houses(i).asInstanceOf[dom.Element]
            val houseName = houseSvg.getAttribute("name")
            if (houseName == startBox.value) {
              startx = houseSvg.getAttribute("doorx").toInt
              starty = houseSvg.getAttribute("doory").toInt
            } else if (houseName == endBox.value) {
              endx = houseSvg.getAttribute("doorx").toInt
              endy = houseSvg.getAttribute("doory").toInt
              endRoomName = houseName
            }
          }
        }
      }
      val bodyStr = Text(xlength, ylength, startx, starty, endx, endy, mapId.toInt, companyId.toInt,endRoomName).asJson.noSpaces
      Http.postJsonAndParse[TextRsp](textUrl, bodyStr).foreach {
        case Right(rsp) =>
          rsp.errCode match {
            case 0 =>
              println("成功")
              //JsFunc.alert(rsp.data.toString)
              drawPath(rsp.data)
            case x =>
              println(s"失败$x")
              JsFunc.alert(s"失败$x")
          }
        case Left(e) =>
          println(s"出错$e")
          JsFunc.alert(s"出错$e")
      }
    }

    restartButton.onclick = { e: MouseEvent =>
      startx = 0
      starty = 0
      endx = 0
      endy = 0
      startDiv.textContent = ""
      endDiv.textContent = ""
      startBox.value = ""
      endBox.value = ""
      endRoomName = ""
      lineList.map { line =>
        document.body.removeChild(line)
      }
      val lineListNew: ListBuffer[Div] = new ListBuffer[Div]
      lineList = lineListNew
    }


    //选择类别
    val stateRoom = select(*.cls:="form-control",*.width:="150px")(
      option(*.selected := "selected", *.disabled := "disabled")("--类型选择--"),
      option(*.value := s"food")("餐厅"),
      option(*.value := s"clothes")("衣服"),
      option(*.value := s"shoes")("鞋子"),
      option(*.value := s"weishengjian")("卫生间")
    ).render

    stateRoom.onchange={e:Event=>handleRoom()}
    def handleRoom()={
      println("change********")
      val roomClass=stateRoom.value
      if(roomClass=="selected"){
        JsFunc.alert("请选择类别")
      }else{
        val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")
        for (i <- 0 to houses.length - 1) {
          val houseSvg = houses(i).asInstanceOf[dom.Element]
          val houseClass = houseSvg.getAttribute("room")
          val houseName=houseSvg.getAttribute("name")
          val doorx = houseSvg.getAttribute("doorx").toInt
          val doory = houseSvg.getAttribute("doory").toInt
//          println("houseClass"+houseClass)
//          println("roomClass*********"+roomClass)
          if(houseClass==roomClass){
            println("houseClass+++++++++++++"+houseClass)
            println("doorx"+doorx+"doory"+doory)
            println("houseName"+houseName)
            val roomDom = img(*.id:=s"img-name${houseName}",*.width := "10px", *.height := "20px", *.left := s"${doorx}px", *.top := s"${doory + 72}px", *.position.absolute, *.visibility.visible, *.src := "/hw1701a/static/img/weizhi.jpg").render
            roomDiv.appendChild(roomDom)
            roomDiv.setAttribute("id",s"div-name${houseName}")
            document.body.appendChild(roomDiv)
          }
        }
      }
    }

    val changeRoomBtn=button("重新选择类别").render
    changeRoomBtn.onclick={ e: MouseEvent =>
      val roomClass=stateRoom.value
      if(roomClass=="selected"){

      }else{
        val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")
        for (i <- 0 to houses.length - 1) {
          val houseSvg = houses(i).asInstanceOf[dom.Element]
          val houseClass = houseSvg.getAttribute("room")
          val houseName=houseSvg.getAttribute("name")

          if(houseClass==roomClass){
            println("houseName+++++++++++++"+houseName)
            val deleteDom=dom.document.getElementById(s"div-name${houseName}")
            document.body.removeChild(deleteDom)

          }
        }
      }



    }



    div(
      title,
      svg,
      startBox,endBox,
      changeNameButton,
      restartButton,
      stateRoom,changeRoomBtn
    ).render

  }


  def getMap()={
    Http.getAndParse[SvgInfoRsp](getMapUrl(mapId.toInt)).foreach {
      case Right(rsp) =>
        rsp.errCode match {
          case 0 =>
            println(s"获取${mapId}号地图成功！")
           bodyDom.textContent=""
            bodyDom.appendChild(getSvg(rsp.data))
          case x =>
            println(s"获取${mapId}号地图失败！")
            JsFunc.alert(s"获取${mapId}号地图失败！")
        }
      case Left(e) =>
        println(s"获取${mapId}号地图出错！")
        JsFunc.alert(s"获取${mapId}号地图出错！")
    }
  }





  override def render(): Div = {
    getMap()
    println("render HelloPage")
    bodyDom
  }

}
