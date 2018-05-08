/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import java.util.Properties

import co.horn.AvroProducer.system
import co.horn.avro.User
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.connect.json.JsonSerializer
import org.apache.kafka.common.serialization.LongSerializer
import scala.sys.process._

object JsonProducer {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: AvroProducer <kafka address>")
      System.exit(-1)
    }

    val addr = args(0)
    println(s"Using Kafka suite at $addr")

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,s"$addr:9092")
    props.put("schema.registry.url", s"http://$addr:8081")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "JsonTestProducer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[JsonSerializer].getName)

    val producer = new KafkaProducer[Long, User](props)

    (1l until 100l).foreach{ v ⇒
      println(v)
      val pr = new ProducerRecord("json_user", v, User(v.toString, None, None))
      producer.send(pr)
    }

    system.terminate()


  }
}
