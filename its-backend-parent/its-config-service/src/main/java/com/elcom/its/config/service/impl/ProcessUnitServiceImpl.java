/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.*;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.repository.ProcessUnitRepository;
import com.elcom.its.config.repository.ServerCustomizeRepository;
import com.elcom.its.config.service.BaseModelsService;
import com.elcom.its.config.service.CameraLayoutService;
import com.elcom.its.config.service.CameraModelsService;
import com.elcom.its.config.service.ITSCoreCameraService;
import com.elcom.its.config.service.LayoutAreaService;
import com.elcom.its.config.service.ModelProfileService;
import com.elcom.its.config.service.ProcessUnitService;
import com.elcom.its.config.service.VdsService;
import com.elcom.its.config.thread.ThreadManager;
import com.elcom.its.config.tools.Utils;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Transactional
@Service
public class ProcessUnitServiceImpl implements ProcessUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUnitServiceImpl.class);

    @Autowired
    private ProcessUnitRepository processUnitRepository;

    @Autowired
    private ServerCustomizeRepository serverCustomizeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelProfileService modelProfileService;

    @Autowired
    private CameraLayoutService cameraLayoutService;

    @Autowired
    private LayoutAreaService layoutAreaService;

    @Autowired
    private CameraModelsService cameraModelsService;

    @Autowired
    private BaseModelsService baseModelsService;

    @Autowired
    private ThreadManager threadManager;

    @Autowired
    private ITSCoreCameraService cameraService;

    @Autowired
    private VdsService vdsService;

    @Override
    public ProcessUnit create(ProcessUnit processUnit) {
        processUnit = processUnitRepository.save(processUnit);
        return processUnit;
    }

    @Override
    public Optional<ProcessUnit> findById(String id) {
        return processUnitRepository.findById(id);
    }

    @Override
    public ProcessUnit findByName(String name) {
        return processUnitRepository.findByName(name);
    }

    @Override
    public ProcessUnit update(ProcessUnit processUnit) {
        processUnit = processUnitRepository.save(processUnit);
        return processUnit;
    }

    @Override
    public boolean checkByCode(String code) {
        boolean checkCodeUnique = processUnitRepository.existsByCode(code);
        return checkCodeUnique;
    }

    @Override
    public void delete(ProcessUnit processUnit) {
        processUnitRepository.delete(processUnit);
    }

    @Override
    public Page<ProcessUnit> getAllByPage(Pageable pageable) {
        return processUnitRepository.findAll(pageable);
    }

    @Override
    public List<ProcessUnit> getAll() {
        return processUnitRepository.findAll();
    }

    @Override
    public boolean addServerToProcessUnit(Long idProcessUnit, Long idServer) {
        return true;
    }

    @Override
    public String toJsonSpec(ProcessUnit processUnit) {
        JSONObject json = new JSONObject();
        //Server
        JSONArray servers = getAiServerJson(processUnit);
        json.put("servers", servers);

        //Event notifier
        JSONArray notifiers = getEventNotifierJson(processUnit);
        json.put("event_notifiers", notifiers);

        //Detector
        JSONArray detectors = getDetectorJson(processUnit, true);
        json.put("detectors", detectors);

        //Input sources
        JSONArray inputSources = inputSourceJson(processUnit);
        json.put("input_sources", inputSources);
        if (processUnit.getVersion() == null) {
            processUnit.setVersion(1);
        }

        //Version
        json.put("version", processUnit.getVersion() + 1);

        String oldSpec = processUnit.getSpec();
        String newSpec = json.toString();
        if (StringUtil.isNullOrEmpty(json.toString())) {
            newSpec = oldSpec;
        } else {
            threadManager.execute(() -> {
                saveJsonSpecToDBM(json, processUnit);
            });
        }
        return newSpec;
    }

    private JSONArray getAiServerJson(ProcessUnit processUnit) {
        List<Servers> servers = getServers(processUnit);

        JSONArray jsonArray;
        if (servers != null) {
            List<JSONObject> list = servers.stream().map(s -> {
                return new JSONObject()
                        .put("id", s.getId())
                        .put("ipaddress", s.getIpAddress())
                        .put("name", s.getName())
                        .put("gpu", s.getGpu())
                        .put("enable", s.getStatus() != null && s.getStatus() == 1)
                        .put("server_status", s.getServerStatus())
                        .put("hardware", Utils.normalizeJsonAttrs(s.getHardwareAttributes()))
                        .put("os", Utils.normalizeJsonAttrs(s.getOsAttributes()))
                        .put("software", Utils.normalizeJsonAttrs(s.getSoftwareAttributes()));
            }).collect(Collectors.toList());
            jsonArray = new JSONArray(list);
        } else {
            jsonArray = new JSONArray();
        }

        return jsonArray;
    }

    private List<Servers> getServers(ProcessUnit processUnit) {
        if (processUnit == null) {
            return null;
        }
        List<Servers> servers = new ArrayList<>();
        Long idServer = processUnit.getServers();
        if (idServer != null) {
            try {
                Servers s = serverCustomizeRepository.findById(idServer);
                if (s != null) {
                    servers.add(s);
                } else {
                    LOGGER.info("Server not exist");
                }
            } catch (Exception e) {
                LOGGER.error(e.toString());
                e.printStackTrace();
            }
        } else {
            servers = Collections.emptyList();
        }
        return servers;
    }

    private JSONArray getEventNotifierJson(ProcessUnit processUnit) {
        if (processUnit == null) {
            return null;
        }
        List<EventNotifier> notifiers = getEventNotifier(processUnit);
        if (notifiers != null) {
            try {
                return new JSONArray(notifiers);
            } catch (Exception e) {
                LOGGER.error(e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<EventNotifier> getEventNotifier(ProcessUnit processUnit) {
        if (processUnit == null) {
            return null;
        }
        List<EventNotifier> eventNotifiers = new ArrayList<>();
        Map<String, Object> mapServers = processUnit.getEventNotifiers();
        if (mapServers != null) {
            try {
                EventNotifier eventNotifier = new EventNotifier();
                eventNotifier.setRoute(mapServers.get("route").toString());
                eventNotifier.setPort(mapServers.get("port").toString());
                eventNotifier.setApiKey(mapServers.get("api_key").toString());
                eventNotifier.setIpaddess(mapServers.get("ip").toString());
                eventNotifier.setName(mapServers.get("name").toString());
                eventNotifier.setToken(mapServers.get("_token").toString());
                eventNotifiers.add(eventNotifier);
            } catch (Exception e) {
                LOGGER.warn("Can not parse EventNotifier, invalid json structure.");
            }
        } else {
            LOGGER.warn("AiServer is not initialized yet.");
            eventNotifiers = Collections.emptyList();
        }
        return eventNotifiers;
    }

    private JSONArray getDetectorJson(ProcessUnit processUnit, boolean b) {
        List<ModelProfiles> modelProfiles = getModelProfiles(processUnit);
        if (modelProfiles == null || modelProfiles.isEmpty()) {
            return new JSONArray();
        }

        List<JSONObject> list = modelProfiles.stream().map(s -> {
            Collection<Object> filterObjects = flatEmbedded(s.getFilterObjects(), "filterObjects");

            String baseModel = "";
            CameraModels camModel = getCameraModel(s.getCameraModelId());
            if (camModel != null) {
                if (getBaseModel(camModel.getBaseModelId()) != null) {
                    baseModel = getBaseModel(camModel.getBaseModelId()).getName();
                }
            } else {
                camModel = new CameraModels();
            }

            Collection<Object> meta = flatEmbedded(s.getMetaAttributes(), "metaAttributes");

            return new JSONObject()
                    .put("id", camModel.getId())
                    .put("model_name", camModel.getName())
                    .put("base_model", baseModel)
                    .put("enable", camModel.getStatus() != null && camModel.getStatus() == 1)
                    .put("filter_objects", filterObjects)
                    .put("input_rate", s.getInputRate())
                    .put("output_rate", s.getOutputRate())
                    .put("min_object", s.getMinObject())
                    .put("max_object", s.getMaxObject())
                    .put("output_height", s.getOutputHeight())
                    .put("output_width", s.getOutputWidth())
                    .put("sub_model", camModel.getSubModel())
                    .put("nms", s.getNms())
                    .put("hier", s.getHier())
                    .put("threshold", s.getThreshold())
                    .put("net_file", camModel.getNetDescFile())
                    .put("labels_file", camModel.getLabelFile())
                    .put("weights_file", camModel.getWeightFile())
                    .put("static_object", camModel.getStaticObject())
                    .put("meta", meta);

        }).collect(Collectors.toList());

        return new JSONArray(list);
    }

    private List<ModelProfiles> getModelProfiles(ProcessUnit processUnit) {
        if (processUnit == null) {
            return null;
        }

        List<ModelProfiles> modelProfiles = null;

        Map<String, Object> mapModelProfiles = processUnit.getModelProfiles();

        LOGGER.debug("processUnit modelProfiles {}", mapModelProfiles);

        if (mapModelProfiles != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //{"profiles": [30, 31]}
                List<Long> profileIds = Arrays.asList(objectMapper.convertValue(mapModelProfiles.get("profiles"), Long[].class));
                LOGGER.debug("Got profileIds: {} ", profileIds);
                if (profileIds != null && profileIds.size() > 0) {
                    modelProfiles = modelProfileService.getAIModelProfiles(profileIds);
                } else {
                    LOGGER.warn("AiModelProfile has not initialized yet.");
                }
            } catch (Exception e) {
                LOGGER.warn("Can not parse AIModelProfile, invalid json structure.");
            }
        } else {
            LOGGER.warn("AIModelProfile has not initialized yet.");
        }

        LOGGER.debug("modelProfiles: {}", modelProfiles);

        if (modelProfiles == null) {
            modelProfiles = Collections.emptyList();
        } else {
            LOGGER.info("modelProfiles size: {}", modelProfiles.size());
            for (ModelProfiles profile : modelProfiles) {
                LOGGER.info(profile.toString());
            }
        }

        return modelProfiles;
    }

    private Collection<Object> flatEmbedded(Map<String, Object> input, String attribute) {
        Collection result;
        if (input != null) {
            if (input.containsKey(attribute)) {
                Object o = input.get(attribute);
                if (o instanceof ArrayList) {
                    result = (ArrayList) o;
                } else {
                    result = Collections.emptyList();
                }
            } else {
                result = input.values();
            }
        } else {
            result = Collections.emptyList();
        }

        return result;
    }

    private JSONArray inputSourceJson(ProcessUnit processUnit) {
        //Change from pu_video_thread => vds
        //List<PuVideoThreads> vdsList = puVideoThreadsService.getListPuVideoThreadsByIdProcessUnit(processUnit.getId());
        List<Vds> vdsList = vdsService.findByProcessUnitId(processUnit.getId());
        if (vdsList == null || vdsList.isEmpty()) {
            return new JSONArray();
        }
        JSONArray jsonSource = new JSONArray();
        for (Vds v : vdsList) {
            CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(v.getCameraId());
            CameraDTO camera = null;
            if (cameraDetailDTOMessage == null || cameraDetailDTOMessage.getData() == null) {
                camera = new CameraDTO();
            } else {
                camera = cameraDetailDTOMessage.getData();
            }
            JSONObject j = new JSONObject();
            //Fix thanh uuid => Cho ben AI muon thanh uuid
            //j.put("id", camera.getId());
            j.put("uuid", camera.getId());
            j.put("auto_adjust", 1);
            j.put("cache_length", 1);
            j.put("cache_url", "");
            j.put("capability", convertMapValueToLong(Utils.normalizeJsonAttrs(v.getCapability())));

            Collection<Object> detectors = flatEmbedded(v.getDetectors(), "detectors");
            detectors = convertLong(detectors);
            j.put("detectors", detectors);
            j.put("device_model", ""); // bind later
            j.put("device_name", ""); // later
            j.put("enable", v.getStatus() != null && v.getStatus() == 1);
            //j.put("focal_length", ""); // later
            //j.put("gps_coord", "{}"); // later
            //j.put("installation_height", ""); //
            Map<String, Object> inputAttributes = new HashMap<>();
            inputAttributes.put("camera_key", camera.getCameraKey());
            j.put("input_attributes", Utils.normalizeJsonAttrs(inputAttributes));

            j.put("setup_attributes", setSetupAttributescamera(camera.getSetupAttributes()));

            j.put("urls", setUrls(camera.getUrls()));
            j.put("video_output", Utils.normalizeJsonAttrs(v.getRender()));

            JSONObject layout = new JSONObject();
            List<Map<String, Object>> listLayoutAreas;
            List<LayoutAreas> layoutAreas = null;
            CameraLayouts cameraLayout = getCameraLayouts(v.getLayoutId());
            if (cameraLayout != null) {
                layoutAreas = getLayoutAreas(cameraLayout.getId());
            }

            if (layoutAreas != null) {
                listLayoutAreas = layoutAreas.stream().map(la -> {
                    Map<String, Object> roiItem = new HashMap<>();
                    roiItem.put("item", Utils.normalizeJsonAttrs(la.getJsonDetailArea()));
                    roiItem.put("type", la.getRoiType());

                    return roiItem;
                }).collect(Collectors.toList());
            } else {
                listLayoutAreas = Collections.emptyList();
            }

            JSONObject viewbox = new JSONObject();
            if (cameraLayout != null) {
                viewbox.put("h", cameraLayout.getOriginHeight());
                viewbox.put("w", cameraLayout.getOriginWidth());
                viewbox.put("x", cameraLayout.getWidthImgScale());
                viewbox.put("y", cameraLayout.getHeightImgScale());
            }

            layout.put("roi_list", listLayoutAreas);
            layout.put("viewbox", viewbox);

            j.put("layout", layout);
            jsonSource.put(j);
        }
        return jsonSource;
    }

    public boolean saveJsonSpecToDBM(JSONObject json, ProcessUnit processUnit) {
        try {
            final String uri = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/process-unit/config";
            JsonSpecDTO jsdto = new JsonSpecDTO();
            jsdto.setNotes(processUnit.getName());
            jsdto.setProcess_unit_id(processUnit.getId());
            jsdto.setSpec_json(json.toString());
            jsdto.setType(processUnit.getPuType());
            Response response = restTemplate.postForObject(uri, jsdto, Response.class);
            if (response == null || (response.getStatus() != HttpStatus.OK.value()
                    && response.getStatus() != HttpStatus.CREATED.value())) {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Map<String, Object> convertMapValueToLong(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                Long l = Long.valueOf(entry.getValue().toString());
                result.put(entry.getKey(), l);
            } catch (Exception e) {
                LOGGER.error("Can not convert Obj to Long");
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    private Collection<Object> convertLong(Collection<Object> col) {
        try {
            return col.stream()
                    .map(x -> Long.valueOf(x.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Can not convert collection of Object to collection of Long");
            return col;
        }
    }

    private CameraLayouts getCameraLayouts(long layoutId) {
        CameraLayouts cameraLayouts = null;
        try {
            cameraLayouts = cameraLayoutService.findById(layoutId).get();
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return cameraLayouts;
    }

    private List<LayoutAreas> getLayoutAreas(long id) {
        List<LayoutAreas> layoutAreases = null;
        try {
            layoutAreases = layoutAreaService.findByCameraLayoutId(id);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return layoutAreases;
    }

    private CameraModels getCameraModel(Long cameraModelId) {
        CameraModels cameraModels = new CameraModels();
        try {
            cameraModels = cameraModelsService.getCameraModels(cameraModelId);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return cameraModels;
    }

    private BaseModels getBaseModel(long baseModelId) {
        BaseModels baseModels = null;
        try {
            baseModels = baseModelsService.getbaseModels(baseModelId);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return baseModels;
    }

    private JSONObject setSetupAttributescamera(CameraSetupAttribute cameraSetupAttribute) {
        JSONObject jSONObject = new JSONObject();
        try {
            if (cameraSetupAttribute != null) {
                jSONObject.put("nearest_sp", cameraSetupAttribute.getNearestSp());
                jSONObject.put("installation_height", cameraSetupAttribute.getInstallationHeight());
                jSONObject.put("farest_sp", cameraSetupAttribute.getFarestSp());
                jSONObject.put("focal_length", cameraSetupAttribute.getFocalLength());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return jSONObject;
    }

    private JSONObject setUrls(CameraUrls cameraUrl) {
        JSONObject jSONObject = new JSONObject();
        try {
            if(cameraUrl != null){
                jSONObject.put("stream_url", cameraUrl.getStreamUrl());
                jSONObject.put("admin_url", cameraUrl.getStreamUsername());
                jSONObject.put("hls_url", cameraUrl.getHlsUrl());
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
            e.printStackTrace();
        }
        return jSONObject;
    }

    @Override
    public Page<ProcessUnit> getByNamePage(String search, Pageable pageable) {
        try {
            return processUnitRepository.search(StringUtil.replaceSpecialSQLCharacter(URLDecoder.decode(search, "UTF-8").toUpperCase()), pageable);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<ProcessUnit> findProcessUnit(Integer puType, String search, Pageable pageable) {
        return processUnitRepository.findAll(Specification.where(ProcessUnitSpec.findProcessUnit(puType, search)), pageable);
    }
}
