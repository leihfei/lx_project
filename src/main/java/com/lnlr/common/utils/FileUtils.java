package com.lnlr.common.utils;

import com.lnlr.common.exception.ParamException;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * author:郭涛
 * Date:2018/12/7 16:43
 */
@NoArgsConstructor
public class FileUtils {

    private MultipartFile file = null;

    private Integer kb = 1024;
    private Integer mb = kb * kb;


    public FileUtils(MultipartFile file) {
        this.file = file;
    }

    /**
     * 获取文件名（不带后缀）
     *
     * @return
     */
    public String getName() {
        return file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
    }

    /**
     * 获取文件名（带后缀）
     *
     * @return
     */
    public String getFullName() {
        return file.getOriginalFilename();
    }

    /**
     * 获取文件后缀
     *
     * @return
     */
    public String getSuffix() {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    }

    /**
     * 获取文件大小（带单位）
     *
     * @return
     */
    public String getSize() {
        long size = file.getSize();
        String sizeStr;
        if (size < kb) {
            sizeStr = size + "b";
        } else if (size % kb > 0 && size % kb < kb) {
            sizeStr = size / kb + "kb";
        } else {
            sizeStr = size / mb + "mb";
        }
        return sizeStr;
    }

    /**
     * 上传文件
     *
     * @param filePath 保存路径
     * @param fileName 文件名
     * @throws IOException
     */
    public void upload(String filePath, String fileName) throws IOException {
        //判断保存文件的路径是否存在，不存在则创建
        File fileLocation = new File(filePath);
        if (!fileLocation.exists()) {
            fileLocation.mkdirs();
        }
        File dest = new File(new File(filePath).getAbsolutePath() + File.separator + fileName);
        file.transferTo(dest);
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void remove(String path) {
        File dest = new File(path);
        if (dest.exists()) {
            dest.delete();
        }
    }

    public static void remove(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 单个文件下载
     *
     * @param response
     * @param filename 下载时显示的文件名
     * @param path     文件路径
     */
    public static void download(HttpServletResponse response, String filename, String path) {
        if (filename != null) {
            FileInputStream is = null;
            BufferedInputStream bs = null;
            OutputStream os = null;
            try {
                File file = new File(path);
                if (file.exists()) {
                    //设置Headers
                    response.setHeader("Content-Type", "application/octet-stream");
                    //设置下载的文件的名称-该方式已解决中文乱码问题
                    response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO8859-1"));
                    is = new FileInputStream(file);
                    bs = new BufferedInputStream(is);
                    os = response.getOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = bs.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    throw new ParamException("文件不存在！");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (bs != null) {
                        bs.close();
                    }
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 多文件下载（以ZIP包的方式）
     *
     * @param response
     * @param zipPath   zip缓存路径
     * @param fileNames 文件名称
     * @param filePaths 文件路径
     */
    public static void downloadZIP(HttpServletResponse response, String zipPath, List<String> fileNames, List<String> filePaths) {
        //存放zip文件的临时目录
        File zipTempPath = new File(zipPath);
        if (!zipTempPath.isDirectory() && !zipTempPath.exists()) {
            zipTempPath.mkdirs();
        }
        //设置最终输出zip文件的目录+文件名
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        String zipFileName = formatter.format(new Date()) + ".zip";
        String strZipPath = zipTempPath + "\\" + zipFileName;

        ZipOutputStream zipStream = null;
        FileInputStream zipSource = null;
        BufferedInputStream bufferStream = null;
        File zipFile = new File(strZipPath);
        try {
            //构造最终压缩包的输出流
            zipStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < fileNames.size(); i++) {
                //解码获取真实路径与文件名
                String fileName = fileNames.get(i);
                String filePath = filePaths.get(i);
                File file = new File(filePath);
                if (file.exists()) {
                    zipSource = new FileInputStream(file);//将需要压缩的文件格式化为输入流
                    /**
                     * 压缩条目不是具体独立的文件，而是压缩包文件列表中的列表项，称为条目，就像索引一样这里的name就是文件名,
                     * 文件名和之前的重复就会导致文件被覆盖
                     */
                    ZipEntry zipEntry = new ZipEntry(fileName);//在压缩目录中文件的名字
                    zipStream.putNextEntry(zipEntry);//定位该压缩条目位置，开始写入文件到压缩包中
                    bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                    int read = 0;
                    byte[] buf = new byte[1024 * 10];
                    while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                        zipStream.write(buf, 0, read);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            try {
                if (null != bufferStream) {
                    bufferStream.close();
                }
                if (null != zipStream) {
                    zipStream.flush();
                    zipStream.close();
                }
                if (null != zipSource) {
                    zipSource.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断系统压缩文件是否存在：true-把该压缩文件通过流输出给客户端后删除该压缩文件  false-未处理
        if (zipFile.exists()) {
            download(response, zipFileName, strZipPath);
            zipFile.delete();
        }
    }
}
