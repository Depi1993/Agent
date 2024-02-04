import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Path;

public class JFRAnalyzer {

    public static void main(String[] args) {
        // Replace "path/to/your/recording.jfr" with the actual path to your JFR file
        String jfrFilePath = "path/to/your/recording.jfr";

        try {
            // Open the JFR file for reading
            try (RecordingFile recordingFile = new RecordingFile(Path.of(jfrFilePath))) {

                // Iterate through the events in the recording file
                while (recordingFile.hasMoreEvents()) {
                    // Read the next recorded event
                    RecordedEvent event = recordingFile.readEvent();

                    // Extract class and method information from the event
                    String className = event.getValue("class").toString();
                    String methodName = event.getValue("method").toString();

                    // Print or process the class and method information as needed
                    System.out.println("Class: " + className + ", Method: " + methodName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
