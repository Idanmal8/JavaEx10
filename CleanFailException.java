
public class CleanFailException extends Exception{
	public CleanFailException() {
		super("the clean has faild.");
	}
	
	public CleanFailException(String S) {
		super(S);
	}

}
