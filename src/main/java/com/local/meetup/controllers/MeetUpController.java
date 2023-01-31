package com.local.meetup.controllers;

import com.local.meetup.apis.MeetUpRequest;
import com.local.meetup.reconcilers.MeetUpConfigMapReconciler;
import com.local.meetup.reconcilers.MeetUpDeploymentReconciler;
import com.local.meetup.services.MeetUpService;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.quarkiverse.operatorsdk.bundle.runtime.CSVMetadata;

import javax.inject.Inject;

import static io.javaoperatorsdk.operator.api.reconciler.Constants.WATCH_CURRENT_NAMESPACE;

@CSVMetadata(permissionRules = @CSVMetadata.PermissionRule(apiGroups = "meetup.local.io", resources = "meetuprequests"), icon = @CSVMetadata.Icon(fileName = "icon.png", mediatype = "image/png"))
@ControllerConfiguration(namespaces = WATCH_CURRENT_NAMESPACE)
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
