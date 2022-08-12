package de.zemux1613.autobroadcast;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class Autobroadcast extends Plugin {

    private List<String> messages;

    @Override
    public void onEnable() {
        messages = Lists.newArrayList();
        loadConfig();
        AtomicInteger i = new AtomicInteger();
        ProxyServer.getInstance().getScheduler().schedule(this, () -> {
            if (i.get() >= messages.size()) {
                i.set(0);
            }

            ProxyServer.getInstance().broadcast(TextComponent.fromLegacyText(messages.get(i.get()).replace("&", "§")));
            i.getAndIncrement();
        }, 0, 5, TimeUnit.MINUTES);
    }

    private void loadConfig() {
        final File file = new File(getDataFolder(), "config.yml");

        boolean init = false;
        if (!file.exists()) {
            file.getParentFile().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            init = true;

        }

        Configuration config = null;
        try {

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            if (init) {
                config.set("messages", Arrays.asList("1", "2"));
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.messages = config.getStringList("messages");

    }
}
