package h32.ssmrender

import org.springframework.context.annotation.Bean
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.guard.Guard
import org.springframework.stereotype.Component

@Component
class Stubs {
    @Bean
    fun IsRedGuard() : Guard<String,String>{
        return Guard { true }
    }
    @Bean
    fun SomeEffectAction() : Action<String, String> {
        return Action { }
    }
    @Bean
    fun SomeAction() : Action<String, String> {
        return Action { }
    }
}