package com.yulrud.nasaapi;

import com.yulrud.nasaapi.APIRequestSteps.PhotoRequestSteps;
import com.yulrud.nasaapi.APISteps.PhotoSteps;
import com.yulrud.nasaapi.POJOs.Photo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class SolAndEarthDateRequestsSameResultTest extends BaseTest {
    PhotoSteps photoSteps;
    PhotoRequestSteps photoRequestSteps;
    private int solDate = 1000;
    private String earthDate = "2015-05-30";
    private int pageToCheckFrom = 1;
    private String pathToExpectedPhotos = "src/test/resources/ExpectedPhotoIsDownloadedTest/ExpectedPhotoIsDownloadedTest%d.jpg";

    @BeforeClass
    public void init() {
        super.init();
        photoSteps = new PhotoSteps();
        photoRequestSteps = new PhotoRequestSteps();
    }

    @Test(description = "Test that requests by Earth date and by Sol to Curiosity return the same set of data")
    public void solAndEarthDateRequestsSameResultTest() {
        List<Photo> photosBySol = photoRequestSteps.getOnePageOfCuriosityPhotosBySol(this.solDate,
                this.pageToCheckFrom);
        List<Photo> photosByEarthDate = photoRequestSteps.getOnePageOfCuriosityPhotosByEarthDate(this.earthDate,
                this.pageToCheckFrom);
        photoSteps.checkTwoPhotoListsHaveTheSameNFirstElements(photosBySol, photosByEarthDate, 10);
    }

    @Test(description = "Test that requests by Earth date and by Sol to Curiosity return the same set of data. Will fail due to different photos sets")
    public void solAndEarthDateRequestsSameResultTestToFail() {
        List<Photo> photosBySol = photoRequestSteps.getOnePageOfCuriosityPhotosBySol(this.solDate + 1,
                this.pageToCheckFrom);
        List<Photo> photosByEarthDate = photoRequestSteps.getOnePageOfCuriosityPhotosByEarthDate(this.earthDate,
                this.pageToCheckFrom);
        photoSteps.checkTwoPhotoListsHaveTheSameNFirstElements(photosBySol, photosByEarthDate, 10);
    }

    @Test(description = "Test that the expected photos are returned by request to Curiosity")
    public void comparePhotosTest() {
        List<Photo> downloadedPhotos = photoRequestSteps.getOnePageOfCuriosityPhotosBySol(this.solDate,
                this.pageToCheckFrom);
        photoSteps.checkDownloadedPhotosAreExpected(downloadedPhotos, this.pathToExpectedPhotos, 5);
    }

    @Test(description = "Test that the expected photos are returned by request to Curiosity. Will fail because it is not possible to read an image from a file")
    public void comparePhotosTestToFailCannotReadAFile() {
        List<Photo> downloadedPhotos = photoRequestSteps.getOnePageOfCuriosityPhotosBySol(this.solDate,
                this.pageToCheckFrom);
        String photoPath = "src/test/resources/00/ExpectedPhotoIsDownloadedTest/ExpectedPhotoIsDownloadedTest%d.jpg";
        photoSteps.checkDownloadedPhotosAreExpected(downloadedPhotos, photoPath, 5);
    }

    @Test(description = "Test that the expected photos are returned by request to Curiosity. Will fail because photos are not the same")
    public void comparePhotosTestToFailPhotosAreNotTheSame() {
        List<Photo> downloadedPhotos = photoRequestSteps.getOnePageOfCuriosityPhotosBySol(this.solDate,
                this.pageToCheckFrom + 1);
        photoSteps.checkDownloadedPhotosAreExpected(downloadedPhotos, this.pathToExpectedPhotos, 5);
    }
}
