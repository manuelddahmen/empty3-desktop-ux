/*
 *
 *  *
 *  *  * Copyright (c) 2025. Manuel Daniel Dahmen
 *  *  *
 *  *  *
 *  *  *    Copyright 2024 Manuel Daniel Dahmen
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *  *
 *
 *
 *
 *  * Created by $user $date
 *
 *
 */

package one.empty3.apps.facedetect.gcp;

import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageProcessorTest {

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private BufferedWriter bufferedWriter;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(httpResponse.getWriter()).thenReturn(bufferedWriter);
    }

    @Test
    void testService_ValidJsonInput() throws IOException {
        // Arrange
        ImageProcessor imageProcessor = new ImageProcessor();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image1", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="); // Replace with valid base64 data
        jsonObject.addProperty("textFile1", "someTextData");
        jsonObject.addProperty("token", "testToken");
        jsonObject.addProperty("model", "v 1.0\n");
        jsonObject.addProperty("image3", "someImageData3");
        jsonObject.addProperty("textFile2", "file2");
        jsonObject.addProperty("textFile3", "file3");
        jsonObject.addProperty("hd_texture", "true");
        jsonObject.addProperty("selected_algorithm", "1");
        jsonObject.addProperty("selected_texture_type", "Bezier texture");

        BufferedReader reader = new BufferedReader(new StringReader(jsonObject.toString()));
        when(httpRequest.getReader()).thenReturn(reader);

        // Act
        imageProcessor.service(httpRequest, httpResponse);

        // Assert
        ArgumentCaptor<String> responseCaptor = ArgumentCaptor.forClass(String.class);
        verify(bufferedWriter, atLeastOnce()).write(responseCaptor.capture());
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Origin"), eq("*"));
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Methods"), eq("POST, OPTIONS"));
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Headers"), eq("Content-Type"));

        // Check if response contains completion and image
        String responseString = responseCaptor.getValue();
        assertTrue(responseString.contains("\"completion\""));
        assertTrue(responseString.contains("\"image\""));
    }
    @Test
    void testService_ValidJsonInput_data() throws IOException {
        // Arrange
        ImageProcessor imageProcessor = new ImageProcessor();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image1", "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="); // Replace with valid base64 data
        jsonObject.addProperty("textFile1", "someTextData");
        jsonObject.addProperty("token", "testToken");
        jsonObject.addProperty("model", "v 1.0\n");
        jsonObject.addProperty("image3", "someImageData3");
        jsonObject.addProperty("textFile2", "file2");
        jsonObject.addProperty("textFile3", "file3");
        jsonObject.addProperty("hd_texture", "true");
        jsonObject.addProperty("selected_algorithm", "1");
        jsonObject.addProperty("selected_texture_type", "Bezier texture");

        BufferedReader reader = new BufferedReader(new StringReader(jsonObject.toString()));
        when(httpRequest.getReader()).thenReturn(reader);

        // Act
        imageProcessor.service(httpRequest, httpResponse);

        // Assert
        ArgumentCaptor<String> responseCaptor = ArgumentCaptor.forClass(String.class);
        verify(bufferedWriter, atLeastOnce()).write(responseCaptor.capture());
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Origin"), eq("*"));
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Methods"), eq("POST, OPTIONS"));
        verify(httpResponse, atLeastOnce()).appendHeader(eq("Access-Control-Allow-Headers"), eq("Content-Type"));

        // Check if response contains completion and image
        String responseString = responseCaptor.getValue();
        assertTrue(responseString.contains("{\"completion\":"));
        assertTrue(responseString.contains("\"image\":"));
        assertTrue(responseString.contains("testToken"));
    }

    @Test
    void testService_NullJsonInput() throws IOException {
        // Arrange
        ImageProcessor imageProcessor = new ImageProcessor();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        when(httpRequest.getReader()).thenReturn(reader);

        // Act
        imageProcessor.service(httpRequest, httpResponse);

        // Assert
        ArgumentCaptor<String> responseCaptor = ArgumentCaptor.forClass(String.class);
        verify(bufferedWriter, atLeastOnce()).write(responseCaptor.capture());
        String responseString = responseCaptor.getValue();
        assertTrue(responseString.contains("jSonObject is null in ImageProcessor"));
    }

    @Test
    void testService_RuntimeException() throws IOException {
        // Arrange
        ImageProcessor imageProcessor = new ImageProcessor();
        BufferedReader reader = mock(BufferedReader.class);
        when(httpRequest.getReader()).thenReturn(reader);
        when(reader.readLine()).thenThrow(new RuntimeException("Test Exception"));

        // Act
        imageProcessor.service(httpRequest, httpResponse);

        // Assert
        verify(httpResponse).setStatusCode(500);
    }
}