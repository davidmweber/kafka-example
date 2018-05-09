/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import java.util.{Collections, Properties}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import co.horn.models.Frog
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.connect.json.JsonDeserializer

object JsonConsumer {

  implicit val system: ActorSystem = ActorSystem("kafka-stream")
  implicit val mat: ActorMaterializer = ActorMaterializer()

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: JasonConsumer <kafka address>")
      System.exit(-1)
    }

    val addr = args(0)
    println(s"Using Kafka suite at $addr")

    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$addr:9092")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "Test")
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "JavaTestConsumer")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[JsonDeserializer].getName)

    val consumer = new KafkaConsumer[Long, JsonNode](props)
    consumer.subscribe(Collections.singletonList("json_user"))

    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    while (true) {
      val records = consumer.poll(100)
      println(s"Record count = ${records.count()}")
      records.forEach { r ⇒
        val u = mapper.treeToValue(r.value(), classOf[Frog])
        println(u)
      }

    }
    system.terminate()
  }

}
