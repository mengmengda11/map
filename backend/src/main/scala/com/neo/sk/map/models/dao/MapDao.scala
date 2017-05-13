package com.neo.sk.map.models.dao

import org.slf4j.LoggerFactory

import com.neo.sk.utils.DBUtil.db
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.map.models.SlickTables._
import org.slf4j.LoggerFactory

/**
  * Created by mengmengda on 2017/4/20.
  */
object MapDao {
  private val log = LoggerFactory.getLogger(this.getClass)

  def create(q:rMaps ) = db.run{
    tMaps.returning(tMaps.map(_.id)) += q
  }

  def getMap(companyId: Int) = db.run{
    tMaps.filter(_.companyid === companyId).sortBy(_.createtime).result
  }

  def delete(id:Int, companyId:Int) = db.run{
    tMaps.filter(_.id === id).filter(_.companyid === companyId).delete
  }

  def update(companyId:Int, id:Int, mapName: String,des:String, map: String,mapPic:String) = db.run{
    tMaps.filter(_.id === id).filter(_.companyid === companyId)
      .map(s => (s.mapname,s.des, s.map,s.mapPic))
      .update(mapName,des, map,mapPic)
  }

  def getMapByCompanyId(id:Int) = db.run{
    tMaps.filter(_.id === id).map(_.companyid).result.headOption
  }

  def getMapById(id:Int)=db.run(
    tMaps.filter(_.id===id).result.headOption
  )

  def unableMap(id:Int)=db.run(
    tMaps.filter(_.id===id).map(_.state).update(0)
  )

  def ableMap(id:Int)=db.run(
    tMaps.filter(_.id===id).map(_.state).update(1)
  )
  def  reMap=db.run(
    tMaps.filter(_.review===0).result
  )

  def notReMap=db.run(
    tMaps.filter(_.review>0).result
  )

  def getMapByCompany(id:Int)=db.run(
    tMaps.filter(_.companyid===id).result
  )

  def passMap(id:Int)=db.run(
    tMaps.filter(_.id===id).map(_.review).update(1)
  )

  def notPassMap(id:Int)=db.run(
    tMaps.filter(_.id===id).map(_.review).update(2)
  )

  def getImgByMapId(mapId:Int)=db.run(
    tMaps.filter(_.id===mapId).map(_.mapPic).result.headOption
  )

  def getSvgByMap(mapId:Int)=db.run(
    tMaps.filter(_.id===mapId).map(_.map).result.headOption
  )



}
