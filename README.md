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




//------

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.openjdk.jmc.common.item.IItem;
import org.openjdk.jmc.common.item.IItemCollection;
import org.openjdk.jmc.common.item.IItemIterable;
import org.openjdk.jmc.flightrecorder.CouldNotLoadRecordingException;
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit;

public class JFRClassUsageAnalyzer {

    public static void main(String[] args) {

        String jfrFilePath = "/Users/deepakdhaka/Downloads/basic.jfr";
        Path jfrPath = Paths.get(jfrFilePath);
        File jfrFile = jfrPath.toFile();

        try {
            IItemCollection items = JfrLoaderToolkit.loadEvents(jfrFile);
            Set<String> classesUsed = extractUsedClasses(items);

            // Print the classes used
            classesUsed.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CouldNotLoadRecordingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Set<String> extractUsedClasses(IItemCollection items) {
        Set<String> classesUsed = new HashSet<>();
        Set<String> classesUsed1 = new HashSet<>();
        for (IItemIterable item : items) {
            if ("jdk.ClassLoad".equals(item.getType().getIdentifier())) {
                classesUsed.add(item.getType().getIdentifier());
            }

        }
        items.forEach(item -> {
            if ("jdk.ClassLoad".equals(item.getType().getIdentifier())) {
                item.get().forEach(loadedClass -> {
                    String className =  loadedClass.toString();
                    classesUsed1.add(className);
                });
            }
        });
        return classesUsed;
    }
}

