package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChannelSettingValidator implements ConstraintValidator<ValidChannelSetting, ChannelSettingProvider> {
    @Override
    public boolean isValid(ChannelSettingProvider request, ConstraintValidatorContext context) {
        ChannelJoinPolicy joinPolicy = request.joinPolicy();
        ChannelContentVisibility visibility = request.contentVisibility();

        boolean isValid = true;
        /**
         * visibility가 public이지만 joinPolicy NONE일수는 없다
         */
        if(visibility == ChannelContentVisibility.PUBLIC && joinPolicy == ChannelJoinPolicy.DENY) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("visibility가 public이지만 joinPolicy가 DENY일수는 없습니다")
                    .addPropertyNode("setting")
                    .addConstraintViolation();
            isValid = false;
        }

        if(visibility == ChannelContentVisibility.PRIVATE && joinPolicy != ChannelJoinPolicy.DENY) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("visibility가 private이라면 joinPolicy는 DENY이여야 합니다.")
                    .addPropertyNode("setting")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }
}
