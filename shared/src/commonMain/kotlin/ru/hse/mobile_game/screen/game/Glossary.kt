package ru.hse.mobile_game.screen.game

/** In-game glossary entries for lore terms, proper nouns, and world-building concepts. */
object Glossary {

    data class Entry(val title: String, val description: String)

    private val entries: Map<String, Entry> =
        mapOf(
            "Ashenmoor" to
                Entry(
                    "Ashenmoor",
                    "A once-prosperous trading city built on the mouth of the Grey River. "
                        + "Named for the fine grey dust — called *ash* by the locals — "
                        + "that blows in from the surrounding badlands. The city is governed by "
                        + "Duke Aldren and has long been a vital waypoint on the eastern trade routes. "
                        + "Recently plagued by disappearances and political intrigue.",
                ),
            "Duke Aldren" to
                Entry(
                    "Duke Aldren",
                    "The aging ruler of Ashenmoor, a broad-shouldered man "
                        + "once known for his sharp mind and fair governance. "
                        + "He has fallen mysteriously ill in recent weeks, and his steward Goran "
                        + "has assumed control of the estate's affairs. Some whisper he is being poisoned.",
                ),
            "Goran" to
                Entry(
                    "Goran",
                    "The Duke's steward — a small, sharp-featured man with restless hands. "
                        + "Appointed two years ago, he has systematically replaced the Duke's veteran guard "
                        + "with hired mercenaries and redirected estate finances to unknown accounts. "
                        + "His connection to Harren and the conspiracy runs deep.",
                ),
            "Harren" to
                Entry(
                    "Harren",
                    "Head of the Ashenmoor Merchants' Guild. A wealthy, smiling man "
                        + "whose pleasant demeanor conceals ruthless ambition. "
                        + "He has been secretly smuggling weapons into the city on behalf of "
                        + "Baron Varketh, preparing for a coup. Responsible for the disappearance "
                        + "of merchants who discovered his operations.",
                ),
            "Baron Varketh" to
                Entry(
                    "Baron Varketh",
                    "A minor warlord from the eastern marches, ambitious and well-funded. "
                        + "He has struck a deal with Harren to take Ashenmoor without a prolonged siege — "
                        + "using agents inside the walls to weaken the city from within. "
                        + "Commands a force of roughly five hundred soldiers.",
                ),
            "Merchants' Guild" to
                Entry(
                    "Merchants' Guild",
                    "The powerful trade organization that controls commerce in Ashenmoor. "
                        + "Under Harren's leadership, the Guild has become a front for weapons smuggling "
                        + "and political manipulation. Its harbor offices and warehouses hide "
                        + "an arsenal of military-grade weapons.",
                ),
            "Hollow Crown" to
                Entry(
                    "Hollow Crown",
                    "A rough-and-tumble tavern in the Ashen Market, run by the formidable "
                        + "barkeep Marta. Known for sticky floors, strong ale, and a strict "
                        + "\"mind your own business\" policy. It becomes the meeting place "
                        + "for the resistance against Harren's conspiracy.",
                ),
            "Marta" to
                Entry(
                    "Marta",
                    "The barkeep of the Hollow Crown — a massive woman with arms thick as ship ropes "
                        + "and a scar from her left ear to her jaw. Despite her intimidating appearance, "
                        + "she is fiercely protective of Ashenmoor and its people. "
                        + "She was a friend of the missing merchant Aldric.",
                ),
            "Edric" to
                Entry(
                    "Edric",
                    "A veteran soldier who served twelve years in the Duke's personal guard "
                        + "before being unexpectedly discharged by Goran. A haunted but honorable man "
                        + "with grey eyes and scarred knuckles. He suspects something is deeply wrong "
                        + "in Ashenmoor and may join your cause.",
                ),
            "Sable" to
                Entry(
                    "Sable",
                    "A lean, hooded woman who knows the hidden passages beneath Ashenmoor — "
                        + "the \"rat roads\" — like her own veins. Sharp-eyed and amused by danger. "
                        + "She operates in the city's shadows and may become a valuable ally "
                        + "for those cunning enough to earn her trust.",
                ),
            "Thomas" to
                Entry(
                    "Thomas",
                    "The Duke's personal scribe — a thin, nervous young man with ink-stained fingers. "
                        + "Loyal to Duke Aldren, he has secretly been copying financial records "
                        + "that reveal the embezzlement and corruption within the estate. "
                        + "He suspects the Duke is being poisoned.",
                ),
            "Brother Orin" to
                Entry(
                    "Brother Orin",
                    "A gaunt priest at the Temple of the Ashen God, with a tonsured head "
                        + "and eyes that have seen too much. He serves as the temple's herbalist "
                        + "and has identified nightbloom extract — a slow poison — in a sample "
                        + "from the Duke's household.",
                ),
            "Aldric" to
                Entry(
                    "Aldric",
                    "Aldric Voss — a merchant whose shop was ransacked after he discovered "
                        + "discrepancies in shipping manifests pointing to weapons smuggling. "
                        + "One of three merchants who disappeared. Depending on the player's path, "
                        + "he may be found alive in Harren's cellar prison.",
                ),
            "Brynn" to
                Entry(
                    "Brynn",
                    "Guard Captain Brynn — a compact woman with iron-grey hair and the permanent scowl "
                        + "of someone who files too many reports about things she cannot fix. "
                        + "Half her veteran guards were replaced by Goran's mercenaries, "
                        + "but she commands twelve loyal soldiers she trusts.",
                ),
            "Kess" to
                Entry(
                    "Kess",
                    "A tattooed woman who runs cargo for the Merchants' Guild — "
                        + "or what passes for legitimate cargo. A skilled dice player "
                        + "and a useful source of information about the harbor's secrets.",
                ),
            "Ashen God" to
                Entry(
                    "Ashen God",
                    "The deity worshipped at Ashenmoor's oldest temple. "
                        + "The Ashen God teaches that from ruin comes renewal — "
                        + "a philosophy deeply embedded in the city's identity. "
                        + "The temple is a place of confession, healing, and ancient knowledge.",
                ),
            "nightbloom" to
                Entry(
                    "Nightbloom Extract",
                    "A rare and deadly slow-acting poison derived from the nightbloom flower. "
                        + "When dissolved in wine, it is nearly undetectable by taste or smell. "
                        + "Causes progressive weakness, clouded thinking, and eventually death "
                        + "over a period of weeks. Brother Orin identified it in the Duke's goblet.",
                ),
            "Ashen Market" to
                Entry(
                    "Ashen Market",
                    "The sprawling marketplace at the heart of Ashenmoor. "
                        + "Stalls of weathered canvas and splintered wood fill the square, "
                        + "where merchants sell smoked fish, leather goods, and provisions. "
                        + "Recently subdued due to the disappearances and growing unrest.",
                ),
            "Lowmarket" to
                Entry(
                    "Lowmarket",
                    "The rougher district of Ashenmoor near the harbor, known for its narrow alleys, "
                        + "cheaper goods, and higher crime rate. The guards warn travelers "
                        + "to avoid it after dark.",
                ),
            "Kerhold" to
                Entry(
                    "Kerhold",
                    "A distant port city and major trading partner of Ashenmoor. "
                        + "Three merchant ships due from Kerhold have gone missing — "
                        + "officially attributed to weather delays, but suspected to be connected "
                        + "to the conspiracy.",
                ),
        )

    /** Look up a glossary entry by its key (case-insensitive). */
    fun lookup(term: String): Entry? {
        return entries[term] ?: entries.entries.find { it.key.equals(term, ignoreCase = true) }?.value
    }

    /** Get all glossary term keys for text matching. Sorted longest-first to avoid partial matches. */
    fun allTerms(): List<String> {
        return entries.keys.sortedByDescending { it.length }
    }
}
