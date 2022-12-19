package search

import java.io.File

class DataBase {
    private val list = mutableListOf<String>()
    private val indexMap = mutableMapOf<String, MutableList<Int>>()

    fun add(line: String) = list.add(line)

    fun getMap() = indexMap
    fun getList() = list

    private fun searchLines(word: String): MutableList<Int> {
        val listOfIndices = mutableListOf<Int>()
        loop@for (l in list.indices) {
            val wordsInLine = list[l].split(" ")
            for (w in wordsInLine) {
                if (w == word) {
                    listOfIndices.add(l)
                    continue@loop
                }
            }
        }
        return listOfIndices
    }

    fun index() {
        for (line in list) {
            val wordsInLine = line.split(" ")
            for (word in wordsInLine) {
                indexMap[word.lowercase()] = searchLines(word)
            }
        }
    }

    fun displayList() {
        for (l in list) {
            println(l)
        }
    }
}

/**
 * Searches for all given words in one line
 */
fun searchStrategyAll(dataBase: DataBase) {
    println("\nEnter data to search:")
    val itemsSearch = readln().split(" ")
    // building list of index lists for all given words
    val map = dataBase.getMap()
    val indexMatrix = mutableListOf<MutableList<Int>>()
    for (word in itemsSearch) {
        if (!map.containsKey(word)) {
            println("Nothing")
            return
        } else {
            indexMatrix.add(map.getValue(word))
        }
    }
    // searching for common elements in all lists of index lists
    var commonList = indexMatrix[0].toSet()
    if (indexMatrix.size > 1) {
        for (i in indexMatrix.indices) {
            if (i + 1 < indexMatrix.size) {
                commonList = commonList.intersect(indexMatrix[i + 1].toSet())
            }
        }
    }
    // displaying the result
    if (commonList.isNotEmpty()) {
        for (i in commonList) {
            println(dataBase.getList()[i])
        }
    } else {
        println("Nothing")
    }
}

/**
 * Searches for all occurrences for any given words
 */
fun searchStrategyAny(dataBase: DataBase) {
    println("\nEnter data to search:")
    val itemsSearch = readln().split(" ")
    // building set of words occurrences
    val setOfIndices = mutableSetOf<Int>()
    for (word in itemsSearch) {
        for ((k, v) in dataBase.getMap()) {
            if (k == word.lowercase()) {
                for (i in v) {
                    setOfIndices.add(i)
                }
            }
        }
    }
    // displaying the result
    if (setOfIndices.isNotEmpty()) {
        for (i in setOfIndices) {
            println(dataBase.getList()[i])
        }
    } else {
        println("Nothing")
    }
}

/**
 * Searches for all lines that do not contain words from input
 */
fun searchStrategyNone(dataBase: DataBase) {
    println("\nEnter data to skip:")
    val itemsSearch = readln().split(" ")
    // building set of words occurrences
    val setOfIndices = mutableSetOf<Int>()
    for (word in itemsSearch) {
        for ((k, v) in dataBase.getMap()) {
            if (k == word.lowercase()) {
                for (i in v) {
                    setOfIndices.add(i)
                }
            }
        }
    }
    // displaying the result
    for (i in dataBase.getList().indices) {
        if (!setOfIndices.contains(i)) {
            println(dataBase.getList()[i])
        }
    }
}

/**
 * Searches through the database
 */
fun find(dataBase: DataBase) {
    try {
        println("\nSelect a matching strategy: ALL, ANY, NONE")
        when (readln().lowercase()) {
            "all" -> searchStrategyAll(dataBase)
            "any" -> searchStrategyAny(dataBase)
            "none" -> searchStrategyNone(dataBase)
            else -> throw Exception()
        }
    } catch (e: Exception) {
        println("Wrong strategy input.")
    }
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
            dataBase.index()
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
            println("\nWrong command input.")
        }
    } while (!exitCommand)
}

fun main(args: Array<String>) {
    searchText(args)
}
