package com.neo.sk.map.models.dao
import com.neo.sk.map.models.SlickTables._

import com.neo.sk.utils.DBUtil._

import slick.jdbc.PostgresProfile.api._
/**
  * Created by mengmengda on 2017/5/12.
  */
object UserDao {

  def add(r:rUser)=db.run(
    tUser+=r
  )

  def getUserByMap(mapId:Long)=db.run(
   tUser.filter(_.mapId===mapId).groupBy(_.roomName).map(t=>(t._1,t._2.length)).result
  )
}
