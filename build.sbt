name := "kafka-example"
version := "0.1"
scalaVersion := "2.12.6"
fork in run := true
resolvers += "Confluent" at "http://packages.confluent.io/maven/"
scalacOptions in ThisBuild ++= Seq("-target:jvm-1.8", "-feature", "-language:postfixOps", "-deprecation")

val akkaV = "2.5.12"
val confluentV = "4.1.0"
val pickleV = "0.6.4"
val connectV = "1.1.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20",
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.lihaoyi" %% "upickle" % pickleV,
  "org.apache.kafka" % "connect-json" % connectV,
  "io.confluent" % "kafka-avro-serializer" % confluentV,
  "com.typesafe.akka" %% "akka-testkit" % akkaV % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
)

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue
sourceGenerators in Test += (avroScalaGenerateSpecific in Test).taskValue
