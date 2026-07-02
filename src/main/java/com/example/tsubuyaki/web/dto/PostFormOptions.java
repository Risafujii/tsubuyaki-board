package com.example.tsubuyaki.web.dto;

import com.example.tsubuyaki.domain.AvatarColor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class PostFormOptions {

    public List<String> avatarColors() {
        return AvatarColor.names();
    }

    public List<AvatarColorOption> avatarColorOptions() {
        return AvatarColor.names().stream()
                .map(AvatarColor::from)
                .map(AvatarColorOption::from)
                .toList();
    }

    public record AvatarColorOption(String value, String displayName, String cssClass) {

        private static AvatarColorOption from(AvatarColor color) {
            return new AvatarColorOption(
                    color.name(),
                    color.getDisplayName(),
                    "post__avatar-color--" + color.name().toLowerCase(Locale.ROOT));
        }
    }
}
