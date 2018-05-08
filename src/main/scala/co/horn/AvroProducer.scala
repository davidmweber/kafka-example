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

object AvroProducer {

  implicit val system: ActorSystem = ActorSystem("kafka-stream")
  implicit val mat: ActorMaterializer = ActorMaterializer()

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
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "AvroTestProducer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer].getName)

    val producer = new KafkaProducer[Long, User](props)

    (1l until 100l).foreach{ v ⇒
      val pr = new ProducerRecord("avro_user", v, User(v.toString, None, None))
      producer.send(pr)
    }
    println("Sent 100 records")
    system.terminate()
  }
}
