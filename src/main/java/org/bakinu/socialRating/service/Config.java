package org.bakinu.socialRating.service;

import lombok.Getter;
import net.elytrium.serializer.SerializerConfig;
import net.elytrium.serializer.language.object.YamlSerializable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Config extends YamlSerializable {
    private static final SerializerConfig CONFIG = new SerializerConfig.Builder().build();

    public Config() {
        super(Config.CONFIG);
    }

    private Command command = new Command();
    private LowRatingEvents lowRatingEvents = new LowRatingEvents();

    @Getter
    public static final class Command {
        private Command.GetRating getrating = new Command.GetRating();
        private Command.SetRating setrating = new Command.SetRating();
        private Command.SrReload srreload = new Command.SrReload();

        @Getter
        public static final class GetRating {
            private String message = "<color:#cb52ff>Rating of <color:#e0a2fa>{player_name}</color>: <color:#e0a2fa>{rating}";
        }
        @Getter
        public static final class SetRating {
            private String message = "<color:#cb52ff>Rating of <color:#e0a2fa>{player_name}</color> setted to <color:#e0a2fa>{value}</color>, new rating: <color:#e0a2fa>{rating}";
        }
        @Getter
        public static final class SrReload {
            private String message = "<color:#cb52ff>Plugin reloaded!";
        }
    }

    @Getter
    public static final class LowRatingEvents {
        private MuteChat muteChat = new MuteChat();

        @Getter
        public static final class MuteChat {
            private boolean enabled = false;
            private int rating = 90;
            private String message = "<red>You can't speak because you'r rating is too low";
        }

        private MuteCommands muteCommands = new MuteCommands();

        @Getter
        public static final class MuteCommands {
            private boolean enabled = false;
            private int rating = 90;
            private String message = "";
            private List<String> blockedCommands = new ArrayList<>(List.of(
                    "/me"
            ));
        }
    }
}
