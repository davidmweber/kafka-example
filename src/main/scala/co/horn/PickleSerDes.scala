/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import co.horn.models._
import upickle.default._

object PickleSerDes {
  implicit def rwUser: ReadWriter[Frog] = macroRW
}
