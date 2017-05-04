package com.neo.sk.utils
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream

import scala.Array._;
/**
  * Created by mengmengda on 2017/4/26.
  */
object Draw {
  @throws[Exception]
  def main(path:String) {
    val result: Array[Array[Int]] = imgMtr(path)
    //val mapArray=ofDim[Int](result.length,result(1).length)
    for(i<- 0 to result.length-1 ){
      for(j<- 0 to result(1).length-1){
        System.out.print(result(i)(j))
      }
      System.out.print("\n")
    }
    System.out.print("\n")
    System.out.print("**********************")

//    for (row <- result) {
//      for (col <- row) {
//        System.out.print(col)
//      }
//      System.out.print("\n")
//    }


  }

  def imgMtr(imagePath: String): Array[Array[Int]] = {
    val image: BufferedImage = javax.imageio.ImageIO.read(new FileInputStream(new File(imagePath)))
    val w: Int = image.getWidth/5
    val h: Int = image.getHeight/5
    val mtr=ofDim[Int](h,w)

    for(i<-0 to h-1){
      for(j<- 0 to w-1){
        val rgb: Int = image.getRGB(j*5, i*5)
        val r: Int = (rgb & 0x00ff0000) >> 16
        val g: Int = (rgb & 0x0000ff00) >> 8
        val b: Int = (rgb & 0x000000ff)
        val a: Int = 0xff / 2
        if (r > a && g > a && b > a) {
          mtr(i)(j) = 0
        }
        else {
          mtr(i)(j) = 1
        }
      }
    }

    //    var i: Int = 0
    //    while (i < h) {
    //      {
    //        var j: Int = 0
    //        while (j < w) {
    //          {
    //            val rgb: Int = image.getRGB(j, i)
    //            val r: Int = (rgb & 0x00ff0000) >> 16
    //            val g: Int = (rgb & 0x0000ff00) >> 8
    //            val b: Int = (rgb & 0x000000ff)
    //            val a: Int = 0xff / 2
    //            if (r > a && g > a && b > a) {
    //              mtr(i)(j) = 1
    //            }
    //            else {
    //              mtr(i)(j) = 0
    //            }
    //          }
    //          ({
    //            j += 1; j - 1
    //          })
    //        }
    //      }
    //      ({
    //        i += 1; i - 1
    //      })
    //    }
    return mtr
  }
}


