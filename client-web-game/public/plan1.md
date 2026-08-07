# Advance Opad Web Client to Playable Multiplayer

## Goal

Bring `client-web-game` from a partial Three.js scaffold to a **real peer** of the Java multiplayer game `one.empty3.apps.opad`: join the TCP server (via the existing WS proxy), move on the shared map, see other players, pick bonuses, and show scores — matching the wire protocol and coordinate system of the Java client.

## Current state

| Piece | Status |
| --- | --- |
| Vite + Three.js scene | Working scaffold |
| `ground.js` SolPlan mesh | Present |
| `proxy.js` WS→TCP bridge | Present (`8080` → `4712`) |
| `protocol.ts` message types | Stubbed |
| `GameScene` movement / render | Partial, several bugs |
| JOIN handshake | **Missing** — server never assigns an id |
| Message handling | Only partial `welcome` / `state` |
| Bonus pick / game over | **Missing** |
| Map switch from `mapName` | **Missing** (hardcoded SolPlan) |
| Scale / angle units | **Wrong** vs Java |

## Critical protocol facts (from Java)

- Wire: **one JSON `NetMessage` per line**, UTF-8, TCP port **4712**, protocol **VERSION = 1**.
- Positions live in **map parameter space**: `x,y ∈ [0,1]`, `z` = height above surface (`BonusKind.HEIGHT = 0.005`). World 3D is `Terrain.p3(x,y,z)` = surface point + normal × z.
- `angleZ` is in **turns** (full rotations), not radians. Direction uses `cos(angleZ * 2π)`, `sin(angleZ * 2π)` (see `PositionMobile` / `PositionUpdateImpl`).
- Client must **JOIN first** with `protocolVersion`, `playerName`, `colorRgb` before MOVE/PICK.
- Authoritative flow mirrors `NetworkPositionUpdate`: local movement + periodic MOVE; local collision only **claims** with PICK; bonuses disappear only on `bonusTaken`.
- Entity scale from `Bundle.properties`: `bonus.scale=0.005`, `persoCube.mlc=0.006`, `hauteur=0.007`.

## Bugs to fix first

1. **No JOIN** in `main.js` after connect → no `welcome`, `localPlayerId` stays `-1`.
2. **Wrong UV axes**: `updatePlayer` / `createBonus` use `(x, z)` but server sends `(x, y)`.
3. **Angle treated as radians**; Java uses turns → camera and facing drift.
4. **Meshes too large**: boxes/spheres ~0.5 on a ~1×1 ground; should be ~0.006 / radius from server.
5. **Camera `lookAt` incomplete** (missing Z) and not surface-aware.
6. **TCP framing**: one TCP chunk may hold several `\n`-delimited messages (or a partial line); browser + proxy need a line buffer.
7. **Move rate**: every animation frame floods MOVE; Java sends ~every 50 ms (`tickMillis`).
8. **Scaffold leftovers**: `game.ts` / Vite template CSS unused; keep `game.js` as source of truth for this pass (or consolidate later).

## Architecture (target)

```
Browser GameScene  --WS-->  proxy.js  --TCP-->  GameServer :4712
     |                         |
  local input              line-buffered
  + Three.js render        bidirectional
  + protocol.ts
```

```
src/
  main.js          # connect → join → wire onMessage
  network.js       # WS client + line buffer + send
  protocol.ts      # encode/decode + constants (VERSION)
  game.js          # GameScene: maps, entities, input, pick, HUD
  ground.js        # createGroundMesh(calculerPoint3D)
  maps.js          # NEW: SolPlan / SolRelief / SolSphere calculators + p3()
  proxy.js         # WS↔TCP with line-safe forwarding
  hud.css          # minimal overlay styles
```

## Implementation plan

### PR / step 1 — Protocol + connection (must work before anything else)

**Files:** `protocol.ts`, `network.js`, `main.js`, `proxy.js`

- Extend `NetMessage` / `Protocol` with: `VERSION`, `protocolVersion`, `playerName`, `colorRgb`, `x,y,z,angleZ`, full field set matching `NetMessage.java`.
- `NetworkHandler`:
  - Buffer incoming text, split on `\n`, decode each complete line.
  - Handle Blob/string `event.data` safely.
  - `join({ playerName, colorRgb })` helper that sends `Protocol.JOIN` with `protocolVersion: 1`.
- `main.js`: on connect, send join (name from query `?name=` or default `"WebPlayer"`; color `0` = server pick).
- `proxy.js`: ensure binary/text is written as UTF-8 string; optional line reassembly if TCP splits mid-line (buffer incomplete lines).

**Done when:** connecting with a running Java server logs a `welcome` with `playerId`, `mapName`, `bonuses`, `tickMillis`.

### PR / step 2 — Coordinate system + maps + camera

**Files:** `maps.js` (new), `ground.js`, `game.js`

- `maps.js`:
  - `calculerPoint3D(mapName, u, v)` ports:
    - **SolPlan**: `(u-0.5, 0, v-0.5)`
    - **SolRelief**: `(u, 0.002*sin(2πuT)*cos(2πvT), v)` with `T=6`
    - **SolSphere**: use Sphere of radius 10 (match `SolSphere.java` `ps`), fallback if hard — document approximation
    - **SolTube / SolReliefMouvant**: fallback to SolPlan with console warn for this pass
  - `p3(mapName, x, y, z)`: surface + approximate normal × z (finite differences like `Terrain.calcNormale`).
- Rebuild ground mesh when `welcome.mapName` arrives (remove old mesh, `createGroundMesh`).
- Camera: follow local player with rear offset using turns → radians; `lookAt` full 3D point from `p3`.
- Clamp local `u,v` to `[0,1]` (or wrap — prefer clamp to match soft server state).

**Done when:** local player stands on the correct ground for `SolPlan` / `SolRelief`, camera tracks behind ship.

### PR / step 3 — Entities, movement, authority loop

**Files:** `game.js`

- Movement:
  - Arrow keys: forward along `(cos(angle*2π), sin(angle*2π))` in UV; left/right change `angleZ` in turns.
  - Speed tuned so motion feels close to Java (`unitPerSec` / frame-delta based, not fixed 0.01 per frame).
  - Send MOVE at most every `tickMillis` (from welcome, default 50).
- Players:
  - Create/update meshes from `state.players`; color from `colorRgb`; size ~`0.006`.
  - Local player: prefer local prediction for camera; still apply server snapshot for others (and optionally soft-correct local).
  - Remove disconnected players when absent from snapshot (or `connected === false`).
- Bonuses:
  - Spawn from `welcome.bonuses` (skip `taken`); size from `radius` (~0.005); color by `kind` (SIMPLE red, LICORNE blue, ESCARGOT black, FUITE gray).
  - On `bonusTaken`: remove mesh, update score if local.
  - Local collision every frame (or ~20 ms): if `hypot(u-bx, v-by) < pickTolerance` (~0.05 param) or world distance &lt; radius, send `pick` with current `x,y,z`; throttle retries 1 s per bonus id (mirror `NetworkPositionUpdate`).
- Handle `gameOver` / `error` messages.

**Done when:** two clients (or web + Java) see each other move; picking a bonus removes it for both and updates score.

### PR / step 4 — HUD + UX polish

**Files:** `index.html`, `style.css` (or `hud.css`), `game.js`, `main.js`

- Overlay: connection status, map name, scoreboard (name + score), remaining bonuses, game-over banner.
- Window resize → update camera aspect + renderer size.
- Dark full-viewport canvas (replace Vite template chrome).
- Optional simple join form (name) before connect; keep URL params as shortcut.
- Reduce noisy `console.log` on every state tick.

**Done when:** a browser-only session against local server is playable without DevTools.

### Out of scope (later)

- Full SolTube / SolReliefMouvant parametric fidelity.
- 3D ship models / STL assets from Java resources.
- Client-side prediction reconciliation / lag compensation beyond soft local move.
- Auth, rooms, matchmaking.
- Converting entire client to TypeScript (optional follow-up once JS loop is stable).

## Run stack (verification)

1. Start Java game server on **4712** (`GameServerMain` / multiplayer GUI host).
2. `node src/proxy.js` in `client-web-game` (WS **8080**).
3. `npm run dev` — open browser, confirm join + state stream.
4. Optional second tab or Java client for multiplayer check.

## Risk notes

- **SolSphere / SolTube** world scale differs a lot from SolPlan; keep camera near-clip low (`0.001`) and distance adaptive.
- Proxy must not double-append `\n` if client already sends it.
- If server is remote (k8s `pad.md` host), proxy `TCP_SERVER_HOST` must be configurable via env.

## Recommended first implementation slice

Implement **steps 1–3** in one focused pass on `game.js` / `network.js` / `main.js` / `protocol.ts` / new `maps.js`, then HUD. That turns the current scaffold into a working Opad web multiplayer client aligned with the Java server.
