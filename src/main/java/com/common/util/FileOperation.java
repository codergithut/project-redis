package com.common.util;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@Service
public interface FileOperation {

    byte[] getFileAsBytes(File file) throws IOException;

    boolean saveFileByByte(File file, byte[] content) throws IOException;

    String getFileAsString(File file) throws IOException;

    boolean removeFile(File file);

    List<File> getFilesByPath(File file, FileFilter fileFilter);



}
