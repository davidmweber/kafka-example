# Scala/Kafka/Avro examples

Example Kafka consumer and producer using Scala and Avro. The system is "Avro first" and relies on
having Avro schema files stored in `src/main/avro` which are converted into Scala case classes using
[avrohugger](https://github.com/julianpeeters/avrohugger) and it's [sbt plugin](https://github.com/julianpeeters/sbt-avrohugger).

Generate the case classes using `sbt avroScalaGenerateSpecific` and then consume the classes like any
scala case class. The code generation task should be automatically done as part of the test and compile
tasks. Note IntelliJ's regular builder does not call the code generator.

