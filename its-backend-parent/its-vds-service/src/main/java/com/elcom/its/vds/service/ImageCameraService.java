/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.ImageCamera;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface ImageCameraService {

    Page<ImageCamera> getAllByPage(Pageable pageable);

    List<ImageCamera> getAll();

    ImageCamera create(ImageCamera imageCamera);

    ImageCamera update(ImageCamera imageCamera);

    Optional<ImageCamera> findById(String id);

    void delete(ImageCamera imageCamera);

    List<ImageCamera> findByCameraId(String cameraId);
}
