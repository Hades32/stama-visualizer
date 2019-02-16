package h32.ssmrender

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["h32"])
class SsmRenderApplication

fun main(args: Array<String>) {
    runApplication<SsmRenderApplication>(*args)
}

