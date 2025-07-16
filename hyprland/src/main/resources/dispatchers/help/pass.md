#### Pass Keybinds To Windows (pass)

Pass the keybind (with mods) to a specified window. When you need specific keybind to work in a window but have that keybind as a global
keybind, this method can be used.

##### Use Case

Using `shift+enter` to make split in a application but you have that as a bind `pass` dispatcher can be use to do that in that window.
Use with flag like `o`(longpress) to make the keybind trigger twice.

```
bind = shift+enter # This open a termial
bindo = shift+enter with pass dispatcher # This will tregger keybind that needed in the window you provied
```

##### How To Specify A Window

The Window Can be specified by using any following options.

```
Using command,

hyprctl clients

Window 560e6d1f2d00 -> bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor:
	mapped: 1
	hidden: 0
	at: 964,47
	size: 952,1029
	workspace: 5 (5)
	floating: 0
	pseudo: 0
	monitor: 0
	class: org.gnome.TextEditor
	title: bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor
	initialClass: org.gnome.TextEditor
	initialTitle: bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor
	pid: 59047
	xwayland: 0
	pinned: 0
	fullscreen: 0
	fullscreenClient: 0
	grouped: 0
	tags: 
	swallowing: 0
	focusHistoryID: 1
	inhibitingIdle: 0
	xdgTag: 
	xdgDescription: 

```

- Using `initialclass:` / `class:` followed by a regex of it, (org.gnome.TextEditor)
- Using `title:` followed by a regex of it, (bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor)
- Using `initialtitle` followed by a regex of it, ...
- Using `tag:`
- Using `pid`, easiest way to do it(pid:59047),
- Using `address:`,
- Or Using `activewindow` an active window, `floating` the first floating window on the current workspace, `tiled` the first tiled window on the current workspace