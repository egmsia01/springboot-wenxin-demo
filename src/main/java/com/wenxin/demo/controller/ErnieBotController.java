package com.wenxin.demo.controller;

import com.gearwenxin.client.ernie.ErnieBotClient;
import com.gearwenxin.entity.chatmodel.ChatErnieRequest;
import com.gearwenxin.entity.response.ChatResponse;
import com.wenxin.demo.common.BaseResponse;
import com.wenxin.demo.exception.ResultUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author Ge Mingjia
 * @date 2023/7/29
 */
@RestController
@RequestMapping("/ernie")
public class ErnieBotController {

    @Resource
    private ErnieBotClient ernieBotClient;

    //------------------默认参数------------------//

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(String msg) {
        ChatResponse chatResponse = ernieBotClient.chatSingle(msg).block();
        return ResultUtils.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(String msg, String msgUid) {
        ChatResponse chatResponseMono = ernieBotClient.chatCont(msg, msgUid).block();
        return ResultUtils.success(chatResponseMono);
    }

    // 流式返回，单次对话
    @GetMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatSingleStream(@RequestParam String msg) {
        Flux<ChatResponse> chatResponse = ernieBotClient.chatSingleOfStream(msg);

        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    // 流式返回，连续对话
    @GetMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatContStream(@RequestParam String msg, @RequestParam String msgUid) {
        Flux<ChatResponse> chatResponse = ernieBotClient.chatContOfStream(msg, msgUid);

        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatErnieRequest chatErnieRequest) {
        ChatResponse chatResponse = ernieBotClient.chatSingle(chatErnieRequest).block();
        return ResultUtils.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatErnieRequest chatErnieRequest) {
        ChatResponse response = ernieBotClient.chatCont(chatErnieRequest, chatErnieRequest.getUserId()).block();
        return ResultUtils.success(response);
    }

}
