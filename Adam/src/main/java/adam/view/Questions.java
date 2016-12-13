package adam.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Questions {
	
	@SuppressWarnings("unchecked")
	public Questions() {
		questionAndAnswers = (HashMap<String, ArrayList<String>>) getDataFromFile("questionAndAnswers");
		correctAnswers = (HashMap<String, ArrayList<Integer>>) getDataFromFile("correctAnswers");
	}
	
	private HashMap<String, ArrayList<String>> questionAndAnswers = new HashMap<String, ArrayList<String>>();
	private HashMap<String, ArrayList<Integer>> correctAnswers = new HashMap<String, ArrayList<Integer>>();

	public HashMap<String, ArrayList<String>> getQuestionAndAnswers() {
		return questionAndAnswers;
	}

	public HashMap<String, ArrayList<Integer>> getCorrectAnswers() {
		return correctAnswers;
	}

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
