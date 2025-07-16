### Move To Workspace (movetoworkspace)

This can move the focused window to specified workspaces. There are nine methods that you can specify workspace to change to.

```
 ID,
  e.g. 1, 2, or 3

 Relative ID,
  e.g. +1, -3 or +100

 workspace on monitor, relative with + or -, absolute with ~,
  e.g. m+1, m-2 or m~3

 workspace on monitor including empty workspaces, relative with + or -, absolute with ~,
  e.g. r+1 or r~3

 open workspace, relative with + or -, absolute with ~,
  e.g. e+1, e-10, or e~2

 Name,
  e.g. name:Web, name:Anime or name:Better anime

 Previous workspace,
  previous, or previous_per_monitor

 First available empty workspace, empty, suffix with m to only search on monitor. and/or n to make it the next available empty workspace,
  e.g. emptynm

 Special Workspace: special or special:name for named special workspaces.
```

##### Specific Window

You can add a specific window to move to a workspace that is provided.

- Using `initialclass:` / `class:` followed by a regex of it, (org.gnome.TextEditor)
- Using `title:` followed by a regex of it, (bind.conf (~/.config/hypr/hyprConfigAutoGen) - Text Editor)
- Using `initialtitle` followed by a regex of it, ...
- Using `tag:`
- Using `pid`, easiest way to do it(pid:59047),
- Using `address:`,
- Or Using `activewindow` an active window, `floating` the first floating window on the current workspace, `tiled` the first tiled window on the current workspace