package adam.model.speech;

import java.beans.PropertyVetoException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * The speech model that uses a synthesizer and a recognizer for the voice.
 * 
 * Using 3rd party libraries under GNU and MIT licenses (FreeTTS & Sphinx 4).
 * @author Razvan-Gabriel Geangu
 */
public class Speech {
	
	// A String that is used for the result text of the recognized text from the user's voice.
	private String resultText;

	// A ConfigurationManager object.
	private ConfigurationManager cm;

	// A Recognizer object.
	private Recognizer recognizer;
	
	// A Microphone object.
	private Microphone microphone;
	
	// A Synthesizer object.
	private Synthesizer synth;
	
	// A SynthesizerModeDesc object.
	private SynthesizerModeDesc desc;
	
	// A Voice object.
	private Voice voice;
	
	/**
	 * A constructor for the speech model.
	 */
	public Speech() {
		
		// Getting resources.
		cm = new ConfigurationManager(Speech.class.getResource("adam.config.xml"));

		// Initiated the recognizer.
		recognizer = (Recognizer) cm.lookup("recognizer");
		
		// Start the microphone if it is possible.
		microphone = (Microphone) cm.lookup("microphone");
		if (!microphone.startRecording()) {
			System.out.println("Cannot start microphone.");
			recognizer.deallocate();
			System.exit(1);
		}
		
		try {
			// Setting properties and configurations.
			System.setProperty("FreeTTSSynthEngineCentral", "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
			System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
			Central.registerEngineCentral("com.sun.speech.freetts.jsapi.FreeTTSEngineCentral");
			
			// Initiating the SynthesizerModeDesc
			desc = new SynthesizerModeDesc(null, "general", Locale.US, null, null);
			
			// Initiating the synthesizer.
			synth = Central.createSynthesizer(desc);
			
			// Allocating memory to the synthesizer.
			synth.allocate();
			
			// Getting the engine for the syntesizer
	        desc = (SynthesizerModeDesc) synth.getEngineModeDesc();
	        
	        // Getting the voice from the resources.
	        Voice[] voices = desc.getVoices();
	        voice = null;
	        for (Voice entry : voices) {
	            if(entry.getName().equals("kevin16")) {
	                voice = entry;
	                break;
	            }
	        }
			
			// Setting the voice for the synthesizer.
			synth.getSynthesizerProperties().setVoice(voice);
			
			// Resuming the object.
			synth.resume();
		} catch (EngineException | PropertyVetoException | AudioException | EngineStateError e) {
			e.printStackTrace();
		}
		
		// Feedback
		System.out.println("Speech initiated!");
	}

	/**
	 * A method that uses a 3rd party library to listen to the voice of the user.
	 */
	public String listenVoiceToString() {
		
		// A boolean value to know when to stop listening.
		boolean shouldBeListening = true;
		
		recognizer.allocate();
		
		System.out.println("Start speaking!");

		// Loop the recognition until it matches any of the available options.
		while (shouldBeListening) {

			Result result = recognizer.recognize();

			if (result != null) {
				resultText = result.getBestFinalResultNoFiller();
				
				String option = "";
				
				// A pattern to match the available.
				Pattern pattern = Pattern.compile("(?<option>statistics|time)");
				Matcher matcher = pattern.matcher(resultText);
				
				if (matcher.find()) {
					option = matcher.group("option");
					
					// Feedback
					System.out.println("You said: " + resultText + '\n');
					
					synchronized (recognizer) {
						// Speak the appropriate message.
						speakMessage(option);
						
						// Closing the action of listening.
						shouldBeListening = false;
						recognizer.deallocate();
						microphone.stopRecording();
					}
					
					return option;
				} else {
					System.out.println("Sorry, can you try again?");
					Random random = new Random();
					int i = random.nextInt(2);
					
					if (i == 1 || i == 2) {
						speakMessage("Sorry, can you try again");
					}
				}
			} else {
				System.out.println("I can't hear what you said.\n");
				microphone.startRecording();
			}
		}
		
		return "Nothing matched";
	}

	/**
	 * A method that uses a 3rd party library to speak a text to the user.
	 * @param text A String that represents the text to be spoken.
	 */
	private void speak(String text) {
		if (text == null || text.trim().isEmpty())
			return;

		try {
			
			// Speak the text.
			synth.speakPlainText(text, null);
			
			// Feedback
			System.out.println("Speaking: " + text);
			
			// Wait until the queue is empty.
			synth.waitEngineState(Synthesizer.QUEUE_EMPTY);
		} catch (Exception e) {
			String message = "Exception - Missing speech.properties in " + System.getProperty("user.home") + "\n";
			
			System.out.println(e);
			System.out.println(message);
			
			e.printStackTrace();
		}
	}
	
	/**
	 * A method to speak the favourites sentence.
	 */
	private void speakMessage(String option) {
		if (!option.equals("")) {
			speak("Here is your " + option + "!");
		} else {
			speak("I am not sure what you said there.");
		}
	}
	
//	public static void main(String[] args) {
//		Speech test = new Speech();
//		test.speak(test.listenVoiceToString());
//		test.speak("Hello Toma, I am a robot and I can speak to you!");
//	}

}