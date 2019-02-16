package h32.ssmrender

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.statemachine.StateMachine
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/stama")
class StaMaStateApi @Autowired constructor(
        private val stateMachine: StateMachine<String, String>
){
    @RequestMapping("/state")
    fun getState() : ScJsonDto{
        stateMachine.start()
        stateMachine.sendEvent("E1")
        return ScJsonFactory.from(stateMachine)
    }
}