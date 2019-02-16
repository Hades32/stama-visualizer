package h32.ssmrender

data class ScJsonDto(
        val states: List<Node>,
        val transitions: List<Transition>
)

open class Node(
        val name: String,
        val type: String,
        val color: String? = null
)

class State(
        name: String,
        color: String? = null,
        val statemachine: Statemachine?
) : Node(name, "regular", color)

class Choice(
        name: String,
        color: String? = null
) : Node(name, "choice", color)

class Final(
        name: String,
        color: String? = null
) : Node(name, "final", color)

class Initial(
        name: String,
        color: String? = null
) : Node(name, "initial", color)

data class Statemachine(
        val states: List<Node>
)

data class Transition(
        val from: String,
        val to: String,
        val color: String? = null,
        val event: String? = null,
        val label: String? = null,
        val cond: String? = null,
        val action: String? = null
)

