/*
 * Fixes javax.ws.rs-api.${packaging.type} errors. Very icky state of affairs
 */
import sbt._

object PackagingTypePlugin extends AutoPlugin {
  override val buildSettings = {
    sys.props += "packaging.type" -> "jar"
    Nil
  }
}
