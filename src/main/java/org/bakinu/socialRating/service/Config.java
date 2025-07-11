package org.bakinu.socialRating.service;

import lombok.Getter;
import net.elytrium.serializer.SerializerConfig;
import net.elytrium.serializer.annotations.Comment;
import net.elytrium.serializer.annotations.CommentValue;
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

    @Comment(@CommentValue("Base rating value when player joining to server first time"))
    private int baseRating = 100;

    @Getter
    public static final class Command {
        private Command.GetRating getrating = new Command.GetRating();
        private Command.SetRating setrating = new Command.SetRating();
        private Command.SrReload srreload = new Command.SrReload();
        private Command.TopRating topRating = new Command.TopRating();

        @Getter
        public static final class GetRating {
            private String message = "<color:#cb52ff>Rating of <color:#e0a2fa>{player_name}</color>: <color:#e0a2fa>{rating}";
        }
        @Getter
        public static final class SetRating {
            private String message = "<color:#cb52ff>Rating of <color:#e0a2fa>{player_name}</color> setted to <color:#e0a2fa>{value}</color>, new rating: <color:#e0a2fa>{rating}";
        }
        @Getter
        public static final class TopRating {
            @Comment(@CommentValue("Limit for table with highest ratings"))
            private int limit = 10;
            private String message = "<color:#9636bf>{index}. <color:#cb52ff>{player_name} <color:#e0a2fa>{rating}";
        }
        @Getter
        public static final class SrReload {
            private String message = "<color:#cb52ff>Plugin reloaded!";
        }
    }

    @Getter
    public static final class LowRatingEvents {
        private MuteChat muteChat = new MuteChat();

        @Comment(@CommentValue("When a player's rating is too low, they cant speak in chat"))
        @Getter
        public static final class MuteChat {
            private boolean enabled = true;
            private int rating = 30;
            private String message = "<red>You can't speak because your rating is too low";
        }

        private MuteCommands muteCommands = new MuteCommands();

        @Comment(@CommentValue("When a player's rating is too low, they cant use commands"))
        @Getter
        public static final class MuteCommands {
            private boolean enabled = true;
            private int rating = 30;
            private String message = "<red>You can't use commands because your rating is too low";
            private List<String> blockedCommands = new ArrayList<>(List.of(
                    "/me"
            ));
        }
    }
}
