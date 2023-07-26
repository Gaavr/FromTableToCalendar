package org.gaavr;

import org.gaavr.config.GoogleConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ContextConfiguration(classes = TestContext.class)
public class GoogleConfigTest {

    @Autowired
    private GoogleConfig googleConfig;

    @Test
    public void testGoogleConfigValues() {
        assertNotNull(googleConfig);
        assertEquals("YOUR_SPREADSHEET_ID", googleConfig.getSpreadsheetId());
        assertEquals("path/to/credentials.json", googleConfig.getCredentialsPath());
        assertEquals("YOUR_APPLICATION_NAME", googleConfig.getApplicationName());
    }
}

