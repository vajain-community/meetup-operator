package com.local.meetup.reconcilers;

import com.local.meetup.apis.MeetUpRequest;
import com.local.meetup.StatusUtil;
import com.local.meetup.TemplateLoaderUtil;
import com.local.meetup.apis.ConditionType;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static com.local.meetup.apis.ConditionStatus.FALSE;
import static com.local.meetup.apis.ConditionStatus.TRUE;

@ApplicationScoped
public class MeetUpDeploymentReconciler {

    @ConfigProperty(name = "meetup.image")
    String meetupImage;

    @Inject
    KubernetesClient kubernetesClient;

    public void reconcile(MeetUpRequest meetUpRequest) {
        // create requested resource
        Deployment requestedDeployment = createRequestedDeployment(meetUpRequest);

        // fetch deployed resource
        Deployment deployedDeployment = fetchDeployedDeployment(meetUpRequest);

        // process delta
        processDelta(requestedDeployment, deployedDeployment);

        // update status
        if (isMeetUpDeploymentReady(meetUpRequest)) {
            StatusUtil.updateStatusCondition(meetUpRequest.getStatus(), ConditionType.DEPLOYMENT_READY, TRUE);
        } else {
            StatusUtil.updateStatusCondition(meetUpRequest.getStatus(), ConditionType.DEPLOYMENT_READY, FALSE);
        }
    }

    private Deployment createRequestedDeployment(MeetUpRequest meetUpRequest) {
        Deployment requestedDeployment = TemplateLoaderUtil.loadMeetUpDeployment();
        requestedDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(meetupImage);
        return requestedDeployment;
    }

    private Deployment fetchDeployedDeployment(MeetUpRequest meetUpRequest) {
        return kubernetesClient.resources(Deployment.class)
                .inNamespace(meetUpRequest.getMetadata().getNamespace())
                .withName("meetup-deployment")
                .get();
    }

    private void processDelta(Deployment requestedDeployment, Deployment deployedDeployment) {
        if (deployedDeployment == null || !isDeploymentEqual(requestedDeployment, deployedDeployment)) {
            kubernetesClient.resources(Deployment.class)
                    .inNamespace(requestedDeployment.getMetadata().getNamespace())
                    .withName(requestedDeployment.getMetadata().getName())
                    .get();
        }
    }

    private boolean isDeploymentEqual(Deployment deployment1, Deployment deployment2) {
        return deployment1.getSpec().getTemplate().getSpec().getContainers().get(0).getImage().equals(deployment2.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
    }

    private boolean isMeetUpDeploymentReady(MeetUpRequest meetUpRequest) {
        Deployment deployment = kubernetesClient.resources(Deployment.class)
                .inNamespace(meetUpRequest.getMetadata().getNamespace())
                .withName("meetup-deployment")
                .get();
        return deployment != null && deployment.getStatus().getAvailableReplicas() > 0;
    }
}
