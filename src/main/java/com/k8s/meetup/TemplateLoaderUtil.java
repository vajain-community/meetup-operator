package com.k8s.meetup;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.utils.Serialization;

import java.io.IOException;
import java.io.InputStream;

public class TemplateLoaderUtil {

    private static String TEMPLATE_BASE_PATH = "/template";

    public static ConfigMap loadMeetUpConfigMap() {
        return loadYaml(ConfigMap.class, TEMPLATE_BASE_PATH+"meetup-configmap");
    }

    public static Deployment loadMeetUpDeployment() {
        return loadYaml(Deployment.class, TEMPLATE_BASE_PATH+"meetup-deployment");
    }

    static protected <T> T loadYaml(Class<T> clazz, String yaml) {
        try (InputStream is = TemplateLoaderUtil.class.getResourceAsStream(yaml)) {
            return Serialization.unmarshal(is, clazz);
        } catch (IOException ex) {
            throw new IllegalStateException("Cannot find yaml on classpath: " + yaml);
        }
    }
}
