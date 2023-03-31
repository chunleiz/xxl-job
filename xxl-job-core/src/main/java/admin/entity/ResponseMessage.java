package admin.entity;

public class ResponseMessage {
	private String requestId;
	private Object result;

	public ResponseMessage() {
	}

	public ResponseMessage(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
