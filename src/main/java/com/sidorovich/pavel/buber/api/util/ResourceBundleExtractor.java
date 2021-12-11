package com.sidorovich.pavel.buber.api.util;

import com.sidorovich.pavel.buber.api.controller.CommandRequest;

import java.util.ResourceBundle;

public interface ResourceBundleExtractor {

    ResourceBundle extractResourceBundle(CommandRequest request, String baseName);

}
