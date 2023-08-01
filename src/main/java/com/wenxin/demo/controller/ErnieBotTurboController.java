package com.wenxin.demo.controller;

import com.gearwenxin.client.ErnieBotTurboClient;
import com.gearwenxin.model.chatmodel.ChatTurbo7BRequest;
import com.gearwenxin.model.response.ChatResponse;
import com.wenxin.demo.common.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author Ge Mingjia
 * @date 2023/7/29
 */
@RestController
@RequestMapping("/turbo")
public class ErnieBotTurboController {

    @Resource
    private ErnieBotTurboClient ernieBotTurboClient;

    //------------------默认参数------------------//

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(String content) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(content);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(String msg, String chatUid) {
        ChatResponse response = ernieBotTurboClient.chatCont(msg, chatUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Flux<ChatResponse>> chatSingleStream(String msg) {
        Flux<ChatResponse> chatResponseFlux = ernieBotTurboClient.chatSingleOfStream(msg);
        return BaseResponse.success(chatResponseFlux);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Flux<ChatResponse>> chatContStream(String msg, String msgUid) {
        Flux<ChatResponse> chatResponseFlux = ernieBotTurboClient.chatContOfStream(msg, msgUid);
        return BaseResponse.success(chatResponseFlux);
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatTurbo7BRequest chatTurbo7BRequest) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(chatTurbo7BRequest);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatTurbo7BRequest chatTurbo7BRequest, String chatUid) {
        ChatResponse response = ernieBotTurboClient.chatCont(chatTurbo7BRequest, chatUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/param/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Flux<ChatResponse>> pChatSingleStream(@RequestBody ChatTurbo7BRequest chatTurbo7BRequest) {
        Flux<ChatResponse> chatResponseFlux = ernieBotTurboClient.chatSingleOfStream(chatTurbo7BRequest);
        return BaseResponse.success(chatResponseFlux);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/param/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public BaseResponse<Flux<ChatResponse>> pChatContStream(@RequestBody ChatTurbo7BRequest chatTurbo7BRequest, String msgUid) {
        Flux<ChatResponse> chatResponseFlux = ernieBotTurboClient.chatContOfStream(chatTurbo7BRequest, msgUid);
        return BaseResponse.success(chatResponseFlux);
    }

}
