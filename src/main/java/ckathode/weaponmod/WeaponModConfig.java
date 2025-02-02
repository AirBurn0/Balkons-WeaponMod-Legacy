package ckathode.weaponmod;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraftforge.common.config.Configuration;

public class WeaponModConfig {
    private final Configuration config;
    public boolean cannonDoesBlockDamage;
    public boolean dynamiteDoesBlockDamage;
    public boolean mortarDoesBlockDamage;
    public boolean canThrowKnife;
    public boolean canThrowSpear;
    public boolean allCanPickup;
    public boolean guiOverlayReloaded;
    // TODO: public boolean itemModelForEntity;
    private final Map<String, EnableSetting> enableSettings;
    private final Map<String, ReloadTimeSetting> reloadTimeSettings;
    private final Map<String, DataWatcherIdSetting> dataWatcherIds;

    public WeaponModConfig(final Configuration configuration) {
        config = configuration;
        enableSettings = new LinkedHashMap<>();
        reloadTimeSettings = new LinkedHashMap<>();
        dataWatcherIds = new LinkedHashMap<>();
    }

    public void addEnableSetting(final String weapon) {
        enableSettings.put(weapon, new EnableSetting(weapon, true));
    }

    public void addReloadTimeSetting(final String weapon, final int defaulttime) {
        reloadTimeSettings.put(weapon, new ReloadTimeSetting(weapon, defaulttime));
    }

    public void addDataWatcherIdSetting(final String desc, final int defaultId) {
        dataWatcherIds.put(desc, new DataWatcherIdSetting(desc, defaultId));
    }

    public boolean isEnabled(final String weapon) {
        final EnableSetting es = enableSettings.get(weapon);
        return es == null || es.enabled;
    }

    public int getReloadTime(final String weapon) {
        final ReloadTimeSetting rs = reloadTimeSettings.get(weapon);
        return (rs == null) ? 0 : rs.reloadTime;
    }

    public int getDataWatcherId(final String desc) {
        final DataWatcherIdSetting ds = dataWatcherIds.get(desc);
        return (ds == null) ? 0 : ds.id;
    }

    public void loadConfig() {
        config.load();
        config.addCustomCategoryComment("enable", "Enable or disable certain weapons "
                                                  + "(only disables their recipes; they are still obtainable "
                                                  + "through Creative mode!)");
        config.addCustomCategoryComment("reloadtime", "The reload durations of the reloadable weapons");
        config.addCustomCategoryComment("settings", "Miscellaneous mod settings");
        config.addCustomCategoryComment("datawatcher", "Data Watcher IDs - if your game crashes because there is a "
                                                       + "conflict with another mod's Data Watcher entries, you can "
                                                       + "fix it here by changing the ID(s)!");
        cannonDoesBlockDamage = config.get("settings", "cannon-block-damage", true).getBoolean(true);
        dynamiteDoesBlockDamage = config.get("settings", "dynamite-block-damage", true).getBoolean(true);
        mortarDoesBlockDamage = config.get("settings", "mortar-block-damage", true).getBoolean(true);
        canThrowKnife = config.get("settings", "can-throw-knife", true).getBoolean(true);
        canThrowSpear = config.get("settings", "can-throw-spear", true).getBoolean(true);
        allCanPickup = config.get("settings", "pickup-all", true, "Change this to 'false' to allow only the"
                                                                  + " thrower/shooter of the projectile to "
                                                                  + "pick the item up. If set to 'true' "
                                                                  + "everyone can pick the item up.").getBoolean(true);
        guiOverlayReloaded = config.get("settings", "reload-progress", true, "Show reload progress in "
                                                                             + "hotbar.").getBoolean(true);
        /*itemModelForEntity = config.get("settings", "render-entity-model", true, "Item model for entity "
                                                                       + "(knife, spear, etc).").getBoolean(true);*/
        for (final EnableSetting es : enableSettings.values()) {
            es.enabled = config.get("enable", es.settingName, es.enabled).getBoolean(es.enabled);
        }
        for (final ReloadTimeSetting rs : reloadTimeSettings.values()) {
            rs.reloadTime = config.get("reloadtime", rs.settingName, rs.reloadTime).getInt(rs.reloadTime);
        }
        for (final DataWatcherIdSetting ds : dataWatcherIds.values()) {
            ds.id = config.get("datawatcher", ds.settingName, ds.id).getInt(ds.id);
        }
        config.save();
    }

    private abstract static class Setting {
        final String settingName;

        Setting(final String name) {
            settingName = name;
        }
    }

    private static class ReloadTimeSetting extends Setting {
        int reloadTime;

        ReloadTimeSetting(final String name, final int time) {
            super(name + ".reloadtime");
            reloadTime = time;
        }
    }

    private static class EnableSetting extends Setting {
        boolean enabled;

        EnableSetting(final String name, boolean enabled) {
            super(name + ".enabled");
            this.enabled = enabled;
        }
    }

    private static class DataWatcherIdSetting extends Setting {
        int id;

        DataWatcherIdSetting(final String desc, int id) {
            super(desc);
            this.id = id;
        }
    }
}
