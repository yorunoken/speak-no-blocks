import json
import sys
import os
import pyaudio
from vosk import Model, KaldiRecognizer
import urllib.request
import dotenv
import re

# Load environment variables
dotenv.load_dotenv()

# --- Configuration Checks ---
OUTGOING_SERVER = os.getenv("OUTGOING_SERVER")
USERNAME = os.getenv("USERNAME")

if not OUTGOING_SERVER:
    print("ERROR: OUTGOING_SERVER not set in .env")
    sys.exit(1)
if not USERNAME:
    print("ERROR: USERNAME not set in .env")
    sys.exit(1)

FLAG_WORDS = [
    # --- COMMON TERRAIN ---
    {"word": ["grass", "dirt"], "blocks": ["minecraft:grass_block", "minecraft:dirt", "minecraft:coarse_dirt", "minecraft:podzol", "minecraft:mycelium", "minecraft:rooted_dirt", "minecraft:dirt_path", "minecraft:farmland"]},
    {"word": ["mud", "blood"], "blocks": ["minecraft:mud", "minecraft:packed_mud", "minecraft:mud_bricks"]},
    {"word": ["clay", "play"], "blocks": ["minecraft:clay"]},
    {"word": ["sand", "send"], "blocks": ["minecraft:sand", "minecraft:red_sand", "minecraft:sandstone", "minecraft:red_sandstone"]},
    {"word": ["gravel", "travel"], "blocks": ["minecraft:gravel"]},
    
    # --- STONE VARIANTS ---
    {"word": ["stone", "cobble"], "blocks": ["minecraft:stone", "minecraft:cobblestone", "minecraft:mossy_cobblestone", "minecraft:smooth_stone", "minecraft:andesite", "minecraft:polished_andesite"]},
    {"word": ["granite", "planet", "granted"], "blocks": ["minecraft:granite", "minecraft:polished_granite"]},
    {"word": ["diorite", "die right", "die or i"], "blocks": ["minecraft:diorite", "minecraft:polished_diorite"]},
    {"word": ["andesite", "and the sight", "and a sight", "and the site", "and aside"], "blocks": ["minecraft:andesite", "minecraft:polished_andesite"]},
    
    {"word": ["deepslate", "deep slate", "deep sleep", "deep slight"], "blocks": ["minecraft:deepslate"]}, 
    {"word": ["tuff", "tough"], "blocks": ["minecraft:tuff"]},
    {"word": ["calcite", "cow sight", "call sight"], "blocks": ["minecraft:calcite"]},
    {"word": ["dripstone", "drip stone", "drop stone"], "blocks": ["minecraft:dripstone_block", "minecraft:pointed_dripstone"]},
    {"word": ["amethyst", "crystal"], "blocks": ["minecraft:amethyst_block", "minecraft:budding_amethyst", "minecraft:amethyst_cluster"]},
    {"word": ["bedrock"], "blocks": ["minecraft:bedrock"]},

    # --- LIQUIDS & ICE ---
    {"word": ["water"], "blocks": ["minecraft:water"]},
    {"word": ["lava", "larva"], "blocks": ["minecraft:lava"]},
    {"word": ["ice"], "blocks": ["minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice"]},
    {"word": ["snow"], "blocks": ["minecraft:snow_block", "minecraft:snow", "minecraft:powder_snow"]},

    # --- VEGETATION & WOOD ---
    {"word": ["log", "wood", "would", "tree"], "blocks": ["minecraft:oak_log", "minecraft:spruce_log", "minecraft:birch_log", "minecraft:jungle_log", "minecraft:acacia_log", "minecraft:dark_oak_log", "minecraft:mangrove_log", "minecraft:cherry_log"]},
    {"word": ["planks", "plank", "pranks"], "blocks": ["minecraft:oak_planks", "minecraft:spruce_planks", "minecraft:birch_planks", "minecraft:jungle_planks", "minecraft:acacia_planks", "minecraft:dark_oak_planks", "minecraft:mangrove_planks", "minecraft:cherry_planks", "minecraft:bamboo_planks"]},
    {"word": ["leaves", "leafs", "leaf"], "blocks": ["minecraft:oak_leaves", "minecraft:spruce_leaves", "minecraft:birch_leaves", "minecraft:jungle_leaves", "minecraft:acacia_leaves", "minecraft:dark_oak_leaves", "minecraft:mangrove_leaves", "minecraft:cherry_leaves", "minecraft:azalea_leaves"]},
    {"word": ["moss"], "blocks": ["minecraft:moss_block", "minecraft:moss_carpet"]},

    # --- MAN-MADE / DECORATION ---
    {"word": ["glass", "class", "glance"], "blocks": ["minecraft:glass", "minecraft:glass_pane", "minecraft:tinted_glass"]},
    {"word": ["wool", "wall", "will"], "blocks": ["minecraft:white_wool", "minecraft:orange_wool", "minecraft:magenta_wool", "minecraft:light_blue_wool", "minecraft:yellow_wool", "minecraft:lime_wool", "minecraft:pink_wool", "minecraft:gray_wool", "minecraft:light_gray_wool", "minecraft:cyan_wool", "minecraft:purple_wool", "minecraft:blue_wool", "minecraft:brown_wool", "minecraft:green_wool", "minecraft:red_wool", "minecraft:black_wool"]},
    {"word": ["obsidian"], "blocks": ["minecraft:obsidian", "minecraft:crying_obsidian"]},

    # --- ORES ---
    {"word": ["coal", "goal", "call", "cole"], "blocks": ["minecraft:coal_ore", "minecraft:deepslate_coal_ore", "minecraft:coal_block"]},
    {"word": ["iron", "iran"], "blocks": ["minecraft:iron_ore", "minecraft:deepslate_iron_ore", "minecraft:raw_iron_block"]},
    {"word": ["copper"], "blocks": ["minecraft:copper_ore", "minecraft:deepslate_copper_ore", "minecraft:raw_copper_block"]},
    {"word": ["gold", "cold"], "blocks": ["minecraft:gold_ore", "minecraft:deepslate_gold_ore", "minecraft:nether_gold_ore", "minecraft:raw_gold_block"]},
    {"word": ["diamond", "die mond"], "blocks": ["minecraft:diamond_ore", "minecraft:deepslate_diamond_ore", "minecraft:diamond_block"]},
    {"word": ["emerald"], "blocks": ["minecraft:emerald_ore", "minecraft:deepslate_emerald_ore", "minecraft:emerald_block"]},
    {"word": ["lapis"], "blocks": ["minecraft:lapis_ore", "minecraft:deepslate_lapis_ore", "minecraft:lapis_block"]},
    {"word": ["redstone"], "blocks": ["minecraft:redstone_ore", "minecraft:deepslate_redstone_ore", "minecraft:redstone_block"]},
    {"word": ["quartz", "quarts", "courts"], "blocks": ["minecraft:nether_quartz_ore", "minecraft:quartz_block"]},
    {"word": ["debris", "ancient", "day bree"], "blocks": ["minecraft:ancient_debris"]},
    
    # --- NETHER ---
    {"word": ["nether", "netherrack", "neither rack", "nether rack", "neither"], "blocks": ["minecraft:netherrack"]},
    {"word": ["soul", "sole", "sold"], "blocks": ["minecraft:soul_sand", "minecraft:soul_soil"]},
    {"word": ["basalt", "bath salt"], "blocks": ["minecraft:basalt", "minecraft:polished_basalt", "minecraft:smooth_basalt"]},
    {"word": ["blackstone", "black stone"], "blocks": ["minecraft:blackstone", "minecraft:polished_blackstone", "minecraft:gilded_blackstone"]},
    {"word": ["glowstone", "glow stone"], "blocks": ["minecraft:glowstone", "minecraft:shroomlight"]},
    {"word": ["magma", "magma block"], "blocks": ["minecraft:magma_block"]},
    {"word": ["crimson", "fungus"], "blocks": ["minecraft:crimson_nylium", "minecraft:crimson_stem", "minecraft:nether_wart_block"]},
    {"word": ["warped", "fungus", "wrapped"], "blocks": ["minecraft:warped_nylium", "minecraft:warped_stem", "minecraft:warped_wart_block"]},

    # --- END ---
    {"word": ["end", "endstone", "end stone", "and stone", "friendship", "friend", "and"], "blocks": ["minecraft:end_stone", "minecraft:end_stone_bricks"]},
    {"word": ["purpur", "purple", "pepper"], "blocks": ["minecraft:purpur_block", "minecraft:purpur_pillar"]},
    {"word": ["chorus", "course"], "blocks": ["minecraft:chorus_plant", "minecraft:chorus_flower"]},

    # --- UTILITY & WORKSTATIONS ---
    {"word": ["torch", "touch", "porch"], "blocks": ["minecraft:torch", "minecraft:wall_torch", "minecraft:soul_torch", "minecraft:soul_wall_torch"]},
    {"word": ["lantern", "light"], "blocks": ["minecraft:lantern", "minecraft:soul_lantern", "minecraft:sea_lantern"]},
    {"word": ["chest", "just", "guest", "box"], "blocks": ["minecraft:chest", "minecraft:trapped_chest", "minecraft:ender_chest", "minecraft:barrel", "minecraft:shulker_box"]},
    {"word": ["furnace", "oven", "burn", "stove"], "blocks": ["minecraft:furnace", "minecraft:blast_furnace", "minecraft:smoker"]},
    {"word": ["table", "crafting", "craft"], "blocks": ["minecraft:crafting_table", "minecraft:fletching_table", "minecraft:smithing_table", "minecraft:cartography_table"]},
    {"word": ["anvil", "and will"], "blocks": ["minecraft:anvil", "minecraft:chipped_anvil", "minecraft:damaged_anvil"]},
    {"word": ["bed", "sleep", "bad"], "blocks": ["minecraft:white_bed", "minecraft:red_bed", "minecraft:black_bed", "minecraft:blue_bed", "minecraft:brown_bed", "minecraft:cyan_bed", "minecraft:gray_bed", "minecraft:green_bed", "minecraft:light_blue_bed", "minecraft:light_gray_bed", "minecraft:lime_bed", "minecraft:magenta_bed", "minecraft:orange_bed", "minecraft:pink_bed", "minecraft:purple_bed", "minecraft:yellow_bed"]},
    {"word": ["bookshelf", "book", "shelf"], "blocks": ["minecraft:bookshelf", "minecraft:chiseled_bookshelf"]},

    # --- REDSTONE & MECHANISMS ---
    {"word": ["piston"], "blocks": ["minecraft:piston", "minecraft:sticky_piston"]},
    {"word": ["observer", "observe"], "blocks": ["minecraft:observer"]},
    {"word": ["dispenser", "dropper"], "blocks": ["minecraft:dispenser", "minecraft:dropper"]},
    {"word": ["hopper", "hope", "hop"], "blocks": ["minecraft:hopper"]},
    {"word": ["rail", "track", "train"], "blocks": ["minecraft:rail", "minecraft:powered_rail", "minecraft:detector_rail", "minecraft:activator_rail"]},
    {"word": ["tnt"], "blocks": ["minecraft:tnt"]},

    # --- OCEAN / UNDERWATER ---
    {"word": ["prismarine", "prison", "marine"], "blocks": ["minecraft:prismarine", "minecraft:prismarine_bricks", "minecraft:dark_prismarine"]},
    {"word": ["sponge", "spunge"], "blocks": ["minecraft:sponge", "minecraft:wet_sponge"]},
    {"word": ["coral"], "blocks": ["minecraft:brain_coral_block", "minecraft:bubble_coral_block", "minecraft:fire_coral_block", "minecraft:horn_coral_block", "minecraft:tube_coral_block"]},
    {"word": ["kelp", "help", "seaweed"], "blocks": ["minecraft:kelp", "minecraft:kelp_plant", "minecraft:dried_kelp_block"]},

    # --- FARMING & PLANTS ---
    {"word": ["pumpkin", "punk in"], "blocks": ["minecraft:pumpkin", "minecraft:carved_pumpkin", "minecraft:jack_o_lantern"]},
    {"word": ["melon", "watermelon"], "blocks": ["minecraft:melon"]},
    {"word": ["hay", "wheat", "hey"], "blocks": ["minecraft:hay_block"]},
    {"word": ["cactus"], "blocks": ["minecraft:cactus"]},
    {"word": ["bamboo"], "blocks": ["minecraft:bamboo"]},
    {"word": ["sugar", "cane", "sugarcane"], "blocks": ["minecraft:sugar_cane"]},

    # --- BUILDING BLOCKS ---
    {"word": ["brick", "bricks"], "blocks": ["minecraft:bricks", "minecraft:stone_bricks", "minecraft:nether_bricks", "minecraft:mud_bricks"]},
    {"word": ["terracotta", "terra", "cotta"], "blocks": ["minecraft:terracotta", "minecraft:white_terracotta", "minecraft:orange_terracotta", "minecraft:magenta_terracotta", "minecraft:light_blue_terracotta", "minecraft:yellow_terracotta", "minecraft:lime_terracotta", "minecraft:pink_terracotta", "minecraft:gray_terracotta", "minecraft:light_gray_terracotta", "minecraft:cyan_terracotta", "minecraft:purple_terracotta", "minecraft:blue_terracotta", "minecraft:brown_terracotta", "minecraft:green_terracotta", "minecraft:red_terracotta", "minecraft:black_terracotta"]},
    {"word": ["concrete", "cement"], "blocks": ["minecraft:white_concrete", "minecraft:orange_concrete", "minecraft:magenta_concrete", "minecraft:light_blue_concrete", "minecraft:yellow_concrete", "minecraft:lime_concrete", "minecraft:pink_concrete", "minecraft:gray_concrete", "minecraft:light_gray_concrete", "minecraft:cyan_concrete", "minecraft:purple_concrete", "minecraft:blue_concrete", "minecraft:brown_concrete", "minecraft:green_concrete", "minecraft:red_concrete", "minecraft:black_concrete"]},
]

def break_block(targeted_blocks):
    """Sends the HTTP request to the bot server."""
    print(f" -> SENDING: {targeted_blocks}...")
    
    data = {"user": USERNAME, "blocks": list(set(targeted_blocks))} 
    json_data = json.dumps(data).encode("utf-8")
    url = f"{OUTGOING_SERVER}/break"
    
    print(f" -> Sending command to {url} for user {USERNAME}...")
    
    try:
        req = urllib.request.Request(
            url, 
            data=json_data, 
            headers={"Content-Type": "application/json"}
        )
        urllib.request.urlopen(req)
        print(" -> Request success.")
    except Exception as e:
        print(f" -> Request FAILED: {e}")

def listen_loop():
    if not os.path.exists("model"):
        print("ERROR: Model folder not found. Please download 'vosk-model-small-en-us' and unpack it as a folder named 'model'.")
        sys.exit(1)

    # valid_words = ["[unk]"] 
    # for entry in FLAG_WORDS:
    #     valid_words.extend(entry["word"])
    # vocab_str = json.dumps(list(set(valid_words)))
    
    model = Model("model")
    rec = KaldiRecognizer(model, 16000)

    p = pyaudio.PyAudio()
    stream = p.open(format=pyaudio.paInt16, channels=1, rate=16000, input=True, frames_per_buffer=4000)
    stream.start_stream()

    print(f"Microphone opened successfully.")

    print("------------------------------------------------")
    print(f" LISTENING FOR FLAGS")
    print(" (Press Ctrl+C to stop)")
    print("------------------------------------------------")

    SORTED_CHECKS = []
    for entry in FLAG_WORDS:
        for keyword in entry["word"]:
            SORTED_CHECKS.append((keyword, entry["blocks"]))
    SORTED_CHECKS.sort(key=lambda x: len(x[0]), reverse=True)

    while True:
        data = stream.read(2000, exception_on_overflow=False)
        rec.AcceptWaveform(data)

        partial = json.loads(rec.PartialResult())
        text = partial.get("partial", "")

        if text:
            best_match = None
            earliest_pos = float("inf")

            for keyword, blocks in SORTED_CHECKS:
                pattern = r"\b" + re.escape(keyword) + r"\b"
                match = re.search(pattern, text)

                if match:
                    if match.start() < earliest_pos:
                        earliest_pos = match.start()
                        best_match = (keyword, blocks)
                
            if best_match:
                keyword, blocks = best_match
                print(f"Heard: '{text}' -> Matched: '{keyword}'")
                break_block(targeted_blocks=blocks)
                rec.Reset()

                
if __name__ == "__main__":
    try:
        listen_loop()
    except KeyboardInterrupt:
        print("\nStopping...")
        sys.exit()