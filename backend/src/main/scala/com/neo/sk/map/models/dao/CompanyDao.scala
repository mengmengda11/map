package com.neo.sk.map.models.dao

/**
  * Created by mengmengda on 2017/4/19.
  */
import com.neo.sk.map.models.SlickTables._

import com.neo.sk.utils.DBUtil._

import slick.jdbc.PostgresProfile.api._

object CompanyDao {


  def getCompanyByEmail(email:String)=db.run(
    tCompany.filter(_.email===email).result.headOption
  )

  def isCompanyExists(cId:Int) = db.run(
    tCompany.filter(_.id === cId).exists.result
  )





}
