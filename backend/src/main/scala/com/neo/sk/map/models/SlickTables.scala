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
  lazy val schema: profile.SchemaDescription = tCompany.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tCompany
   *  @param id Database column id SqlType(serial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(255,true)
   *  @param password Database column password SqlType(varchar), Length(255,true)
   *  @param email Database column email SqlType(varchar), Length(255,true) */
  case class rCompany(id: Int, name: String, password: String, email: String)
  /** GetResult implicit for fetching rCompany objects using plain SQL queries */
  implicit def GetResultrCompany(implicit e0: GR[Int], e1: GR[String]): GR[rCompany] = GR{
    prs => import prs._
    rCompany.tupled((<<[Int], <<[String], <<[String], <<[String]))
  }
  /** Table description of table company. Objects of this class serve as prototypes for rows in queries. */
  class tCompany(_tableTag: Tag) extends profile.api.Table[rCompany](_tableTag, "company") {
    def * = (id, name, password, email) <> (rCompany.tupled, rCompany.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(password), Rep.Some(email)).shaped.<>({r=>import r._; _1.map(_=> rCompany.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(255,true) */
    val name: Rep[String] = column[String]("name", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column email SqlType(varchar), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table tCompany */
  lazy val tCompany = new TableQuery(tag => new tCompany(tag))
}
