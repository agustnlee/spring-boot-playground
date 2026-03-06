package com.agustin.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String upload(MultipartFile file) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder",        "products",
                    "resource_type", "auto"
            )
            );
            return (String) result.get("secure_url");  // URL stored in DB
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    public void delete(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Image deletion failed: " + e.getMessage());
        }
    }

    // pull "products/filename" full url
    private String extractPublicId(String imageUrl) {
        String[] parts    = imageUrl.split("/");
        String filename   = parts[parts.length - 1];
        String folder     = parts[parts.length - 2];
        String nameNoExt  = filename.split("\\.")[0];
        return folder + "/" + nameNoExt;
    }
}