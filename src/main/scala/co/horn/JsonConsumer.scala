/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

class JsonConsumer {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: AvroProducer <kafka address>")
      System.exit(-1)
    }

    val addr = args(0)
    println(s"Using Kafka suite at $addr")
  }
}
