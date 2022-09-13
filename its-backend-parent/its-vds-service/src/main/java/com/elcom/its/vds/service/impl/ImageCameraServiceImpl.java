/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.ImageCamera;
import com.elcom.its.vds.repository.ImageCameraRepository;
import com.elcom.its.vds.service.ImageCameraService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ImageCameraServiceImpl implements ImageCameraService {

    @Autowired
    private ImageCameraRepository imageCameraRepository;

    @Override
    public ImageCamera create(ImageCamera imageCamera) {
        imageCamera = imageCameraRepository.save(imageCamera);
        return imageCamera;
    }

    @Override
    public Optional<ImageCamera> findById(String id) {
        return imageCameraRepository.findById(id);
    }

    @Override
    public ImageCamera update(ImageCamera imageCamera) {
        imageCamera = imageCameraRepository.save(imageCamera);
        return imageCamera;
    }

    @Override
    public void delete(ImageCamera imageCamera) {
        imageCameraRepository.delete(imageCamera);
    }

    @Override
    public Page<ImageCamera> getAllByPage(Pageable pageable) {
        return imageCameraRepository.findAll(pageable);
    }

    @Override
    public List<ImageCamera> getAll() {
        return imageCameraRepository.findAll();
    }

    @Override
    public List<ImageCamera> findByCameraId(String cameraId) {
        return imageCameraRepository.findByCameraId(cameraId);
    }
}
