package pie;

public class ResponseOption {

	private int responseOptionID;
	private ResponseQuestion responseQuestion;
	private String responseOptionText;
	
	public int getResponseOptionID() {
		return responseOptionID;
	}
	
	public void setResponseOptionID(int responseOptionID) {
		this.responseOptionID = responseOptionID;
	}
	
	public ResponseQuestion getResponseQuestion() {
		return responseQuestion;
	}
	
	public void setResponseQuestion(ResponseQuestion responseQuestion) {
		this.responseQuestion = responseQuestion;
	}
	
	public String getResponseOptionText() {
		return responseOptionText;
	}
	
	public void setResponseOptionText(String responseOptionText) {
		this.responseOptionText = responseOptionText;
	}
	
}
