# Speak No Blocks

**Speak No Blocks** is a Spigot/Paper plugin that allows external applications to interact with the Minecraft world in real-time via HTTP.

It embeds a lightweight web server directly into the Minecraft server, listening for commands to destroy specific block types around players. It was primarily designed to work with a [local offline voice recognition client](./client) to allow players to mine blocks simply by saying their names.

## Installation

1. Build the project (or download the latest release).
2. Drop the `.jar` file into your server's `plugins/` folder.
3. Restart the server.
4. The config file will be generated at `plugins/SpeakNoBlocks/config.yml`.

## Configuration

You can configure the web server port and the mining radius in `config.yml`:

```yaml
# The port the embedded HTTP server listens on
server-port: 8080

# The radius (in blocks) around the player to search for blocks
radius: 16

```

## Commands

The main command is `/speaknoblocks` (alias `/snb`).

| Command | Description |
| --- | --- |
| `/snb start` | Starts the HTTP listener. |
| `/snb stop` | Stops the HTTP listener. |
| `/snb reload` | Reloads the configuration and restarts the listener. |

## API Reference

If you want to build your own client or integrate with other tools (Stream Deck, Discord bot, etc.), you can send requests directly to the plugin.

### Endpoint: `/break`

* **Method:** `POST`
* **URL:** `http://<server-ip>:<port>/break`
* **Content-Type:** `application/json`

#### Payload

```json
{
  "user": "yorunoken",
  "blocks": [
    "minecraft:stone",
    "minecraft:dort"
  ]
}

```

* **user**: (String) The identifier of the user initiating the command (currently used for logging).
* **blocks**: (Array) A list of Bukkit `Material` names to destroy.

#### Response

* `200 OK`: `{"status": "success"}`
* `400 Bad Request`: JSON missing fields.
* `405 Method Not Allowed`: Used a non-POST method.

## Python Client

This plugin is designed to be used with the **Voice Client** included in this repository. The client uses an offline Vosk model to listen for block names and automatically sends the API requests to this plugin.

**[> Go to Client Documentation](./client)**

## Building from Source

This is a standard Maven project.

```bash
git clone https://github.com/yorunoken/speak-no-blocks.git
cd speak-no-blocks
mvn clean package

```

The resulting jar will be in the `target/` directory.