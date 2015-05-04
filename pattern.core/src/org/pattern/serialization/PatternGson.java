/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pattern.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.opencv.core.MatOfPoint;

/**
 * Provides gson with specified serializers and deserializers for easy access.
 *
 * @author palas
 */
public final class PatternGson {

    /**
     * Provides gson with specified serializers and deserializers for easy
     * access.
     *
     * @return
     */
    public static Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(
                        MatOfPoint.class, 
                        MatOfPointUtil.getSerializer())
                .registerTypeAdapter(
                        MatOfPoint.class, 
                        MatOfPointUtil.getDeserializer())
                .serializeSpecialFloatingPointValues()
                .setPrettyPrinting()
                .create();
    }

    // utility class
    private PatternGson() {
    }
}
