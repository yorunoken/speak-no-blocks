# Speak No Blocks

A Fabric mod that allows players to destroy specific blocks using their voice!

## Installation

### Requirements
- **Minecraft**: 1.21
- **Loader**: Fabric Loader
- **Dependencies**: 
    - Fabric API
    - **Mod Menu** (Required to access the model downloader)

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