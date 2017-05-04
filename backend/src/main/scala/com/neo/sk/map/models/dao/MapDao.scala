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

  def update(companyId:Int, id:Int, mapName: String, map: String) = db.run{
    tMaps.filter(_.id === id).filter(_.companyid === companyId)
      .map(s => (s.mapname, s.map))
      .update(mapName, map)
  }

  def getMapByCompanyId(id:Int) = db.run{
    tMaps.filter(_.id === id).map(_.companyid).result.headOption
  }

}
