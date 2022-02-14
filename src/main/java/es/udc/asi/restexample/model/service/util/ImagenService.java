package es.udc.asi.restexample.model.service.util;

import org.springframework.web.multipart.MultipartFile;

import es.udc.asi.restexample.model.exception.ModelException;
import es.udc.asi.restexample.model.service.dto.ImagenDTO;

public interface ImagenService {

  String saveImage(MultipartFile file, Long id) throws ModelException;

  ImagenDTO getImage(String imagePath, Long id) throws ModelException;
}

