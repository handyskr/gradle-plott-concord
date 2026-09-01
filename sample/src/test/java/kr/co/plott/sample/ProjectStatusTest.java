package kr.co.plott.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectStatusTest {
    @Test
    void reportsReady() {
        assertEquals("ready", ProjectStatus.current());
    }
}
