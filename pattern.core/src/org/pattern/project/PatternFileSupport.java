/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.project;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.netbeans.api.project.FileOwnerQuery;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.pattern.api.MultiImageImporter;
import org.pattern.api.MultiImageImporterFinder;
import org.pattern.data.MultiImage;
import org.pattern.data.Particle;
import org.pattern.data.ParticleImage;
import org.pattern.data.UniImage;
import org.pattern.serialization.PatternGson;

/**
 * Util operations to support files with {@code .pattern} extension.
 * @author palas
 */
public class PatternFileSupport {

    private static final String PATTERN_EXT = "pattern";
    private static final String PARTICLES = "particles";
    private static final String IMAGE = "image";
    private static final String NAME = "name";
    private static final String ENCODING = "UTF-8";
    
    public static FileObject getImageFileObject(FileObject patternFile){
        try {
            String json = FileUtils.readFileToString(FileUtil.toFile(patternFile), ENCODING);
            JsonObject root = new JsonParser().parse(json).getAsJsonObject();
            String imagePath = root.get(IMAGE).getAsString();
            FileObject projDir = FileOwnerQuery.getOwner(patternFile).getProjectDirectory();
            return projDir.getFileObject(
                "images/"+ imagePath);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    /**
     * Loads pattern {@link MultiImage} according to information contained in
     * given file.
     *
     * @param fp .pattern file, must exist already
     * @return
     * @throws IOException
     */
    public static MultiImage load(FileObject fp) throws IOException {
        if (fp.isFolder()) {
            throw new IllegalArgumentException("Given folder. File expected");
        }

        Gson gson = PatternGson.gson();
        String json = FileUtils.readFileToString(FileUtil.toFile(fp), ENCODING);

        JsonObject root = new JsonParser().parse(json).getAsJsonObject();

        String name = root.get(NAME).getAsString();
        String imagePath = root.get(IMAGE).getAsString();

        FileObject projectFolder = fp.getParent().getParent(); // comming from .pattern -> images -> project
        FileObject imagesFolder = projectFolder.getFileObject(
                "images/"+ imagePath);
        File imageFile = FileUtil.toFile(imagesFolder);

        MultiImageImporter importer = MultiImageImporterFinder.findImporter(imageFile);
        MultiImage image = importer.importData(imageFile);
        image.image = imagePath;
        image.name = name;

        if (image instanceof UniImage) {
            List<Particle> particleList;
            JsonArray particles = root.getAsJsonArray(PARTICLES);
            if (particles.size() == 0) {
                particleList = new ArrayList<>();
            } else {
                particleList = gson.fromJson(particles,
                        new TypeToken<List<Particle>>() {
                        }.getType());
            }
            image.getSelectedImage().assign(particleList);
        } else {
            throw new UnsupportedOperationException("Parsing multi image particles not supported!");
        }

        return image;
    }

    /**
     * Saves data to file. Extracts data from {@link ParticleImage} contained in
     * {@link MultiImage} and saves them to given file
     *
     * @param fp .pattern where to save, must exist already
     * @param image
     * @throws IOException
     */
    public static void savePatternFile(FileObject fp, MultiImage image) throws IOException {
        if (image != null) {
            Gson gson = PatternGson.gson();
            JsonObject root = new JsonObject();
            root.addProperty(NAME, image.name);
            root.addProperty(IMAGE, image.image);

            List<Particle> particles = image.getSelectedImage().getParticles();
            Type type  = new TypeToken<List<Particle>>() {}.getType();
            root.add(PARTICLES, gson.toJsonTree(particles, type));
            String json = gson.toJson(root);
            FileUtils.writeStringToFile(FileUtil.toFile(fp), json);
        }
    }

    /**
     * Creates new "name".pattern file in project in correct folder.
     *
     * @param project
     * @param name name of image
     * @param imageNameExt assigned image name with file extension eg.
     * image1.tif
     * @throws IOException
     */
    public static void createPatternFile(PatternProject project, String name, String imageNameExt) throws IOException {
        FileObject patternFile = project.getDataFolder()
                .createData(name, PATTERN_EXT);
        JsonObject root = new JsonObject();
        root.addProperty(NAME, name);
        root.addProperty(IMAGE, imageNameExt);
        root.add(PARTICLES, new JsonArray());

        String json = PatternGson.gson().toJson(root);

        FileUtils.writeStringToFile(
                FileUtil.toFile(patternFile),
                json
        );
    }

    /**
     * Loads image data from files.
     *
     * @param fi
     * @param fp
     */
    public static MultiImage load(FileObject fi, FileObject fp) {
        try {
            File file = FileUtil.toFile(fi);
            MultiImageImporter importer = MultiImageImporterFinder.findImporter(file);
            MultiImage image = importer.importData(file);

            if (image instanceof UniImage) {
                List<Particle> particles = loadParticlesForUniImage(fp);
                image.getSelectedImage().assign(particles);
            } else {
                throw new UnsupportedOperationException("Parsing multi image particles not supported!");
            }
            return image;
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    public static void save(MultiImage image, FileObject primary, FileObject secondary) {
        if (image instanceof UniImage) {
            try {
                saveParticlesForUniImage(secondary, image.getSelectedImage().getParticles());
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        } else {
            throw new UnsupportedOperationException("Parsing multi image particles not supported!");
        }
    }

    private static List<Particle> loadParticlesForUniImage(FileObject secondary) throws IOException {
        Gson gson = PatternGson.gson();
        File file = FileUtil.toFile(secondary);
        String json = FileUtils.readFileToString(file, "UTF-8");
        if (json.isEmpty()) {
            return new ArrayList<Particle>();
        }
        return gson.fromJson(json,
                new TypeToken<List<Particle>>() {
                }.getType());
    }

    private static void saveParticlesForUniImage(FileObject secondary, List<Particle> particles) throws IOException {
        Gson gson = PatternGson.gson();
        File file = FileUtil.toFile(secondary);
        if (file.exists()) {

            String json = gson.toJson(particles);
            FileUtils.writeStringToFile(file, json);
        } else {
            throw new FileNotFoundException("Writting to non existing file");
        }
    }

}
