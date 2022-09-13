package com.elcom.its.vds.tools;

import com.elcom.its.vds.model.CameraDTO;
import com.elcom.its.vds.model.dto.CameraCreateUpdateDTO;
import com.elcom.its.vds.model.dto.CameraUrls;
import com.elcom.its.vds.model.dto.DirectionDto;
import com.elcom.its.vds.model.dto.PtzInfo;
import com.elcom.its.vds.service.impl.LaneRouteListMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.POJONode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CameraDtoUtils {
    private ObjectMapper mapper;

    public static CameraCreateUpdateDTO getCreateUpdateCameraDtoFromBodyParam(Map<String, Object> bodyParam) {
        String name = (String) bodyParam.get("name");
        String cameraKey = (String) bodyParam.get("cameraKey");
        String cameraModel = (String) bodyParam.get("cameraModel");
        String cameraType = (String) bodyParam.get("cameraType");
        String siteId = (String) bodyParam.get("siteId");
        String directionCode = (String) bodyParam.get("directionCode");
        String directionString = (String) bodyParam.get("directionString");
        String vmsId = (String) bodyParam.get("vmsId");

        //Ptz info
        PtzInfo ptzInfo = convertToPtzInfo(bodyParam.get("ptzInfo"));
        Integer ptz = (Integer) bodyParam.get("ptz");

        //urls
        String hlsUrl = (String) bodyParam.get("hlsUrl");
        String streamUrl = (String) bodyParam.get("streamUrl");
        String liveUrl = (String) bodyParam.get("liveUrl");

        CameraUrls urls = new CameraUrls();
        urls.setHlsUrl(hlsUrl);
        urls.setLiveUrl(liveUrl);
        urls.setStreamUrl(streamUrl);

        CameraCreateUpdateDTO cameraDTO = new CameraCreateUpdateDTO();

        cameraDTO.setUrls(urls);
        cameraDTO.setName(name);
        cameraDTO.setCameraKey(cameraKey);
        cameraDTO.setCameraModel(cameraModel);
        cameraDTO.setCameraType(cameraType);
        cameraDTO.setSiteId(siteId);
        cameraDTO.setUrls(urls);
        cameraDTO.setDirectionCode(directionCode);
        cameraDTO.setPtzInfo(ptzInfo);
        cameraDTO.setPtz(ptz);
        cameraDTO.setDirectionString(directionString);
        cameraDTO.setVmsId(vmsId);

        return cameraDTO;
    }

    public static PtzInfo convertToPtzInfo(Object object) {
        if (object != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            PtzInfo ptzInfo = objectMapper.convertValue(object, PtzInfo.class);

            return ptzInfo;
        }
        return new PtzInfo();
    }

    public static CameraUrls convertIntoUrls(Object object) {
        if (object != null) {
            ObjectMapper objectMapper = new ObjectMapper();

            CameraUrls urls = objectMapper.convertValue(object, CameraUrls.class);

            return urls;
        }
        return new CameraUrls();
    }

    public static Set<DirectionDto> getDirections(LaneRouteListMessage message) {
        Set<DirectionDto> directionDtoSet = new HashSet<>();

        List<DirectionDto> directionDtoList = message.getData().getContent();

        for(DirectionDto dto : directionDtoList) {
            directionDtoSet.add(dto);
        }

        return directionDtoSet;
    };
}
