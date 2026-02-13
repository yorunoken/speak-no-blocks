package com.yorunoken.speakNoBlocks.client.util;

import java.util.*;

public class VoiceDefinitions {
    public static final Map<String, List<String>> COMMANDS = new HashMap<>();

    static {
        // --- COMMON TERRAIN ---
        register(List.of("grass", "dirt"), "minecraft:grass_block", "minecraft:dirt", "minecraft:coarse_dirt", "minecraft:podzol", "minecraft:mycelium", "minecraft:rooted_dirt", "minecraft:dirt_path", "minecraft:farmland");
        register(List.of("mud", "blood"), "minecraft:mud", "minecraft:packed_mud", "minecraft:mud_bricks");
        register(List.of("clay", "play"), "minecraft:clay");
        register(List.of("sand", "send"), "minecraft:sand", "minecraft:red_sand", "minecraft:sandstone", "minecraft:red_sandstone");
        register(List.of("gravel", "travel"), "minecraft:gravel");

        // --- STONE VARIANTS ---
        register(List.of("stone", "cobble"), "minecraft:stone", "minecraft:cobblestone", "minecraft:mossy_cobblestone", "minecraft:smooth_stone", "minecraft:andesite", "minecraft:polished_andesite");
        register(List.of("granite", "planet", "granted"), "minecraft:granite", "minecraft:polished_granite");
        register(List.of("diorite", "die right", "die or i"), "minecraft:diorite", "minecraft:polished_diorite");
        register(List.of("andesite", "and the sight", "and a sight", "and the site", "and aside"), "minecraft:andesite", "minecraft:polished_andesite");
        register(List.of("deepslate", "deep slate", "deep sleep", "deep slight"), "minecraft:deepslate");
        register(List.of("tuff", "tough"), "minecraft:tuff");
        register(List.of("calcite", "cow sight", "call sight"), "minecraft:calcite");
        register(List.of("dripstone", "drip stone", "drop stone"), "minecraft:dripstone_block", "minecraft:pointed_dripstone");
        register(List.of("amethyst", "crystal"), "minecraft:amethyst_block", "minecraft:budding_amethyst", "minecraft:amethyst_cluster");
        register(List.of("bedrock"), "minecraft:bedrock");

        // --- LIQUIDS & ICE ---
        register(List.of("water"), "minecraft:water");
        register(List.of("lava", "larva"), "minecraft:lava");
        register(List.of("ice"), "minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice");
        register(List.of("snow"), "minecraft:snow_block", "minecraft:snow", "minecraft:powder_snow");

        // --- VEGETATION & WOOD ---
        register(List.of("log", "wood", "would", "tree"), "minecraft:oak_log", "minecraft:spruce_log", "minecraft:birch_log", "minecraft:jungle_log", "minecraft:acacia_log", "minecraft:dark_oak_log", "minecraft:mangrove_log", "minecraft:cherry_log");
        register(List.of("planks", "plank", "pranks"), "minecraft:oak_planks", "minecraft:spruce_planks", "minecraft:birch_planks", "minecraft:jungle_planks", "minecraft:acacia_planks", "minecraft:dark_oak_planks", "minecraft:mangrove_planks", "minecraft:cherry_planks", "minecraft:bamboo_planks");
        register(List.of("leaves", "leafs", "leaf"), "minecraft:oak_leaves", "minecraft:spruce_leaves", "minecraft:birch_leaves", "minecraft:jungle_leaves", "minecraft:acacia_leaves", "minecraft:dark_oak_leaves", "minecraft:mangrove_leaves", "minecraft:cherry_leaves", "minecraft:azalea_leaves");
        register(List.of("moss"), "minecraft:moss_block", "minecraft:moss_carpet");

        // --- MAN-MADE / DECORATION ---
        register(List.of("glass", "class", "glance"), "minecraft:glass", "minecraft:glass_pane", "minecraft:tinted_glass");
        register(List.of("wool", "wall", "will"), "minecraft:white_wool", "minecraft:orange_wool", "minecraft:magenta_wool", "minecraft:light_blue_wool", "minecraft:yellow_wool", "minecraft:lime_wool", "minecraft:pink_wool", "minecraft:gray_wool", "minecraft:light_gray_wool", "minecraft:cyan_wool", "minecraft:purple_wool", "minecraft:blue_wool", "minecraft:brown_wool", "minecraft:green_wool", "minecraft:red_wool", "minecraft:black_wool");
        register(List.of("obsidian"), "minecraft:obsidian", "minecraft:crying_obsidian");

        // --- ORES ---
        register(List.of("coal", "goal", "call", "cole"), "minecraft:coal_ore", "minecraft:deepslate_coal_ore", "minecraft:coal_block");
        register(List.of("iron", "iran"), "minecraft:iron_ore", "minecraft:deepslate_iron_ore", "minecraft:raw_iron_block");
        register(List.of("copper"), "minecraft:copper_ore", "minecraft:deepslate_copper_ore", "minecraft:raw_copper_block");
        register(List.of("gold", "cold"), "minecraft:gold_ore", "minecraft:deepslate_gold_ore", "minecraft:nether_gold_ore", "minecraft:raw_gold_block");
        register(List.of("diamond", "die mond"), "minecraft:diamond_ore", "minecraft:deepslate_diamond_ore", "minecraft:diamond_block");
        register(List.of("emerald"), "minecraft:emerald_ore", "minecraft:deepslate_emerald_ore", "minecraft:emerald_block");
        register(List.of("lapis"), "minecraft:lapis_ore", "minecraft:deepslate_lapis_ore", "minecraft:lapis_block");
        register(List.of("redstone"), "minecraft:redstone_ore", "minecraft:deepslate_redstone_ore", "minecraft:redstone_block");
        register(List.of("quartz", "quarts", "courts"), "minecraft:nether_quartz_ore", "minecraft:quartz_block");
        register(List.of("debris", "ancient", "day bree"), "minecraft:ancient_debris");

        // --- NETHER ---
        register(List.of("nether", "netherrack", "neither rack", "nether rack", "neither"), "minecraft:netherrack");
        register(List.of("soul", "sole", "sold"), "minecraft:soul_sand", "minecraft:soul_soil");
        register(List.of("basalt", "bath salt"), "minecraft:basalt", "minecraft:polished_basalt", "minecraft:smooth_basalt");
        register(List.of("blackstone", "black stone"), "minecraft:blackstone", "minecraft:polished_blackstone", "minecraft:gilded_blackstone");
        register(List.of("glowstone", "glow stone"), "minecraft:glowstone", "minecraft:shroomlight");
        register(List.of("magma", "magma block"), "minecraft:magma_block");
        register(List.of("crimson", "fungus"), "minecraft:crimson_nylium", "minecraft:crimson_stem", "minecraft:nether_wart_block");
        register(List.of("warped", "fungus", "wrapped"), "minecraft:warped_nylium", "minecraft:warped_stem", "minecraft:warped_wart_block");

        // --- END ---
        register(List.of("end", "endstone", "end stone", "and stone", "friendship", "friend", "and"), "minecraft:end_stone", "minecraft:end_stone_bricks");
        register(List.of("purpur", "purple", "pepper"), "minecraft:purpur_block", "minecraft:purpur_pillar");
        register(List.of("chorus", "course"), "minecraft:chorus_plant", "minecraft:chorus_flower");

        // --- UTILITY & WORKSTATIONS ---
        register(List.of("torch", "touch", "porch"), "minecraft:torch", "minecraft:wall_torch", "minecraft:soul_torch", "minecraft:soul_wall_torch");
        register(List.of("lantern", "light"), "minecraft:lantern", "minecraft:soul_lantern", "minecraft:sea_lantern");
        register(List.of("chest", "just", "guest", "box"), "minecraft:chest", "minecraft:trapped_chest", "minecraft:ender_chest", "minecraft:barrel", "minecraft:shulker_box");
        register(List.of("furnace", "oven", "burn", "stove"), "minecraft:furnace", "minecraft:blast_furnace", "minecraft:smoker");
        register(List.of("table", "crafting", "craft"), "minecraft:crafting_table", "minecraft:fletching_table", "minecraft:smithing_table", "minecraft:cartography_table");
        register(List.of("anvil", "and will"), "minecraft:anvil", "minecraft:chipped_anvil", "minecraft:damaged_anvil");
        register(List.of("bed", "sleep", "bad"), "minecraft:white_bed", "minecraft:red_bed", "minecraft:black_bed", "minecraft:blue_bed", "minecraft:brown_bed", "minecraft:cyan_bed", "minecraft:gray_bed", "minecraft:green_bed", "minecraft:light_blue_bed", "minecraft:light_gray_bed", "minecraft:lime_bed", "minecraft:magenta_bed", "minecraft:orange_bed", "minecraft:pink_bed", "minecraft:purple_bed", "minecraft:yellow_bed");
        register(List.of("bookshelf", "book", "shelf"), "minecraft:bookshelf", "minecraft:chiseled_bookshelf");

        // --- REDSTONE & MECHANISMS ---
        register(List.of("piston"), "minecraft:piston", "minecraft:sticky_piston");
        register(List.of("observer", "observe"), "minecraft:observer");
        register(List.of("dispenser", "dropper"), "minecraft:dispenser", "minecraft:dropper");
        register(List.of("hopper", "hope", "hop"), "minecraft:hopper");
        register(List.of("rail", "track", "train"), "minecraft:rail", "minecraft:powered_rail", "minecraft:detector_rail", "minecraft:activator_rail");
        register(List.of("tnt"), "minecraft:tnt");

        // --- OCEAN / UNDERWATER ---
        register(List.of("prismarine", "prison", "marine"), "minecraft:prismarine", "minecraft:prismarine_bricks", "minecraft:dark_prismarine");
        register(List.of("sponge", "spunge"), "minecraft:sponge", "minecraft:wet_sponge");
        register(List.of("coral"), "minecraft:brain_coral_block", "minecraft:bubble_coral_block", "minecraft:fire_coral_block", "minecraft:horn_coral_block", "minecraft:tube_coral_block");
        register(List.of("kelp", "help", "seaweed"), "minecraft:kelp", "minecraft:kelp_plant", "minecraft:dried_kelp_block");

        // --- FARMING & PLANTS ---
        register(List.of("pumpkin", "punk in"), "minecraft:pumpkin", "minecraft:carved_pumpkin", "minecraft:jack_o_lantern");
        register(List.of("melon", "watermelon"), "minecraft:melon");
        register(List.of("hay", "wheat", "hey"), "minecraft:hay_block");
        register(List.of("cactus"), "minecraft:cactus");
        register(List.of("bamboo"), "minecraft:bamboo");
        register(List.of("sugar", "cane", "sugarcane"), "minecraft:sugar_cane");

        // --- BUILDING BLOCKS ---
        register(List.of("brick", "bricks"), "minecraft:bricks", "minecraft:stone_bricks", "minecraft:nether_bricks", "minecraft:mud_bricks");
        register(List.of("terracotta", "terra", "cotta"), "minecraft:terracotta", "minecraft:white_terracotta", "minecraft:orange_terracotta", "minecraft:magenta_terracotta", "minecraft:light_blue_terracotta", "minecraft:yellow_terracotta", "minecraft:lime_terracotta", "minecraft:pink_terracotta", "minecraft:gray_terracotta", "minecraft:light_gray_terracotta", "minecraft:cyan_terracotta", "minecraft:purple_terracotta", "minecraft:blue_terracotta", "minecraft:brown_terracotta", "minecraft:green_terracotta", "minecraft:red_terracotta", "minecraft:black_terracotta");
        register(List.of("concrete", "cement"), "minecraft:white_concrete", "minecraft:orange_concrete", "minecraft:magenta_concrete", "minecraft:light_blue_concrete", "minecraft:yellow_concrete", "minecraft:lime_concrete", "minecraft:pink_concrete", "minecraft:gray_concrete", "minecraft:light_gray_concrete", "minecraft:cyan_concrete", "minecraft:purple_concrete", "minecraft:blue_concrete", "minecraft:brown_concrete", "minecraft:green_concrete", "minecraft:red_concrete", "minecraft:black_concrete");
    }

    private static void register(List<String> triggers, String... blockIds) {
        List<String> blockList = Arrays.asList(blockIds);
        for (String trigger : triggers) {
            COMMANDS.put(trigger.toLowerCase(), blockList);
        }
    }
}