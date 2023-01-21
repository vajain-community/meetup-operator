package com.k8s.meetup.controllers;

import com.k8s.meetup.apis.MeetUpRequest;
import com.k8s.meetup.reconcilers.MeetUpConfigMapReconciler;
import com.k8s.meetup.reconcilers.MeetUpDeploymentReconciler;
import com.k8s.meetup.services.MeetUpService;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

import javax.inject.Inject;

@ControllerConfiguration
public class MeetUpController implements Reconciler<MeetUpRequest> {

    @Inject
    KubernetesClient kubernetesClient;

    @Inject
    MeetUpConfigMapReconciler meetUpConfigMapReconciler;

    @Inject
    MeetUpDeploymentReconciler meetUpDeploymentReconciler;

    @Inject
    MeetUpService meetUpService;

    @Override
    public UpdateControl<MeetUpRequest> reconcile(MeetUpRequest meetUpRequest, Context<MeetUpRequest> context) throws Exception {

        // reconcile configmap
        meetUpConfigMapReconciler.reconcile(meetUpRequest);

        // reconcile deployment
        meetUpDeploymentReconciler.reconcile(meetUpRequest);

        if (meetUpService.isMeetUpStatusChange(meetUpRequest)) {
            return UpdateControl.updateStatus(meetUpRequest);
        }
        return UpdateControl.noUpdate();
    }
}
