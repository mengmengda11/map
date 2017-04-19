package com.neo.sk.map

/**
  * Created by mengmengda on 2017/2/9.
  */
package object models {

  case class User(
                   id:Long,
                   name:String,
                   timestamp:Long
                 )
  case class Company(
                   id:Long,
                   name:String,
                   timestamp:Long
                 )

  case class Admin(
                   id:Long,
                    name:String,
                    timestamp:Long
                  )

}
