package com.jzue.controller;

import com.google.common.io.ByteStreams;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: junzexue
 * @Date: 2019/4/22 下午4:45
 * @Description:
 **/
public class ImageFileUtil {

    /**
     * 上传主题图文封面
     * @param request
     * @param response
     * @param delayTime
     * @param path
     * @param url
     * @param width
     * @param height
     * @param smallWidth
     * @param smallHeight
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws FileUploadException
     */
    public static Map<String, Object> compressUploadTwCover(HttpServletRequest request, HttpServletResponse response,
                                                            int delayTime,
                                                            String path, String url, Integer width, Integer height,
                                                            Integer smallWidth, Integer smallHeight) throws ServletException, IOException, FileUploadException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        response.setContentType("text/html; charset=UTF-8");
        /**  1. 检查文件是否存在 */
        if (!ServletFileUpload.isMultipartContent(request)) {
            return getStatusMsg("请选择文件。");
        }

        /** 2. 生成目录 */
        String ymd = sdf.format(new Date());
        path += "image/" + ymd + "/";
        url += "image/" + ymd + "/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        /**  3. 检查文件是否存在 */
        if (!dirFile.canWrite()) {
            return getStatusMsg("上传目录没有写权限。");
        }

        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
        upload.setHeaderEncoding("UTF-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;

        Iterator item = multipartRequest.getFileNames();
        while (item.hasNext()) {
            String fileName = (String) item.next();
            MultipartFile file = multipartRequest.getFile(fileName);
            long fileSize = file.getSize();
            /**  4. 检查文件是否存在 */
            if (fileSize > CmsConstant.FILE_MAX_SIZE) {
                return getStatusMsg("上传文件大小超过限制。");
            }
            /**  5. 检查文件是否存在 */
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            if (!Arrays.asList(CmsConstant.FILE_IMG_EXT.split(",")).contains(fileExt)) {
                return getStatusMsg("上传文件扩展名是不允许的扩展名。\n只允许" + CmsConstant.FILE_IMG_EXT + "格式。");
            }

            String initFileName = generateInitFileName();
            String srcFileName = initFileName +CmsConstant.IMG_SPECI_SRC+ "." + fileExt;
            String thumbFileName = initFileName + CmsConstant.IMG_SPECI_THUMB + "." + fileExt;
            try {
                /**  6. 上传原图 */
                BufferedImage image = ImageIO.read(file.getInputStream());
                int srcWidth = image.getWidth();      // 源图宽度
                int srcHeight = image.getHeight();    // 源图高度
                if (width!=null && height!=null) {
                    if (width != srcWidth || height < srcHeight) {
                        return getStatusMsg("上传的图片尺寸错误！");
                    }
                }
                File uploadedFile = new File(path, srcFileName);
                ByteStreams.copy(file.getInputStream(), new FileOutputStream(uploadedFile));

                /**  7. 根据原图生成缩略图 */
                generateCmsImageToThumb(path,srcFileName,thumbFileName,smallWidth,smallHeight);
                Thread.sleep(delayTime);
                Map<String, Object> msg = new HashMap<String, Object>();
                msg.put("error", 0);
                msg.put("message","success");
                msg.put("filePath", path);
                msg.put("fileName", srcFileName);
                msg.put("thumbUrl", url + thumbFileName);
                msg.put("url", url + srcFileName);
                msg.put("size", fileSize);
                msg.put("width", srcWidth);
                msg.put("height", srcHeight);
                return msg;
            } catch (Exception e) {
                return getStatusMsg("上传文件失败。");
            }
        }
        return getStatusMsg("没有找到上传的文件。");
    }

    /**
     * 生成初始文件名
     * @return
     */
    private static String generateInitFileName() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder fileNameStr = new StringBuilder(df.format(new Date()));
        fileNameStr.append("-").append(getFixLenthString(6)).append("-");
        return fileNameStr.toString();
    }

    /**
     * 返回固定长度的随机数
     * @param strLength
     * @return
     */
    private static String getFixLenthString(int strLength) {

        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }
    private static void generateCmsImageToThumb(String path, String srcFileName, String specFileName, int width, int height) throws IOException {
        try {
            Thumbnails.of(path + srcFileName).size(width, height).outputQuality(1.0).toFile(path + specFileName);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 获得返回消息
     * @param message
     * @return
     */
    private static Map<String, Object> getStatusMsg(String message) {
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("error", 1);
        msg.put("message", message);
        return msg;
    }
}
