# morecommands
MoreCommands Minecraft Mod

Description

MoreCommands is a mod which adds over 100 Commands to the minecraft chat console. It is inspired by the Singleplayer Commands mod, which isn't developed further. I really liked this mod and it was the only mod I used extensively, but since it isn't developed anymore, I decided to write my own commands mod. Singleplayer Commands had to be entirely rewritten in the 1.3 update, because since this update, singleplayer is just an internal server and many commands had to be removed. I adopted many of the currently working commands but I also reimplemented many of the commands which have been removed. Nevertheless this mod is NOT just an update for Singleplayer Commands, I added lots of new commands and not only commands but also lots of other features. Generally all the functionality Singleplayer Commands had is available in this mod as well (with few exceptions), most times in a much improved version. Additonally this mod has an almost completely different codebase than Singleplayer Commands: It is a Forge Mod, you don't have to modify your minecraft.jar. This mod also uses the client-server model and is therefore NOT only restricted to singleplayer, you can also use it on a Forge Server.

Installation



This mod requires Minecraft Forge. If you haven't installed it already, go here and install it:
Minecraft Forge

Client
1.Navigate to your Minecraft installation Folder (e.g. on windows: %appdata%\.minecraft)
2. If there isn't already a "mods" Folder, create it
3. Put the downloaded file into this folder

Server
1. Navigate to your Forge Server installation Folder
2. If there isn't already a "mods" Folder, create it
3. Put the downloaded file into this folder


Download

v6.0 for Minecraft 1.12.2: MoreCommands

v6.0 for Minecraft 1.12.1: MoreCommands

v6.0 for Minecraft 1.12: MoreCommands

v5.0 for Minecraft 1.11.2: MoreCommands

v5.0 for Minecraft 1.11: MoreCommands

v4.1 for Minecraft 1.10.2: MoreCommands

v4.1 for Minecraft 1.10: MoreCommands

v3.1 for Minecraft 1.9.4: MoreCommands

v3.1 for Minecraft 1.9: MoreCommands

v2.8 for Minecraft 1.8.9: MoreCommands

v2.8 for Minecraft 1.8: MoreCommands

v1.8 for Minecraft 1.7.10: MoreCommands

Older Versions:
Spoiler (click to hide)

1.10.2

v4.0 for Minecraft 1.10.2: MoreCommands

1.10

v4.0 for Minecraft 1.10: MoreCommands

1.9.4

v3.0 for Minecraft 1.9.4: MoreCommands

1.9

v3.0 for Minecraft 1.9: MoreCommands

1.8.9

v2.7 for Minecraft 1.8.9: MoreCommands

1.8

v2.7 for Minecraft 1.8: MoreCommands

v2.6 for Minecraft 1.8: MoreCommands

v2.5 for Minecraft 1.8: MoreCommands

v2.4 for Minecraft 1.8: MoreCommands

v2.3 for Minecraft 1.8: MoreCommands

v2.2 for Minecraft 1.8: MoreCommands

v2.1 for Minecraft 1.8: MoreCommands

v2.0 for Minecraft 1.8: MoreCommands

1.7.10

v1.7 for Minecraft 1.7.10: MoreCommands

v1.6 for Minecraft 1.7.10: MoreCommands

v1.5 for Minecraft 1.7.10: MoreCommands

v1.4 for Minecraft 1.7.10: MoreCommands

v1.3 for Minecraft 1.7.10: MoreCommands

v1.2 for Minecraft 1.7.10: MoreCommands

v1.1 for Minecraft 1.7.10: MoreCommands

v1.0 for Minecraft 1.7.10: MoreCommands

Changelog
Spoiler (click to hide)

6.0, 5.0, 4.1, 3.1, 2.8

Versions corresponding to v1.8 for Minecraft versions 1.12.2, 1.12.1, 1.12, 1.11.2, 1.11, 1.10.2, 1.10, 1.9.4, 1.9, 1.8.9, 1.8

1.8

- Improvements of the new settings system of version 1.7. These improvements are more of a progamming-wise nature but it's easier for the user as well in terms of the old "savemode" command which are now named "settings" commands. See Settings System for more details.

- Improvements of the permissions system. It is now possible to assign different permission leves to a single command depending on the "action" a command performs. "Action" means the first argument passed to a command

- Lots of changes under the hood that you don't notice as a user. Every single source file was changed multiple times.

- Lots of new configuration properties

- Some commands now have "results" which means that their execution can produce a result that can be used elsewhere. Specifically the /var command was extended to grab results of a command exectuin. This allows e.g. to grab the uuid of a player and store it in a variable, e.g. /var grab uuid /playeruuid

- Improved the /execute command so that it can now be configured not to directly execute a command for another player but to send an execution request to player which can be confirmed or denied.

- Added Chat Channels. A Chat Channel is basically a Chat Room. Now you can create such Chat Rooms e.g. to separate players in different message groups. Such a Chat Room has a policy that determines which players are allowed in a Chat Channel, e.g. only whitelisted players. The Chat Channels are structured in a tree which allows to receive messages from parent channels. See Chat Channels for more details

- Changed how the startup commands are stored. They are now part of the general mod configuration (for server executed commands) and part of the normal client side settings (for player executed startup commands) and therefore stored in the respective files, they are not treated as separate settings anymore.

- Added the possibility to delay the execution of startup commands.

- Added the "chatchannel " command which can be used to manage chat channels

- Changed the "permission" command according to the changes made in the permissions system

- Changed the "home" command: It now allows to go to the global spawn point as well

- Changed the "ignorespawn" command: It now allows to check for nbt data as well (can be used e.g. to check for entity subtypes, e.g. skeleton type or only for sheared sheeps, etc.)

- Added "/var grab" which allows to grab either the result of a command execution, or if there is no result, the output of a command and stores it in a variable

- Added the "playeruuid" command: Prints the uuid of the executing player into the chat

- Added the "chatstyle" command: Allows to customize the style of the chat. This means you can set the color of you chat messages and the color of your name in the chat. There are other formatting as well, e.g. bold, italic, etc.

- Added the "chatstyle_global" command: Same as "chatstyle" but global. Changes the chatstyle of ALL messages

- Added the "hurt" command: Allows to hurt an entity or player using a specific damage source

- Added the "thru" command: Allows to pass through an obstacle, e.g. a wall

- Added the "exec_confirm", "exec_deny" and "exec_requests" commands: As described above these commands can be used to confirm, deny or print command execution requests.

- Removed the "savemode" commands which were replaced with the "settings" commands which allow to set constraints on the validity and availability of settings. See Settings System for more details.

- Lots of minor changes not noticable by the user

4.0, 3.0, 2.7

Versions corresponding to v1.7 for Minecraft versions 1.11.2, 1.11, 1.10.2, 1.10, 1.9.4, 1.9, 1.8.9, 1.8

1.7

- Completely new settings system which allows to give settings a certain scope. This means that a settings can

have different values depending on the server, the world and the dimension. Explaining it in detail would be

too much. See Settings System for more details.

- A permissions system which allows to assign a permission to a command which will then be required for a

command to be executed (unfortunately works only for commands added by MoreCommands). Corresponding

to that, it is possible to give players different permission level. Vanilla assigns always the highest permission level

by using the "op" command.

- This update improves minecraft's target selectors: Some arguments can be used multiple times and the arguments "nbt" and "nbtm" were added. Additionally a new target selector for blocks is introduced: "@b"

- Some performance improvements

- Lots of changes under the hood that you don't notice as a user. Every single source file was changed multiple times.

- Lots of new configuration properties

- Every single command has been revised so that it is less sensitive for the command sender, e.g. the "firework"

command does not need a player as command sender anymore to work properly. Or you can now execute the

enchant command for a zombie to enchant his sword, etc. Commands now work properly for more command

sender types

- Since the "execute" command was added with this release, the ridiculous system with a target selector as last

argument as possibility to target other entities for a command is gone.

- global versions of the "alias" and the "var" command: "alias_global" and "var_global". These versions allow to set

global aliases or variables. "Global" means that all players can use them instead of only the player who created it.

- Added the "unbindid" command. I didn't include it in the last versions because I thought it is not needed because

the regular "unbind" command detects when a number is given and will use that as an id. The problem was that

this makes it impossible to remove bindings to numeric keys^^

- Added the "compass" command: This command allows to set the compass target

- Added the "execute" command: Allows to execute commands for other entities

- Added the "kill" command: Does exactly the same as the vanilla versions but allows other entities than players too

- Added the "nbt_apply_inventory" command: Allows to transform inventories

- Added the "nbt_apply_block" command: Allows to set or transform blocks

- Added the "nbt_apply_entity" command: Allows to transform entities

- Added the "nbt_test_inventory" command: Checks inventories for some conditions

- Added the "nbt_test_block" command: Checks blocks for some conditions

- Added the "nbt_test_inventory" command: Checks entities for some conditions

- Added a modified "op" command: Plays a role for the permissions system. Sets the permissions for a player

- Added the command "open": Allows to open containers, e.g. the enderchest or by looking at a chest

- Added the command "permission": Plays a role for the permissions system. Sets permissions for commands

- Added the so called "savemode" commands: These are the three commands "savemode_client",

"savemode_server" and "savemode_global". They play a role for the new settings system.

- new syntaxes for the following commands (most of them are now more senstive, e.g. for nbt data):

"achievement", "blocklight", "bring", "cleardrops", "creeper", "destroy", "effect", "enchant", "enderman", "firework",

"freeze", "give", "itemdamage", "op", "return", "sign", "spawn", "teleport"

- removed the "clearwater" command which is equal to "/blocklight water opacity 0"

- removed the "endercrystal" command which is equal to "/spawn EnderCrystal"


1.8-2.0 to 1.8-2.6

Versions of their respective 1.7.10-1.x versions for Minecraft 1.8

1.6

- Changed how startup commands are executed (see "Executing commands on startup" below)

- Added an updater. If there's a new version or an update for the current version out, you will be notified.

- Modified the "speed" command: It's now possible to modify the minecart speed!

- Added the command "stackcombine": Combines all item stacks in your inventory

- Added the command "invrotate": Rotates the items in your inventory

- Added the command "fluidmovement": Allows to enable/disable the special movement in fluids

- Added the command "pickup": Allows to disable picking up certain items

- Updated the ugly command list to a little website with all important information to MoreCommands

1.5

- Improved network: With previous versions there was often the problem that the so called "handshake" packet wasn't sent properly. Everytime you join a server, the server sends such a packet and the client sends one back. With this method, server and client can be sure that the mod is installed on both sides which is required for some commands (e.g. xray). As already said, the packet wasn't sent properly sometimes so you couldn't use some commands (e.g. xray). This versions retries the handshake so the handshake shouldn't fail anymore!

- Added the "handshake" command: In case that the handshake fails despite the mentioned network improvements, there is the handshake command: with "/handshake status", you can check if the handshake was done properly, if not you can resend a handshake with "/handshake redo"

- Improved the "startup.cfg" file: In previous versions, the startup file could only be used with a modded server (or to be precise: the startup commands were executed after the handshake was received). Now it works the following way: as soon as you join a server, the mod waits a certain time for the handshake packet, if it is received of if the time ran out (e.g. if the server doesn't have MoreCommands installed), the startup commands are executed.

- Added the command "command": With this command you can disable/enable other commands, e.g. "/command disable ignorespawn" disables the ignorespawn command. Please note: Commands written into the "disable.cfg" file are not even loaded and therefore you can't enable them!

- Added the command "noattack": With this command you can prevent entities from attacking you, e.g. spiders won't attack after you wrote "/noattack Spider" into the console. Be aware that creepers start the explosion as soon as they are near enough to you. Disabling creeper attacks shortly before the creeper explodes won't stop the explosion!

- Improved the command "duplicate": Now all properties of the item are duplicated (e.g. enchantments)

- Improved the command "hunger": Now you can disable/enable hunger entirely. Using "/hunger off" lets your hunger staying full all the time

- Improved the command "freeze": Works entirely server side now, client doesn't have to be modded anymore (results in not being buggy anymore too)

- Improved the command "ride": Same as for "freeze"

1.4

- Added command 'var' which allows you to set variables in the chat, you can also disable/enable

variables entirely. Variables can be used with %var%, to get a single % sign, use two % sign

(e.g. %%%var%%% = %value%)

- Added new parameters to the xray command: with "/xray save NAME" you can save the current xray

config and load it again with "/xray load NAME"

1.3

- Added command 'blocklight' which allows you to set the light level for each block

- Added command 'calc', which is a basic calculator (brackets, plus, minus, modulo, multiply, divide and

power)

- Added command 'ignorespawn', which allows to prevent entities from spawning

1.2

- Improved Command Block Support (if the player has the mod installed client side, client commands

work too)

- Added the "music" command (allows to play, skip and stop music)

- Switched to an event driven network system.

1.1

Added Command Block Support. Use Target Selectors (e.g. "@p[name=Player1,name=Player2,..]") as LAST Argument to target players. You don't need to specify players for every command (e.g. the "dodrops" command does not need a player)

1.0

Initital release

Usage

Client vs Server Commands

See "Client vs Server Commands" for more details.

As I already said, this mod uses the client-server model. By default commands are always handled by the server, so if you join a server, the server must have this mod installed to use commands (singleplayer is just an internal server). Forge adds the possibility to handle commands client side, so you can use some commands on servers not having this mod installed. Nevertheless the majority of the commands are handled server side, simply because they must operate on the server. There are also commands which require the mod to be installed on both sides. Commands which don't require that can be used without the client having installed this mod or without the server having installed this mod (e.g. players (clients) on a server can use the "fly" command without having the mod installed if the mod is installed on the server. It works the other way around too, e.g. the "clouds" command can be used on any server. Finally commands like "noclip" require the mod to be installed on the server and on the client). If you want to turn off all client side commands, you can use "clientcommands disable" (or enable to enable).

The "help" command

See "The help command" for more details.

This mod alters the"´help command. The problem with this command is, that it is processed on the server side and client commands aren't displayed. Therefore using "help" will just display an information that you have to use "chelp" for client commands and "shelp" for server commands. This of course applies only if client commands are enabled. (You can turn them off with "clientcommands disable")

Disabling commands

See "Disabling commands" for more details.

The mod will create a file called "disable.cfg" (located in "minecraft_installation_folder/config/morecommands"). You can put commands in there and they will be disabled (e.g. if you want to use the vanilla "enchant" command)

Executing Commands on startup

See "Executing Commands on startup" for more details.

MoreCommands allows you to execute commands right when you join a server or when a server starts. This can be used e.g. to setup a special configuration on server startup. Since the way how startup commands will be executed has gone through lots of changes, I won't explain it here (tbh I don't want to write everything twice :) and generally the website is a good resource on how to use the mod). Use the link from above to get more details.

Modify the configuration (e.g. the welcome message)

The configuration is stored in a file called config.cfg which is located at MC_FOLDER/config/morecommands. This file is the configuration of MoreCommands and consists of key-value pairs which are properties that define some behaviours of the mod. A list of all properties and what they do can you see here: Configuration

The settings system

The settings system is quite extensive and it is too much to explain all that here. The default behaviour of the settings system is definetely enough but if you wan't to understand it which allows you an advanced usage, you have to read

this wall of text: The settings system

The permissions system

Minecraft already has a kind of permissions system. Based on that MoreCommands allows you to set individual permissions for players and permission requirements for commands. You can read how that works here.

Commands with nbt data

Some commands have a "nbt" parameter. This parameter expects nbt data. If you don't know what that is, you can look for that in the minecraft wiki, there's a good explanation. Directly following after the nbt parameter you will often see a parameter named "merge" or "equal". If the former is the case and you give the argument "merge", lists will be merged instead of being replaced entirely. If the latter is the case and you give the argument "equal", lists will be compared for total equality, otherwise lists are compared in the way that one list must contain all elements of the other.

You can see more details here.

Target Selectors

This mod expands the possiblities which minecraft's "target selectors" offer. If you don't know what that is, you can read about it in the minecraft wiki here. MoreCommands adds the following possibilites:

- Some arguments can be used multiple times, e.g. "@e[type=Sheep,type=Pig]" works with MoreCommands but not with vanilla

- The "nbt" and "nbtm" arguments were added, They allow to specify nbt data a target must have.

- A new target selectors is added for blocks: "@b". This target selectors has the following arguments which other target

selectors have aswell: x, y, z, dx, dy, dz, r, nbt, nbtm. The "id" argument is the block's id or name and the "meta"

argument is the block's metadata. The block target selector can only be used for some commands.

You can see more details here.

Chat Channels

The newest version has some kind of Chat Rooms which are named Chat Channels. All Chat Channels are structured in a tree structure and every channel has a policy which determines whether a player is allowed to join a channel or not. This way you can e.g. make a private chat room where only players on a whitelist can be a member of. To see more details on how this works, see Chat Channels.

Website

You can find a little website with all important information at a glance here

Command List
A command list is available here: Command List

A list as downloadable pdf is available here (thanks to LoRaM100): PDF list

Source

The source code is available at this github repo: GitHub
