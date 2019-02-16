package h32.ssmrender

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.StateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer
import org.springframework.statemachine.config.model.StateMachineModelFactory
import org.springframework.statemachine.uml.UmlStateMachineModelFactory


@Configuration
@EnableStateMachine
class SimpleStateMachineConfiguration : StateMachineConfigurerAdapter<String, String>() {

    @Throws(Exception::class)
    override fun configure(model: StateMachineModelConfigurer<String, String>) {
        model.withModel().factory(modelFactory())
    }

    @Bean
    fun modelFactory(): StateMachineModelFactory<String, String> {
        return UmlStateMachineModelFactory("classpath:stama-test1/stama-test1.uml")
    }
}