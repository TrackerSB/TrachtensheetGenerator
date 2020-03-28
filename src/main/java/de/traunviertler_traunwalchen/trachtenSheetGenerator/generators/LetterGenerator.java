package de.traunviertler_traunwalchen.trachtenSheetGenerator.generators;

import de.traunviertler_traunwalchen.trachtenSheetGenerator.model.LetterData;
import de.traunviertler_traunwalchen.trachtenSheetGenerator.model.ReceivingAssociation;
import de.traunviertler_traunwalchen.trachtenSheetGenerator.model.SendingAssociation;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LetterGenerator {
    private static final Logger LOGGER = Logger.getLogger(LetterGenerator.class.getName());
    private static final Configuration TEMPLATE_ENGINE_CONFIGURATION = new Configuration(Configuration.VERSION_2_3_30) {
        {
            try {
                setDirectoryForTemplateLoading(new File(LetterGenerator.class.getResource("templates").getFile()));
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "Generation configuration failed", ex);
            }
            setDefaultEncoding("UTF-8");
            setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            setLogTemplateExceptions(false);
            setWrapUncheckedExceptions(true);
            setFallbackOnNullLoopVariable(false);
            setInterpolationSyntax(Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
        }
    };
    private static final Template EMPTY_LETTER;

    static { // Initialize templates
        try {
            EMPTY_LETTER = TEMPLATE_ENGINE_CONFIGURATION.getTemplate("emptyLetter.tex");
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private LetterGenerator() {
    }

    private static Path createTempFile() throws IOException {
        Path tempFilePath = Files.createTempFile("TrachtenSheetGenerator", ".tex");
        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    try {
                        Files.delete(tempFilePath);
                    } catch (IOException ex) {
                        LOGGER.log(Level.INFO, "Could not delete temporary file", ex);
                    }
                }));
        return tempFilePath;
    }

    @NotNull
    public static Map<ReceivingAssociation, Path> from(
            Iterable<ReceivingAssociation> receivers, LetterData letterData) {
        Map<ReceivingAssociation, Path> generatedLetters = new HashMap<>();
        receivers.forEach(association -> {
            try {
                Path tempFilePath = createTempFile();
                EMPTY_LETTER.process(Map.of(
                        "sender", SendingAssociation.TRAUNVIERTLER,
                        "letter", letterData,
                        "receiver", association
                ), Files.newBufferedWriter(tempFilePath));
                generatedLetters.put(association, tempFilePath);
            } catch (TemplateException | IOException ex) {
                LOGGER.log(Level.WARNING, "Could not generate template", ex);
            }
        });
        return generatedLetters;
    }
}
