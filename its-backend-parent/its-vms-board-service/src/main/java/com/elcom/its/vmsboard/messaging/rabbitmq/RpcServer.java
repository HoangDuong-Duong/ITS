/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vmsboard.messaging.rabbitmq;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);


    @Autowired
    private VmsBoardController vmsBoardController;

    @Autowired
    private DisplayScriptController displayScriptController;

    @Autowired
    private NewsLetterTemplateController newsLetterTemplateController;

    @Autowired
    private ScriptBaseController scriptBaseController;

    @Autowired
    private VmsLayoutController vmsLayoutController;


    @RabbitListener(queues = "${vmsboard.rpc.queue}")
    public String processService(String json) {
        long start = System.currentTimeMillis();
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                switch (request.getRequestMethod()) {
                    case "GET":
                        if ("/vmsboard".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = vmsBoardController.getVmsBoardById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            } else {
                                response = vmsBoardController.getAllVmsBoard(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if ("/vmsboard/displayscript/baseId".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getAllDisplayScript(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/displayscript/parentId".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getDisplayScriptByParentId(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/base".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = scriptBaseController.getScriptBaseById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            } else {
                                response = scriptBaseController.getAllScriptBase(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }else if("/vmsboard/base/active".equalsIgnoreCase(requestPath)){
                            response = scriptBaseController.activeScripBase(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/vmsboard/displayscript/detail".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getDetailScript(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/displayscript/time".equalsIgnoreCase(requestPath)) {
                              response = displayScriptController.getTimeByBaseIdAndBoardId(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/vmsboard/displayscript/news-letter/eventType".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getNewsLetterByEventType(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/vmsboard/direction".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.getVmsBoardByDirection(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/vmsboard/displayscript/plan".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getDisplayScriptPlan(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/news-letter".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = newsLetterTemplateController.getNewsLetterById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            } else {
                                response = newsLetterTemplateController.getAllNewsLetterTemplate(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }else if ("/vmsboard/history".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.getHistoryByVms(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/vmsboard/displayscript/board".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getDisplayScriptByBoardId(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/displayscript/default".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getDisplayScriptDefault(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/current-display".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.getCurrentDisplay(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        else if ("/vmsboard/displayscript/running".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getScriptRunningInBoard(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/news-letter/running".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.getNewsLetterRunningInBoard(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/layout".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.getAllLayout(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/content".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.getAllContentLayout(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/vmsboard/attribute".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.getAttributeInLayout(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        break;
                    case "POST":
                        if ("/vmsboard".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.createVmsBoard(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        } else if ("/vmsboard/displayscript".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.createDisplayScript(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/vmsboard/stage".equalsIgnoreCase(requestPath)){
                            response = vmsBoardController.getAllVmsByGroup(requestPath,  headerParam, bodyParam, request.getRequestMethod(),  pathParam);
                        }
                        else if ("/vmsboard/displayscript/max-priority".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.createScriptMaxPriority(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/vmsboard/displayscript/top-priority".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.createScriptTopPriority(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/vmsboard/news-letter".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.createNewsLetterTemplate(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        } else if ("/vmsboard/base".equalsIgnoreCase(requestPath)) {
                            response = scriptBaseController.createScriptBase(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/vmsboard/layout".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.createLayout(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/vmsboard/content".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.createContentLayout(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        break;
                    case "PUT":
                        if ("/vmsboard".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.updateVmsBoard(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/displayscript".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.updateDisplayScript(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/displayscript/detail".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.updateDetailScript(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/news-letter".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.updateNewsLetterTemplate(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/base".equalsIgnoreCase(requestPath)) {
                            response = scriptBaseController.updateScriptBase(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/attribute".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.createAttribute(headerParam, bodyParam, requestPath, pathParam);
                        }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ("/vmsboard".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.deleteVmsBoard(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = vmsBoardController.deleteMultiVmsBoard(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/displayscript".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.deleteDisplayScript(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/displayscript/detail".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.deleteDetailScript(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/news-letter".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.deleteNewsLetterTemplate(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/news-letter/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.deleteMultiNewsLetter(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/base".equalsIgnoreCase(requestPath)) {
                            response = scriptBaseController.deleteScriptBase(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vmsboard/base/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = scriptBaseController.deleteMultiScriptBase(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/vmsboard/content".equalsIgnoreCase(requestPath)) {
                            response = vmsLayoutController.deleteContentLayout(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response.toJsonString());
            long end = System.currentTimeMillis();
            LOGGER.info("[RpcServer] ================> Time to process data : " + (end - start) + " miliseconds");
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to process request >>> " + ex);
            ex.printStackTrace();
        }
        return null;
    }
}
