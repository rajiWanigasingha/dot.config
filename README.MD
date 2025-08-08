## Dot.Config Project

This is a project that aims to making editing and creating new or existing hyprland settings in a graphical settings app.
This will work with your existing hyprland config (For now only work in hyprland v49) and allow you to edit everything in real time.
This is still in early development but will get better and support all settings of hyprland.

[![Watch the video](https://github.com/rajiWanigasingha/dot.config.git/doc/doc.mkv)](https://github.com/rajiWanigasingha/dot.config.git/doc/doc.mkv)

> [!CAUTION]
> This project is in development.

### Development

This project is under active development.
##### Support

This project only supports ***version 49 of hyprland*** and if you try this in any other version, it will break the hyprland.

> [!CAUTION]
> FOR NOW ONLY HYPRLAND VERSION 49 SUPPORTED 

##### Progess So Far

- All Hyprland Settings are supported. except for pre-config device

If you need to see how this project will be developed, see it in the project tab

### Build Steps

- Package is not available yet.

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

- This Will Build Server
 
```shell
./gradlew installDist # This is to build server. if you want to only run it use ./gradlew run
```

This will download and build server. This will take a few minutes (7-9min)

The build version will be in `dot.config/hyprland/build/install/hyprland/bin/hyprland`

Running that hyprland file will begin the server.

- To Run It In Dev Mode

```shell
./gradlew run
```

This will begin the server and parse all hyprland settings

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
