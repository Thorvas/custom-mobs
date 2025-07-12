# Custom Mobs Plugin

This plugin includes a simple spell leveling system. Damage for fireball and meteor spells is calculated using the following formula:

```
damage = (5 * experience) / (experience + 15)
```

Where `experience` is the experience level stored inside each spell instance. As the spell gains experience, the computed damage grows accordingly.
