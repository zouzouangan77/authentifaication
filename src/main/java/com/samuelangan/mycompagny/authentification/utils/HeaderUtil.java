package com.samuelangan.mycompagny.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public final class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    private static final String APPLICATION_NAME = "mycompagnyApp";

    private HeaderUtil() {
        // Classe utilitaire : empêche l'instanciation
    }

    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);
        headers.add("X-" + applicationName + "-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String applicationName, String entityName, String param) {
        return createAlert(applicationName, entityName + " created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String applicationName, String entityName, String param) {
        return createAlert(applicationName, entityName + " updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String applicationName, String entityName, String param) {
        return createAlert(applicationName, entityName + " deleted", param);
    }

    public static HttpHeaders createFailureAlert(String applicationName, String entityName, String errorKey, String defaultMessage) {
        log.error("Entity processing failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", defaultMessage);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }
}
