package reversedummy

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.util.UUID

@JvmInline
value class BookId(val value: UUID) {
    companion object {
        fun generate(): BookId = BookId(UUID.randomUUID())
    }
}

data class NonBlankString(val value: String) {
    init {
        require(value.isNotBlank()) { "String must not be blank" }
    }
}

@JvmInline
value class PositiveNumber(val value: Int) {
    init {
        require(value > 0) { "Number must be positive" }
    }
}

typealias Author = NonBlankString
typealias BookTitle = NonBlankString
typealias LibraryName = NonBlankString
typealias LibraryAddress = NonBlankString

data class Book(
    val id: BookId,
    val title: BookTitle,
    val author: Author,
    val publishedDate: LocalDate,
    val pages: PositiveNumber,
)

data class Library(
    val name: LibraryName,
    val address: LibraryAddress,
    val books: List<Book>
)

class ReverseDummyKtTest2 : FunSpec({

    test("reverseDummy") {
        val book1 = Book(
            id = BookId.generate(),
            title = BookTitle("Effective Kotlin"),
            author = Author("Marcin Moskala"),
            publishedDate = LocalDate.of(2019, 11, 1),
            pages = PositiveNumber(360)
        )

        val book2 = Book(
            id = BookId.generate(),
            title = BookTitle("Kotlin in Action"),
            author = Author("Dmitry Jemerov"),
            publishedDate = LocalDate.of(2017, 2, 1),
            pages = PositiveNumber(400)
        )

        val library = Library(
            name = LibraryName("Central Library"),
            address = LibraryAddress("123 Library St"),
            books = listOf(book1, book2)
        )

        val dummy = reverseDummy(
            library, matcher = defaultCustomMatcher + mapOf(
                LocalDate::class to { t -> "LocalDate.parse(\"${t.toString().trim()}\")" }
            )
        )

        println(dummy)

        val expected = """
        val dummy = reversedummy.Library(
            name = reversedummy.NonBlankString(value = "Central Library"),
            address = reversedummy.NonBlankString(value = "123 Library St"),
            books = listOf(
                reversedummy.Book(
                    id = reversedummy.BookId(value = UUID.fromString("${book1.id.value}")),
                    title = reversedummy.NonBlankString(value = "Effective Kotlin"),
                    author = reversedummy.NonBlankString(value = "Marcin Moskala"),
                    publishedDate = LocalDate.parse("2019-11-01"),
                    pages = reversedummy.PositiveNumber(value = 360)
                ),
                reversedummy.Book(
                    id = reversedummy.BookId(value = UUID.fromString("${book2.id.value}")),
                    title = reversedummy.NonBlankString(value = "Kotlin in Action"),
                    author = reversedummy.NonBlankString(value = "Dmitry Jemerov"),
                    publishedDate = LocalDate.parse("2017-02-01"),
                    pages = reversedummy.PositiveNumber(value = 400)
                )
            )
        )
        """

        dummy.normalize() shouldBe expected.normalize()
    }
}) {
    companion object {
        private fun String.normalize(): String = this.replace(" ", "").replace("\n", "").trim()
    }
}
