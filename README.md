# Custom Mobs Plugin

This plugin includes a simple spell leveling system based on total experience points (XP).
Damage for fireball and meteor spells is calculated using the following formula, where `level`
is derived from XP:

```
damage = (5 * level) / (level + 15)
```

The `experience` field on each spell now stores the cumulative XP earned. Levels are computed
using `SpellExperienceUtil.levelForExperience(xp)`. The utility mirrors Minecraft's experience
curve:

- Level 1 starts at 7 XP
- Level 2 starts at 16 XP
- Level 3 starts at 27 XP

You can check how much XP is required for the next level using `SpellExperienceUtil.xpNeeded(xp)`.
