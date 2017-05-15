package com.neo.sk.map.models.dao

/**
  * Created by mengmengda on 2017/4/19.
  */
import com.neo.sk.map.models.SlickTables._

import com.neo.sk.utils.DBUtil._

import slick.jdbc.PostgresProfile.api._

object CompanyDao {

  def createCompany(company:rCompany)=db.run(
    tCompany+=company
  )


  def getCompanyByEmail(email:String)=db.run(
    tCompany.filter(_.email===email).result.headOption
  )

  def isCompanyExists(cId:Int) = db.run(
    tCompany.filter(_.id === cId).exists.result
  )

  def isCompanyEmailExist(email:String)=db.run(
    tCompany.filter(_.email===email).exists.result
  )

  def deleteCompany(companyId:Int) = db.run{
    tCompany.filter(_.id === companyId).delete
   // tMaps.filter(_.companyid===companyId).delete
  }

  def unableCompany(companyId:Int) = db.run{
    tCompany.filter(_.id === companyId).map(_.state).update(0)
   // tMaps.filter(_.companyid===companyId).map(_.state).update(0)
  }

  def ableCompany(companyId:Int) = db.run{
    tCompany.filter(_.id === companyId).map(_.state).update(1)
   // tMaps.filter(_.companyid===companyId).map(_.state).update(1)
  }

  def getCompanyById(companyId: Int) = db.run{
    tCompany.filter(_.id === companyId).result.headOption
  }

  def updatePsw(id:Int,psw:String)=db.run(
    tCompany.filter(_.id===id).map(_.password).update(psw)
  )






}
