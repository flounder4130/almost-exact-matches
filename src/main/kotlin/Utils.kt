import opennlp.tools.stemmer.PorterStemmer

val stemmer = PorterStemmer()

fun String?.tokenizeAndStem(): Set<String> {

    if (this == null) return emptySet()

    else this.replace("<[/]?.*?>".toRegex(), "")
        .split("\\W+".toRegex())
        .map(stemmer::stem)
        .filter { it !in stopWordsList }
        .toSet()
        .let{ return it }
}


val stopWordsList = setOf(
    "of",
    "a",
    "an",
    "on",
    "to",
    "with",
    "by",
    "click",
    "the",
    "in",
    "be",
    "of",
    "and",
    "for",
    "not",
    "as",
    "you",
    "do",
    "at",
    "this",
    "that",
    "but",
    "by",
    "from",
    "or",
    "will",
    "would",
    "out",
    "if",
    "about",
    "get",
    "which",
    "click",
    "when",
    "can",
    "like",
    "you",
    "know",
    "into",
    "your",
    "any",
    "all",
    "it",
    "|",
    "open",
    "press"
)


