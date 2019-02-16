package h32.ssmrender

import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.state.State as ssmState
import org.springframework.statemachine.transition.Transition as ssmTransition

object ScJsonFactory {
    fun from(stateMachine: StateMachine<String, String>): ScJsonDto {
        return ScJsonDto(
                states = stateMachine.states
                        .plus(stateMachine.initialState)
                        .map { convert(it) },
                transitions = stateMachine.transitions
                        .map { convert(it) }
        )
    }

    private fun convert(transition: ssmTransition<String, String>): Transition {
        val event = transition.trigger?.event ?: ""
        return Transition(
                from = transition.source.id,
                to = transition.target.id,
                event = event,
                label = "$event[]/" //TODO,
                //action =
                //color =
                //cond =
        )
    }

    private fun convert(state: ssmState<String, String>): State {
        return State(
                name = state.id,
                statemachine = Statemachine(
                        state.states.minus(state).map { convert(it) }
                ),
                color = if (state.id == "State5Parent" || state.id == "State3Child") "green" else "black"
        )
    }
}