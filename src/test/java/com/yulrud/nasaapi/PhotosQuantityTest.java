package com.yulrud.nasaapi;

import com.yulrud.nasaapi.APIRequestSteps.PhotoRequestSteps;
import com.yulrud.nasaapi.APISteps.PhotoSteps;
import com.yulrud.nasaapi.POJOs.Photo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class PhotosQuantityTest extends BaseTest {
    PhotoSteps photoSteps;
    PhotoRequestSteps photoRequestSteps;

    @BeforeClass
    public void init() {
        super.init();
        photoSteps = new PhotoSteps();
        photoRequestSteps = new PhotoRequestSteps();
    }

    @Test(description = "Test that there is no camera that does 10 times photos more that others. Test is suppose to fail")
    public void photosQuantityTestToFail() {
        List<Photo> photosBySol = photoRequestSteps.getAllPagesOfCuriosityPhotosBySol(1000);
        photoSteps.checkThereAreNoCameraNTimesMoreProductiveThanOthers(photosBySol, 10);
    }

    @Test(description = "Test that there is no camera that does 10 times photos more that others")
    public void photosQuantityTest() {
        List<Photo> photosBySol = photoRequestSteps.getAllPagesOfCuriosityPhotosBySol(2500);
        photoSteps.checkThereAreNoCameraNTimesMoreProductiveThanOthers(photosBySol, 10);
    }
}
