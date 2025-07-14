package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.entity.Channel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class ChannelTitleValidator implements ConstraintValidator<ValidChannelTitle, ChannelTitleProvider> {
    private static final Pattern ALLOWED_CHARS_PATTERN = Pattern.compile("^[a-zA-Z0-9가-힣 ]*$");

    @Override
    public boolean isValid(ChannelTitleProvider request, ConstraintValidatorContext context) {
        String title = request.title();

        if(!StringUtils.hasText(title)) {
            return setInvalid(context, "채널 이름은 비어있을 수 없습니다.");
        }
        String trimmedTitle = title.trim();

        if(trimmedTitle.length() > Channel.MAX_TITLE_LENGTH || trimmedTitle.length() < Channel.MIN_TITLE_LENGTH) {
            return setInvalid(context, "채널 이름은 " + Channel.MIN_TITLE_LENGTH + "자에서 " + Channel.MAX_TITLE_LENGTH + "자 사이여야 합니다 (양쪽 끝 공백 제외).");
        }

        if (!ALLOWED_CHARS_PATTERN.matcher(title).matches()) {
            return setInvalid(context, "채널 이름에는 영문, 한글, 숫자, 띄어쓰기만 허용됩니다.");
        }
        return true;
    }

    private boolean setInvalid(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("title")
                .addConstraintViolation();
        return false;
    }
}
