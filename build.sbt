name := "kafka-example"
version := "0.1"
scalaVersion := "2.12.7"
fork in run := true
resolvers += "Confluent" at "http://packages.confluent.io/maven/"
scalacOptions in ThisBuild ++= Seq("-target:jvm-1.8", "-feature", "-language:postfixOps", "-deprecation")

val akkaV = "2.5.18"
val streamKafkaV = "1.0-M1"
val confluentV = "5.0.1"
val pickleV = "0.6.7"
val connectV = "2.0.1"
val jacksonV = "2.9.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % streamKafkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.lihaoyi" %% "upickle" % pickleV,
  "org.apache.kafka" % "connect-json" % connectV,
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % jacksonV,
  "io.confluent" % "kafka-avro-serializer" % confluentV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
)

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue
sourceGenerators in Test += (avroScalaGenerateSpecific in Test).taskValue
