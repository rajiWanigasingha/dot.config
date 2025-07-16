### Create Shortcut Keys

#### Mod Keys

* Mod keys (**modifier keys**) are special keys you hold in combination with other keys to perform actions—similar to 
how you'd use `Ctrl+C` to copy in many apps.
* There Are 8 mod keys that are supported in hyprland
  
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

* There can be may mod keys in single keybind, and it's allowed you to put any separators you please except for a `,`
  * `SUPER + CTRL` or `SHIFT CAPS`
* Mod keys are not required. This Can Be Empty If Needed

#### Keys

* For This, you can put any key your like to create keybind, There can be only a single key for one keybind (You Can put more than
  one if you're using flags).
* You can put a `keycode` if you need like `code:28 for T`

#### Special Keybinds With Flags

| Flag | Name                | Description                                                                       |
|------|---------------------|-----------------------------------------------------------------------------------|
| l    | **locked**          | Will also work when an input inhibitor (e.g. a lockscreen) is active              |
| r    | **release**         | Triggers on release of a key                                                      |
| c    | **click**           | Triggers on release of a key/button if mouse stays inside `binds:drag_threshold`  |
| g    | **drag**            | Triggers on release of a key/button if mouse moves outside `binds:drag_threshold` |
| o    | **longPress**       | Triggers on long press of a key                                                   |
| e    | **repeat**          | Will repeat when held                                                             |
| n    | **non-consuming**   | Key/mouse events also go to active window, not just to the dispatcher             |
| m    | **mouse**           | Specifies mouse-related behavior (see Hyprland docs)                              |
| t    | **transparent**     | Cannot be shadowed by other binds                                                 |
| i    | **ignore mods**     | Ignores modifiers like Shift, Ctrl, etc.                                          |
| s    | **separate**        | Arbitrarily combines keys between each mod/key (see *Keysym combos*)              |
| d    | **has description** | Allows writing a description for the bind                                         |
| p    | **bypass inhibit**  | Bypasses the app’s request to inhibit keybinds                                    |

##### Mouse Keys
* There can be mouse button shortcuts like `mouse:272` if ues Flag `m`.
```
LMB -> 272 
RMB -> 273
```

##### Description
* You can create a description for keybind if use Flag `d`

##### Multiple Keys
* If you need to use multiple keys, you can use Flag `s`



```
CTRL + A&Z
SUPER&ALT + Z&L
```

##### Mod Keys Only
* If you need to use mod keys only to trigger a keybind use `r`.When creating them need to specify the side (exactly where it positions) it is in.
```
ALT + ALT_L
when the alt key of the left side been press and released keybind will trigger

SUPER + ALT + ALT_R
when press super/win and right alt this will trigger

SUPER + SUPER_L
when press super/win key of the left side keybind will trigger
```