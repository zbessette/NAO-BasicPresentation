


import java.util.*;

import com.aldebaran.qi.*;
import com.aldebaran.qi.helper.*;
import com.aldebaran.qi.helper.proxies.*;

public class ReactToEvents {

    public static void main(String[] args) throws Exception {

        String robotUrl = "tcp://karl.local:9559";
        
        
        // Create a new application
        Application application = new Application(args, robotUrl);
        
        
        // Start your application
        application.start();
        System.out.println("Successfully connected to the robot");
        
        
        // Subscribe to selected ALMemory events
        ReactToEvents reactor = new ReactToEvents();
        reactor.run(application.session());
        System.out.println("Program is now running");
        
        
        // Run your application
        application.run();

    }
    
    ALBasicAwareness awareness;
    ALSpeechRecognition speech;
    ALMemory memory;
    ALTextToSpeech tts;
    ALAnimatedSpeech animTTS;
    ALMotion motion;
    ALFaceCharacteristics faces;
    ALFaceDetection detectFace;
    ALPeoplePerception percep;
    ALAudioPlayer audio;
    long frontTactilSubscriptionId;
    long rearTactilSubscriptionId;
    long leftBumperSubscriptionId;
    long rightBumperSubscriptionId;
    long leftHandSubscriptionId;
    long rightHandSubscriptionId;
    long wordRecognizedId;
    long face;
    int face1;
    int count;
    int headCount;
    int footCount;
    ArrayList<Integer> peopleList;

    
    public void run(Session session) throws Exception {

        memory = new ALMemory(session);
        tts = new ALTextToSpeech(session);
        animTTS = new ALAnimatedSpeech(session);
        motion = new ALMotion(session);
        speech = new ALSpeechRecognition(session);
        awareness = new ALBasicAwareness(session);
        faces = new ALFaceCharacteristics(session);
        audio = new ALAudioPlayer(session);
        detectFace = new ALFaceDetection(session);
        percep = new ALPeoplePerception(session);
        
        frontTactilSubscriptionId = 0;
        rearTactilSubscriptionId = 0;
        leftBumperSubscriptionId = 0;
        rightBumperSubscriptionId = 0;
        leftHandSubscriptionId = 0;
        rightHandSubscriptionId = 0;
        wordRecognizedId = 0;
        count = 0;
        face1 = 0;
        peopleList = new ArrayList<Integer>();

        
        speech.setVisualExpression(true);
        speech.setLanguage("English");
        animTTS.setBodyLanguageMode(2);
        motion.setPushRecoveryEnabled(true);
        percep.setFaceDetectionEnabled(true);
        System.out.println(tts.getAvailableVoices());
        System.out.println(tts.getAvailableLanguages());
        tts.setLanguage("English");
        tts.setVoice("naoenu");
        memory.subscribeToEvent("BasicChannel", null);
        /*
        //subscribe to left hand 
        //say touched right of left hand
        leftHandSubscriptionId = memory.subscribeToEvent("HandLeftRightTouched" , 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 0){
        				animTTS.say("That is the right side of my left hand."); // 안녕하세요
        				count++;
        			}
        		}
        	});
        
        //say touched left of left hand 
        leftHandSubscriptionId = memory.subscribeToEvent("HandLeftLeftTouched" , 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 1){
        				animTTS.say("That is the left side of my left hand.");
        				count++;
        			}
        		}
        	});
        //say stop touching my hands
        leftHandSubscriptionId = memory.subscribeToEvent("HandRightLeftTouched" , 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 2){
        				animTTS.say("Stop touching my hands.");
        				count++;
        			}
        		}
        	});
        // Subscribe to FrontTactilTouched event,
        // create an EventCallback expecting a Float.
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 3) {
        				animTTS.say("That is the top of my head.");
        				count++;
        				headCount++;
        			}
        		}
        	});
        //Left foot touched
        //respond "you touched my left foot
        leftBumperSubscriptionId = memory.subscribeToEvent("LeftBumperPressed", 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if(arg0 > 0 && count == 4){
        				animTTS.say("You touched my left foot.");
        				count++;
        				footCount++;
        			}
        		}
        	});
        //right foot pressed
        //respond you touched my right foot
        rightBumperSubscriptionId = memory.subscribeToEvent("RightBumperPressed", 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if(arg0 > 0 && count == 5){
        				animTTS.say("you touched my right foot.");
        				count++;
        				footCount++;
        			}
        		}
        	});
        
        //say hello in Korean
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
            	new EventCallback<Float>() {
            		public void onEvent(Float arg0)
            				throws InterruptedException, CallError {
            			if (arg0 > 0 && count == 6) {
            				tts.setLanguage("Korean");
            		        tts.setVoice("sora");
            				animTTS.say("안녕하세요");
            				count++;
            				headCount++;
            				tts.setLanguage("English");
            		        tts.setVoice("naoenu");
            			}
            		}
            	});
            	
        
        //see face; guess age and gender        
        
        
        
        */
        face = memory.subscribeToEvent("FrontTactilTouched", 
        		new EventCallback<Float>() {
    				public void onEvent(Float arg0)
    						throws InterruptedException, CallError {
    					if(arg0 > 0){ // && count == 7
    				        //detectFace.clearDatabase();
    						int[] gender = new int[2];
            				int[] age = new int[2];
            				String sex;
            				detectFace.learnFace("zyon");
            				ArrayList<String> tempAge = new ArrayList<String>();
            				ArrayList<String> tempGender = new ArrayList<String>();
            				//System.out.println(Integer.valueOf(tempAge.get(0).replaceAll("\\D+","")));
            				tempAge.addAll(memory.getDataList("PeoplePerception/Person"));
            				tempGender.addAll(memory.getDataList("PeoplePerception/Person"));
            				//faces.analyzeFaceCharacteristics(Integer.valueOf(tempAge.get(0).replaceAll("\\D+","")));
            				//tempAge.addAll(memory.getDataList("PeoplePerception/Person"));
            				//tempGender.addAll(memory.getDataList("PeoplePerception/Person"));
            				System.out.println(tempAge + "\n" + tempGender);
            				/*age = (int[]) memory.getData(tempAge.get(0));
            				gender = (int[]) memory.getData(tempGender.get(0));
            				System.out.println(age[0]);
            				System.out.println(gender[0]);
            				if(gender[0] == 0){
            				    sex = "girl";
            				}
            				else{
            				    sex = "boy";
            				}
            				animTTS.say("I think you are a " + sex + " and I think you are " + age[0] + " but I am only " + age[1] + " percent sure");
            				*/
            				animTTS.say("I think your shirt color is" + memory.getData(tempAge.get(6)));
            				count++;
    					}
    				}
    		});
    	
        
        //how many times head and feet have been touched
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 8) { 
        				headCount++;
        				animTTS.say("You have touched my head" + headCount + " times. You have touched my feet " + footCount + " times.");
        				count++;
        			}
        		}
        	});
        	
        
        //play audio 
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
        	new EventCallback<Float>() {
        		public void onEvent(Float arg0)
        				throws InterruptedException, CallError {
        			if (arg0 > 0 && count == 9) {  
        				headCount++;
        				count++;
        				audio.playFile("/home/nao/karlSong.mp3"); 
        				
        			}
        		}
        	});
        //Stop audio
         
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
            	new EventCallback<Float>() {
            		public void onEvent(Float arg0)
            				throws InterruptedException, CallError {
            			if (arg0 > 0 && count == 10) {  
            				headCount++;
            				audio.stopAll(); 
            				count++;
            			}
            		}
            });
        
        frontTactilSubscriptionId = memory.subscribeToEvent("FrontTactilTouched", 
            	new EventCallback<Float>() {
            		public void onEvent(Float arg0)
            				throws InterruptedException, CallError {
            			if (arg0 > 0 && count == 11) { 
            				motion.moveTo(0.5f, 0.0f, 0.0f);
            				count++;
            			}
            		}
            });
            
			
    }
}