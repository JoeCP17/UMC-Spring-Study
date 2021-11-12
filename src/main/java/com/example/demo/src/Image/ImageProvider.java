package com.example.demo.src.Image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageProvider {
    private final ImageDao imageDAO;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImageProvider(ImageDao imageDAO){
        this.imageDAO = imageDAO;
    }

}
