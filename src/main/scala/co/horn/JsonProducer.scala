/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import java.util.Properties

import co.horn.AvroProducer.system
import co.horn.models.Frog
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.connect.json.JsonSerializer

object JsonProducer {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: JsonProducer <kafka address>")
      System.exit(-1)
    }

    val addr = args(0)
    println(s"Using Kafka suite at $addr")

    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,s"$addr:9092")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "JsonTestProducer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[JsonSerializer].getName)

    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val producer = new KafkaProducer[Long, JsonNode](props)

    (1l until 100l).foreach{ v ⇒
      println(v)
      //val node: JsonNode = om.valueToTree(Frog(v.toString, "foo", "bar"))
      val f = Frog("a", "b", "c")
      val node = mapper.convertValue(f, classOf[JsonNode])
      val pr = new ProducerRecord("json_user", v, node)
      producer.send(pr)
    }

    system.terminate()
  }
}
