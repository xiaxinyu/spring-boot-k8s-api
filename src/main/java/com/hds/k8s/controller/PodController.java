package com.hds.k8s.controller;

import com.hds.k8s.core.K8sClient;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class PodController {
    private static final Logger log = LoggerFactory.getLogger(PodController.class);

    @GetMapping("/pod")
    public String sayCalculation(@RequestParam(required = true, value = "namespace", defaultValue = "kube-system") String namespace) {
        String kubeConfigPath = "/Users/xinyu.xia/Documents/workspace/project/hds/spring-boot-k8s-api/src/main/resources/kube-config";
        if (!new File(kubeConfigPath).exists()) {
            System.out.println("kubeConfig not exist，jump over");
            return "";
        }
        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        V1PodList podList = k8sClient.getAllPodList(namespace);
        for (V1Pod item : podList.getItems()) {
            System.out.println(item.getMetadata().getNamespace() + ":" + item.getMetadata().getName());
        }
        return "success";
    }
}
