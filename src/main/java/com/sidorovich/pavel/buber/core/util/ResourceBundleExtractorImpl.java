package com.sidorovich.pavel.buber.core.util;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;
import com.sidorovich.pavel.buber.api.util.ResourceBundleExtractor;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceBundleExtractorImpl implements ResourceBundleExtractor {

    private static final String LANGUAGE_COOKIE_NAME = "lang";
    private static final String LOCALE_REGEX = "(\\w{2})_(\\w{2})";

    private ResourceBundleExtractorImpl() {
    }

    @Override
    public ResourceBundle extractResourceBundle(CommandRequest request, String baseName) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<Cookie> lang = Arrays.stream(cookies)
                                          .filter(cookie -> cookie.getName().equals(LANGUAGE_COOKIE_NAME))
                                          .findAny();
            if (lang.isPresent()) {
                Matcher matcher = Pattern.compile(LOCALE_REGEX).matcher(lang.get().getValue());
                if (matcher.find()) {
                    return ResourceBundle.getBundle(baseName, new Locale(matcher.group(1), matcher.group(2)));
                }
            }
        }

        return ResourceBundle.getBundle(baseName, Locale.US);
    }

    public static ResourceBundleExtractorImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ResourceBundleExtractorImpl INSTANCE = new ResourceBundleExtractorImpl();
    }

}
