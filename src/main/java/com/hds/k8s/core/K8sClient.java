package com.hds.k8s.core;

import com.hds.k8s.controller.PodController;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;


public class K8sClient {
    private static final Logger log = LoggerFactory.getLogger(PodController.class);

    private ApiClient apiClient;

    /**
     * loading the in-cluster config, including:
     * 1. service-account CA
     * 2. service-account bearer-token
     * 3. service-account namespace
     * 4. master endpoints(ip, port) from pre-set environment variables
     */
    public K8sClient() {
        try {
            this.apiClient = ClientBuilder.cluster().build();
        } catch (IOException e) {
            log.error("build K8s-Client error", e);
            throw new RuntimeException("build K8s-Client error");
        }
    }

    /**
     * loading the out-of-cluster config, a kubeconfig from file-system
     *
     * @param kubeConfigPath
     */
    public K8sClient(String kubeConfigPath) {
        try {
            this.apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        } catch (IOException e) {
            log.error("read kubeConfigPath error", e);
            throw new RuntimeException("read kubeConfigPath error");
        } catch (Exception e) {
            log.error("build K8s-Client error", e);
            throw new RuntimeException("build K8s-Client error");
        }
    }

    /**
     * get all Pods
     *
     * @return podList
     */
    public V1PodList getAllPodList(String namespace) {
        // new a CoreV1Api
        CoreV1Api api = new CoreV1Api(apiClient);

        // invokes the CoreV1Api client
        try {
            return api.listNamespacedPod(namespace).execute();
        } catch (ApiException e) {
            log.error("get podlist error:" + e.getResponseBody(), e);
        }
        return null;
    }
}