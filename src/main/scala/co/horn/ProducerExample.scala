/*
 * Copyright © ${year} 8eo Inc.
 */
package co.horn

import java.util.Properties

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import co.horn.avro.User
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.sys.process._

object ProducerExample {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("kafka-stream")
    implicit val mat: ActorMaterializer = ActorMaterializer()

    // Retrieve the address of the container with the Kafka stack
    val addr = ("lxc list" #| "grep kafka-streams" !!).split(" ")(5)
    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,s"$addr:9092")
    props.put("schema.registry.url", s"http://$addr:8081")
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "AvroTestProducer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)

    val producer = new KafkaProducer[Long, User](props)

    (1l until 100l).foreach{ v ⇒
      val pr = new ProducerRecord("topic1", v, User(v.toString, None, None))
      producer.send(pr)
    }

    system.terminate()
  }
}
