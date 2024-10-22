package reversedummy

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class Person(val name: String, val age: Int, val isStudent: Boolean)
class ReverseDummyKtTest : FunSpec({

    test("reverseDummy") {
        val dummy = Person("John", 25, true)
        val reversed = reverseDummy(dummy, "reversed")

        println(reversed)

        val expected = """
        val reversed = reversedummy.Person(name = "John", age = 25, isStudent = true)
        """

        reversed.trim() shouldBe expected.trimIndent().trim()

    }
})
