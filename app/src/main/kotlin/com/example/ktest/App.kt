/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.example.ktest

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

class ThingSerializer : org.apache.kafka.common.serialization.Serializer<Things> {
    override fun serialize(topic: String, data: Things?): ByteArray? {
        if (data == null) return null
        return Json.encodeToString(data).toByteArray()
    }
    override fun close() {}
    override fun configure(configs: MutableMap<String, *>?, isKey: Boolean) {}

}

@Serializable
data class Things(
    val name: String,
    val description: String
)

fun createProducer(brokers: String): Producer<String, Things> {
    val props = Properties()
    props["bootstrap.servers"] = brokers
    props["key.serializer"] = StringSerializer::class.java.canonicalName
    props["value.serializer"] = ThingSerializer::class.java
//        StringSerializer::class.java.canonicalName
    return KafkaProducer<String, Things>(props)
}

//class LoggerInCompanionObject {
//
//
//    companion object {
//        fun getLogger(forClass: Class<*>): Logger =
//            LoggerFactory.getLogger(forClass)
//
//        private val loggerWithWrongClass = getLogger(javaClass)
//    }
//}
////...


fun main() {
    println(App().greeting)


    val data = Things("kotlin", "Kotlin")
    val string = Json.encodeToString(data)
    println(string) // {"name":"kotlinx.serialization","language":"Kotlin"}
    // Deserializing back into objects
    val producer = createProducer("localhost:9094")
    val result = producer.send(ProducerRecord("test","my-key",data))
    result.get()

    val obj = Json.decodeFromString<Things>(string)
    println(obj) // Project(name=kotlinx.serialization, language=Kotlin)
}
