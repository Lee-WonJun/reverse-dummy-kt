package reversedummy
import kotlin.reflect.full.memberProperties

fun reverseDummy(dummy: Any, name: String = "dummy"): String {
    return "val $name = ${generateInitializationCode(dummy)}"
}

fun generateInitializationCode(value: Any): String {
    return when (value) {
        is String -> "\"$value\""
        is Int, is Double, is Float, is Boolean -> value.toString()
        is List<*> -> value.joinToString(prefix = "listOf(", postfix = ")") { generateInitializationCode(it!!) }
        is Map<*, *> -> value.entries.joinToString(prefix = "mapOf(", postfix = ")") {
            "${generateInitializationCode(it.key!!)} to ${generateInitializationCode(it.value!!)}"
        }
        is Enum<*> -> "${value::class.qualifiedName}.${value.name}"
        else -> generateClassInitializationCode(value)
    }
}

fun generateClassInitializationCode(obj: Any): String {
    val kClass = obj::class
    val constructor = kClass.constructors.firstOrNull() ?: return "${kClass.qualifiedName}"
    val args = constructor.parameters.joinToString(", ") { parameter ->
        val properties = kClass.memberProperties
        val property = properties.find { it.name == parameter.name}

        val value = property?.call(obj)
        "${parameter.name} = ${generateInitializationCode(value!!)}"
    }
    return "${kClass.qualifiedName}($args)"
}