### Send Shortcut key State To Windows (sendkeystate)

Send a key state (with mods) to a specified window. You can trigger in application shortcuts with global shortcuts.

There are four parts that are needed,

- Mod Keys (optional)
```
1. SHIFT
2. CAPS
3. CTRL/CONTROL
4. ALT
5. MOD2
6. MOD3
7. SUPER/WIN/LOGO/MOD4
8. MOD5
```
- A bind key. (a-z, 0–9, ...)
- Key State
```
1. down
2. up
3. repeat
```
- window to match

##### How To Specify A Window

The Window Can be specified by using any following options.

- Using `initialclass:` / `class:` followed by a regex of it, (org.gnome.TextEditor)
- Using `title:` followed by a regex of it, (bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor)
- Using `initialtitle` followed by a regex of it, ...
- Using `tag:`
- Using `pid`, easiest way to do it(pid:59047),
- Using `address:`,
- Or Using `activewindow` an active window, `floating` the first floating window on the current workspace, `tiled` the first tiled window on the current workspace