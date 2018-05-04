package org.papa.seckill.event.disruptor;

import org.papa.seckill.RequestDto;
import org.papa.seckill.command.CommandCollector;
import org.papa.seckill.ResponseDto;

/**
 * Created by PaperCut on 2018/2/26.
 * Request上下文
 */
public class RequestEvent<T> {
    private RequestDto<T> requestDto;
    private ResponseDto responseDto;
    private CommandCollector commandCollector = new CommandCollector();

    public RequestDto<T> getRequestDto() {
        return requestDto;
    }

    public void setRequestDto(RequestDto<T> requestDto) {
        this.requestDto = requestDto;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public CommandCollector getCommandCollector() {
        return commandCollector;
    }

    public boolean hasErrorOrException() {
        return responseDto != null && !responseDto.isSuccess();
    }

    public void clearForGc() {
        this.requestDto = null;
        this.responseDto = null;
    }
}
