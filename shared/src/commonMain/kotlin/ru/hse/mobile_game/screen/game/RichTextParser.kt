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

private val DIALOGUE_REGEX = Regex("""^([\w\s''-]+):\s*"(.+)"$""", RegexOption.DOT_MATCHES_ALL)

/**
 * Parses scene text with simple markup conventions:
 * - A paragraph wrapped in `*asterisks*` is narrator text (rendered italic, muted color)
 * - A paragraph matching `Speaker: "dialogue"` is dialogue (bold speaker, italic quote)
 * - Within regular paragraphs, inline `*italic*` markers are supported
 *
 * Paragraphs are separated by double newlines (`\n\n`).
 */
fun parseNarrativeText(text: String): AnnotatedString {
    return buildAnnotatedString {
        val paragraphs = text.split("\n\n")
        paragraphs.forEachIndexed { index, raw ->
            val paragraph = raw.trim()
            if (paragraph.isEmpty()) return@forEachIndexed
            if (index > 0) append("\n\n")
            parseParagraph(paragraph)
        }
    }
}

private fun AnnotatedString.Builder.parseParagraph(paragraph: String) {
    when {
        // Full narrator paragraph: *entire text*
        paragraph.startsWith("*") && paragraph.endsWith("*") && paragraph.length > 2 -> {
            withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = NarratorColor)) {
                append(paragraph.substring(1, paragraph.length - 1))
            }
        }
        // Dialogue: Speaker: "text"
        DIALOGUE_REGEX.matches(paragraph) -> {
            val match = DIALOGUE_REGEX.find(paragraph)!!
            val speaker = match.groupValues[1].trim()
            val dialogue = match.groupValues[2].trim()
            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = SpeakerColor)) {
                append(speaker)
            }
            withStyle(SpanStyle(color = BodyColor)) { append(": ") }
            withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = DialogueColor)) {
                append("\u201C$dialogue\u201D")
            }
        }
        // Regular text with possible inline *italic* markers
        else -> {
            parseInlineFormatting(paragraph)
        }
    }
}

private fun AnnotatedString.Builder.parseInlineFormatting(text: String) {
    var i = 0
    while (i < text.length) {
        val starIndex = text.indexOf('*', i)
        if (starIndex == -1) {
            withStyle(SpanStyle(color = BodyColor)) { append(text.substring(i)) }
            break
        }
        // Append text before the *
        if (starIndex > i) {
            withStyle(SpanStyle(color = BodyColor)) { append(text.substring(i, starIndex)) }
        }
        // Find closing *
        val endStar = text.indexOf('*', starIndex + 1)
        if (endStar == -1) {
            withStyle(SpanStyle(color = BodyColor)) { append(text.substring(starIndex)) }
            break
        }
        // Render italic
        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = NarratorColor)) {
            append(text.substring(starIndex + 1, endStar))
        }
        i = endStar + 1
    }
}
