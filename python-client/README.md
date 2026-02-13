The python client that listens for block names and sends HTTP requests to the plugin.

It uses **Vosk** with a 1.8GB local model. It's offline, private, and most importantly, it's instant.

## Setup

**Windows**
Just run `setup.bat`. It creates the venv, installs dependencies, and downloads the model (1.8GB) automatically.

**Linux / Mac**

1. Install `python-pyaudio` (via apt/pacman/brew).
2. Standard venv setup:

    ```bash
    python -m venv venv
    source venv/bin/activate
    pip install -r requirements.txt
    ```

3. Download `vosk-model-en-us-0.22` from [alphacephei.com](https://alphacephei.com/vosk/models) and unzip it as a folder named `model`.

## Config

Rename `.env.example` to `.env` (or let the script generate it) and set your server details:

```ini
OUTGOING_SERVER=http://localhost:3000
USERNAME=Steve
```

## Usage

1. Run `run.bat` (Windows) or `python listen.py` (Linux).
2. Wait for it to say **LISTENING**.
3. Say a block name (e.g., "Stone", "Diamond Ore").

The script filters for specific keywords defined in `FLAG_WORDS` inside `listen.py`. If it hears one, it POSTs to `/break`.

## Adding Words

Open `listen.py` and add entries to the `FLAG_WORDS` list. The script automatically rebuilds the grammar on restart.
