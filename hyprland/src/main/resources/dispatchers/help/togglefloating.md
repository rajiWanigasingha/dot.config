### Toggle Floating (togglefloating)

Toggle floating mode in the focus window or specified window.

##### Toggle Mode

When window is in floating mode, it behaves like a regular window (link in windows, ubuntu).

##### How To Specify A Window

The Window Can be specified by using any following options.

- Using `initialclass:` / `class:` followed by a regex of it, (org.gnome.TextEditor)
- Using `title:` followed by a regex of it, (bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor)
- Using `initialtitle` followed by a regex of it, ...
- Using `tag:`
- Using `pid`, easiest way to do it(pid:59047),
- Using `address:`,
- Or Using `activewindow` an active window, `floating` the first floating window on the current workspace, `tiled` the first tiled window on the current workspace