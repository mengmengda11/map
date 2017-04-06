package com.neo.sk.map.frontend

import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 3:27 PM
  */
object HiPage extends Component[Div] {


  import scalatags.JsDom.short._


  private val counter = new CounterOnServer()


  override def render(): Div = {

    div(
      h1("welcoume to 2017"),
      p("click button below:"),
      div(*.backgroundColor := "yellow")(
        counter.render()
      )
    ).render


  }


}
