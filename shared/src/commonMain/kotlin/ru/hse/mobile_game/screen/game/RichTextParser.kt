package ru.hse.mobile_game.screen.game

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/** Narrative text colors. */
private val NarratorColor = Color(0xFFCDBFAA)
private val SpeakerColor = Color(0xFFE0C080)
private val DialogueColor = Color(0xFFEDE4D4)
private val BodyColor = Color(0xFFF0EAE0)
val GlossaryColor = Color(0xFFE0C080)

private val DIALOGUE_REGEX = Regex("""^([\w\s''-]+):\s*"([\s\S]+)"$""")

/**
 * Parses scene text with simple markup conventions, highlighting only the [activeTerms] glossary
 * entries (those the player has unlocked).
 * - A paragraph wrapped in `*asterisks*` is narrator text (rendered italic, muted color)
 * - A paragraph matching `Speaker: "dialogue"` is dialogue (bold speaker, italic quote)
 * - Within regular paragraphs, inline `*italic*` markers are supported
 * - Glossary terms from [activeTerms] are highlighted gold and annotated with tag "glossary"
 *
 * Paragraphs are separated by double newlines (`\n\n`).
 */
fun parseNarrativeText(text: String, activeTerms: List<String> = emptyList()): AnnotatedString {
    return buildAnnotatedString {
        val paragraphs = text.split("\n\n")
        paragraphs.forEachIndexed { index, raw ->
            val paragraph = raw.trim()
            if (paragraph.isEmpty()) return@forEachIndexed
            if (index > 0) append("\n\n")
            parseParagraph(paragraph, activeTerms)
        }
    }
}

/**
 * Parses a single paragraph of text (no double-newline splitting). Used for glossary description
 * text which may contain inline formatting.
 */
fun parseSingleParagraph(text: String, activeTerms: List<String> = emptyList()): AnnotatedString {
    return buildAnnotatedString { parseParagraph(text.trim(), activeTerms) }
}

@Suppress("DEPRECATION")
private fun AnnotatedString.Builder.parseParagraph(paragraph: String, activeTerms: List<String>) {
    when {
        // Full narrator paragraph: *entire text*
        paragraph.startsWith("*") && paragraph.endsWith("*") && paragraph.length > 2 -> {
            val inner = paragraph.substring(1, paragraph.length - 1)
            appendWithGlossary(
                inner,
                SpanStyle(fontStyle = FontStyle.Italic, color = NarratorColor),
                activeTerms,
            )
        }
        // Dialogue: Speaker: "text"
        DIALOGUE_REGEX.matches(paragraph) -> {
            val match = DIALOGUE_REGEX.find(paragraph)!!
            val speaker = match.groupValues[1].trim()
            val dialogue = match.groupValues[2].trim()

            // Speaker name — may itself be a glossary term
            appendWithGlossary(
                speaker,
                SpanStyle(fontWeight = FontWeight.Bold, color = SpeakerColor),
                activeTerms,
            )
            withStyle(SpanStyle(color = BodyColor)) { append(": ") }
            // Dialogue text
            appendWithGlossary(
                "\u201C$dialogue\u201D",
                SpanStyle(fontStyle = FontStyle.Italic, color = DialogueColor),
                activeTerms,
            )
        }
        // Regular text with possible inline *italic* markers
        else -> {
            parseInlineFormatting(paragraph, activeTerms)
        }
    }
}

@Suppress("DEPRECATION")
private fun AnnotatedString.Builder.parseInlineFormatting(text: String, activeTerms: List<String>) {
    var i = 0
    while (i < text.length) {
        val starIndex = text.indexOf('*', i)
        if (starIndex == -1) {
            appendWithGlossary(text.substring(i), SpanStyle(color = BodyColor), activeTerms)
            break
        }
        // Append text before the *
        if (starIndex > i) {
            appendWithGlossary(
                text.substring(i, starIndex),
                SpanStyle(color = BodyColor),
                activeTerms,
            )
        }
        // Find closing *
        val endStar = text.indexOf('*', starIndex + 1)
        if (endStar == -1) {
            appendWithGlossary(text.substring(starIndex), SpanStyle(color = BodyColor), activeTerms)
            break
        }
        // Render italic with glossary support
        appendWithGlossary(
            text.substring(starIndex + 1, endStar),
            SpanStyle(fontStyle = FontStyle.Italic, color = NarratorColor),
            activeTerms,
        )
        i = endStar + 1
    }
}

/**
 * Appends [text] using [baseStyle], scanning for glossary terms from [activeTerms]. Glossary terms
 * are rendered with gold bold style and annotated with the "glossary" tag for click handling.
 */
@Suppress("DEPRECATION")
private fun AnnotatedString.Builder.appendWithGlossary(
    text: String,
    baseStyle: SpanStyle,
    activeTerms: List<String>,
) {
    if (text.isEmpty()) return
    if (activeTerms.isEmpty()) {
        withStyle(baseStyle) { append(text) }
        return
    }

    var pos = 0
    while (pos < text.length) {
        // Find the earliest glossary term match from current position
        var bestMatch: String? = null
        var bestStart = Int.MAX_VALUE

        for (term in activeTerms) {
            val idx = text.indexOf(term, pos, ignoreCase = true)
            if (
                idx != -1 &&
                    (idx < bestStart ||
                        (idx == bestStart && term.length > (bestMatch?.length ?: 0)))
            ) {
                bestStart = idx
                bestMatch = term
            }
        }

        if (bestMatch == null) {
            // No more glossary terms — append remaining text with base style
            withStyle(baseStyle) { append(text.substring(pos)) }
            break
        }

        // Append text before the glossary term
        if (bestStart > pos) {
            withStyle(baseStyle) { append(text.substring(pos, bestStart)) }
        }

        // Append the glossary term with gold style + annotation
        val matchedText = text.substring(bestStart, bestStart + bestMatch.length)
        val glossaryStyle = baseStyle.copy(color = GlossaryColor, fontWeight = FontWeight.Bold)
        pushStringAnnotation(tag = "glossary", annotation = bestMatch)
        withStyle(glossaryStyle) { append(matchedText) }
        pop()

        pos = bestStart + bestMatch.length
    }
}
