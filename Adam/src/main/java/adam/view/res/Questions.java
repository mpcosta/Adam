package adam.view.res;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Questions {
	
		public String readStateFromFile() {

			String dataString = "";

			try {
				FileInputStream in = new FileInputStream("src/main/java/adam/view/res/Questions.txt");
				int i = 0;
								
				while ((i = in.read()) != -1) {
					dataString = dataString + (char) i;
				}
				
				in.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}

			return dataString;
		}

		public Object getDataFromFile(String type) {
			
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
