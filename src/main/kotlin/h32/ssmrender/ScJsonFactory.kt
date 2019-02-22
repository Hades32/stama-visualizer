package h32.ssmrender

import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.guard.Guard
import org.springframework.statemachine.state.PseudoStateKind
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
        val guard = pretty(transition.guard)
        val action = pretty(transition.actions)
        return Transition(
                from = transition.source.id,
                to = transition.target.id,
                event = event,
                label = "$event$guard$action"
                //action =
                //color =
                //cond =
        )
    }

    private fun pretty(actions: Collection<Action<String, String>>): Any {
        return when {
            actions.isNotEmpty() ->
                actions.joinToString(prefix = "/", separator = ",")
            else ->
                ""
        }
    }

    private fun pretty(guard: Guard<String, String>?) =
        guard?.let { "[$it]" } ?: ""

    private fun convert(state: ssmState<String, String>): Node {
        return when (state.pseudoState?.kind) {
            PseudoStateKind.INITIAL -> Initial(state.id)
            PseudoStateKind.END -> Final(state.id)
            PseudoStateKind.CHOICE -> Choice(state.id)
            PseudoStateKind.JUNCTION -> Choice(state.id)
            PseudoStateKind.HISTORY_DEEP -> TODO("HISTORY_DEEP not handled yet")
            PseudoStateKind.HISTORY_SHALLOW -> TODO("HISTORY_SHALLOW not handled yet")
            PseudoStateKind.FORK -> TODO("FORK not handled yet")
            PseudoStateKind.JOIN -> TODO("JOIN not handled yet")
            PseudoStateKind.ENTRY -> TODO("ENTRY not handled yet")
            PseudoStateKind.EXIT -> TODO("EXIT not handled yet")
            null -> State(
                    name = state.id,
                    statemachine = Statemachine(
                            state.states.minus(state).map { convert(it) }
                    ),
                    color = if (state.id == "State5Parent" || state.id == "State3Child") "green" else "black"
            )
        }
    }
}