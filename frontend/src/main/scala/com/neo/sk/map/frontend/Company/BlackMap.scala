package com.neo.sk.map.frontend.Company
import com.neo.sk.map.frontend.{Component, Counter}
import com.neo.sk.map.frontend.Routes.{CompanyRoute, MapRoute}
import com.neo.sk.map.frontend.utils.{Http, JsFunc}
import com.neo.sk.map.ptcl._
import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.Image
import org.scalajs.dom.html._
import org.scalajs.dom.svg._
import io.circe.generic.auto._
import io.circe.{Decoder, Error}
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scalatags.JsDom.short.{*, _}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by mengmengda on 2017/5/4.
  */
object BlackMap {
  var imgSrc=""
  val tbody=div().render
  val imgBox = img(
    *.height := "80px", *.width := "80px",
    *.src := ""
  ).render
  val imgInput = new ImageUpload(imgBox).render()
  val fileUploadUrl = CompanyRoute.uploadMap

  val fileUploadButton = button(*.cls := "btn btn-primary")("上传").render



  val imageInput = input(*.cls := "", *.`type`:="file", *.name:="fileUpload").render



  val imageUpload =
    form(*.enctype:="multipart/form-data", *.action:=fileUploadUrl, *.method:="post", *.display.inline)(
      imageInput
    ).render


  private def updateImg(imgPath: String) = {
    val imgSrc = CompanyRoute.getMap(imgPath)
    svg.setAttribute("src","/hw1701a/static/img/"+imgSrc.split("=")(imgSrc.split("=").length-1))
  }

  private def Parse[T](bodyStr: Future[String])(implicit decoder: Decoder[T]): Future[Either[Error, T]] = {
    import io.circe.parser._
    bodyStr.map(s => decode[T](s))
  }

  private def uploadImg()= {
    val oData = new FormData(imageUpload)
    val oReq = new XMLHttpRequest()
    oReq.open("POST", fileUploadUrl, true)
    oReq.send(oData)
    oReq.onreadystatechange =  { e:Event=>
      if (oReq.readyState == 4) {
        if(oReq.status == 200){
          val message = Future(oReq.responseText)
          Parse[ImageUploadRsp](message).map{
            case Right(info) =>
              if(info.errCode == 0){
                println("ok")
                updateImg(info.filePath)
                println(s"url=====${info.filePath}")
                JsFunc.alert(s"上传成功！")
                tbody.appendChild(divDom)
                imgSrc="/hw1701a/static/img/"+info.filePath.split("=")(info.filePath.split("=").length-1)
                println("***********"+info.filePath.split("=")(info.filePath.split("=").length-1))
                dom.window.setTimeout(()=>{
                makeBlack()
                },2000)
              }else{
                println(s"error:${info.msg}")
                JsFunc.alert(s"上传失败！")
              }
            case Left(error) =>
              println(s"parse error:$error")
              JsFunc.alert(s"上传失败！parse error:$error")
          }
        }
      }
    }

  }


  fileUploadButton.onclick = { e: MouseEvent =>
    e.preventDefault()
    uploadImg()
  }
  //以上为上传图片

  val startDiv=div().render
  val endDiv=div().render
  var lineList:ListBuffer[Div]=new ListBuffer[Div]

  val counter = new Counter("hello", 10)

  val svg = iframe(*.id:="svg")(*.src:="/hw1701a/static/img/newmap.svg",*.width:="100%",*.height:="550px").render
  val inputDom = input(*.placeholder := "counter name").render
  val startBox=input(*.`type`:="text").render
  val endBox=input(*.`type`:="text").render
  val changeButton = button("二值化").render
  val saveButton = button("保存图片").render

  def makeBlack()={
      val xlength=dom.document.getElementById("svg").scrollWidth
      val ylength=dom.document.getElementById("svg").scrollHeight
      val houses = dom.document.getElementById("svg").asInstanceOf[IFrame].contentDocument.getElementsByClassName("house")
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
        //        var w=window.open("about:blank","image from canvas")
        //        window.location.href=url
        val imageA=img(*.src:=url).render
        divDom.textContent=""
        divDom.appendChild(imageA)
    }

      changeButton.onclick={
        e:MouseEvent=>
          e.preventDefault()
          println("change********")
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
  }

  val divDom= div(
    svg,
    changeButton,
    saveButton
  ).render




  def  clean={
    tbody.textContent=""
  }

  def render(): Div =  {
    clean
    div(
      div(*.cls := "col-md-9")(
        imageUpload,fileUploadButton
      ),
      tbody
    ).render
  }

}
