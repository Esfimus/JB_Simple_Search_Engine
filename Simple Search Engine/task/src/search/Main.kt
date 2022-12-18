package search

import java.io.File

class DataBase {
    private val list = mutableListOf<String>()

    fun add(line: String) = list.add(line)

    fun search(line: String) {
        val foundList = mutableListOf<String>()
        for (l in list) {
            if (""".*${line.lowercase()}.*""".toRegex().matches(l.lowercase())) {
                foundList.add(l)
            }
        }
        if (foundList.isNotEmpty()) {
            println("\nFound:")
            for (l in foundList) {
                println(l)
            }
        } else {
            println("Nothing.")
        }
    }

    fun displayList() {
        for (l in list) {
            println(l)
        }
    }
}

/**
 * Searches through the database
 */
fun find(dataBase: DataBase) {
    println("\nEnter data to search:")
    val itemSearch = readln()
    dataBase.search(itemSearch)
}

/**
 * Prints all items from the database
 */
fun print(dataBase: DataBase) {
    println("\n=== List of items ===")
    dataBase.displayList()
}

/**
 * Saves text items to database, searches for pieces of text
 */
fun searchText(args: Array<String>) {
    val dataBase = DataBase()
    try {
        // adding items to database
        if (args.size > 1 && args[0] == "--data") {
            val fileLines = File(args[1]).readLines()
            for (line in fileLines) {
                dataBase.add(line)
            }
        }
    } catch (e: Exception) {
        println("\nFile does not exist.")
    }
    // main menu
    var exitCommand = false
    do {
        try {
            println()
            println("""
                === Menu ===
                1. Find a person
                2. Print all people
                0. Exit""".trimIndent())
            when (readln().toInt()) {
                1 -> find(dataBase)
                2 -> print(dataBase)
                0 -> exitCommand = true
                else -> println("\nIncorrect option! Try again.")
            }
        } catch (e: Exception) {
            println("\nWrong input.")
        }
    } while (!exitCommand)

}

fun main(args: Array<String>) {
    searchText(args)
}
