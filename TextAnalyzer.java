
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.io.*;

public class TextAnalyzer {
	
	private File firstFile;
	private File Cleand;
	private String tempFilePath;
	private List<String> forBiddenWordsList;
	private String[] WordsCount;
	private HashMap<String, Integer> goOverWords;
	private int countingWords;
	private String fileName;

	
	/**
	 * this constructor is Checking if the file exists within this terms 
	 * @param firstFile that is the new file , len that is holding the length , tempFilePath that is the parent
	 * @return if the file exists or not 
	 * **/
	public TextAnalyzer(String textPath) throws IOException{
	    firstFile  = new File(textPath); 
	    tempFilePath = firstFile.getAbsolutePath();
	    int len = tempFilePath.length();
	    int pos = 0; 
	    while (tempFilePath.charAt(len - pos-1) != '/') //this is checking the path based in what OS u have 
	    {
	    	pos++;
	    }
	    tempFilePath = tempFilePath.substring(0,len - pos);	    
		if(firstFile.exists() && textPath.substring(textPath.length() - 4).equals(".txt")) {
			System.out.println("File is up");
			 fileName = firstFile.getName();
			 fileName = fileName.substring(0,fileName.length() - 4);
		}else {
			throw new IOException("the file does not exists or it is not an txt file");
		}

	}
/***
 * this method is cleaning the text and creating a new file that is normalized and cleand from the stop words 
 * @param 2 new files that is newFile and CleanFile
 * @return 2 Files , one is clean and normalized , the seconds is normalized 
 * **/
	public void cleanText() throws CleanFailException , IOException{
		File newFile = null;
		FileWriter fw2 = null;
		FileWriter CleanFile = null;
		FileReader fr2 = null;
		BufferedReader readerOfInput = null;
		try {
		int i = 0;
		int j = 0;
		char temp = 0;
		String tempWord = "";
		fr2 = new FileReader(firstFile);
		File forbiddenWords = new File(tempFilePath + "stopWords.txt");//לשנות
		FileInputStream fileForbiddenWordStream = new FileInputStream(forbiddenWords);
		InputStreamReader inputFileForBid = new InputStreamReader(fileForbiddenWordStream);
	    readerOfInput = new BufferedReader(inputFileForBid);
		int size = (int) firstFile.length();
		String forBiddenContent = "";
		forBiddenWordsList = new ArrayList<String>();
		while((forBiddenContent = readerOfInput.readLine()) != null) {
			forBiddenWordsList.add(forBiddenContent);
		}
		char[] data = new char[size];
		String[] newData = new String[size + 1];
		fr2.read(data);
		newFile = new File(tempFilePath + fileName + "Normalize.txt");
		fw2 = new FileWriter(newFile);
		newFile.createNewFile();
		for(i = 0 ; i < data.length ; i++) {
			if(data[i] != ' ' && (data[i] >= 'a' && data[i] <= 'z') || (data[i] >= 'A' && data[i] <= 'Z')) {
				if((data[i] >= 'A' && data[i] <= 'Z')) {
					temp = data[i];
					temp = Character.toLowerCase(temp);
					data[i] = temp;
					tempWord += data[i];
				}
				else {
					tempWord += data[i];
				}
			}
			else if(data[i] == 39) {
				continue;
			}
			else if(data[i] == ' ' || data[i] == '.' || data[i] == 45){
				newData[j] = tempWord.toLowerCase() + " ";
				tempWord = "";
				j++;
			}
			else {
				continue;
			}
		}

		for(int k = 0 ; k < newData.length ; k++) {
			if(newData[k] == null) {
				break;
			}
			fw2.write(newData[k] + " ");
		}

		String[] tempArray = new String[forBiddenWordsList.size()];

		for(j = 0 ; j < forBiddenWordsList.size() ; j++) {
			tempArray[j] = forBiddenWordsList.get(j) + " ";
		}
		for(j = 0 ; j < newData.length ; j++) {
			for(i = 0 ; i < tempArray.length ; i++) {
				if(newData[j] == null || tempArray[i] == null) {
					break;
				}
				else if(newData[j].equals(tempArray[i])) {
					newData[j] = "";
				}
			}

		}

		Cleand = new File(tempFilePath + fileName + "Clean.txt");
		Cleand.createNewFile();
		CleanFile = new FileWriter(Cleand);
		for(i = 0 ; i <= newData.length ; i++) {
			if(newData[i] == null) {
				break;
			}else if(newData[i] == " ") {
				continue;
			}
			CleanFile.write(newData[i] + "");
		}
		

		System.out.println("file copied");
	}catch(IOException e) {
		e.printStackTrace();
		newFile.delete();
		Cleand.delete();
	}finally {
		try {
		CleanFile.close();
		readerOfInput.close();
		fr2.close();
		fw2.close();
		}
		catch(IOException e) {
			throw new CleanFailException();
		}
	}
}
/***
 * this method is a void method that is counting the words and building the hashMap
 * @param goOverWords as the HashMap
 **/
	public void countWords() throws CountFailException{
		int i = 0;
		BufferedReader cleanBufferRead = null;
		goOverWords = new HashMap<String, Integer>();
		try {
			FileInputStream CleanReaderStream = new FileInputStream(Cleand);
			InputStreamReader cleanStreamReader = new InputStreamReader(CleanReaderStream);
			cleanBufferRead = new BufferedReader(cleanStreamReader);
			StringBuilder Sb = new StringBuilder();
			String tempLine = cleanBufferRead.readLine();
			WordsCount = tempLine.split(" ");
			while(tempLine != null) {
				for(i = 0; i < WordsCount.length ; i++) {
					if(WordsCount[i].equals("")) {
						continue;
					}
					else if(goOverWords.get(WordsCount[i]) == null) {
						goOverWords.put(WordsCount[i], 1);
					}else {
						int newValue = Integer.valueOf(String.valueOf(goOverWords.get(WordsCount[i])));
						newValue++;
						goOverWords.put(WordsCount[i], newValue);
					}
				}
				Sb.append(System.lineSeparator());
				tempLine = cleanBufferRead.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				cleanBufferRead.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/***
	 * this method is generating a new file with the statistics of the clean file .
	 * @param sortedMap as a new map that is sorted by the alphabet
	 *  **/
	public void generateStatistics() throws GenerationFailException{
		TreeMap<String , Integer> sortedMap2 = new TreeMap<>();
		sortedMap2.putAll(goOverWords);
		String[] countingLetters = new String[goOverWords.size()];
		HashMap<Character, Integer> finalCountLetters = new HashMap<Character, Integer>();
		File Stats = new File(tempFilePath + fileName + "Stat.txt");
		try {
			Stats.createNewFile();
			FileWriter statsWrite = new FileWriter(Stats);
			for (Entry<String, Integer> entry : goOverWords.entrySet()) {
		        countingWords += entry.getValue();
		        }
			
			statsWrite.write("Number of words in the file is : " + countingWords + "\n");
			
			statsWrite.write("Number of unique words : " + goOverWords.size() + "\n");
			int maxValueInMap=(Collections.max(goOverWords.values())); 
		    for (Entry<String, Integer> entry : goOverWords.entrySet()) {
		        if (entry.getValue()==maxValueInMap) {
				    statsWrite.write("Most occurred word : " + entry.getKey() + "\n");
		        }
		    }
		   int lenghtOfLongestWrod = 0;
		    goOverWords.keySet().toArray(countingLetters);
		    for(int i = 0 ; i < countingLetters.length ; i++) {
		    	char letter = countingLetters[i].charAt(0);
		    	if (finalCountLetters.containsKey(letter))
		    		finalCountLetters.put(letter, finalCountLetters.get(letter) + 1);
		    	else
		    		finalCountLetters.put(letter, 1);
		    	
		    	lenghtOfLongestWrod = countingLetters[i].length() > lenghtOfLongestWrod ?
		    										countingLetters[i].length() : lenghtOfLongestWrod;
		    	
		    }
		    
		    statsWrite.write("Words by letter: " + "\n");
			for (int i = (char)'a'; i < (int)'z'; i++) {
				char letter = (char) i;
				statsWrite.write(String.valueOf(letter).toUpperCase() + ":" +
						(finalCountLetters.get(letter) != null ? finalCountLetters.get(letter) : 0) + "\n");
				
			}
			
			statsWrite.write("Word count: " + "\n");
			statsWrite.write("Word				count	% from total\n" + 
					"---------------------------------------------------\n"); 
			String table = new String();
			for (Entry<String, Integer> entry : sortedMap2.entrySet()) {
				int numberToAdd = Math.max(0, lenghtOfLongestWrod - entry.getKey().length());
				String strAdd = "";
				for (int i = 0; i < numberToAdd; i++)
					strAdd += ' ';

				double precent = (((double)entry.getValue()) / ((double) countingWords)) * 100;
				table += entry.getKey() + strAdd + "\t" + entry.getValue() + "\t" + precent + "\n";
				
			}
			

			statsWrite.write(table);
			
			
			
			statsWrite.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new GenerationFailException();
		}
		
		
	}
	
	
	

public static void main(String[] args) throws CleanFailException, IOException, CountFailException, GenerationFailException {
    TextAnalyzer ta = new TextAnalyzer("/Users/idanmalka/Desktop/java-2020-06/Project4Semester2TheSecond/beatles.txt");

    ta.cleanText();
    ta.countWords();
    ta.generateStatistics();


}


}

