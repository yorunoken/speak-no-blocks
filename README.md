# Speak No Blocks

This is a Fabric mod I cooked up in a span of a few days

I originally saw this concept on a few YouTube videos, but none of them had any way to install them, so I made this!

It uses a local offline AI model to process your voice, so no data leaves your computer. It’s fast, private, and works even if your internet dies (unless you were connected to a server).

Enjoy! =)

## Installation

Grab the latest `.jar` from [Releases](https://github.com/yorunoken/speak-no-blocks/releases/tag/v1.0) or [Modrinth](https://google.com).

### Requirements

I coded this to work for 1.21 because I have no idea how to do versioning

You just need `Fabric API` and `Mod Menu` as dependencies (mod menu is for downloading the local AI model)

### Setup Guide
This mod must be installed on **both the Client and the Server**.

1. **Install the Mod**: 
   - Drop the `.jar` into the `mods` folder on both Client and Server.
   
2. **Install the AI Model (Client Only)**:
   > **Critical Step:** The mod will not work without the voice recognition model (approx. 1.8 GB).
   
   - Launch Minecraft and go to the main menu.
   - Click the **Mods** button.
   - Find **Speak No Blocks** in the list.
   - Click the **Config** (or Settings) button.
   - Click **"Download Model"** and wait for the 1.8GB download to finish.
   
3. **Join Server**: Once the download is complete, join the server and start speaking!

## Configuration

The config file is located at `config/speak-no-blocks.json`.

```json
{
  "radius": 5,
  "breakBedrock": false,
  "blacklist": [
    "minecraft:air",
    "minecraft:barrier"
  ]
}

```

## Usage

1. Ensure your microphone is set as the default input device in your OS.
2. Join the server.
3. Speak the name of a block clearly (e.g., "Oak Log").
4. Valid blocks within the radius will be destroyed.

## Developer Info

### Building from Source

```bash
# Linux / Mac
./gradlew build

# Windows
gradlew build

```

## License

MIT