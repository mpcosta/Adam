package adam.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Questions {
	/**
	 * A constructor containing the Hash map of question, answers, and correct answers 
	 */
	@SuppressWarnings("unchecked")
	public Questions() {
		questionAndAnswers = (HashMap<String, ArrayList<String>>) getDataFromFile("questionAndAnswers");
		correctAnswers = (HashMap<String, ArrayList<Integer>>) getDataFromFile("correctAnswers");
	}
	/**
	 * Variables calling the data for the multiple choice quiz 
	 */
	private HashMap<String, ArrayList<String>> questionAndAnswers = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<Integer>> correctAnswers = new HashMap<String, ArrayList<Integer>>();
/**
	 * A getter for the question and answers from the HashMap reference 
	 * @return the questions and answers 
	 */
	public HashMap<String, ArrayList<String>> getQuestionAndAnswers() {
		return questionAndAnswers;
	}
/**
	 * A getter for the correct Answers from the HashMap reference 
	 * @return the correct answers 
	 */
	public HashMap<String, ArrayList<Integer>> getCorrectAnswers() {
		return correctAnswers;
	}
/**
	 * A method to read the state of the file Questions.txt 
	 * Reads the file by char through the data string 
	 * @return the data string 
	 */
	private String readStateFromFile() {
		String result = "";
		
		InputStream in = getClass().getResourceAsStream("/Questions.txt"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		try {
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				result += currentLine + "\n";
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return result;
	}
/**
	 * A getter that retrieves the data from the file Questions.txt 
	 * Calls the hashmap references of questions, answers, and correct answers 
	 * Goes through it by switch 
	 * Case one for the method to read and retrieve the number of answers the question will contain 
	 * Case two for method to read and retrieve the actual question 
	 * Case three for the method to read and retrieve the answer options
	 * Case four for method to read and retrieve the correct answer 
	 * @param type
	 * @return the questions and answers and correct answer
	 */
	private Object getDataFromFile(String type) {
		
		String line;
		int newLineIndex = 0;
		int indicator = 0;
		
		HashMap<String, ArrayList<String>> questionAndAnswers = new HashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<Integer>> correctAnswers = new HashMap<String, ArrayList<Integer>>();
		
		int numberOfAnswers = 0;
		String question = "";
		ArrayList<String> answers = new ArrayList<String>();
		ArrayList<Integer> numberOfCorrectAnswers = new ArrayList<Integer>();

		int i = 0;
		String data = readStateFromFile();

		while (i < data.length()) {
			
			if (data.charAt(i) == '\n') {
				line = data.substring(newLineIndex, i).trim();
				
//					System.out.println(line);
				
				switch (indicator) {
					case 0: { 
						
						numberOfAnswers = Integer.parseInt(line);
						indicator += 1;
						break;
					}
					case 1: { 
						question = line;
						
						indicator += 1;
						break;
					}
					case 2: { 
						
						answers.add(line);
						
						if (answers.size() == numberOfAnswers) {
							indicator += 1;
						}
						break;
					}
					case 3: { 
						String[] numbersData = line.split(",");
						
						for (int j = 0; j < numbersData.length; j++) {
							numberOfCorrectAnswers.add(Integer.parseInt(numbersData[j]));
						}
						
						questionAndAnswers.put(question, answers);
						correctAnswers.put(question, numberOfCorrectAnswers);
						
						question = "";
						answers = new ArrayList<String>();
						numberOfCorrectAnswers = new ArrayList<Integer>();
						
						indicator = 0;
						
						break;
					}
				}					
				
				newLineIndex = i;
			}
			i += 1;
		}
			
	if (type.equals("questionAndAnswers")) {
		return questionAndAnswers;
	} else if (type.equals("correctAnswers")) {
			return correctAnswers;
		}
		
		return null;
	}
}
