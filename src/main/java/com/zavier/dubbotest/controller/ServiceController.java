package com.zavier.dubbotest.controller;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Splitter;
import com.zavier.dubbotest.common.PagingResult;
import com.zavier.dubbotest.model.ProviderDTO;
import com.zavier.dubbotest.service.ProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Service controller.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
@Slf4j
@RestController
@RequestMapping("api")
public class ServiceController {
    /**
     * The Provider service.
     */
    private final ProviderService providerService;

    /**
     * Instantiates a new Service controller.
     *
     * @param providerService the provider service
     */
    public ServiceController(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * Find all service list.
     *
     * @param key  the key
     * @param page the page
     * @param size the size
     * @return the list
     */
    @GetMapping("services")
    public PagingResult<ProviderDTO> findAllService(@RequestParam(required = false) String key,
                                                    @RequestParam(defaultValue = "1") Integer page,
                                                    @RequestParam(defaultValue = "20") Integer size) {
        Map<String, Map<String, URL>> serviceMap = providerService.findService();
        if (serviceMap == null || serviceMap.size() == 0) {
            return PagingResult.wrapSuccessfulResult(new ArrayList<>(), 0);
        }
        log.info("service size:{}", serviceMap.size());
        List<ProviderDTO> providerDTOS = new ArrayList<>();
        List<String> serviceKey = serviceMap.keySet().stream()
                .filter(s -> {
                    if (StringUtils.isEmpty(key)) {
                        return true;
                    }
                    return s.toLowerCase().contains(key.toLowerCase());
                })
                .collect(Collectors.toList());
        serviceKey.forEach(sevice -> {
            Map<String, URL> services = serviceMap.get(sevice);
            services.forEach((k, v) -> {
                String methods = v.getParameter(Constants.METHODS_KEY);
                ProviderDTO providerDTO = new ProviderDTO();
                providerDTO.setId(k);
                providerDTO.setServiceKey(v.getServiceKey());
                providerDTO.setServiceInterface(v.getServiceInterface());
                providerDTO.setVersion(v.getParameter(Constants.VERSION_KEY));
                providerDTO.setIp(v.getIp());
                providerDTO.setPort(v.getPort());
                providerDTO.setMethods(Splitter.on(",").splitToList(methods));
                providerDTOS.add(providerDTO);
            });
        });

        List<ProviderDTO> result = providerDTOS.stream().skip((page - 1) * size).limit(size).collect(Collectors.toList());
        int total = providerDTOS.size();
        return PagingResult.wrapSuccessfulResult(result, total);
    }
}
