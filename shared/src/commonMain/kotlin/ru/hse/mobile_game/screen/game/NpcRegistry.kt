package ru.hse.mobile_game.screen.game

/**
 * Registry of every NPC that has a relation value in the game. Each entry contains a display name,
 * narrative description, and flavor text for various relation levels.
 */
object NpcRegistry {

    data class NpcInfo(
        val displayName: String,
        val description: String,
        /** Flavor text describing the relation at different levels. */
        val relationFlavor: Map<RelationTier, String>,
    )

    /** Broad relation tiers derived from the numeric relation value. */
    enum class RelationTier {
        HOSTILE,
        COLD,
        NEUTRAL,
        WARM,
        ALLIED,
    }

    private val entries: Map<String, NpcInfo> =
        mapOf(
            "sable" to
                NpcInfo(
                    displayName = "Sable",
                    description =
                        "A shadowy operative with piercing eyes and a network " +
                            "of informants that stretches across the region. " +
                            "No one knows her true name or origin, but her " +
                            "skills in espionage and combat are undeniable. " +
                            "She pursues her own agenda in Ashenmoor — one " +
                            "that may or may not align with yours.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Sable considers you a threat to be eliminated. " +
                                    "Watch the shadows.",
                            RelationTier.COLD to
                                "Sable keeps her distance, regarding you with " +
                                    "suspicion and calculating eyes.",
                            RelationTier.NEUTRAL to
                                "Sable acknowledges your presence with a curt nod. " +
                                    "You are neither friend nor foe — yet.",
                            RelationTier.WARM to
                                "Sable shares intelligence freely with you. A rare " +
                                    "trust has formed between you.",
                            RelationTier.ALLIED to
                                "Sable stands firmly at your side. Her network, her " +
                                    "blades, and her loyalty are yours.",
                        ),
                ),
            "guard_captain" to
                NpcInfo(
                    displayName = "Captain Brynn",
                    description =
                        "The steadfast captain of Ashenmoor's town guard. " +
                            "Brynn is a woman of iron principle, torn between " +
                            "her duty to uphold the law and the growing " +
                            "corruption she sees in the institutions she serves. " +
                            "She commands the loyalty of the honest guards who " +
                            "remain.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Captain Brynn views you as a lawbreaker and a danger. " +
                                    "The guard watches your every move.",
                            RelationTier.COLD to
                                "Brynn tolerates your presence but makes her disapproval " +
                                    "clear. You've given her reason to doubt you.",
                            RelationTier.NEUTRAL to
                                "Captain Brynn regards you with professional detachment — " +
                                    "neither trusting nor distrusting.",
                            RelationTier.WARM to
                                "Brynn respects your actions and is willing to cooperate. " +
                                    "She sees you as a potential ally.",
                            RelationTier.ALLIED to
                                "Captain Brynn has committed her guards to your cause. " +
                                    "She trusts you with Ashenmoor's safety.",
                        ),
                ),
            "thomas" to
                NpcInfo(
                    displayName = "Thomas",
                    description =
                        "A weary dock foreman with calloused hands and honest " +
                            "eyes. Thomas has worked the harbor for twenty years " +
                            "and has seen the changes wrought by the smuggling " +
                            "operation. He wants to do the right thing but fears " +
                            "for his family's safety.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Thomas refuses to speak with you. Whatever you did, " +
                                    "he considers you as bad as the smugglers.",
                            RelationTier.COLD to
                                "Thomas answers your questions reluctantly, clearly " +
                                    "uncomfortable in your presence.",
                            RelationTier.NEUTRAL to
                                "Thomas is cautiously polite but guards his words " +
                                    "carefully around you.",
                            RelationTier.WARM to
                                "Thomas speaks openly with you, trusting that you " +
                                    "will use what he shares to help, not harm.",
                            RelationTier.ALLIED to
                                "Thomas is fully committed to helping you. He reports " +
                                    "suspicious activities and risks his safety for the cause.",
                        ),
                ),
            "orin" to
                NpcInfo(
                    displayName = "Orin",
                    description =
                        "A reclusive scholar who haunts the dusty shelves of " +
                            "Ashenmoor's library and his own cluttered study. " +
                            "Orin possesses an encyclopedic knowledge of the " +
                            "town's history and an analytical mind that can " +
                            "piece together patterns from seemingly unrelated facts.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Orin has locked his study door against you. He considers " +
                                    "your methods destructive and your cause reckless.",
                            RelationTier.COLD to
                                "Orin peers at you over his spectacles with barely " +
                                    "concealed disdain. You are an unwelcome interruption.",
                            RelationTier.NEUTRAL to
                                "Orin finds you tolerable — a useful if unscholarly " +
                                    "source of current events.",
                            RelationTier.WARM to
                                "Orin eagerly shares his findings with you, delighted " +
                                    "to have someone who takes his research seriously.",
                            RelationTier.ALLIED to
                                "Orin has devoted his full intellectual resources to your " +
                                    "cause. His research is your greatest weapon.",
                        ),
                ),
            "dock_workers" to
                NpcInfo(
                    displayName = "Dock Workers",
                    description =
                        "The laborers who load and unload cargo at Ashenmoor's " +
                            "harbor. They are a tight-knit group who look out " +
                            "for their own. Some have been complicit in the " +
                            "smuggling; others simply keep their heads down " +
                            "to survive.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "The dock workers view you as a threat to their " +
                                    "livelihoods and safety. They actively obstruct you.",
                            RelationTier.COLD to
                                "The dock workers give you the cold shoulder, " +
                                    "turning away when you approach.",
                            RelationTier.NEUTRAL to
                                "The dock workers are wary but not openly hostile. " +
                                    "They watch and wait.",
                            RelationTier.WARM to
                                "Several dock workers nod to you in passing and " +
                                    "share tips about unusual cargo.",
                            RelationTier.ALLIED to
                                "The dock workers stand with you. They refuse to " +
                                    "move contraband and report all suspicious shipments.",
                        ),
                ),
            "harren" to
                NpcInfo(
                    displayName = "Harren",
                    description =
                        "A figure of myth and menace in Ashenmoor's underworld. " +
                            "Harren operates from the shadows, pulling strings " +
                            "through intermediaries and fear. His true motives " +
                            "remain inscrutable — he may be playing all sides " +
                            "against each other.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Harren has marked you for death. His agents lurk " +
                                    "around every corner, waiting for an opening.",
                            RelationTier.COLD to
                                "Harren watches you from the darkness with calculating " +
                                    "disinterest. You are a pawn, nothing more.",
                            RelationTier.NEUTRAL to
                                "Harren acknowledges you as a player in the game. " +
                                    "Neither ally nor enemy — merely useful, for now.",
                            RelationTier.WARM to
                                "Harren shows you a measure of respect, sharing " +
                                    "guarded insights into the town's hidden power structure.",
                            RelationTier.ALLIED to
                                "Harren considers you a worthy partner. His network " +
                                    "of spies and cutthroats moves at your command.",
                        ),
                ),
            "edric" to
                NpcInfo(
                    displayName = "Edric",
                    description =
                        "A former guardsman who was dismissed from service " +
                            "under murky circumstances. Edric is bitter but " +
                            "principled — he lost his position because he " +
                            "refused to look the other way. His combat training " +
                            "and knowledge of guard protocols make him a " +
                            "formidable ally.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Edric wants nothing to do with you. In his eyes, " +
                                    "you're just another part of the corrupt system.",
                            RelationTier.COLD to
                                "Edric watches you with wary eyes, not yet convinced " +
                                    "you're any different from those who wronged him.",
                            RelationTier.NEUTRAL to
                                "Edric gives you a chance, but he's been burned before. " +
                                    "Trust must be earned.",
                            RelationTier.WARM to
                                "Edric fights alongside you willingly. He's found " +
                                    "purpose again in your shared cause.",
                            RelationTier.ALLIED to
                                "Edric is your most devoted combat companion. He would " +
                                    "lay down his life to see justice done.",
                        ),
                ),
            "marta" to
                NpcInfo(
                    displayName = "Marta",
                    description =
                        "The sharp-tongued proprietress of the Crowned Stag, " +
                            "Ashenmoor's oldest tavern. Marta has poured drinks " +
                            "for nobles and cutthroats alike, and she remembers " +
                            "every word spoken in her establishment. She trades " +
                            "in information as readily as in ale.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Marta has barred you from her tavern. You'll find " +
                                    "no drink, no warmth, and no information here.",
                            RelationTier.COLD to
                                "Marta serves you with a stony silence that speaks " +
                                    "louder than words.",
                            RelationTier.NEUTRAL to
                                "Marta offers a knowing smile but keeps her best " +
                                    "secrets close. Business is business.",
                            RelationTier.WARM to
                                "Marta slides you a drink 'on the house' and leans " +
                                    "in to share what she's heard.",
                            RelationTier.ALLIED to
                                "Marta's tavern is your safe house. Her network of " +
                                    "gossip and goodwill is entirely at your service.",
                        ),
                ),
            "goran" to
                NpcInfo(
                    displayName = "Goran",
                    description =
                        "The powerful guildmaster of Ashenmoor, a man of " +
                            "immense wealth and influence. Behind his veneer " +
                            "of respectability lies a ruthless schemer who " +
                            "orchestrated the smuggling operation and the plot " +
                            "against the Duke. He believes he is saving " +
                            "Ashenmoor by seizing control of it.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Goran considers you his greatest threat and will " +
                                    "stop at nothing to destroy you.",
                            RelationTier.COLD to
                                "Goran regards you with thinly veiled contempt, " +
                                    "measuring you as an obstacle to be removed.",
                            RelationTier.NEUTRAL to
                                "Goran treats you with guarded politeness, unsure " +
                                    "whether you can be bought or must be broken.",
                            RelationTier.WARM to
                                "Goran seems almost amicable, perhaps seeing you " +
                                    "as a potential asset in his plans.",
                            RelationTier.ALLIED to
                                "Goran treats you as a trusted confederate. Whether " +
                                    "this alliance serves justice remains to be seen.",
                        ),
                ),
            "kess" to
                NpcInfo(
                    displayName = "Kess",
                    description =
                        "A street-smart urchin who has survived Ashenmoor's " +
                            "harshest districts through wit, speed, and an " +
                            "uncanny ability to be in the right place at the " +
                            "right time. Despite their youth, Kess has an " +
                            "old soul and a sharp eye for danger.",
                    relationFlavor =
                        mapOf(
                            RelationTier.HOSTILE to
                                "Kess avoids you like the plague and spreads " +
                                    "warnings about you through the street network.",
                            RelationTier.COLD to
                                "Kess watches you from rooftops and alley mouths " +
                                    "but won't come close.",
                            RelationTier.NEUTRAL to
                                "Kess is curious about you but cautious. A coin " +
                                    "or a kind word might shift the balance.",
                            RelationTier.WARM to
                                "Kess darts in and out of your path, eagerly " +
                                    "sharing rumors and street intelligence.",
                            RelationTier.ALLIED to
                                "Kess is your shadow, your scout, and your most " +
                                    "resourceful little ally in Ashenmoor's streets.",
                        ),
                ),
        )

    /** Look up an NPC by their relation key. */
    fun lookup(npcKey: String): NpcInfo? = entries[npcKey]

    /** Get the display name for an NPC, with fallback. */
    fun displayName(npcKey: String): String {
        return entries[npcKey]?.displayName
            ?: npcKey.replace("_", " ").replaceFirstChar { it.uppercase() }
    }

    /** Determine the relation tier from a numeric value. */
    fun tierFor(value: Int): RelationTier {
        return when {
            value <= -3 -> RelationTier.HOSTILE
            value < 0 -> RelationTier.COLD
            value == 0 -> RelationTier.NEUTRAL
            value < 3 -> RelationTier.WARM
            else -> RelationTier.ALLIED
        }
    }

    /** Get flavor text for an NPC at a given relation value. */
    fun flavorText(npcKey: String, value: Int): String? {
        val info = entries[npcKey] ?: return null
        return info.relationFlavor[tierFor(value)]
    }
}
