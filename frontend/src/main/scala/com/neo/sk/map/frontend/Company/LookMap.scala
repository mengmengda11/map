package com.neo.sk.map.frontend.Company

import com.neo.sk.map.frontend.{Component, Counter}
import com.neo.sk.map.frontend.Routes.MapRoute
import com.neo.sk.map.frontend.utils.{Http, JsFunc, Shortcut}
import com.neo.sk.map.ptcl._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.Image
import org.scalajs.dom.html._
import org.scalajs.dom.svg._
import scalatags.JsDom.short._

import scala.collection.mutable.ListBuffer

/**
  * Created by mengmengda on 2017/5/4.
  */
class LookMap(item:MapInfo) {


  val startDiv=div().render
  val endDiv=div().render
  var lineList:ListBuffer[Div]=new ListBuffer[Div]
  val svgUrl=item.map.split("=")(item.map.split("=").length-1)
  val imgUrl=item.mapPic.split("=")(item.mapPic.split("=").length-1)

  val counter = new Counter("hello", 10)

  val svg = iframe(*.id:="svg")(*.src:="/hw1701a/static/img/"+svgUrl,*.width:="100%",*.height:="600px").render
  val inputDom = input(*.placeholder := "counter name").render
  val startBox=input(*.`type`:="text").render
  val endBox=input(*.`type`:="text").render
  val changeButton = button("二值化").render
  val saveButton = button("保存图片").render

  dom.window.setTimeout(()=>{
   val xlength=dom.document.getElementById("svg").scrollWidth
   val ylength=dom.document.getElementById("svg").scrollHeight
    val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")
    changeButton.onclick={
      e:MouseEvent=>
        e.preventDefault()
        for(i<- 0 to houses.length-1){
          val houseSvg=houses(i).asInstanceOf[SVG]
          val color=houseSvg.getAttribute("fill")
          houseSvg.setAttribute("fill","#000000")
          houseSvg.setAttribute("stroke","#000000")
          val text=houseSvg.parentNode.lastChild
          val parent=houseSvg.parentNode
          parent.removeChild(parent.lastChild)
          parent.removeChild(parent.lastChild)
        }
    }

    saveButton.onclick={
      e:MouseEvent=>
        e.preventDefault()
        val canvass=document.createElement("canvas").asInstanceOf[Canvas]
        canvass.width=xlength
        canvass.height=ylength
        val context=canvass.getContext("2d")
        val cc=Image.createBase64Svg("/hw1701a/static/img/newmap.svg")
        //val cc=dom.document.createElement("img").asInstanceOf[dom.html.Image]
        println("cc"+cc)
        context.drawImage(cc,0,0)
        println("context",context)
        canvass.toDataURL("image/png")
        val url=canvass.toDataURL("image/png")
        val aa=document.createElement("a").asInstanceOf[A]
        println("canvass"+canvass)
        println("url"+url)
        val imageA=img(*.src:=url).render
        divDom.textContent=""
        divDom.appendChild(imageA)

    }
  },2000)

  val copyButton=button(*.cls:="btn btn-default")("复制").render
  val urlBox=input(*.cls:="form-control",*.`type`:="text",*.readonly).render
  urlBox.value=s"http://localhost:49001/hw1701a/map/path?id=${item.id}&company=${item.companyId}"

  copyButton.onclick={
    e:MouseEvent =>
      e.preventDefault()
      urlBox.select()
      document.execCommand("Copy")
      JsFunc.alert("复制成功")
  }

  val divDom= div(
    div(*.cls:="panel panel-default")(
      div(*.cls:="panel-heading")(
        h3(*.cls:="panel-title")("地图基本信息")
      ),
      div(*.cls:="panel-body")(
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")(s"地图名称：${item.mapName}")
        ),
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")(s"地图描述：${item.des}")
        ),
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")(s"创建时间：${Shortcut.formatyyyyMMddHHmm(item.createTime.toInt)}")
        ),
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")("Url"),
          urlBox,
          copyButton
        )
      )
    ),
    div(*.cls:="panel panel-default")(
      div(*.cls:="panel-heading")(
        h3(*.cls:="panel-title")("地图信息")
      ),
      div(*.cls:="panel-body")(
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")(s"地图SVG"),
          svg
        ),
        div(*.cls:="form-group")(
          label(*.`for`:="serviceQuestion")(s"地图二值图"),
          img(*.src:="/hw1701a/static/img/"+imgUrl,*.width:="100%",*.height:="600px")
        )
      )
    )
  ).render






 def render(): Div =  {

    println("render HelloPage")
   divDom
  }

}
