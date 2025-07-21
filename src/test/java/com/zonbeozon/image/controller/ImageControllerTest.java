package com.zonbeozon.image.controller;


import com.zonbeozon.SimpleSecurityEnabledWebMvcTest;
import com.zonbeozon.image.service.ImageS3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SimpleSecurityEnabledWebMvcTest(value = ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final byte[] testImageByte = "dummy_image_content_for_png".getBytes();;

    @MockitoBean
    private ImageS3Uploader imageS3Uploader;

    MockMultipartFile multipartFile = new MockMultipartFile(
            "image",
            "dsdsdf.png",
            MediaType.IMAGE_PNG_VALUE,
            testImageByte
    );

    @BeforeEach
    void setup() throws FileNotFoundException {
        Mockito.when(imageS3Uploader.uploadImage(Mockito.any(InputStream.class), Mockito.anyLong(), Mockito.anyString())).thenReturn(1L);
    }

    @DisplayName("Mutlipart-form 내부의 image 형식이 잘못되면 400 리턴")
    @Test
    @WithMockUser
    void returnBadRequestWhenFilePartContentTypeIsInvalid() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "image",
                "aaaa",
                MediaType.MULTIPART_FORM_DATA_VALUE, // 유효하지 않은 Content-Type
                testImageByte
        );

        mockMvc.perform(
                        multipart("/image")
                                .file(invalidFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Mutlipart-form이 아니라면 400 리턴")
    @Test
    @WithMockUser
    void returnBadRequestWhenRequestContentTypeIsNotMultipart() throws Exception {
        mockMvc.perform(
                        multipart("/image")
                                .file(multipartFile)
                                .contentType(MediaType.IMAGE_PNG_VALUE)
                )
                .andExpect(status().isBadRequest());
    }



    @DisplayName("로그인되어 있지 않다면 401 리턴")
    @Test
    void returnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(
                        multipart("/image")
                                .file(multipartFile)
                                .contentType(MediaType.IMAGE_PNG_VALUE)
                )
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("정상 수행되었다면 201과 함께 imageId 리턴")
    @Test
    @WithMockUser
    void returnCreatedWithImageIdOnValidRequest() throws Exception {
        mockMvc.perform(
                        multipart("/image")
                                .file(multipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1L));
    }
}
