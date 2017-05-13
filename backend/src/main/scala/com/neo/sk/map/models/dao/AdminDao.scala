package com.neo.sk.map.models.dao

import com.neo.sk.map.models.SlickTables._
import com.neo.sk.utils.DBUtil._

/**
  * Created by mengmengda on 2017/5/6.
  */
import com.neo.sk.map.models.SlickTables._

import com.neo.sk.utils.DBUtil._

import slick.jdbc.PostgresProfile.api._
object AdminDao {
  def getAdminByEmail(email:String)=db.run(
    tAdmin.filter(_.email===email).result.headOption
  )

  def isAdminExists(cId:Int) = db.run(
    tAdmin.filter(_.id === cId).exists.result
  )

  def getAllCompany=db.run(
    tCompany.result
  )

  def getAdminById(id: Int) = db.run{
    tAdmin.filter(_.id === id).result.headOption
  }

  def updatePsw(id:Int,psw:String)=db.run(
    tAdmin.filter(_.id===id).map(_.password).update(psw)
  )

}
