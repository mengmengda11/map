package com.neo.sk.map.frontend.Company

import com.neo.sk.map.frontend.Routes.CompanyRoute
import com.neo.sk.map.frontend.utils.JsFunc
import com.neo.sk.map.ptcl.ImageUploadRsp
import io.circe.generic.auto._
import io.circe.{Decoder, Error}
import org.scalajs.dom.html.Div
import org.scalajs.dom.{html, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalatags.JsDom.short._

/**
  * Created by zm on 2017/4/21
  */
class ImageUpload(
                 imageBox: html.Image
                 ) {

  val fileUploadUrl = CompanyRoute.uploadMap

  val fileUploadButton = button(*.cls := "btn btn-primary")("上传").render



  val imageInput = input(*.cls := "", *.`type`:="file", *.name:="fileUpload").render



  val imageUpload =
    form(*.enctype:="multipart/form-data", *.action:=fileUploadUrl, *.method:="post", *.display.inline)(
      imageInput
    ).render


  private def updateImg(imgPath: String) = {
    val imgSrc = CompanyRoute.getMap(imgPath)
    imageBox.setAttribute("src", imgSrc)
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

  def render(): Div = {
    div(
      div(*.cls := "form-group")(
        label(*.cls := "col-md-2 control-label"),
        div(*.cls := "col-md-10")(
          //div(*.cls := "col-md-3")(imageBox),
          div(*.cls := "col-md-9")(
            imageUpload,
            div(*.marginTop := "20px")(
              fileUploadButton
            )
          )
        )
      )
    ).render
  }

}
