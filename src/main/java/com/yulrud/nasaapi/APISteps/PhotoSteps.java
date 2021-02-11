package com.yulrud.nasaapi.APISteps;

import com.yulrud.nasaapi.APIRequestSteps.PhotoRequestSteps;
import com.yulrud.nasaapi.POJOs.Photo;
import com.yulrud.nasaapi.Utils.ImageUtils;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yulrud.nasaapi.TestLogger.LOGGER;

public class PhotoSteps {
    private PhotoRequestSteps photoRequestSteps = new PhotoRequestSteps();

    @Step
    public double getRatioBetweenMostAndLessProductiveCameras(List<Photo> photos) {
        Map<String, Integer> photosByCamera = this.getPhotosQtyByCamera(photos);
        double maxPhotos = photosByCamera.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();
        double minPhotos = photosByCamera.entrySet().stream().min(Comparator.comparing(Map.Entry::getValue)).get().getValue();
        return maxPhotos / minPhotos;
    }

    @Step
    public void checkThereAreNoCameraNTimesMoreProductiveThanOthers(List<Photo> photos, int productivityRatio) {
        Assert.assertTrue(this.getRatioBetweenMostAndLessProductiveCameras(photos) < productivityRatio,
                "One camera is more productive then another");
    }

    private Map<String, Integer> getPhotosQtyByCamera(List<Photo> photos) {
        Map<String, Integer> photosByCamera = new HashMap<>();
        String cameraName;
        Integer cameraPhotos;

        for (Photo photo : photos) {
            cameraName = photo.getCamera().getName();
            cameraPhotos = photosByCamera.get(cameraName);
            photosByCamera.put(cameraName, cameraPhotos == null ? 1 : cameraPhotos + 1);
        }
        LOGGER.info(String.format("Quantity of photos that was made by each camera was calculated \n %s", photosByCamera));
        return photosByCamera;
    }

    @Step
    public void checkTwoPhotoListsHaveTheSameNFirstElements(List<Photo> photosList1, List<Photo> photosList2, int numberOfElementsToCompare) {
        for (int i = 0; i < numberOfElementsToCompare; i++) {
            Assert.assertEquals(photosList1.get(i), photosList2.get(i), String.format("the photos %d are not equal", i));
        }
    }

    @Step
    public BufferedImage openPhotoFromFile(String path) {
        return ImageUtils.openImageFromFile(path);
    }

    @Step
    public boolean isPhotosTheSame(String path, String url) {
        return ImageUtils.compareImagesByPixels(this.openPhotoFromFile(path), photoRequestSteps.getPhotoAsBufferedImage(url));
    }

    @Step
    public void checkDownloadedPhotosAreExpected(List<Photo> photos, String pathToPhotoFolder, int numberOfPhotoToCompare) {
        String expectedPhoto;
        String actualPhoto;

        for (int i = 0; i < numberOfPhotoToCompare; i++) {
            expectedPhoto = String.format(pathToPhotoFolder, i);
            actualPhoto = photos.get(i).getImg_src();
            Assert.assertTrue(this.isPhotosTheSame(expectedPhoto, actualPhoto),
                    String.format("Downloaded photo %d is not expected", i));
        }
    }
}
