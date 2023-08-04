# Command API

![CI](https://github.com/The-Coding-Mods/command-api/actions/workflows/ci.yaml/badge.svg)
![Code QL](https://github.com/The-Coding-Mods/command-api/actions/workflows/codeql-analysis.yaml/badge.svg)

This project creates some quality of life api endpoints for Trackmania related information

## Endpoints

### /cotd/position

#### Parameter:

- query: `playerId`

#### Description:

Retrieve the position of the provided player during the last run Cup of the Day (COTD).

---

### /cotd/winner

#### Parameter:

None

#### Description:

Retrieve the winner of the last run COTD.

---

### /followage/{channel}/{user}

#### Parameter:

- path: `channel`
- path: `user`

#### Description:

Retrieves how long `user` has followed `channel`.

---

### /match-making/{playerId}/rank

#### Parameter:

- path: `playerId`

#### Description:

Retrieves the current match making rank of the player.

---

### /match-making/{playerId}/points

#### Parameter:

- path: `playerId`

#### Description:

Retrieves the current match making points of the player.
