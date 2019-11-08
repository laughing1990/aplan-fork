package com.augurit.aplanmis.rest.cod.service;

import lombok.extern.slf4j.Slf4j;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CodDrawingService {

    public String markDiffPoint(HttpServletRequest request, boolean isDenoise, int keySize, int maxCorners,
                                int minDistance) throws Exception {
        String pointsString = "";
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Mat matBase = getDrawingGray(request, "baseDrawing");
            Mat matOther = getDrawingGray(request, "otherDrawing");
            Mat baseAddedDrawing = null;
            Mat otherAddedDrawing = null;
            if (isDenoise) {
                //分别对两张图去噪
                baseAddedDrawing = getDenoiseDrawing(matBase, matOther, keySize);
            } else {
                //直接叠加相减
                baseAddedDrawing = getDiffDrawing(matBase, matOther);
                //Imgcodecs.imwrite("F:\\images\\baseDeleteDrawing.png",baseDeleteDrawing);
            }
            pointsString = getDrawingCorners(baseAddedDrawing, maxCorners, minDistance);

            String rectString = getContours(baseAddedDrawing);

        } catch (Exception e) {
            System.out.println("例外：" + e);
        }
        return pointsString;
    }

    public String compareDrawingContours(HttpServletRequest request, boolean isDenoise, int keySize) throws Exception {
        String rectString = "";
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Mat matBase = getDrawingGray(request, "baseDrawing");
            Mat matOther = getDrawingGray(request, "otherDrawing");

            Mat baseAddedDrawing = null;
            Mat otherAddedDrawing = null;
            if (isDenoise) {
                //分别对两张图去噪
                baseAddedDrawing = getDenoiseDrawing(matBase, matOther, keySize);
            } else {
                //直接叠加相减
                baseAddedDrawing = getDiffDrawing(matBase, matOther);
                //Imgcodecs.imwrite("F:\\images\\baseDeleteDrawing.png",baseDeleteDrawing);
            }
            rectString = getContours(baseAddedDrawing);

        } catch (Exception e) {
            System.out.println("例外：" + e);
        }
        return rectString;
    }


    //获取图纸灰度图
    private Mat getDrawingGray(HttpServletRequest request, String fileName) throws Exception {
        StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) request;
        if (multipartRequest == null) return null;

        MultipartFile baseDrawing = multipartRequest.getFile(fileName);

        if (baseDrawing == null) return null;

        byte[] baseFileByte = baseDrawing.getBytes();

        Mat matBase = Imgcodecs.imdecode(new MatOfByte(baseFileByte), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);

        if (matBase == null) return null;

        Imgproc.cvtColor(matBase, matBase, Imgproc.COLOR_RGB2GRAY);

        Mat dst = new Mat(matBase.rows(), matBase.cols(), matBase.type());

        Imgproc.threshold(matBase, matBase, 254, 255, Imgproc.THRESH_BINARY_INV);
        return matBase;
    }

    //获取去噪后比对的图纸
    private Mat getDenoiseDrawing(Mat matBase, Mat matOther, int keySize) {
        Mat dilateMat = new Mat(matBase.rows(), matBase.cols(), matBase.type());
        Mat dst = new Mat(matBase.rows(), matBase.cols(), matBase.type());
        //膨胀base图
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.dilate(matBase, dilateMat, element, new Point(-1, -1), 1);

        //获取包含噪音的整体图
        Mat rustOther = new Mat();
        Core.bitwise_and(dilateMat, matOther, rustOther);
        //获取新图纸增加的内容，减去噪音。
        Core.subtract(matOther, rustOther, dst);
        return dst;
    }

    //图纸直接相减
    private Mat getDiffDrawing(Mat matBase, Mat matOther) {
        Mat dst = new Mat(matBase.rows(), matBase.cols(), matBase.type());
        Core.add(matBase, matOther, dst);
        Core.subtract(dst, matBase, dst);
        return dst;
    }

    //获取标记角点
    private String getDrawingCorners(Mat matDrawing, int maxCorners, int minDistance) {
        if (matDrawing == null || matDrawing.cols() < 1 || matDrawing.rows() < 1) return "";
        String pointsString = "";
        //获取标记点
        MatOfPoint corners = new MatOfPoint();
        final int blockSize = 10;
        final double qualityLevel = 0.01, k = 0.04;
        final boolean useHarrisDetector = false;

        Imgproc.goodFeaturesToTrack(matDrawing, corners, maxCorners, qualityLevel, minDistance,
                new Mat(), blockSize, useHarrisDetector, k);
        Point[] pCorners = corners.toArray();
        if (pCorners == null) return pointsString;
        pointsString = "[";
        for (int i = 0; i < pCorners.length; i++) {
            pointsString += "{'x':" + pCorners[i].x + ",'y':" + pCorners[i].y + "},";
        }

        if (pointsString.length() > 1) {
            pointsString = pointsString.substring(0, pointsString.length() - 1);
            pointsString += "]";
        }

        return pointsString;
    }

    //轮廓检测
    private String getContours(Mat matDrawing) {
        String rectString = "";
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(20, 20));
        Imgproc.dilate(matDrawing, matDrawing, element, new Point(-1, -1), 1);
        List<MatOfPoint> contours = new ArrayList<>();

        Mat hierarchy = new Mat();
        //Imgproc.findContours(matDrawing, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(matDrawing, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        System.out.println("轮廓数量：" + contours.size());
        System.out.println("hierarchy类型：" + hierarchy);
        MatOfPoint2f mat2fsrc = new MatOfPoint2f(), mat2fdst = new MatOfPoint2f();
        Scalar color = new Scalar(0, 250, 255);
        rectString = "[";
        for (int i = 0; i < contours.size(); i++) {
/*            contours.get(i).convertTo(mat2fsrc, CvType.CV_32FC2);
            Imgproc.approxPolyDP(mat2fsrc, mat2fdst, 0.01 * Imgproc.arcLength(mat2fsrc, true), true);
            mat2fdst.convertTo(contours.get(i), CvType.CV_32S);
            Point[] pointsList = contours.get(i).toArray();*/
            //轮廓边线
            MatOfPoint points = new MatOfPoint(contours.get(i).toArray());
            Rect rect = Imgproc.boundingRect(points);
            rectString += "{x:" + rect.x + ",y:" + rect.y + ",w:" + rect.width + ",h:" + rect.height + "},";
            //Imgproc.drawContours(matDrawing, contours, i, color, 2, 8, hierarchy, 0, new Point());
        }

        if (rectString.length() > 1) {
            rectString = rectString.substring(0, rectString.length() - 1);
            rectString += "]";
        }

        return rectString;
    }

}

