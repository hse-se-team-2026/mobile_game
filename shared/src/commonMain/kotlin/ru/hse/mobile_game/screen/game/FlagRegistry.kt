package ru.hse.mobile_game.screen.game

/**
 * Comprehensive registry of every flag used in the game narrative. Each entry provides a
 * human-readable title, narrative description, how the player obtained it, and a gameplay hint.
 */
object FlagRegistry {

    data class FlagInfo(
        val title: String,
        val description: String,
        val howObtained: String,
        val hint: String,
    )

    private val entries: Map<String, FlagInfo> =
        mapOf(
            // ── Entry flags ──────────────────────────────────────────────────
            "noble_entry" to
                FlagInfo(
                    title = "Noble Entry",
                    description =
                        "You arrived at Ashenmoor through the noble gate, presenting your " +
                            "family crest to the guards. The townsfolk regard you with a " +
                            "mixture of deference and suspicion.",
                    howObtained =
                        "Chose to enter Ashenmoor openly through the main gate, " +
                            "leveraging your noble heritage.",
                    hint =
                        "Noble connections may open doors in the upper districts and " +
                            "grant audiences with officials.",
                ),
            "secret_entry" to
                FlagInfo(
                    title = "Secret Entry",
                    description =
                        "You slipped into Ashenmoor through a hidden passage known only " +
                            "to smugglers and outcasts. No one saw you arrive.",
                    howObtained =
                        "Chose to enter the town unseen, using a concealed route " +
                            "through the old drainage tunnels.",
                    hint =
                        "Knowledge of secret ways may prove useful when stealth " +
                            "is more valuable than authority.",
                ),
            "forced_entry" to
                FlagInfo(
                    title = "Forced Entry",
                    description =
                        "You broke through the lesser gate by force, overpowering " +
                            "the sparse guard detail. Word of your violent arrival " +
                            "spreads quickly.",
                    howObtained =
                        "Chose to force your way past the guards when they tried " +
                            "to deny you entry.",
                    hint =
                        "A reputation for violence precedes you — some will fear " +
                            "you, others will seek to test you.",
                ),
            "patient_entry" to
                FlagInfo(
                    title = "Patient Entry",
                    description =
                        "You waited at the gate until the guards changed shifts, " +
                            "then entered quietly during the commotion. Patience " +
                            "revealed much about the town's routines.",
                    howObtained =
                        "Chose to observe and wait for the right moment to " +
                            "enter Ashenmoor.",
                    hint =
                        "Your careful observation has given you insight into " +
                            "the town's guard patterns and daily rhythms.",
                ),

            // ── Knowledge flags ──────────────────────────────────────────────
            "knows_trouble" to
                FlagInfo(
                    title = "Aware of the Trouble",
                    description =
                        "You have learned that something dark stirs beneath " +
                            "Ashenmoor's surface — disappearances, whispered " +
                            "conspiracies, and a pervasive sense of dread.",
                    howObtained =
                        "Gathered rumors from townsfolk or overheard " +
                            "conversations about the troubles plaguing Ashenmoor.",
                    hint =
                        "Knowing the town is in danger lets you ask pointed " +
                            "questions and pursue leads others might miss.",
                ),
            "knows_aldric" to
                FlagInfo(
                    title = "Knowledge of Aldric",
                    description =
                        "You know about Aldric, a scholar who was investigating " +
                            "the disappearances before he himself vanished. His " +
                            "research may hold crucial answers.",
                    howObtained =
                        "Learned about Aldric's investigation and his " +
                            "mysterious disappearance from local sources.",
                    hint =
                        "Finding Aldric or his notes could be the key to " +
                            "unraveling the conspiracy.",
                ),
            "knows_disappearances" to
                FlagInfo(
                    title = "Knowledge of the Disappearances",
                    description =
                        "You are aware that people have been vanishing from " +
                            "Ashenmoor — mostly dockworkers and those who asked " +
                            "too many questions about the harbor shipments.",
                    howObtained =
                        "Pieced together accounts of missing persons from " +
                            "various witnesses around town.",
                    hint =
                        "The pattern of disappearances may point to whoever " +
                            "is behind the conspiracy.",
                ),
            "knows_harbor" to
                FlagInfo(
                    title = "Harbor Secrets",
                    description =
                        "You have learned that suspicious activities center " +
                            "around the harbor — unmarked ships arrive at night, " +
                            "and cargo is moved under heavy guard.",
                    howObtained =
                        "Investigated the harbor district and uncovered " +
                            "evidence of illicit nighttime operations.",
                    hint =
                        "The harbor is the nerve center of the smuggling " +
                            "operation. Watching it closely may reveal the " +
                            "conspirators' network.",
                ),
            "knows_goran" to
                FlagInfo(
                    title = "Knowledge of Goran",
                    description =
                        "You know about Goran, the influential guildmaster " +
                            "who controls much of Ashenmoor's commerce. Behind " +
                            "his respectable facade lurks something sinister.",
                    howObtained =
                        "Gathered intelligence about Goran's role in " +
                            "Ashenmoor's power structure through investigation " +
                            "or conversation.",
                    hint =
                        "Understanding Goran's motives and methods is " +
                            "essential to dismantling the conspiracy.",
                ),
            "knows_harren" to
                FlagInfo(
                    title = "Knowledge of Harren",
                    description =
                        "You have learned about Harren, a mysterious figure " +
                            "who operates in the shadows of Ashenmoor's " +
                            "underworld with his own inscrutable agenda.",
                    howObtained =
                        "Discovered Harren's existence through contacts " +
                            "in the town's less reputable circles.",
                    hint =
                        "Harren may be an ally or an enemy — his true " +
                            "loyalties remain to be tested.",
                ),
            "knows_smuggling" to
                FlagInfo(
                    title = "Smuggling Network Exposed",
                    description =
                        "You have uncovered evidence of a large-scale " +
                            "smuggling operation running through Ashenmoor's " +
                            "harbor, moving contraband under cover of night.",
                    howObtained =
                        "Discovered the smuggling network through " +
                            "investigation of harbor activities and cargo records.",
                    hint =
                        "This knowledge can be leveraged to gain allies " +
                            "among those who oppose the smugglers, or used as " +
                            "blackmail against those involved.",
                ),
            "knows_night_ships" to
                FlagInfo(
                    title = "Night Ships",
                    description =
                        "You have witnessed or learned about unmarked ships " +
                            "that dock at Ashenmoor's harbor under the cover " +
                            "of darkness, carrying illicit cargo.",
                    howObtained =
                        "Observed the nighttime harbor operations or " +
                            "received intelligence about the mysterious ships.",
                    hint =
                        "Tracking the ships' schedule could lead to " +
                            "intercepting a crucial shipment.",
                ),
            "knows_forge_mark" to
                FlagInfo(
                    title = "Forge Mark Identified",
                    description =
                        "You have identified a distinctive forge mark on " +
                            "weapons being smuggled through Ashenmoor, tracing " +
                            "them to a specific source.",
                    howObtained =
                        "Examined confiscated or discovered weapons and " +
                            "recognized the unique smithing marks.",
                    hint =
                        "The forge mark can help trace the weapons to their " +
                            "manufacturer and the conspirators who commissioned them.",
                ),
            "knows_coup" to
                FlagInfo(
                    title = "Coup Plot Uncovered",
                    description =
                        "You have learned of a planned coup against the Duke " +
                            "of Ashenmoor. Powerful forces are conspiring to " +
                            "seize control of the town by force.",
                    howObtained =
                        "Uncovered evidence of the plot through investigation, " +
                            "intercepted communications, or a confidant's warning.",
                    hint =
                        "This is the heart of the conspiracy. Acting on this " +
                            "knowledge could save the Duke — or change the " +
                            "balance of power entirely.",
                ),
            "knows_full_plan" to
                FlagInfo(
                    title = "The Full Conspiracy",
                    description =
                        "You have pieced together the entire conspiracy — the " +
                            "weapons, the alliances, the timeline, and the " +
                            "ultimate goal of overthrowing Ashenmoor's rule.",
                    howObtained =
                        "Assembled all the evidence and intelligence gathered " +
                            "throughout your investigation.",
                    hint =
                        "With the full picture in hand, you can plan a " +
                            "comprehensive counter-strategy.",
                ),
            "knows_passages" to
                FlagInfo(
                    title = "Secret Passages Known",
                    description =
                        "You have discovered a network of hidden passages " +
                            "beneath Ashenmoor that connect key locations " +
                            "throughout the town.",
                    howObtained =
                        "Found the passages through exploration, old maps, " +
                            "or information from a knowledgeable contact.",
                    hint =
                        "These passages allow movement through the town " +
                            "unseen — invaluable for infiltration or escape.",
                ),

            // ── Met NPC flags ────────────────────────────────────────────────
            "met_marta" to
                FlagInfo(
                    title = "Met Marta",
                    description =
                        "You have encountered Marta, the sharp-witted tavern " +
                            "keeper who serves as Ashenmoor's unofficial " +
                            "information broker.",
                    howObtained = "Visited the tavern and spoke with Marta.",
                    hint =
                        "Marta hears everything that passes through her " +
                            "tavern. She may share what she knows — for the " +
                            "right price or the right cause.",
                ),
            "met_edric" to
                FlagInfo(
                    title = "Met Edric",
                    description =
                        "You have met Edric, a former guardsman who was " +
                            "dismissed under suspicious circumstances. He " +
                            "harbors a deep grudge against the current order.",
                    howObtained =
                        "Encountered Edric during your travels through " +
                            "Ashenmoor's streets or establishments.",
                    hint =
                        "Edric's knowledge of the guard's routines and his " +
                            "desire for justice could make him a valuable ally.",
                ),
            "edric_ally" to
                FlagInfo(
                    title = "Edric's Alliance",
                    description =
                        "Edric has pledged his support to your cause. The " +
                            "disgraced guardsman stands ready to fight " +
                            "alongside you when the time comes.",
                    howObtained =
                        "Earned Edric's trust and convinced him to join " +
                            "your effort against the conspirators.",
                    hint =
                        "Edric's combat experience and insider knowledge " +
                            "of guard operations will be crucial in any " +
                            "direct confrontation.",
                ),
            "met_kess" to
                FlagInfo(
                    title = "Met Kess",
                    description =
                        "You have crossed paths with Kess, a street-smart " +
                            "urchin who navigates Ashenmoor's back alleys " +
                            "with uncanny ease.",
                    howObtained =
                        "Encountered Kess in the town's less reputable " +
                            "districts.",
                    hint =
                        "Kess knows every shortcut and hiding place in " +
                            "Ashenmoor. Their knowledge of the streets " +
                            "could prove invaluable.",
                ),
            "met_sable" to
                FlagInfo(
                    title = "Met Sable",
                    description =
                        "You have encountered Sable, a mysterious operative " +
                            "who seems to have her own agenda regarding " +
                            "Ashenmoor's troubles.",
                    howObtained =
                        "Sable made contact with you — or you sought " +
                            "her out — in the shadows of Ashenmoor.",
                    hint =
                        "Sable's network of informants and her combat " +
                            "skills make her a dangerous friend — and " +
                            "an even more dangerous enemy.",
                ),
            "sable_contact" to
                FlagInfo(
                    title = "Sable's Contact",
                    description =
                        "You have established a working relationship with " +
                            "Sable. She shares intelligence with you and " +
                            "considers you a trusted contact.",
                    howObtained =
                        "Proved your worth to Sable through actions that " +
                            "aligned with her interests.",
                    hint =
                        "As Sable's contact, you gain access to " +
                            "intelligence that would otherwise remain hidden.",
                ),
            "met_brynn" to
                FlagInfo(
                    title = "Met Captain Brynn",
                    description =
                        "You have met Brynn, the stalwart captain of " +
                            "Ashenmoor's town guard — a woman torn between " +
                            "duty and the corruption she sees around her.",
                    howObtained =
                        "Encountered Captain Brynn during your dealings " +
                            "with Ashenmoor's authorities.",
                    hint =
                        "Brynn commands the town guard. Her support could " +
                            "tip the scales in any open conflict.",
                ),
            "brynn_ally" to
                FlagInfo(
                    title = "Brynn's Alliance",
                    description =
                        "Captain Brynn has thrown her lot in with you. " +
                            "The town guard — or at least those loyal to " +
                            "her — will follow your lead.",
                    howObtained =
                        "Convinced Brynn that the conspiracy threatens " +
                            "everything she swore to protect.",
                    hint =
                        "With Brynn on your side, you have the backing " +
                            "of legitimate armed force in Ashenmoor.",
                ),
            "met_thomas" to
                FlagInfo(
                    title = "Met Thomas",
                    description =
                        "You have met Thomas, a weary dock foreman who " +
                            "sees more than he lets on about the harbor's " +
                            "nocturnal activities.",
                    howObtained =
                        "Spoke with Thomas at the docks while investigating " +
                            "the harbor operations.",
                    hint =
                        "Thomas can provide firsthand accounts of what " +
                            "passes through the harbor under cover of night.",
                ),
            "thomas_ally" to
                FlagInfo(
                    title = "Thomas's Alliance",
                    description =
                        "Thomas has agreed to help you. The dock foreman " +
                            "will keep watch and report any suspicious " +
                            "activities at the harbor.",
                    howObtained =
                        "Gained Thomas's trust and persuaded him that " +
                            "helping you is the right thing to do.",
                    hint =
                        "Thomas's position at the docks gives you eyes " +
                            "and ears in the harbor district.",
                ),
            "met_orin" to
                FlagInfo(
                    title = "Met Orin",
                    description =
                        "You have met Orin, a reclusive scholar who " +
                            "studies the ancient histories of Ashenmoor " +
                            "from his cluttered study.",
                    howObtained =
                        "Sought out Orin at the library or his private " +
                            "quarters.",
                    hint =
                        "Orin's historical knowledge may illuminate " +
                            "connections between past events and the " +
                            "current conspiracy.",
                ),
            "orin_ally" to
                FlagInfo(
                    title = "Orin's Alliance",
                    description =
                        "Orin has committed his scholarly resources to " +
                            "your cause. His research and analytical mind " +
                            "are at your disposal.",
                    howObtained =
                        "Showed Orin evidence that convinced him the " +
                            "conspiracy is real and must be stopped.",
                    hint =
                        "Orin can decode documents, identify historical " +
                            "patterns, and provide strategic insights.",
                ),
            "met_harren_directly" to
                FlagInfo(
                    title = "Confronted Harren",
                    description =
                        "You have met Harren face to face — a dangerous " +
                            "proposition, as few who seek him out return " +
                            "unchanged.",
                    howObtained =
                        "Tracked down Harren's location and confronted " +
                            "him directly.",
                    hint =
                        "Having met Harren in person, you understand " +
                            "his methods and motivations better than most.",
                ),

            // ── Evidence flags ───────────────────────────────────────────────
            "has_manifest" to
                FlagInfo(
                    title = "Shipping Manifest",
                    description =
                        "You possess a shipping manifest listing illegal " +
                            "cargo moving through Ashenmoor's harbor — " +
                            "weapons, supplies, and coded references to buyers.",
                    howObtained =
                        "Seized or copied a manifest from the harbor " +
                            "warehouses or a smuggler's possession.",
                    hint =
                        "The manifest names contacts and locations — " +
                            "hard evidence that could expose the network.",
                ),
            "has_evidence" to
                FlagInfo(
                    title = "Incriminating Evidence",
                    description =
                        "You carry evidence that directly links key " +
                            "figures to the conspiracy against Ashenmoor.",
                    howObtained =
                        "Gathered evidence through investigation — " +
                            "documents, testimonies, or physical proof.",
                    hint =
                        "This evidence could be presented to authorities " +
                            "or used as leverage against the conspirators.",
                ),
            "has_records" to
                FlagInfo(
                    title = "Financial Records",
                    description =
                        "You have obtained financial records showing " +
                            "suspicious transactions — large sums moving " +
                            "between the guild, unknown parties, and " +
                            "offshore accounts.",
                    howObtained =
                        "Acquired the records from a guild office, " +
                            "warehouse, or informant.",
                    hint =
                        "Following the money trail leads to the " +
                            "conspiracy's financiers and their ultimate goals.",
                ),
            "has_ledger" to
                FlagInfo(
                    title = "Goran's Ledger",
                    description =
                        "You possess Goran's private ledger — a detailed " +
                            "record of his dealings, bribes, and the true " +
                            "scope of his criminal enterprise.",
                    howObtained =
                        "Obtained the ledger from Goran's office or " +
                            "through an associate who turned on him.",
                    hint =
                        "The ledger is the single most damning piece of " +
                            "evidence. It can bring down the entire network.",
                ),
            "has_deed" to
                FlagInfo(
                    title = "Property Deed",
                    description =
                        "You hold a deed proving that properties " +
                            "throughout Ashenmoor have been secretly " +
                            "transferred to the conspirators' control.",
                    howObtained =
                        "Found the deed among seized documents or " +
                            "in a hidden cache.",
                    hint =
                        "The deed shows the conspirators' plan extends " +
                            "beyond violence — they aim to own Ashenmoor.",
                ),
            "has_portfolio" to
                FlagInfo(
                    title = "Intelligence Portfolio",
                    description =
                        "You carry a compiled portfolio of intelligence " +
                            "documents — correspondence, maps, and dossiers " +
                            "that paint a comprehensive picture of the plot.",
                    howObtained =
                        "Assembled the portfolio from multiple sources " +
                            "over the course of your investigation.",
                    hint =
                        "The portfolio can convince skeptics and rally " +
                            "support from undecided parties.",
                ),
            "has_copies" to
                FlagInfo(
                    title = "Document Copies",
                    description =
                        "You made copies of important documents, ensuring " +
                            "that even if the originals are destroyed, the " +
                            "evidence survives.",
                    howObtained =
                        "Had the foresight to duplicate crucial evidence " +
                            "before it could be seized or destroyed.",
                    hint =
                        "Copies can be distributed to multiple allies, " +
                            "making it impossible to suppress the truth.",
                ),
            "has_note" to
                FlagInfo(
                    title = "Mysterious Note",
                    description =
                        "You found a cryptic note that hints at a deeper " +
                            "layer of the conspiracy — one that may involve " +
                            "powers beyond Ashenmoor.",
                    howObtained =
                        "Discovered the note hidden in an unexpected " +
                            "place during your investigation.",
                    hint =
                        "Deciphering the note's full meaning could " +
                            "reveal the conspiracy's true mastermind.",
                ),
            "has_goblet" to
                FlagInfo(
                    title = "Poisoned Goblet",
                    description =
                        "You possess a goblet that was used — or intended " +
                            "to be used — in an assassination attempt. " +
                            "Traces of poison still cling to its rim.",
                    howObtained =
                        "Recovered the goblet from the scene of the " +
                            "poisoning attempt on the Duke.",
                    hint =
                        "The goblet is proof of the assassination attempt " +
                            "and may yield clues about the poison's source.",
                ),

            // ── Location / discovery flags ───────────────────────────────────
            "found_warehouse" to
                FlagInfo(
                    title = "Warehouse Discovered",
                    description =
                        "You have discovered a hidden warehouse used by " +
                            "the conspirators to store weapons and contraband.",
                    howObtained =
                        "Found the warehouse through investigation, " +
                            "tracking shipments, or following suspects.",
                    hint =
                        "The warehouse's location and contents are a " +
                            "strategic asset — it can be raided, watched, " +
                            "or sabotaged.",
                ),
            "found_manifests" to
                FlagInfo(
                    title = "Manifests Found",
                    description =
                        "You uncovered shipping manifests in the warehouse " +
                            "that detail the full scope of the smuggling " +
                            "operation.",
                    howObtained =
                        "Searched the warehouse thoroughly and discovered " +
                            "hidden documentation.",
                    hint =
                        "The manifests provide a timeline of operations " +
                            "and expected future shipments.",
                ),
            "found_weapons" to
                FlagInfo(
                    title = "Weapons Cache Found",
                    description =
                        "You have located a cache of weapons — far more " +
                            "than any legitimate operation would require. " +
                            "Someone is arming for war.",
                    howObtained =
                        "Discovered the weapons during a warehouse " +
                            "search or by following the smuggling trail.",
                    hint =
                        "The weapons cache proves that the conspirators " +
                            "are preparing for armed conflict. It can be " +
                            "seized, destroyed, or reported.",
                ),
            "visited_temple" to
                FlagInfo(
                    title = "Visited the Temple",
                    description =
                        "You have visited Ashenmoor's ancient temple — " +
                            "a place of fading holiness that still holds " +
                            "secrets in its weathered stones.",
                    howObtained =
                        "Made the journey to the temple district and " +
                            "explored its grounds.",
                    hint =
                        "The temple's archives and clergy may hold " +
                            "historical knowledge relevant to the conspiracy.",
                ),

            // ── Event flags ──────────────────────────────────────────────────
            "intimidating_arrival" to
                FlagInfo(
                    title = "Intimidating Arrival",
                    description =
                        "Your arrival in Ashenmoor left a strong impression. " +
                            "People remember the forceful stranger who " +
                            "demanded respect from the start.",
                    howObtained =
                        "Made a show of strength or authority upon " +
                            "arriving in Ashenmoor.",
                    hint =
                        "Your fearsome reputation precedes you — it " +
                            "opens some doors through fear but closes " +
                            "others through distrust.",
                ),
            "military_contact" to
                FlagInfo(
                    title = "Military Contact",
                    description =
                        "You have established contact with military " +
                            "personnel — soldiers who may be sympathetic " +
                            "to your cause or willing to share information.",
                    howObtained =
                        "Made connections with military figures through " +
                            "your soldier background or deliberate outreach.",
                    hint =
                        "Military contacts can provide intelligence on " +
                            "troop movements and the garrison's loyalties.",
                ),
            "survived_ambush" to
                FlagInfo(
                    title = "Ambush Survivor",
                    description =
                        "You survived a carefully planned ambush in " +
                            "Ashenmoor's dark alleys. The conspirators " +
                            "know you're a threat.",
                    howObtained = "Fought off attackers who tried to eliminate you.",
                    hint =
                        "Surviving the ambush proves you're dangerous " +
                            "enough to warrant assassination — the " +
                            "conspirators will be more cautious now.",
                ),
            "fought_thugs" to
                FlagInfo(
                    title = "Fought the Thugs",
                    description =
                        "You stood your ground against hired thugs who " +
                            "tried to intimidate or silence you through violence.",
                    howObtained = "Chose to fight when confronted by hostile thugs.",
                    hint =
                        "Word of your combat prowess spreads. Those who " +
                            "rely on thugs for muscle may think twice before " +
                            "crossing you again.",
                ),
            "turned_thug" to
                FlagInfo(
                    title = "Turned an Enforcer",
                    description =
                        "You convinced one of the conspirators' thugs to " +
                            "switch sides. A former enemy now feeds you " +
                            "information from within.",
                    howObtained =
                        "Persuaded or coerced an enemy enforcer into " +
                            "betraying their employers.",
                    hint =
                        "Your mole in the enemy's ranks can warn you " +
                            "of incoming threats and reveal plans.",
                ),
            "captured" to
                FlagInfo(
                    title = "Been Captured",
                    description =
                        "You were captured by the conspirators — held, " +
                            "interrogated, and left to ponder your fate " +
                            "in a cold cell. But you escaped.",
                    howObtained =
                        "Were overpowered or surrendered and held " +
                            "prisoner before breaking free.",
                    hint =
                        "Your captivity gave you a glimpse into the " +
                            "enemy's operations from the inside.",
                ),
            "duke_poisoned" to
                FlagInfo(
                    title = "Duke Poisoned",
                    description =
                        "The Duke has been poisoned — a grave blow to " +
                            "Ashenmoor's stability. Time is running out " +
                            "to find a cure and identify the poisoner.",
                    howObtained =
                        "Witnessed or learned about the poisoning " +
                            "of the Duke.",
                    hint =
                        "Saving the Duke is paramount. His survival " +
                            "could determine the fate of Ashenmoor.",
                ),
            "saw_duke" to
                FlagInfo(
                    title = "Saw the Duke",
                    description =
                        "You have seen the Duke of Ashenmoor — a shadow " +
                            "of his former self, weakened by poison and " +
                            "surrounded by uncertain allies.",
                    howObtained = "Gained an audience with the ailing Duke.",
                    hint =
                        "Having seen the Duke's condition firsthand, " +
                            "you understand the urgency of the situation.",
                ),
            "noble_override" to
                FlagInfo(
                    title = "Noble Authority Invoked",
                    description =
                        "You used your noble authority to override local " +
                            "officials, forcing compliance through the " +
                            "weight of your bloodline and title.",
                    howObtained =
                        "Invoked your noble privileges to bypass " +
                            "bureaucratic obstacles.",
                    hint =
                        "Noble authority is a powerful but limited " +
                            "resource — use it too often and you may " +
                            "provoke resentment.",
                ),
            "traced_funds" to
                FlagInfo(
                    title = "Funds Traced",
                    description =
                        "You have traced the flow of money through " +
                            "Ashenmoor's financial networks, identifying " +
                            "key accounts and transactions.",
                    howObtained =
                        "Investigated financial records and followed " +
                            "the money trail.",
                    hint =
                        "The financial trail leads to the conspiracy's " +
                            "backers and reveals who profits from the chaos.",
                ),
            "followed_shipment" to
                FlagInfo(
                    title = "Shipment Tracked",
                    description =
                        "You successfully tracked a suspicious shipment " +
                            "from the harbor to its destination, revealing " +
                            "a key link in the supply chain.",
                    howObtained =
                        "Followed a suspicious cargo from the docks " +
                            "to its hidden destination.",
                    hint =
                        "Knowing the shipment routes lets you plan " +
                            "interceptions or trace the network further.",
                ),
            "confronted_officer" to
                FlagInfo(
                    title = "Officer Confronted",
                    description =
                        "You confronted a corrupt officer — exposing their " +
                            "complicity in the conspiracy and forcing them " +
                            "to choose a side.",
                    howObtained =
                        "Presented evidence of corruption to the officer " +
                            "and demanded answers.",
                    hint =
                        "The confronted officer will either become a " +
                            "reluctant ally or a desperate enemy.",
                ),

            // ── Alliance / action flags ──────────────────────────────────────
            "brynn_forces" to
                FlagInfo(
                    title = "Brynn's Forces Mobilized",
                    description =
                        "Captain Brynn has mobilized her loyal guards — " +
                            "a fighting force ready to defend Ashenmoor " +
                            "against the conspirators.",
                    howObtained =
                        "Convinced Brynn to commit her guards to " +
                            "active opposition against the conspiracy.",
                    hint =
                        "Brynn's forces are your most reliable military " +
                            "asset. Deploy them wisely.",
                ),
            "brynn_guards_duke" to
                FlagInfo(
                    title = "Brynn Guards the Duke",
                    description =
                        "Captain Brynn has personally taken charge of " +
                            "the Duke's security, replacing guards whose " +
                            "loyalty is uncertain.",
                    howObtained =
                        "Persuaded Brynn to prioritize the Duke's " +
                            "protection above all else.",
                    hint =
                        "With Brynn guarding the Duke, assassination " +
                            "attempts will be far harder to execute.",
                ),
            "goran_background" to
                FlagInfo(
                    title = "Goran's Background",
                    description =
                        "You have uncovered Goran's past — his rise to " +
                            "power, the rivals he crushed, and the debts " +
                            "he owes to shadowy patrons.",
                    howObtained =
                        "Researched Goran's history through records, " +
                            "informants, or Orin's scholarship.",
                    hint =
                        "Understanding Goran's vulnerabilities may " +
                            "provide leverage in a confrontation.",
                ),
            "bluffed_goran" to
                FlagInfo(
                    title = "Bluffed Goran",
                    description =
                        "You successfully bluffed Goran into revealing " +
                            "information — convincing him you knew more " +
                            "than you actually did.",
                    howObtained =
                        "Used deception and wit to extract information " +
                            "from Goran during a conversation.",
                    hint =
                        "Goran may realize he's been played. His " +
                            "retaliation could be swift.",
                ),
            "confronted_goran" to
                FlagInfo(
                    title = "Confronted Goran",
                    description =
                        "You faced Goran directly, laying your accusations " +
                            "before him and demanding he answer for his crimes.",
                    howObtained =
                        "Confronted the guildmaster with the evidence " +
                            "you had gathered.",
                    hint =
                        "Having been confronted, Goran will either " +
                            "accelerate his plans or attempt to negotiate.",
                ),
            "goran_arrested" to
                FlagInfo(
                    title = "Goran Arrested",
                    description =
                        "Goran has been placed under arrest — the " +
                            "guildmaster's criminal empire crumbles as " +
                            "he sits in chains.",
                    howObtained =
                        "Provided sufficient evidence to have Goran " +
                            "arrested by the authorities.",
                    hint =
                        "With Goran arrested, his lieutenants may " +
                            "scatter or fight among themselves for control.",
                ),
            "goran_exposed" to
                FlagInfo(
                    title = "Goran Publicly Exposed",
                    description =
                        "Goran's crimes have been exposed to the public. " +
                            "The people of Ashenmoor now know the truth " +
                            "about their guildmaster.",
                    howObtained =
                        "Made Goran's conspiracy public knowledge " +
                            "through proclamation or evidence distribution.",
                    hint =
                        "Public exposure strips Goran of his allies " +
                            "and supporters — few will risk association " +
                            "with a known traitor.",
                ),
            "goran_warned" to
                FlagInfo(
                    title = "Goran Warned",
                    description =
                        "You warned Goran — perhaps hoping to turn him, " +
                            "or perhaps as part of a larger strategy. He " +
                            "knows you are coming.",
                    howObtained =
                        "Chose to warn Goran instead of moving against " +
                            "him immediately.",
                    hint =
                        "A warned enemy is a prepared enemy. Goran will " +
                            "have fortified his defenses and prepared " +
                            "contingencies.",
                ),
            "goran_surrendered" to
                FlagInfo(
                    title = "Goran Surrendered",
                    description =
                        "Goran has surrendered, choosing to face justice " +
                            "rather than die fighting. His cooperation " +
                            "could be valuable.",
                    howObtained =
                        "Cornered Goran and gave him the option to " +
                            "surrender peacefully.",
                    hint =
                        "A cooperative Goran can testify against his " +
                            "co-conspirators and reveal hidden assets.",
                ),
            "goran_captured" to
                FlagInfo(
                    title = "Goran Captured",
                    description =
                        "Goran has been captured by force. The " +
                            "guildmaster was taken alive after resisting " +
                            "arrest.",
                    howObtained =
                        "Captured Goran during a raid or confrontation.",
                    hint =
                        "A captured Goran can be interrogated, though " +
                            "he may be less forthcoming than if he'd " +
                            "surrendered willingly.",
                ),

            // ── Plan flags ───────────────────────────────────────────────────
            "plan_duke" to
                FlagInfo(
                    title = "Duke Protection Plan",
                    description =
                        "You have formulated a plan to protect the Duke " +
                            "from further assassination attempts while " +
                            "seeking a cure for his poisoning.",
                    howObtained =
                        "Chose to prioritize the Duke's safety in your " +
                            "strategy against the conspirators.",
                    hint =
                        "Protecting the Duke maintains legitimate " +
                            "authority in Ashenmoor and denies the " +
                            "conspirators their ultimate prize.",
                ),
            "plan_weapons" to
                FlagInfo(
                    title = "Weapons Interception Plan",
                    description =
                        "You have devised a plan to intercept or " +
                            "neutralize the weapons being smuggled into " +
                            "Ashenmoor for the coup.",
                    howObtained =
                        "Chose to target the weapons supply chain " +
                            "as your primary strategy.",
                    hint =
                        "Without weapons, the conspirators' military " +
                            "arm is crippled. But they may have " +
                            "backup stockpiles.",
                ),
            "plan_goran" to
                FlagInfo(
                    title = "Goran Takedown Plan",
                    description =
                        "You have planned a direct strike against Goran " +
                            "and his operation — cutting the head off " +
                            "the conspiracy.",
                    howObtained =
                        "Chose to target Goran directly as your " +
                            "primary strategy.",
                    hint =
                        "Removing Goran disrupts the conspiracy's " +
                            "leadership, but his underlings may continue " +
                            "the plan without him.",
                ),

            // ── Action flags ─────────────────────────────────────────────────
            "physician_restrained" to
                FlagInfo(
                    title = "Physician Restrained",
                    description =
                        "You restrained the court physician suspected of " +
                            "administering poison to the Duke, preventing " +
                            "further harm.",
                    howObtained =
                        "Acted to physically restrain the physician when " +
                            "suspicion fell on them.",
                    hint =
                        "The restrained physician can be interrogated " +
                            "about the poison and who ordered it.",
                ),
            "physician_confessed" to
                FlagInfo(
                    title = "Physician's Confession",
                    description =
                        "The court physician confessed to poisoning the " +
                            "Duke under duress, revealing they were " +
                            "blackmailed by the conspirators.",
                    howObtained =
                        "Extracted a confession from the physician " +
                            "through interrogation or persuasion.",
                    hint =
                        "The confession implicates the conspirators " +
                            "directly and may contain clues about the " +
                            "antidote.",
                ),
            "poison_swapped" to
                FlagInfo(
                    title = "Poison Swapped",
                    description =
                        "You managed to swap the poisoned substance with " +
                            "a harmless one, thwarting the assassination " +
                            "attempt before it could succeed.",
                    howObtained =
                        "Identified the poison delivery method and " +
                            "replaced it with an inert substitute.",
                    hint =
                        "The conspirators may not realize their " +
                            "poison was swapped — this gives you a " +
                            "tactical advantage.",
                ),
            "weapons_sabotaged" to
                FlagInfo(
                    title = "Weapons Sabotaged",
                    description =
                        "You sabotaged the smuggled weapons — dulling " +
                            "blades, fouling mechanisms, and ensuring " +
                            "they'll fail when the conspirators need them.",
                    howObtained =
                        "Infiltrated the weapons cache and systematically " +
                            "sabotaged the arsenal.",
                    hint =
                        "Sabotaged weapons give the enemy false " +
                            "confidence — they'll discover the betrayal " +
                            "only in the heat of battle.",
                ),
            "weapons_seized" to
                FlagInfo(
                    title = "Weapons Seized",
                    description =
                        "You seized the entire weapons cache, removing " +
                            "the conspirators' arsenal from their control.",
                    howObtained =
                        "Led a raid on the weapons stockpile and " +
                            "confiscated everything.",
                    hint =
                        "Without their weapons, the conspirators must " +
                            "find alternative means — or abandon their " +
                            "military plans.",
                ),
            "weapons_flooded" to
                FlagInfo(
                    title = "Weapons Cache Flooded",
                    description =
                        "You flooded the warehouse containing the " +
                            "weapons, ruining the entire stockpile " +
                            "beyond use.",
                    howObtained =
                        "Diverted water to flood the weapons cache, " +
                            "destroying it completely.",
                    hint =
                        "A flooded arsenal sends a strong message " +
                            "and denies the conspirators their tools of war.",
                ),
            "aldric_rescued" to
                FlagInfo(
                    title = "Aldric Rescued",
                    description =
                        "You rescued Aldric from captivity — the scholar's " +
                            "research and firsthand knowledge of the " +
                            "conspiracy are now at your disposal.",
                    howObtained =
                        "Found and freed Aldric from where the " +
                            "conspirators held him prisoner.",
                    hint =
                        "Aldric's rescued research may contain the " +
                            "final pieces of the puzzle.",
                ),
            "gate_secured" to
                FlagInfo(
                    title = "City Gate Secured",
                    description =
                        "You have secured the city gate, preventing the " +
                            "conspirators from receiving reinforcements " +
                            "or escaping Ashenmoor.",
                    howObtained =
                        "Took control of the gate through force, " +
                            "negotiation, or strategic positioning.",
                    hint =
                        "A secured gate means the conspirators are " +
                            "trapped inside. No help is coming for them.",
                ),
            "defenders_rallied" to
                FlagInfo(
                    title = "Defenders Rallied",
                    description =
                        "You rallied the people of Ashenmoor to defend " +
                            "their town. Citizens, guards, and volunteers " +
                            "stand united against the conspiracy.",
                    howObtained =
                        "Inspired the townspeople to rise up against " +
                            "the threat through speeches or actions.",
                    hint =
                        "A united town is far harder to conquer than " +
                            "a divided one. The conspirators face the " +
                            "will of the people.",
                ),
            "gate_trapped" to
                FlagInfo(
                    title = "Gate Trapped",
                    description =
                        "You set traps at the city gate, preparing " +
                            "a deadly surprise for anyone trying to " +
                            "force their way through.",
                    howObtained =
                        "Rigged the gate area with traps using " +
                            "available materials and cunning placement.",
                    hint =
                        "The traps will slow and demoralize any " +
                            "attacking force, buying precious time.",
                ),
            "champion_duel" to
                FlagInfo(
                    title = "Champion's Duel",
                    description =
                        "You challenged a champion to single combat — " +
                            "a test of honor and skill that could " +
                            "determine the outcome without mass bloodshed.",
                    howObtained =
                        "Accepted or initiated a formal duel to " +
                            "resolve a conflict.",
                    hint =
                        "Victory in a champion's duel carries enormous " +
                            "symbolic weight — it can break the enemy's " +
                            "morale entirely.",
                ),
            "called_bluff" to
                FlagInfo(
                    title = "Called Their Bluff",
                    description =
                        "You called the conspirators' bluff, exposing " +
                            "their threats as empty posturing and their " +
                            "position as weaker than they claimed.",
                    howObtained =
                        "Refused to be intimidated and exposed the " +
                            "enemy's deception.",
                    hint =
                        "Having been exposed once, the enemy's future " +
                            "bluffs carry less weight.",
                ),
            "stormed_guild" to
                FlagInfo(
                    title = "Guild Hall Stormed",
                    description =
                        "You led a direct assault on the guild hall — " +
                            "a bold strike at the heart of Goran's power.",
                    howObtained = "Attacked the guild hall with force.",
                    hint =
                        "Storming the guild is decisive but costly. " +
                            "What was found inside may justify the price.",
                ),
            "flanked_guild" to
                FlagInfo(
                    title = "Guild Flanked",
                    description =
                        "You flanked the guild hall, attacking from an " +
                            "unexpected direction and catching the " +
                            "defenders off guard.",
                    howObtained =
                        "Used tactical knowledge to attack the guild " +
                            "from a vulnerable side.",
                    hint =
                        "A flanking maneuver minimized your losses " +
                            "and maximized the element of surprise.",
                ),
            "negotiated_surrender" to
                FlagInfo(
                    title = "Surrender Negotiated",
                    description =
                        "You negotiated a surrender, ending the conflict " +
                            "without further bloodshed through diplomacy " +
                            "and persuasion.",
                    howObtained =
                        "Offered terms that the enemy found preferable " +
                            "to continued fighting.",
                    hint =
                        "A negotiated peace preserves lives and may " +
                            "yield cooperative prisoners willing to talk.",
                ),
            "saved_documents" to
                FlagInfo(
                    title = "Documents Saved",
                    description =
                        "You saved crucial documents from destruction — " +
                            "records that prove the conspiracy and identify " +
                            "all involved parties.",
                    howObtained =
                        "Rescued important papers before they could " +
                            "be burned or otherwise destroyed.",
                    hint =
                        "Preserved documents ensure that even after " +
                            "the fighting ends, justice can be served.",
                ),

            // ── Strategic flags ──────────────────────────────────────────────
            "final_assault" to
                FlagInfo(
                    title = "Final Assault Launched",
                    description =
                        "You launched the final assault against the " +
                            "conspirators — the decisive battle for " +
                            "Ashenmoor's future has begun.",
                    howObtained =
                        "Committed to the final attack after gathering " +
                            "sufficient forces and intelligence.",
                    hint =
                        "There is no turning back. Victory or defeat " +
                            "awaits at the end of this path.",
                ),
            "secure_gate" to
                FlagInfo(
                    title = "Gate Security Priority",
                    description =
                        "You prioritized securing the city gate as " +
                            "your strategic objective, controlling who " +
                            "enters and leaves Ashenmoor.",
                    howObtained =
                        "Chose gate security as your primary " +
                            "strategic focus.",
                    hint =
                        "Controlling the gate gives you leverage over " +
                            "supply lines and escape routes.",
                ),
            "assault_guild" to
                FlagInfo(
                    title = "Guild Assault Priority",
                    description =
                        "You chose to assault the guild hall as your " +
                            "primary objective — striking at Goran's " +
                            "seat of power.",
                    howObtained =
                        "Selected the guild assault as your main " +
                            "strategic approach.",
                    hint =
                        "Taking the guild cripples the conspiracy's " +
                            "command structure.",
                ),
            "defend_gate" to
                FlagInfo(
                    title = "Gate Defense Priority",
                    description =
                        "You chose to defend the city gate against " +
                            "expected enemy reinforcements or breakout " +
                            "attempts.",
                    howObtained =
                        "Prioritized a defensive position at the " +
                            "city gate.",
                    hint =
                        "A strong defense prevents enemy reinforcements " +
                            "and traps the conspirators inside.",
                ),
            "infiltrate_estate" to
                FlagInfo(
                    title = "Estate Infiltration",
                    description =
                        "You chose to infiltrate a noble estate tied " +
                            "to the conspiracy — a stealthy approach " +
                            "to gather intelligence from within.",
                    howObtained =
                        "Selected infiltration over direct assault " +
                            "as your approach.",
                    hint =
                        "Infiltration yields information that brute " +
                            "force cannot. The estate may hold secrets " +
                            "worth any risk.",
                ),

            // ── Ending flags ─────────────────────────────────────────────────
            "stayed_ashenmoor" to
                FlagInfo(
                    title = "Stayed in Ashenmoor",
                    description =
                        "After the dust settled, you chose to remain " +
                            "in Ashenmoor — to help rebuild, to protect " +
                            "what you fought for, and to find a new home.",
                    howObtained =
                        "Chose to stay when given the option to leave " +
                            "Ashenmoor behind.",
                    hint =
                        "Ashenmoor's future is your future now. The " +
                            "town will remember what you've done.",
                ),
            "departed" to
                FlagInfo(
                    title = "Departed Ashenmoor",
                    description =
                        "With the conspiracy broken, you chose to leave " +
                            "Ashenmoor — there are other places that need " +
                            "someone like you.",
                    howObtained =
                        "Chose to depart when the battle was won.",
                    hint =
                        "The road ahead is uncertain, but you carry " +
                            "the lessons of Ashenmoor with you.",
                ),
            "hunter_path" to
                FlagInfo(
                    title = "The Hunter's Path",
                    description =
                        "You have chosen the path of the hunter — " +
                            "pursuing the remnants of the conspiracy " +
                            "wherever they flee.",
                    howObtained =
                        "Decided to hunt down the escaped conspirators " +
                            "rather than settling into peace.",
                    hint =
                        "The hunter's path is lonely but necessary. " +
                            "As long as conspirators remain free, the " +
                            "threat endures.",
                ),
            "duke_recovered" to
                FlagInfo(
                    title = "Duke Recovered",
                    description =
                        "The Duke has recovered from his poisoning — " +
                            "restored to health and ready to resume " +
                            "his rule over Ashenmoor.",
                    howObtained =
                        "The antidote was found in time, and the Duke " +
                            "was treated successfully.",
                    hint =
                        "A recovered Duke can restore order and " +
                            "reward those who saved his life and his town.",
                ),
        )

    /** Look up a flag by its ID. Returns null if the flag is not in the registry. */
    fun lookup(flagId: String): FlagInfo? = entries[flagId]

    /** Return a formatted display title for a flag, using the registry or a fallback. */
    fun displayTitle(flagId: String): String {
        return entries[flagId]?.title
            ?: flagId.replace("_", " ").replaceFirstChar { it.uppercase() }
    }
}
