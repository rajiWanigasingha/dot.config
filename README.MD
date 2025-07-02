## Dot.Config Project

This is a project that aims to making editing and creating new or existing hyprland settings in a graphical settings app.
This will work with your existing hyprland config (For now only work in hyprland v49) and allow you to edit everything in real time.
This is still in early development but will get better and support all settings of hyprland.

[show.mkv](docs/video/show.mkv)

> [!CAUTION]
> This porject is in development,
> Do not build this project your self without any experiance working with tauri ,kotlin. This could break.

### Development

This project is under active development. Many features are missing and many reactors are waiting to be done.
But it can be used for editing of settings

##### Simple Overview

This project has three parts desktop ui, Websocket server, Hyprland Parser.

First, when open the app it will go to your hyprland settings file at `~/.config/hypr/hypr.config` and search thought all the
source files and build a new hyprland settings configuration file in there with your old config, and it will create backups

So it will support your existing hyprland settings and let you make changes to hyprland settings from the ui without anything breaking.

##### Support

This project only supports ***version 49 of hyprland*** and if you try this in any other version, it will break the hyprland.

> [!CAUTION]
> FOR NOW ONLY HYPRLAND VERSION 49 SUPPORTED 

###### Support Settings

* All The subcategories of supported categories are working

- [x] General settings -> [Hyprland general](https://wiki.hypr.land/Configuring/Variables/#general)
- [x] Input settings -> [Hyprland input](https://wiki.hypr.land/Configuring/Variables/#input)
- [x] Decoration settings -> [Hyprland Decoration](https://wiki.hypr.land/Configuring/Variables/#decoration)
- [x] Gestures settings -> [Hyprland Gestures](https://wiki.hypr.land/Configuring/Variables/#gestures)
- [x] Group settings -> [Hyprland Group](https://wiki.hypr.land/Configuring/Variables/#group)
- [x] Misc settings -> [Hyprland Misc](https://wiki.hypr.land/Configuring/Variables/#misc)
- [x] XWayland settings -> [Hyprland XWayland](https://wiki.hypr.land/Configuring/Variables/#general)
- [x] OpenGL settings -> [Hyprland OpenGL](https://wiki.hypr.land/Configuring/Variables/#opengl)
- [x] Render settings -> [Hyprland Render](https://wiki.hypr.land/Configuring/Variables/#render)
- [x] Cursor settings -> [Hyprland Cursor](https://wiki.hypr.land/Configuring/Variables/#cursor)
- [x] Ecosystem settings -> [Hyprland Ecosystem](https://wiki.hypr.land/Configuring/Variables/#ecosystem)
- [x] Experimental settings -> [Hyprland Experimental](https://wiki.hypr.land/Configuring/Variables/#experimental)
- [x] Debug settings -> [Hyprland Debug](https://wiki.hypr.land/Configuring/Variables/#debug)

Not Working yet but will get support soon

- [ ] Keybinds / Keyboard
- [ ] Window / Workspaces
- [ ] auto strat / env
- [ ] animations / curves
- [ ] variables

### Build Steps

This is very early in this project, so there can be bugs so before you do anything,
make sure you back up your hyprland.conf file, and
there is a way to rescue everything in a worst case scenario. (Probably won't happen)

### Running Testing Version

I have built a version to test in other machines it is a bit big (~200MB). This is not the final product so there are many things that are unoptimized.

###### Requirements

To run this version, you need it.

 - **Hyprland version Needs to be 49 or else it will break your hyprland session**
 - Need to have jvm 21. This is around (~300MB), [Here aur link for open jdk 21](https://archlinux.org/packages/extra/x86_64/jdk21-openjdk/)

###### Building

- Need to have jvm 21 in $PATH variable

First go to your home,

```shell
cd /home/$USERNAME
```

Then clone the git repo this is around (~180-200MB)

```shell
git clone https://github.com/rajiWanigasingha/dot.config.git
```

go into testBuild

```shell
cd ./dot.config/testBuild
```
###### Running The Application

you can use script.sh to run it automatically

```shell
./script.sh
```
When run the script, it will begin a server in the background (It will give you the pid to kill it). This will parse your existing hyprland settings
it to hyprConfigAutoGen and replace hyprland.conf.

After 10 seconds it will open up the ui.

> [!WARNING]
> If you have a settings device {} ([device](https://wiki.hypr.land/Configuring/Keywords/#per-device-input-configs)) This can break those settings
> but hyprland will be fine. It will give an error if your remove it all things would run smoothly

> [!INFO]
> When Click on a sidebar link and nothing rendered that settings not yet developed. So right click and refresh the page so others begin to work again

###### Running It Manually

First need to run the server

```shell
cd ./dot.config/testBuild/hyprland/bin
./hyprland
```

This will open up the server then wait a few seconds and

```shell
cd ./dot.config/testBuild/hyprlandui.AppDir/usr/bin
./app
```

This will open up the ui.

**Do Not Run ./dot.config/testBuild/hyprlandui.AppDir/AppRun It Wont Work**

### Manual Build

###### Requirements

- **Hyprland version Needs to be 49 or else it will break your hyprland session**
- Need to have jvm 21. This is around (~300MB), [Here aur link for open jdk 21](https://archlinux.org/packages/extra/x86_64/jdk21-openjdk/)
- bun runtime.  [Here Link To Bun RunTime](curl -fsSL https://bun.sh/install | bash)
- **Need to set up for tauri for ui**. [prerequisites](https://v2.tauri.app/start/prerequisites/)

###### Building

First go to your home,

```shell
cd /home/$USERNAME
```

Then clone the git repo this is around (~180-200MB)

```shell
git clone https://github.com/rajiWanigasingha/dot.config.git
```

then go into `./dot.config`

###### Building Server

- Need to have jvm 21 in $PATH variable

```shell
cd ./hyprland
```

Then run 

```shell
./gradlew installDist # This is to build server. if you want to only run it use ./gradlew run
```

This will download and build server. This will take a few minutes (7-9min)

The build version will be in `dot.config/hyprland/build/install/hyprland/bin/hyprland`

Running that hyprland file will begin the server.

###### Building UI

```shell
cd ./hyprlandUI
```

then use,

```shell
bun install

bun tauri dev # to run it

bun tauri build # to build it
```
check out tauri docs -> [tauri](https://v2.tauri.app/distribute/)
