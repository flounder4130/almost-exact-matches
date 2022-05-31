import org.w3c.dom.Node
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory


val tagsToCheck = listOf("p", "li", "note", "step", "tip", "topic", "chapter", "procedure")

fun main(args: Array<String>) {
    val rootDirectory = File(if (args.isNotEmpty()) args[0] else ".")
    val tagsGrouping = rootDirectory.walk()
        .filter { it.isFile && it.name.endsWith(".xml") }
        .onEach { println("Parsing: " + it.name) }
        .map { it.childNodesRecursive() }
        .flatten()
        .map { it.textContent.tokenizeAndStem() }
        .filter { it.isNotEmpty() }
        .groupBy { it.hashCode() }

    val result = tagsGrouping.asSequence()
        .filter { it.value.size > 3}
        .sortedByDescending { it.value.size }
        .fold (StringBuilder()) { data, newEntry -> data.append("${newEntry.value.size} ${newEntry.value.first()}\n") }

    println(result)
    FileWriter(rootDirectory.resolveSibling("out")).write(result.toString())
}

private fun File.childNodesRecursive(): List<Node> = try {
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    val xml = builder.parse(ByteArrayInputStream(this.readBytes()))
    xml.documentElement.childNodesRecursive()
} catch (e: Exception) {
    emptyList()
}

fun Node.childNodesRecursive(): List<Node> {
    if (this.nodeName !in tagsToCheck) return emptyList()
    val result = mutableListOf(this)
    if (this.hasChildNodes()) {
        val childNodes = this.childNodes
        for (i in 0 until childNodes.length) {
            val node = childNodes.item(i)
            result += node.childNodesRecursive()
        }
    }
    return result
}

