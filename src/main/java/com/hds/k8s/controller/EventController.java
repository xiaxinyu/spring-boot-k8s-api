package com.hds.k8s.controller;

import com.hds.k8s.core.K8sClient;
import io.kubernetes.client.openapi.models.CoreV1Event;
import io.kubernetes.client.openapi.models.CoreV1EventList;
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
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private String kubeConfigPath = "/Users/xinyu.xia/Documents/workspace/project/hds/spring-boot-k8s-api/src/main/resources/kube-config";

    @GetMapping("/event")
    public List<String> listEvent(@RequestParam(required = true, value = "namespace", defaultValue = "kube-system") String namespace) {
        if (!verifyKubeConfig()) {
            return Lists.newArrayList();
        }

        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        CoreV1EventList eventList = k8sClient.getEventList(namespace);

        List<String> list = new ArrayList<>();
        for (CoreV1Event item : eventList.getItems()) {
            String name = item.getMetadata().getName();
            String reason = item.getReason();
            String type = item.getType();
            String kind = item.getKind();
            Integer count = item.getCount();
            list.add(String.format("%s | %s | %s | %d | %s", name, type, kind, count, reason));
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
