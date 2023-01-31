package com.local.meetup.reconcilers;

import com.local.meetup.apis.MeetUpRequest;
import com.local.meetup.StatusUtil;
import com.local.meetup.TemplateLoaderUtil;
import com.local.meetup.apis.ConditionType;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.local.meetup.apis.ConditionStatus.TRUE;

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
        StatusUtil.updateStatusCondition(meetUpRequest.getStatus(), ConditionType.CONFIGMAP_READY, TRUE);
    }

    private ConfigMap createRequestedConfigMap(MeetUpRequest meetUpRequest) {
        ConfigMap requestedConfigMap = TemplateLoaderUtil.loadMeetUpConfigMap();
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
