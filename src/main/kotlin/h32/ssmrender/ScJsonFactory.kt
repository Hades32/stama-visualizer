package h32.ssmrender

import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.action.Action
import org.springframework.statemachine.guard.Guard
import org.springframework.statemachine.region.Region
import org.springframework.statemachine.state.PseudoStateKind
import org.springframework.statemachine.state.RegionState
import org.springframework.statemachine.state.State as ssmState
import org.springframework.statemachine.transition.Transition as ssmTransition

object ScJsonFactory {
    private val anonFuncMatch = Regex("\\\$([^$]+)\\\$")
    private val instanceMatch = Regex("([^.]+)@")
    private val classMatch = Regex("([^.]+)\$")

    fun from(stateMachine: StateMachine<String, String>): ScJsonDto {
        val activeStates = stateMachine.state.ids
        return ScJsonDto(
                states = stateMachine.states
                        .plus(stateMachine.initialState)
                        .map { convert(it, activeStates) },
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
                actions.joinToString(prefix = "/", separator = ",") { pretty(it) }
            else ->
                ""
        }
    }

    private fun pretty(it: Action<String, String>): String {
        return extractName(it.toString())
    }

    private fun pretty(guard: Guard<String, String>?): String {
        return guard?.toString()?.let { extractName(it) }?.let { "[$it]" } ?: ""
    }

    private fun extractName(s: String): String {
        val match = anonFuncMatch.find(s) ?: instanceMatch.find(s) ?: classMatch.find(s)
        return match?.groupValues?.elementAtOrNull(1) ?: s
    }

    private fun convert(region: Region<String, String>, activeStates: Collection<String>): Node {
        return State(
                name = region.id ?: region.uuid.toString(),
                statemachine = Statemachine(
                        region.states.map { convert(it, activeStates) }
                )
        )
    }

    private fun convert(state: ssmState<String, String>, activeStates: Collection<String>): Node {
        return when (state.pseudoState?.kind) {
            PseudoStateKind.INITIAL -> createInitial(state, activeStates)
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
                    statemachine = if (state.states.size > 1) Statemachine(
                            state.states.minus(state).map { convert(it, activeStates) }
                    ) else null,
                    color = getColor(state, activeStates)
            )
        }
    }

    private fun createInitial(state: org.springframework.statemachine.state.State<String, String>, activeStates: Collection<String>): Node {
        if (state is RegionState) {
            return StateWithRegions(
                    name = state.id,
                    color = getColor(state, activeStates),
                    statemachine = Statemachine(
                            state.regions.map {
                                ScJsonFactory.convert(it, activeStates)
                            }.toList()
                    )

            )
        }
        if (state.id.startsWith("init", ignoreCase = true)) {
            return Initial(state.id)
        }
        return State(
                name = state.id,
                color = getColor(state, activeStates)
        )
    }

    private fun getColor(state: org.springframework.statemachine.state.State<String, String>, activeStates: Collection<String>) =
            if (activeStates.contains(state.id)) "green" else "black"
}