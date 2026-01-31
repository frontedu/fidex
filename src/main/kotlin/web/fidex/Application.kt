package web.fidex

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableAsync

@EnableCaching @EnableAsync @SpringBootApplication class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
