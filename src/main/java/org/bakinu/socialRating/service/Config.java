package org.bakinu.socialRating.service;

import lombok.Getter;
import net.elytrium.serializer.SerializerConfig;
import net.elytrium.serializer.language.object.YamlSerializable;

@Getter
public class Config extends YamlSerializable {
    private static final SerializerConfig CONFIG = new SerializerConfig.Builder().build();

    public Config() {
        super(Config.CONFIG);
    }

}
