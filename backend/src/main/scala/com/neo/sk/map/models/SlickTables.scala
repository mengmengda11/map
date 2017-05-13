package com.neo.sk.map.models

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tAdmin.schema ++ tCompany.schema ++ tMaps.schema ++ tUser.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tAdmin
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param password Database column password SqlType(varchar), Length(255,true)
   *  @param email Database column email SqlType(varchar), Length(255,true) */
  case class rAdmin(id: Int, name: String, password: String, email: String)
  /** GetResult implicit for fetching rAdmin objects using plain SQL queries */
  implicit def GetResultrAdmin(implicit e0: GR[Int], e1: GR[String]): GR[rAdmin] = GR{
    prs => import prs._
    rAdmin.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table admin. Objects of this class serve as prototypes for rows in queries. */
  class tAdmin(_tableTag: Tag) extends profile.api.Table[rAdmin](_tableTag, "admin") {
    def * = (id, name, password, email) <> (rAdmin.tupled, rAdmin.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(password), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> rAdmin.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column email SqlType(varchar), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table tAdmin */
  lazy val tAdmin = new TableQuery(tag => new tAdmin(tag))

  /** Entity class storing rows of table tCompany
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param password Database column password SqlType(varchar), Length(255,true)
   *  @param email Database column email SqlType(varchar), Length(255,true)
   *  @param state Database column state SqlType(int4)
   *  @param createTime Database column create_time SqlType(int8) */
  case class rCompany(id: Int, name: String, password: String, email: String, state: Int, createTime: Long)
  /** GetResult implicit for fetching rCompany objects using plain SQL queries */
  implicit def GetResultrCompany(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rCompany] = GR{
    prs => import prs._
    rCompany.tupled((<<[Int], <<[String], <<[String], <<[String], <<[Int], <<[Long]))
  }
  /** Table description of table company. Objects of this class serve as prototypes for rows in queries. */
  class tCompany(_tableTag: Tag) extends profile.api.Table[rCompany](_tableTag, "company") {
    def * = (id, name, password, email, state, createTime) <> (rCompany.tupled, rCompany.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(password), Rep.Some(email), Rep.Some(state), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rCompany.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column email SqlType(varchar), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column state SqlType(int4) */
    val state: Rep[Int] = column[Int]("state")
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
  }
  /** Collection-like TableQuery object for table tCompany */
  lazy val tCompany = new TableQuery(tag => new tCompany(tag))

  /** Entity class storing rows of table tMaps
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param companyid Database column companyid SqlType(int4)
   *  @param map Database column map SqlType(varchar), Length(255,true)
   *  @param createtime Database column createtime SqlType(int8)
   *  @param mapname Database column mapname SqlType(varchar), Length(255,true)
   *  @param state Database column state SqlType(int4)
   *  @param mapPic Database column map_pic SqlType(varchar), Length(255,true)
   *  @param review Database column review SqlType(int4)
   *  @param des Database column des SqlType(varchar), Length(255,true) */
  case class rMaps(id: Int, companyid: Int, map: String, createtime: Long, mapname: String, state: Int, mapPic: String, review: Int, des: String)
  /** GetResult implicit for fetching rMaps objects using plain SQL queries */
  implicit def GetResultrMaps(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[rMaps] = GR{
    prs => import prs._
    rMaps.tupled((<<[Int], <<[Int], <<[String], <<[Long], <<[String], <<[Int], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table maps. Objects of this class serve as prototypes for rows in queries. */
  class tMaps(_tableTag: Tag) extends profile.api.Table[rMaps](_tableTag, "maps") {
    def * = (id, companyid, map, createtime, mapname, state, mapPic, review, des) <> (rMaps.tupled, rMaps.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(companyid), Rep.Some(map), Rep.Some(createtime), Rep.Some(mapname), Rep.Some(state), Rep.Some(mapPic), Rep.Some(review), Rep.Some(des)).shaped.<>({r=>import r._; _1.map(_=> rMaps.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column companyid SqlType(int4) */
    val companyid: Rep[Int] = column[Int]("companyid")
    /** Database column map SqlType(varchar), Length(255,true) */
    val map: Rep[String] = column[String]("map", O.Length(255,varying=true))
    /** Database column createtime SqlType(int8) */
    val createtime: Rep[Long] = column[Long]("createtime")
    /** Database column mapname SqlType(varchar), Length(255,true) */
    val mapname: Rep[String] = column[String]("mapname", O.Length(255,varying=true))
    /** Database column state SqlType(int4) */
    val state: Rep[Int] = column[Int]("state")
    /** Database column map_pic SqlType(varchar), Length(255,true) */
    val mapPic: Rep[String] = column[String]("map_pic", O.Length(255,varying=true))
    /** Database column review SqlType(int4) */
    val review: Rep[Int] = column[Int]("review")
    /** Database column des SqlType(varchar), Length(255,true) */
    val des: Rep[String] = column[String]("des", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table tMaps */
  lazy val tMaps = new TableQuery(tag => new tMaps(tag))

  /** Entity class storing rows of table tUser
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param mapId Database column map_id SqlType(int8)
   *  @param roomName Database column room_name SqlType(varchar), Length(255,true)
   *  @param createTime Database column create_time SqlType(int8)
   *  @param companyId Database column company_id SqlType(int4) */
  case class rUser(id: Int, mapId: Long, roomName: String, createTime: Long, companyId: Int)
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Int], e1: GR[Long], e2: GR[String]): GR[rUser] = GR{
    prs => import prs._
    rUser.tupled((<<[Int], <<[Long], <<[String], <<[Long], <<[Int]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends profile.api.Table[rUser](_tableTag, "user") {
    def * = (id, mapId, roomName, createTime, companyId) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(mapId), Rep.Some(roomName), Rep.Some(createTime), Rep.Some(companyId)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column map_id SqlType(int8) */
    val mapId: Rep[Long] = column[Long]("map_id")
    /** Database column room_name SqlType(varchar), Length(255,true) */
    val roomName: Rep[String] = column[String]("room_name", O.Length(255,varying=true))
    /** Database column create_time SqlType(int8) */
    val createTime: Rep[Long] = column[Long]("create_time")
    /** Database column company_id SqlType(int4) */
    val companyId: Rep[Int] = column[Int]("company_id")
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}
