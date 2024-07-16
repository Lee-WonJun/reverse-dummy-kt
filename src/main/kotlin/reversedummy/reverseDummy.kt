package reversedummy
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

val defaultCustomMatcher : Map<KClass<out Any>, (obj:Any) -> String> = mapOf(
    java.util.UUID::class to { o -> "UUID.fromString(\"$o\")" },
)
fun reverseDummy(
    dummy: Any,
    name: String = "dummy",
    matcher: Map<KClass<out Any>, (obj: Any) -> String> = defaultCustomMatcher
): String {
    return "val $name = ${generateInitializationCode(dummy, matcher)}"
}

fun generateInitializationCode(
    value: Any?,
    matcher: Map<KClass<out Any>, (obj: Any) -> String>
): String {
    return when (value) {
        is String -> "\"$value\""
        is Number, is Boolean -> value.toString()
        is List<*> -> value.joinToString(prefix = "listOf(", postfix = ")") {
            generateInitializationCode(it, matcher)
        }
        is Map<*, *> -> value.entries.joinToString(prefix = "mapOf(", postfix = ")") {
            "${generateInitializationCode(it.key, matcher)} to ${generateInitializationCode(it.value, matcher)}"
        }
        is Enum<*> -> "${value::class.qualifiedName}.${value.name}"
        null -> "null"
        else -> generateClassInitializationCode(value, matcher)
    }
}

fun generateClassInitializationCode(
    obj: Any,
    matcher: Map<KClass<out Any>, (obj: Any) -> String>
): String {
    val kClass = obj::class

    matcher[kClass]?.let {
        return it(obj)
    }

    val constructor = kClass.constructors.firstOrNull() ?: return "${kClass.qualifiedName}"
    val args = constructor.parameters.joinToString(", ") { parameter ->
        val property = kClass.memberProperties.find { it.name == parameter.name }
        val value = property?.call(obj)

        if (property == null && parameter.name?.startsWith("arg") == true) {
            "???"
        } else {
            "${parameter.name} = ${generateInitializationCode(value, matcher)}"
        }
    }
    return "${kClass.qualifiedName}($args)"
}
