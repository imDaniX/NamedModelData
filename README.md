# NamedModelData
A simple Spigot plugin that allows you to apply CustomModelData tag to an item through anvil by item renaming, just like OptiFine.

![d8c4f9e637deed0566f573552a6eac75d98bc4bf](https://github.com/imDaniX/NamedModelData/assets/18292506/501c52cf-d354-443f-9026-51269a5ac911)

NMD **doesn't offer** you any resource packs - you have to provide one to players yourself.
CustomModelData will be applied only if you're renaming an item **through anvil**, so for existing item you have to reapply its name.
Plugin has an option to ignore colors if you have some plugin that allows renaming items through anvil with colors.

### Commands
Currently NMD has only one command - to reload the plugin.
`/namedmodeldata` (alias `/nmd`) - permission `namedmodeldata.admin`

### Configuration
```yaml
# MATERIAL
IRON_SHOVEL:
  # CustomModelData value
  # Required integer
  '1':
    # Item name
    # Required string
    name: 'magic wand'
    # Ignore colors in the name
    # Optional boolean, defaults to false
    ignore-color: true
    # Ignore case in the name
    # Optional boolean, defaults to false
    ignore-case: true
  '5':
    name: '&4Iron Shovel of Doom'
DIAMOND:
  '1':
    name: '&cRuby'
  '2':
    name: 'Unalivemond'
    ignore-color: true
```

## Build
Maven and Java 11 required. Just `mvn clean package`
