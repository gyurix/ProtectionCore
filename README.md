[img]http://kepfeltoltes.hu/141021/icon_www.kepfeltoltes.hu_.png[/img]

[center][color=red][size=18pt][b]Critical dependency:[/b][/size][/color][/center]

[size=14pt][b]Please download and install [url=http://www.project-rainbow.org/site/downloads/?sa=view;down=201]Api-Collection[/url] plugin. If you didn't do it, this plugin won't work.
[/b][/size]


[size=18pt][b]Flags:[/b][/size]
BookChange,BlockBreak,BlockPlace,BlockInterract,BlockFlow,
Commands,ChatSend,ChatReceive,CropTrample,
AttackEntity,EditFlags,EntityDamage,EntityGrief,EntitySpawn,InterractEntity,
ExpPickup,Explosion,Entry,Leave,Fly,
GameMode,Greeting,Farewell,ItemDrop,ItemPickup,ItemUse,
ManageRegion,
Permissions,PotionEffects,PVP,PlayerDamage,
PistonAction,GetPotionEffects,
Move,Teleport,DimensionChange,SignChange,SpectateEntity

[size=14pt][b]Don't working yet:[/b][/size]
[b]ChatReceive[/b] - Waiting for event in API

[size=14pt][b]Bugs:[/b][/size]
[size=12pt]Not every flag is tested 100 percently, so if you find a bug, please report[/size]


[size=18pt][b]Commands and permissions:[/b][/size]
[b]Permission for using the whole plugin:[/b] protectioncore.use
[b]Permission for using commands, selecting commands not included:[/b] protectioncore.command.<commandname>
[b]Permission for selecting area:[/b] protectioncore.select
[b]Permission for region limit:[/b] protectioncore.limit.<number>
[b]Permission for unlimited region:[/b] protectioncore.limit.-1
[b]Permission for allowing creating regions with area, which contains another region with same priority:[/b] protectioncore.allowsamepriority
[b]Permission for disabling negative flag checks:[/b] protectioncore.disableflagcheck.<flagname>

[s][b]Permission for enabling player management for not owned regions:[/b]
protectioncore.disableowncheck[/s]
[size=12pt][b]No more own checks, you can edit these things using the ManageRegion flag[/b][/size]
/rg
/rg define <region> [template]
/rg area <region> [move|expand|redefine|decrease|grair] [blocks]
/rg remove <region>
/rg priority <region> <priority>
/rg save
/rg reload
/rg flag <region> [group] [flagtype] [value]
/rg player <region> [group] [player]
/rg group <region> [group] [copied-group-flags]
/rg gettool [wand|info]
/rg p1 [x-cord] [y-cord] [z-cord]
/rg p2 [x-cord] [y-cord] [z-cord]
/rg list
/rg info [region]

[size=18pt][b]Permissions:[/b][/size]
[b]All commands:[/b] protectioncore.use
[b]Select region using wooden axe:[/b] protectioncore.select
[b]Disable negative flag checks:[/b] protectioncore.disableflagcheck.<flagname>

[size=18pt][b]Bugs?:[/b][/size]
Please report them to this thread: [url=http://www.project-rainbow.org/site/index.php?topic=444.0]link[/url]

[center][size=18pt][b]Changelog - Beta versions[/b][/size][/center]

[spoiler][size=14pt][b]Beta 0.2:[/b][/size]
- Added /rg command
- Added /rg priority <region> <newpriority> command
- Fixed regions.yml is not working, which caused the whole plugin wasn't worked
- Edited region toString (now /rg info writes the coordinates)
- Fixed region area bugs
[size=14pt][b]Beta 0.3:[/b][/size]
- Added english language file, it will be used in the next version
- Fixed configuration file name separators
- Fixed global regions aren't first region bug, this was a critical bug
- Fixed Fly, GameMode, Entry and Leave flags, they didn't work the expected way
[size=14pt][b]Beta 0.4:[/b][/size]
- Added warning messages, for disabled flags
- Improved addplayer and removeplayer command
- Added full language file support, for messages and commands
- Added German language, thanks to CloudeLecaw for translation
- Fixed some missing flags bug at the global regions (teleport and editflags flag was missing)
- Disabled recreation of existing regions
- Disabled player adding to strange group
- Improved, /rg info, added priorities
- Removed some debug messages
- Minor other bug fixes/improvements
[size=14pt][b]Beta 0.5:[/b][/size]
- Rearranged add added some commands, just see the command list
- Added flag checks when player joins to server
- Fixed flag checking, when entering to parent region from child region
- Added a tiny player support for modifying region flags, and adding players to it (see commands and permissions)
- Added separate permissions for everything
- Added separate no permission messages for everything
- Language support working totally, you can change plugin language using command /kf lang <language>. Supported languages: en, de
[size=14pt][b]Beta 0.6:[/b][/size]
- Huge performance improvement, now it will cause more then 20x less lag, then before.
- Added group management command /rg group <region> [groupname]
- Added the info command output to /rg player <region> and /rg player <region> <group> command
- Fixed region entering, with disabled entry flag bugging up and also the leaving is fixed, thanks to these huge performance improvement
- Added /rg redefine <region> command for redefining region areas
- Added 2 new flags: ManageGroups and ManagePlayers, with them you can manage, that which groups can manage these things
- Added op-perms to config, if you enable it, then permission checking will be a bit faster, because it won't check the permission if you are op
- Added EntityInterract
- Fixed flying errors, when logged in in creative/spectator mode
- Added noperm message for priority command
- Added region templates, they are permission based, so now also players can create regions without any problem
- Added automatic global region and dimension missing error fixes, so if your regions.yml file is empty, then the plugin will generate the global region about its template, with should be the last, if you don't want any permission checking issues.
- Added EntityInterract flag, it was missing for API issues, but now it's working.

[size=14pt][b]Beta 0.7:[/b][/size]
- Added region limitation permissions, calculating with new creator field of the regions
[b]- No more need for copying ConfigFile to the Rainbow.jar[/b]
- Added region size limitations to the templates, so now you can set the minimal and the maximal size of the region, that each template group can set, these groups are handled using permissions, which you can set to the templates
- Removed redefine command
- Added area command, with redefine, move, extend, decrease, groundair subcommands
- Added information tool, which can be used for getting the list of the regions, where are you staying, [b]not clicking[/b] if you right click with it, at left click you can also get the members of each group of the region.
- Added configurable feature for the region change and move testing intervall
- Fixed player management bugs
- Changed fly flag, now works only for gamemode 0 and 2.
- Removed BlockPlace,ManagePlayers,ManageGroups flags
- Added BookChange, ItemUse, ItemPickup, ExpPickup, ManageRegion flag
- Reorganized the whole code of the plugin, now every command is in separate class file, which means easier access, but bigger plugin size
- Added removeflag command
[b]Overall:[/b] - Totally player support, full area management, reorganized code
[size=14pt][b]Beta 0.7.1:[/b][/size]
- Added german language support, thanks to CloudeLecaw
[size=14pt][b]Beta 0.8:[/b][/size]
- Added selection commands: /rg p1, /rg p2 which also works with coordinates
- Added region containing checks for same region priority, this means if a player creates region, then another player can't create region to it, if the another players template priority is same
- Fixed priority loading bug, it caused many issues, because it loaded every region in same priority, which is really bad for global regions, which needs the lowest priority
- Edited default templates, just remove templates.yml file for accessing the new defaults
- Fixed available tool usage, when you don't have blockbreak/blockinterract flag enabled in the region
- Added permission protectioncore.allowsamepriority for enabling creating same priority regions
[size=14pt][b]Beta 0.9, the last Beta version:[/b][/size]
- Fixed inventory testing bugs
- Added color support for flag values, i.e. you can define colored greeting/farewell messages
- Fixed creating region with template cause template break
- Fixed /area groundair command
- Added /rg gettool command, with that you can get the info and the wandtool
- Added one missing string from language file
- Added full german translation, thanks to CloudeLecaw
[size=14pt][b]Beta 0.9.1:[/b][/size]
- Fixed region defining bug, which were caused by empty flag maps
[size=14pt][b]Beta 0.9.2:[/b][/size]
- Removed annoying flag check debug messages[/spoiler]

[center][size=18pt][b]Changelog 1.0.x versions[/b][/size][/center]
[spoiler][size=14pt][b]1.0.0:[/b][/size]
- Added full permission based tab complete support for everything (commands, regions, groups, flags, players)

[size=14pt][b]1.0.1:[/b][/size]
- Added Russian language
- Added spoilers to plugin description  ;)
- Removed unneccessary blockbreak/blockinterract checks from piston action detection

[size=14pt][b]1.0.2:[/b][/size]
- Added BlockPlace flag with same parameters as BlockInterract has
- Implemented EntityGrief flag with lowercased [url=http://rbow.org/javadoc/PluginReference/MC_MiscGriefType.html]MC_MiscGriefType[/url] parameter
- Added SpectateEntity with entityname and my custom entity type parameter (passive, hostile e.t.c).
[b]- Be carefull, this update overwrites the current templates.yml file.[/b]
[size=14pt][b]1.0.3:[/b][/size]
- Fixed permission checking bugs
[b]- You should set the new flags, i.e. BlockPlace in every created region.[/b]
[size=14pt][b]1.0.4:[/b][/size]
- Added multiworld support
[size=14pt][b]1.0.5:[/b][/size]
- Fixed multiworld support
[size=14pt][b]1.0.6:[/b][/size]
- Fixed multiworld support again  :(
[size=14pt][b]1.0.7:[/b][/size]
- Fixed pvp flagcheck, added [url=http://rbow.org/javadoc/PluginReference/MC_DamageType.html]dmgtype[/url] parameter to it.
- Removed some debug messages

[size=14pt][b]1.0.8:[/b][/size]
- Fixed region based permission management, now it uses Api-Collections PermissionAPI for handling the permissions, so you can define [b]per region permissions[/b] using Permissions flag in the PermissionAPIs permission format ([!][-]<permission>[ * ])
- Now region entry and leave flags aren't checked, when you are teleported in/out to a region
- Fixed saving bug
- Improved reloading, now the reload command reloads everything, not only the regions[/spoiler]

[center][size=18pt][b]Changelog 1.1.x versions[/b][/size][/center]

[size=14pt][b]1.1.0:[/b][/size]
- [b]Added creationBlock support[/b] for Ponderas request, which means, you can define blocks for each regiontype, which can be used for creating regions. Defaultly these blocks are coal ore and gold block. You need to set the CreationBlock flag in templates to define these blocks. This flag default value for player region template: [b]-14:0 14:0-5-5-5 16 16-10-5-10[/b] So you need to start it with - and continue it with the blocks, each protection block should have defined 2 times: just for it's id (14:0 or 16), for it's relative protected area (blockid-relx-rely-relz), you must use there the same blockid, that you used first time.

- Moved all the command management to the default command class for console support

- Added console support, which means, you can manage almost everything from the console

- Added filter to area command for disabling modifying  region areas, which are created with RegionCreationBlocks.

- Added dimension prefix for region names, there are optional in player command usage, but they are neccessary in console command usage. For example if you have a test region in the nether, you have 2 possibilities to get information about it:
1. The old way: go to the nether, and use [b]/rg info test[/b] command
2. The new way: use [b]/rg info 1:test[/b] command, which also works from console

- Reorganized region storage, removed all the remaining -1 pointers from previous per dimension regions management

- Added creator and protectingBlock coordinates to the /rg info regionname command

- Fixed some bugs

- Added is valid number filter for priority command

- Added configuration for silk-touch breaking the region creation block.

- Added dimension parameter for list command

[size=14pt][b]1.1.1:[/b][/size]

- Fixed command argument bugs, caused by previous reorganizing
- Fixed region creating block management in regions, which has same or higher priority, then the region creating blocks region
- Fixed permission checking bugs, when a player is died
- Fixed entity spectating flag checks, when you aren't spectating an entity
- Added template parameter for define command, so now the new define command is [b]/rg define <name> [template][/b]. You need to have the templates permission for using it.

[size=14pt][b]1.1.2:[/b][/size]
- Fixed region limit not working bug
- Fixed creation block region name management bug
- Fixed flag value setting error
- Fixed selecting bugs using selection commands

[size=14pt][b]1.2:[/b][/size]
- Fixed tab-complete bugs
- Fixed area command direction management bugs
- Fixed some console usage bugs
- Added a new optional "copyable group" parameter for group command, so now you can easily copy any groups flag settings to a new group, using command [b]/rg group <region> <newgroupname> [group-name-which-flags-will-be-copyed][/b]
- Removed unneccessary disableflagcheck perm checks for PVP and PlayerDamage flags
- Added [b]UUID[/b] support, don't worry about the conversion, it's done automatically, maybe it will cause a bit of slower server startup at the first time, if you have too many regions
[size=18pt][color=red][font=arial black]- Make sure, to update your Api-Collection to the latest "development version", without it the whole plugin won't work[/font][/color][/size]


[size=18pt][b]Suggested permissions for basic players:[/b][/size]
[spoiler]protectioncore.use
protectioncore.select
protectioncore.command.area
protectioncore.command.define
protectioncore.command.remove
protectioncore.command.info
protectioncore.command.player
protectioncore.command.group
protectioncore.command.flag
protectioncore.command.removeflag
protectioncore.regiontype.player
protectioncore.limit.3
[size=12pt][b]IMPORTANT:[/b][/size]
- If you want players to use regionCreation blocks, instead of the selection, you can simple don't give them the [b]protectioncore.select[/b] permission
- Never give protectioncore.command.priority permission to players, because it can cause lot of unexpected problems
- Never give protectioncore.disableflagcheck.editflags permission to players, because it will break the whole flag setting limitations for players
[/spoiler]