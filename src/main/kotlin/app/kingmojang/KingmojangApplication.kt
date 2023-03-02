package app.kingmojang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KingmojangApplication

fun main(args: Array<String>) {
	runApplication<KingmojangApplication>(*args)
}
