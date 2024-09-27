package com.hds.k8s.controller;

import com.hds.k8s.core.K8sClient;
import com.hds.k8s.utils.MapUtils;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PodController {
    private static final Logger log = LoggerFactory.getLogger(PodController.class);
    private String kubeConfigPath = "/Users/xinyu.xia/Documents/workspace/project/hds/spring-boot-k8s-api/src/main/resources/kube-config";

    @GetMapping("/pod")
    public List<String> listPod(@RequestParam(required = true, value = "namespace", defaultValue = "kube-system") String namespace,
                                @RequestParam(required = false, value = "label") String label) {
        if (!verifyKubeConfig()) {
            return Lists.newArrayList();
        }

        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        V1PodList podList = k8sClient.getPodList(namespace, label);

        List<String> list = new ArrayList<>();
        for (V1Pod item : podList.getItems()) {
            String name = item.getMetadata().getName();
            String labels = MapUtils.format(item.getMetadata().getLabels());
            list.add(String.format("%s | %s", name, labels));
        }
        return list;
    }

    private boolean verifyKubeConfig() {

        if (!new File(kubeConfigPath).exists()) {
            log.info("kubeConfig not exist，jump over");
            return false;
        }

        return true;
    }
}
