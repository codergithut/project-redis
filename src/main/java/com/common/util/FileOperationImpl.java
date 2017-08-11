package com.common.util;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianian</a>
 * @version 1.0, 2017/8/11
 * @description
 */
@Service
public class FileOperationImpl implements  FileOperation{

    @Override
    public byte[] getFileAsBytes(File file) throws IOException {
        ByteSource byteSource = null ;

        byteSource = Files.asByteSource(file);

        return byteSource.read();
    }

    @Override
    public boolean saveFileByByte(File file, byte[] content) {

        try {

            Files.write(content, file);

            return true;

        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public String getFileAsString(File file) throws IOException {

        BufferedReader reader = null ;

        String fileString = null;

        try {

            reader = Files.newReader(file, Charsets.UTF_8);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } finally {

            reader.close();

            return fileString;

        }
    }

    @Override
    public boolean removeFile(File file) {

        return file.delete();

    }

    @Override
    public List<File> getFilesByPath(File file, FileFilter fileFilter) {

        List<File> allFile = new ArrayList<File>();

        if(!file.exists()) {
            return null;
        }

        File[] files = file.listFiles();

        for(File fileDetail : files) {

            if(fileDetail.isFile()) {

                if(fileFilter != null && !fileFilter.accept(fileDetail)) {
                    continue;
                }

                allFile.add(fileDetail);
            }

            if(fileDetail.isDirectory()) {

                getFilesByPath(fileDetail, fileFilter);

            }
        }

        return allFile;
    }


}
