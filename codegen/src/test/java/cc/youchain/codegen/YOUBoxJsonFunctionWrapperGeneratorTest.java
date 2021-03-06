package cc.youchain.codegen;

import cc.youchain.utils.Strings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertTrue;

public class YOUBoxJsonFunctionWrapperGeneratorTest {

    private static String youboxBaseDir;
    protected static String tempDirPath;

    static final String JAVA_TYPES_ARG = "--javaTypes";
    static final String SOLIDITY_TYPES_ARG = "--solidityTypes";
    static final String PRIMITIVE_TYPES_ARG = "--primitiveTypes";

    @Before
    public void setUp() throws Exception {

        URL url = YOUBoxJsonFunctionWrapperGeneratorTest.class.getClass().getResource("/youbox");
        youboxBaseDir = url.getPath();

        tempDirPath = youboxBaseDir + "/generated";

    }

    //    @After
//    public void tearDown() throws Exception {
//        for (File file : tempDir.listFiles()) {
//            file.delete();
//        }
//        tempDir.delete();
//    }

    @Test
    public void testGreeter() throws Exception {

        testCodeGeneration( "greeter", "greeter", "cc.youchain.unittests.java", false);
    }

    private void testCodeGeneration(
            String contractName,
            String inputFileName,
            String packageName,
            boolean primitives)
            throws Exception {

        List<String> options = new ArrayList<>();
        options.add(JAVA_TYPES_ARG);
        //options.add(SOLIDITY_TYPES_ARG);
        options.add(
                youboxBaseDir
                        + File.separator
                        + contractName
                        + File.separator
                        + inputFileName
                        + ".json");
        options.add("-p");
        options.add(packageName);
        options.add("-o");
        options.add(tempDirPath);

        if (primitives) {
            options.add(PRIMITIVE_TYPES_ARG);
        }

        YOUBoxJsonFunctionWrapperGenerator.main(options.toArray(new String[options.size()]));

        verifyGeneratedCode(
                tempDirPath
                        + File.separator
                        + packageName.replace('.', File.separatorChar)
                        + File.separator
                        + Strings.capitaliseFirstLetter(inputFileName)
                        + ".java");
    }

    private void verifyGeneratedCode(String sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        try (StandardJavaFileManager fileManager =
                     compiler.getStandardFileManager(diagnostics, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromStrings(Arrays.asList(sourceFile));
            JavaCompiler.CompilationTask task =
                    compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
            boolean result = task.call();

            System.out.println(diagnostics.getDiagnostics());
            assertTrue("Generated contract contains compile time error", result);
        }
    }


}
