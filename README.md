import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
import java.io.File;
import java.io.IOException;

public class JFRFileExtractor {

    public static void main(String[] args) {
        // Replace "path/to/your/recording.jfr" with the path to your JFR file
        String filePath = "path/to/your/recording.jfr";

        try {
            // Open the JFR file
            File file = new File(filePath);
            try (RecordingFile recordingFile = new RecordingFile(file.toPath())) {

                // Iterate over events in the JFR file
                while (recordingFile.hasMoreEvents()) {
                    RecordedEvent event = recordingFile.readEvent();

                    // Check if the event is an execution sample event
                    if ("jdk.ExecutionSample".equals(event.getEventType().getName())) {
                        // Extract class and method information from the stack trace
                                RecordedStackTrace recordedStackTrace = event.getStackTrace();
                        if (recordedStackTrace != null && recordedStackTrace.getFrames().size() > 0) {
                            RecordedStackTrace.StackFrame topFrame = recordedStackTrace.getFrames().get(0);
                            String className = topFrame.getClassName();
                            String methodName = topFrame.getMethodName();

                            // Print class and method information
                            System.out.println("Class: " + className);
                            System.out.println("Method: " + methodName);
                            System.out.println();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
