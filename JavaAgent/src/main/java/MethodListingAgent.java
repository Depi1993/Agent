import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Transformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class MethodListingAgent {
    private static final Logger logger = Logger.getLogger(MethodListingAgent.class.getName());

    static {
        try {
            // Create a FileHandler that writes logs to a file named "agent-logs.log"
            FileHandler fileHandler = new FileHandler("agent-logs.log");
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void premain(String arguments, Instrumentation instrumentation) {
        logger.info("!212");
        new AgentBuilder.Default()
                .type(ElementMatchers.any())
                .transform(new Transformer.ForAdvice()
                        .advice(ElementMatchers.isMethod(), MethodListingAgent.class.getName()))
                .installOn(instrumentation);
    }

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Class<?> clazz, @Advice.Origin("#m") String method) {
        logger.info(String.format("Method invoked: %s#%s", clazz.getName(), method));

    }
}
