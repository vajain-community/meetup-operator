package com.k8s.meetup.reconcilers;

import com.k8s.meetup.apis.MeetUpRequest;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.k8s.meetup.StatusUtil.updateStatusCondition;
import static com.k8s.meetup.TemplateLoaderUtil.loadMeetUpConfigMap;
import static com.k8s.meetup.apis.ConditionStatus.TRUE;
import static com.k8s.meetup.apis.ConditionType.CONFIGMAP_READY;

@ApplicationScoped
public class MeetUpConfigMapReconciler {

    @Inject
    KubernetesClient kubernetesClient;

    public void reconcile(MeetUpRequest meetUpRequest) {

        // create requested resource
        ConfigMap requestedConfigMap = createRequestedConfigMap(meetUpRequest);

        // fetch deployed resource
        ConfigMap deployedConfigMap = fetchDeployedConfigMap(meetUpRequest);

        // process delta
        processDelta(requestedConfigMap, deployedConfigMap);

        // update status
        updateStatusCondition(meetUpRequest.getStatus(), CONFIGMAP_READY, TRUE);
    }

    private ConfigMap createRequestedConfigMap(MeetUpRequest meetUpRequest) {
        ConfigMap requestedConfigMap = loadMeetUpConfigMap();
        requestedConfigMap.getData().put("meetup.user.name", meetUpRequest.getSpec().getUserName());
        return requestedConfigMap;
    }

    private ConfigMap fetchDeployedConfigMap(MeetUpRequest meetUpRequest) {
        return kubernetesClient.resources(ConfigMap.class)
                .inNamespace(meetUpRequest.getMetadata().getNamespace())
                .withName("meetup-config")
                .get();
    }

    private void processDelta(ConfigMap requestedConfigMap, ConfigMap deployedConfigMap) {
        if (deployedConfigMap == null || !requestedConfigMap.getData().equals(deployedConfigMap.getData())) {
            kubernetesClient.resources(ConfigMap.class)
                    .inNamespace(requestedConfigMap.getMetadata().getNamespace())
                    .withName(requestedConfigMap.getMetadata().getName())
                    .createOrReplace();
        }
    }
}
