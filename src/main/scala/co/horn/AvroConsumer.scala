/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import java.util.{Collections, Properties}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import co.horn.avro.User
import io.confluent.kafka.serializers.{KafkaAvroDeserializer, KafkaAvroDeserializerConfig}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

object AvroConsumer {

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("kafka-stream")
    implicit val mat: ActorMaterializer = ActorMaterializer()

    if (args.length != 1) {
      println("Usage: AvroConsumer <kafka address>")
      System.exit(-1)
    }

    val addr = args(0)
    println(s"Using Kafka suite at $addr")
    val props = new Properties()

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,s"$addr:9092")
    props.put("schema.registry.url", s"http://$addr:8081")
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG , "true")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "Test")
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "AvroTestConsumer")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer].getName)
    props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true" )

    val consumer = new KafkaConsumer[Long, User](props)
    consumer.subscribe(Collections.singletonList("avro_user"))

    while(true) {
      val records = consumer.poll(100)
      println(s"Record count = ${records.count()}")
      records.forEach{r ⇒
        val u = r.value()
        println(u.name)
      }

    }
    system.terminate()
  }
}
