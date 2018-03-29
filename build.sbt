name := "kafka-example"
version := "0.1"
scalaVersion := "2.12.5"
fork in run := true
resolvers += "Confluent" at "http://packages.confluent.io/maven/"
scalacOptions in ThisBuild ++= Seq("-target:jvm-1.8", "-feature", "-language:postfixOps", "-deprecation")

val akkaV = "2.5.11"
val confluent = "4.0.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20-SNAPSHOT",
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "io.confluent" % "kafka-avro-serializer" % "4.0.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaV % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
)

sourceGenerators in Compile += (avroScalaGenerateSpecific in Compile).taskValue
sourceGenerators in Test += (avroScalaGenerateSpecific in Test).taskValue
