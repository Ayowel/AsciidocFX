package com.kodedu.service.extension;

/**
 * Created by usta on 25.12.2014.
 */
public interface TreeService extends DefaultSettings {
    public final static String label = "core::service::extension::Tree";
    public void createFileTree(String tree, String type, String imagesDir, String imageTarget, String nodename);
    public void createHighlightFileTree(String tree, String type, String imagesDir, String imageTarget, String nodename);
}
