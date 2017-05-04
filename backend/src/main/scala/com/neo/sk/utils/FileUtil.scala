package com.neo.sk.utils
import java.io._


import akka.http.scaladsl.model.ContentType

import org.slf4j.LoggerFactory

/**
  * Created by mengmengda on 2017/4/21.
  */



object FileUtil {


  val log = LoggerFactory.getLogger(this.getClass)


  def getExtName(fileName: String) = {

    val extIndex = fileName.lastIndexOf('.')

    if (extIndex <= 0) None else Some(fileName.substring(extIndex + 1))

  }


  def imageContentType(fileName: String): Option[ContentType] = {

    getExtName(fileName) match {
      case Some(extName) => ContentType.parse(s"image/$extName") match {
        case Right(ct) => Some(ct)
        case _ => None
      }
      case None => None
    }

  }


  def copyFile(srcFile: File, destFile: File) = {

    var byteRead = 0

    var in: InputStream = null

    var out: OutputStream = null

    try {

      in = new FileInputStream(srcFile)

      out = new FileOutputStream(destFile)

      val buffer = new Array[Byte](1024)


      byteRead = in.read(buffer)

      while (byteRead != -1) {

        out.write(buffer, 0, byteRead)

        byteRead = in.read(buffer)

      }

    } catch {

      case e: Exception =>

        log.error(s"Copy file failed, error: ${e.getMessage}")

    } finally {

      if (out != null)

        out.close()

      if (in != null)

        in.close()

    }

  }


  def fileCopy(srcFile: File, destFileName: String) = {

    var byteRead = 0

    var in: InputStream = null

    var out: OutputStream = null

    try {

      in = new FileInputStream(srcFile)

      out = new FileOutputStream(destFileName)

      val buffer = new Array[Byte](1024)


      byteRead = in.read(buffer)

      while (byteRead != -1) {

        out.write(buffer, 0, byteRead)

        byteRead = in.read(buffer)

      }


      /*数据流复制的另一种方法

      var flag = true

      while (flag){

        val num = in.read(buffer)

        if(num != -1){

          out.write(buffer,0,num)

        }else{

          flag = false

        }

      }*/



    } catch {

      case e: Exception =>

        log.error(s"File copy failed, error: ${e.getMessage}")

    } finally {

      if (out != null)

        out.close()

      if (in != null)

        in.close()

    }

  }


}
