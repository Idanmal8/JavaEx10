
public class CountFailException  extends Exception{
	public CountFailException() {
		super("The count as failed");
	}
	
	public CountFailException(String S) {
		super(S);
	}

}
