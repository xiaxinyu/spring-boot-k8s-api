package com.hds.k8s.controller;

import com.hds.k8s.core.K8sClient;
import com.hds.k8s.utils.DateUtils;
import com.hds.k8s.utils.MapUtils;
import com.hds.k8s.utils.ResourceType;
import io.kubernetes.client.openapi.models.*;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class EventController {
    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private String kubeConfigPath = "/Users/xinyu.xia/Documents/workspace/project/hds/spring-boot-k8s-api/src/main/resources/kube-config";

    @GetMapping("/event/pod")
    public List<String> listPodEvent(@RequestParam(required = true, value = "namespace", defaultValue = "kube-system") String namespace,
                                     @RequestParam(required = true, value = "pod", defaultValue = "") String pod) {
        if (!verifyKubeConfig()) {
            return Lists.newArrayList();
        }

        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        CoreV1EventList eventList = k8sClient.getEventList(namespace);

        return formatEvent(ResourceType.POD, pod, eventList);
    }

    @GetMapping("/event/node")
    public List<String> listNodeEvent(@RequestParam(required = true, value = "namespace", defaultValue = "kube-system") String namespace,
                                      @RequestParam(required = true, value = "node") String node) {
        if (!verifyKubeConfig()) {
            return Lists.newArrayList();
        }

        K8sClient k8sClient = new K8sClient(kubeConfigPath);
        CoreV1EventList eventList = k8sClient.getEventList(namespace);

        return formatEvent(ResourceType.NODE, node, eventList);
    }

    private List<String> formatEvent(ResourceType resourceType, String resource, CoreV1EventList eventList) {
        List<String> list = new ArrayList<>();
        for (CoreV1Event item : eventList.getItems()) {
            V1ObjectMeta meta = item.getMetadata();
            V1ObjectReference reference = item.getInvolvedObject();
            V1EventSource source = item.getSource();

            String condition = "";
            if (ResourceType.POD == resourceType) {
                condition = reference.getName();
            } else if (ResourceType.NODE == resourceType) {
                condition = source.getHost();
            }

            if (StringUtils.isNotBlank(resource) && !resource.equals(condition)) {
                continue;
            }

            String name = meta.getName();
            String reason = item.getReason();
            String type = item.getType();
            String kind = item.getKind();
            Integer count = item.getCount();
            String firstTime = DateUtils.format(item.getFirstTimestamp());
            String lastTime = DateUtils.format(item.getLastTimestamp());
            String objectName = item.getInvolvedObject().getName();
            String host = source.getHost();

            list.add(String.format("%s | %s | %s | %d | %s | %s | %s | %s | %s", name, type, kind, count, reason, firstTime, lastTime, objectName, host));
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
