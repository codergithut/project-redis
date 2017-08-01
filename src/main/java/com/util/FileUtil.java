package com.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tianjian@gtmap.cn">tianjian</a>
 * @version 1.0, 2017/3/23
 * @description 普通文件操作方法可以用更好的东西替代
 */
public class FileUtil {
    public static void saveFile(byte[] data, String filePath) throws IOException {

        InputStream is = new ByteArrayInputStream(data);
        OutputStream fos = new FileOutputStream(filePath);
        //这里对is进行赋值，略
        //...
        // 文件输出流fos
        // openFile()为自定义函数，判断文件是否存在等（略）
        // 将输入流is写入文件输出流fos中
        int ch = 0;
        try {
            while ((ch = is.read()) != -1) {
                fos.write(ch);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            //关闭输入流等（略）
            fos.close();
            is.close();
        }
    }

    public static boolean removeFile(String path) {
        boolean flag = false;
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
                flag = true;
            }
        }
        return flag;
    }


    public static List<File> getFiles(String path) {
        List<File> filess = new ArrayList<File>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                System.out.println("文件夹是空的!");
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        String tempPath = file2.getAbsolutePath();
                        List<File> fileList = getFiles(tempPath);
                        if (null != fileList && !fileList.isEmpty()) {
                            for (File fileTemp : fileList) {
                                filess.add(fileTemp);
                            }

                        }

                    } else {
                        filess.add(file2);
                    }
                }
            }
        } else {
            file.mkdirs();
        }
        return filess;
    }

    public static String getFileString(File file) {
        StringBuffer data = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

//            BufferedReader in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                data.append(str + "\n");
            }
            in.close();

        } catch (IOException e) {
            e.getStackTrace();
        }
        return data.toString();
    }

    //获得指定文件的byte数组
    public static byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);

            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void saveFileByDirs(byte[] data, String backFilePath, List<String> dirs, String fileName) throws IOException {
        String queuePath = backFilePath + "\\" + dirs.get(0);
        File queueFile = new File(queuePath);
        if (!queueFile.exists()) {
            queueFile.mkdirs();
        }
        String codePath = queuePath + "\\" + dirs.get(1);
        File codeFile = new File(codePath);
        if (!codeFile.exists()) {
            codeFile.mkdirs();
        }
        saveFile(data, codeFile.getPath() + "\\" + fileName);
    }


}
