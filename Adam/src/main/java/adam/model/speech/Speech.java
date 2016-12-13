package adam.model.speech;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

/**
 * The speech model that uses a synthesiser and a recogniser for the voice.
 * 
 * Using 3rd party libraries under GNU and MIT licenses (FreeTTS & Sphinx 4).
 * @author Razvan-Gabriel Geangu
 */
public class Speech {
	
	// A Synthesiser object.
	private Synthesizer synth;
	
	// A SynthesizerModeDesc object.
	private SynthesizerModeDesc desc;
	
	// A Voice object.
	private Voice voice;
	
	private static final String ACOUSTIC_MODEL = "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "resource:/gram";
    
    private LiveSpeechRecognizer recognizer;
	
	/**
	 * A constructor for the speech model.
	 */
	public Speech() {	
		Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(ACOUSTIC_MODEL);
        configuration.setDictionaryPath(DICTIONARY_PATH);
        configuration.setGrammarPath(GRAMMAR_PATH);
        configuration.setUseGrammar(true);
        
        cleanConsoleLog();

        configuration.setGrammarName("adam");
        try {
			recognizer = new LiveSpeechRecognizer(configuration);
		} catch (IOException e1) {
			System.out.println("Cannot load recongizer configurations!");
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
	}

	private void cleanConsoleLog() {
		Logger logger = Logger.getLogger("default.config"); logger.setLevel(java.util.logging.Level.OFF);
        String configFile = System.getProperty("java.util.logging.config.file");
        if (configFile == null) System.setProperty("java.util.logging.config.file", "ignoreAllSphinx4LoggingOutput");
	}

	/**
	 * A method that uses a 3rd party library to listen to the voice of the user.
	 */
	public String listenVoiceToString() {
		String utterance = "";
		
		recognizer.startRecognition(true);
        while (true) {
            utterance = recognizer.getResult().getHypothesis();
            
            System.out.println("Exit\n"
            		+ "Change/Switch chart to map/line/bar");

            if (utterance.startsWith("exit")) {
            	System.out.println("All right, exiting the aplication");
                break;
            }

            if (utterance.contains("chart")) {
                System.out.println("You said " + utterance);
                break;
            }
        }
        
        recognizer.stopRecognition();
		
		return utterance;
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
	public void speakMessage(String option) {
		if (!option.equals("")) {
			speak(option);
		} else {
			speak("I am not sure what you said there.");
		}
	}
}