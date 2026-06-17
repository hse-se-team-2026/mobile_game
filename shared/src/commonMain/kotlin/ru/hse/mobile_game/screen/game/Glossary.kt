package ru.hse.mobile_game.screen.game

/**
 * In-game glossary entries for lore terms, proper nouns, and world-building concepts. Entries may
 * require a specific flag to be unlocked — preventing spoilers until the player discovers the
 * information through gameplay.
 */
object Glossary {

    /**
     * @param title Display title
     * @param description Lore description (may contain inline *italic* formatting)
     * @param requiredFlag If non-null, this entry is only visible when the player has this flag.
     *   Null means always available.
     */
    data class Entry(val title: String, val description: String, val requiredFlag: String? = null)

    private val entries: Map<String, Entry> =
        mapOf(
            // ── Always available (basic world knowledge) ──
            "Ashenmoor" to
                Entry(
                    "Ashenmoor",
                    "A once-prosperous trading city built on the mouth of the Grey River. " +
                        "Named for the fine grey dust — called *ash* by the locals — " +
                        "that blows in from the surrounding badlands. The city is governed by " +
                        "Duke Aldren and has long been a vital waypoint on the eastern trade routes.",
                ),
            "Ashen Market" to
                Entry(
                    "Ashen Market",
                    "The sprawling marketplace at the heart of Ashenmoor. " +
                        "Stalls of weathered canvas and splintered wood fill the square, " +
                        "where merchants sell smoked fish, leather goods, and provisions.",
                ),
            "Duke Aldren" to
                Entry(
                    "Duke Aldren",
                    "The aging ruler of Ashenmoor, a broad-shouldered man " +
                        "once known for his sharp mind and fair governance. " +
                        "He has fallen mysteriously ill in recent weeks.",
                ),
            "Kerhold" to
                Entry(
                    "Kerhold",
                    "A distant port city and major trading partner of Ashenmoor. " +
                        "Three merchant ships due from Kerhold have gone missing.",
                ),
            // ── Unlocked through gameplay ──
            "Goran" to
                Entry(
                    "Goran",
                    "The Duke's steward — a small, sharp-featured man with restless hands. " +
                        "Appointed two years ago, he has systematically replaced the Duke's veteran guard " +
                        "with hired mercenaries and redirected estate finances to unknown accounts. " +
                        "His connection to Harren and the conspiracy runs deep.",
                    requiredFlag = "knows_goran",
                ),
            "Harren" to
                Entry(
                    "Harren",
                    "Head of the Ashenmoor Merchants' Guild. A wealthy, smiling man " +
                        "whose pleasant demeanor conceals ruthless ambition. " +
                        "He has been secretly smuggling weapons into the city on behalf of " +
                        "Baron Varketh, preparing for a coup.",
                    requiredFlag = "knows_harren",
                ),
            "Baron Varketh" to
                Entry(
                    "Baron Varketh",
                    "A minor warlord from the eastern marches, ambitious and well-funded. " +
                        "He has struck a deal with Harren to take Ashenmoor without a prolonged siege — " +
                        "using agents inside the walls to weaken the city from within. " +
                        "Commands a force of roughly five hundred soldiers.",
                    requiredFlag = "knows_varketh",
                ),
            "Merchants' Guild" to
                Entry(
                    "Merchants' Guild",
                    "The powerful trade organization that controls commerce in Ashenmoor. " +
                        "Under Harren's leadership, the Guild has become a front for weapons smuggling " +
                        "and political manipulation.",
                    requiredFlag = "knows_harren",
                ),
            "Hollow Crown" to
                Entry(
                    "Hollow Crown",
                    "A rough-and-tumble tavern in the Ashen Market, run by the formidable " +
                        "barkeep Marta. Known for sticky floors, strong ale, and a strict " +
                        "\"mind your own business\" policy.",
                    requiredFlag = "met_marta",
                ),
            "Marta" to
                Entry(
                    "Marta",
                    "The barkeep of the Hollow Crown — a massive woman with arms thick as ship ropes " +
                        "and a scar from her left ear to her jaw. Despite her intimidating appearance, " +
                        "she is fiercely protective of Ashenmoor and its people.",
                    requiredFlag = "met_marta",
                ),
            "Edric" to
                Entry(
                    "Edric",
                    "A veteran soldier who served twelve years in the Duke's personal guard " +
                        "before being unexpectedly discharged by Goran. A haunted but honorable man " +
                        "with grey eyes and scarred knuckles.",
                    requiredFlag = "met_edric",
                ),
            "Sable" to
                Entry(
                    "Sable",
                    "A lean, hooded woman who knows the hidden passages beneath Ashenmoor — " +
                        "the \"rat roads\" — like her own veins. Sharp-eyed and amused by danger.",
                    requiredFlag = "met_sable",
                ),
            "Thomas" to
                Entry(
                    "Thomas",
                    "The Duke's personal scribe — a thin, nervous young man with ink-stained fingers. " +
                        "Loyal to Duke Aldren, he has secretly been copying financial records " +
                        "that reveal the embezzlement and corruption within the estate.",
                    requiredFlag = "met_thomas",
                ),
            "Brother Orin" to
                Entry(
                    "Brother Orin",
                    "A gaunt priest at the Temple of the Ashen God, with a tonsured head " +
                        "and eyes that have seen too much. He serves as the temple's herbalist " +
                        "and has identified nightbloom extract — a slow poison — in a sample " +
                        "from the Duke's household.",
                    requiredFlag = "met_orin",
                ),
            "Aldric" to
                Entry(
                    "Aldric",
                    "Aldric Voss — a merchant whose shop was ransacked after he discovered " +
                        "discrepancies in shipping manifests pointing to weapons smuggling. " +
                        "One of three merchants who disappeared.",
                    requiredFlag = "knows_disappearances",
                ),
            "Brynn" to
                Entry(
                    "Brynn",
                    "Guard Captain Brynn — a compact woman with iron-grey hair and the permanent scowl " +
                        "of someone who files too many reports about things she cannot fix. " +
                        "Half her veteran guards were replaced by Goran's mercenaries.",
                    requiredFlag = "met_brynn",
                ),
            "Kess" to
                Entry(
                    "Kess",
                    "A tattooed woman who runs cargo for the Merchants' Guild — " +
                        "or what passes for legitimate cargo. A skilled dice player " +
                        "and a useful source of information about the harbor's secrets.",
                    requiredFlag = "met_kess",
                ),
            "Ashen God" to
                Entry(
                    "Ashen God",
                    "The deity worshipped at Ashenmoor's oldest temple. " +
                        "The Ashen God teaches that from ruin comes renewal — " +
                        "a philosophy deeply embedded in the city's identity.",
                    requiredFlag = "visited_temple",
                ),
            "nightbloom" to
                Entry(
                    "Nightbloom Extract",
                    "A rare and deadly slow-acting poison derived from the nightbloom flower. " +
                        "When dissolved in wine, it is nearly undetectable by taste or smell. " +
                        "Causes progressive weakness, clouded thinking, and eventually death " +
                        "over a period of weeks.",
                    requiredFlag = "duke_poisoned",
                ),
            "Lowmarket" to
                Entry(
                    "Lowmarket",
                    "The rougher district of Ashenmoor near the harbor, known for its narrow alleys, " +
                        "cheaper goods, and higher crime rate. The guards warn travelers " +
                        "to avoid it after dark.",
                    requiredFlag = "knows_harbor",
                ),
            "Dock Workers" to
                Entry(
                    "Dock Workers",
                    "The laborers who load and unload cargo at Ashenmoor's harbor. " +
                        "A tight-knit group led informally by Thomas, the dock foreman. " +
                        "Some have been unwilling participants in Harren's smuggling operation; " +
                        "others keep their heads down to survive.",
                    requiredFlag = "knows_harbor",
                ),
        )

    /**
     * Map from NPC relation keys (as stored in character state) to glossary entry keys. Allows the
     * CharacterSheet to open the correct glossary entry when an NPC name is tapped.
     */
    private val npcKeyToGlossaryKey: Map<String, String> =
        mapOf(
            "sable" to "Sable",
            "guard_captain" to "Brynn",
            "thomas" to "Thomas",
            "orin" to "Brother Orin",
            "dock_workers" to "Dock Workers",
            "harren" to "Harren",
            "edric" to "Edric",
            "marta" to "Marta",
            "goran" to "Goran",
            "kess" to "Kess",
        )

    /** Look up glossary entry key for an NPC relation key. Returns null if no mapping exists. */
    fun glossaryKeyForNpc(npcKey: String): String? = npcKeyToGlossaryKey[npcKey]

    /** Look up a glossary entry by its key (case-insensitive). Returns null if not found. */
    fun lookup(term: String): Entry? {
        return entries[term]
            ?: entries.entries.find { it.key.equals(term, ignoreCase = true) }?.value
    }

    /**
     * Get glossary term keys that are currently unlocked for the given character flags. Sorted
     * longest-first to avoid partial matches during text scanning.
     */
    fun unlockedTerms(flags: Set<String>): List<String> {
        return entries
            .filter { (_, entry) -> entry.requiredFlag == null || entry.requiredFlag in flags }
            .keys
            .sortedByDescending { it.length }
    }

    /** Get all glossary term keys (regardless of lock state). Used only for testing. */
    fun allTerms(): List<String> {
        return entries.keys.sortedByDescending { it.length }
    }
}
