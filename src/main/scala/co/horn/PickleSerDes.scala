/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import co.horn.avro.User
import upickle.default._

object PickleSerDes {
  implicit def rwUser: ReadWriter[User] = macroRW
}
