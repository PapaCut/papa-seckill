package org.papa.seckill;

/**
 * Created by PaperCut on 2018/2/26.
 * 响应传输实体对象
 */
public class ResponseDto extends MessageDto {
    private final String requestId;
    private boolean success;
    private String errMsg;

    public ResponseDto(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

}
