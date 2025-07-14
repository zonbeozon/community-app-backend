package com.zonbeozon.channel.validation;

import com.zonbeozon.channel.enums.ChannelContentVisibility;
import com.zonbeozon.channel.enums.ChannelJoinPolicy;
import com.zonbeozon.channel.enums.ChannelSearchScope;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ChannelSettingValidator implements ConstraintValidator<ValidChannelSetting, ChannelSettingProvider> {
    @Override
    public boolean isValid(ChannelSettingProvider request, ConstraintValidatorContext context) {
        ChannelJoinPolicy joinPolicy = request.joinPolicy();
        ChannelContentVisibility contentVisibility = request.contentVisibility();
        ChannelSearchScope searchScope = request.searchScope();

        boolean isValid = true;
        /**
         * contentVisibility가 public이지만 searchScope가 NONE일수는 없다
         */
        if(contentVisibility == ChannelContentVisibility.PUBLIC && searchScope == ChannelSearchScope.NONE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("contentVisibility가 public이지만 searchScope가 NONE일수는 없다")
                    .addPropertyNode("setting")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }
}
