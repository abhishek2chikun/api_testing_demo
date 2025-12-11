package com.example.api.tests;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Optional integration test that runs Schemathesis (external CLI) against the live OpenAPI spec.
 *
 * Requirements:
 * - Schemathesis must be installed and available on PATH (schemathesis --version should work)
 * - Set environment variable RUN_SCHEMATHESIS=true to enable running this test
 *
 * This test will be skipped when the environment variable is not set or schemathesis is not present.
 */
public class SchemathesisRunnerTest extends BaseTest {

    private static String openApiUrl;

    @BeforeAll
    public static void setup() {
        configureBaseUri();
        // OpenAPI is expected at /openapi.json relative to baseURI
        openApiUrl = RestAssured.baseURI + "/openapi.json";
    }

    /**
     * Attempt to invoke schemathesis CLI to perform lightweight property-based checks.
     * The test is skipped if RUN_SCHEMATHESIS env var is not "true" or schemathesis CLI is missing.
     */
    @Test
    public void testRunSchemathesisIfAvailable() throws Exception {
        String runFlag = System.getenv("RUN_SCHEMATHESIS");
        Assumptions.assumeTrue("true".equalsIgnoreCase(runFlag), "RUN_SCHEMATHESIS not enabled; skipping schemathesis tests");

        // Verify schemathesis CLI is installed
        ProcessBuilder versionPb = new ProcessBuilder("schemathesis", "--version");
        Process versionProc = versionPb.start();
        int verExit = versionProc.waitFor();
        Assumptions.assumeTrue(verExit == 0, "schemathesis CLI not found in PATH; skipping");

        // Run a lightweight schemathesis invocation. We keep it conservative to avoid long runs.
        // Note: CLI flags may vary by schemathesis version; common flags used below are conservative.
        ProcessBuilder pb = new ProcessBuilder(
                "schemathesis",
                "run",
                openApiUrl,
                "--hypothesis-max-examples=5",
                "-q"
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = r.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            int exitCode = process.waitFor();
            // Schemathesis returns non-zero when failures are found. We assert that the command executed.
            // Instead of failing the test on non-zero (which would indicate property violations), we surface output.
            // Tests depending on CI policy may enforce exitCode == 0. Here we simply log and assert the CLI executed.
            System.out.println("Schemathesis exit code: " + exitCode);
            System.out.println("Schemathesis output:\n" + output.toString());
            // If exitCode != 0, fail the test to signal issues found by schemathesis
            if (exitCode != 0) {
                throw new AssertionError("Schemathesis found issues. Exit code: " + exitCode + "\nOutput:\n" + output.toString());
            }
        }
    }
}