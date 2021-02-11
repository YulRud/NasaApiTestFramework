package com.yulrud.nasaapi.APIRequestSteps;

import com.yulrud.nasaapi.Enums.Endpoints;
import com.yulrud.nasaapi.POJOs.Photo;
import io.qameta.allure.Step;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.yulrud.nasaapi.TestLogger.LOGGER;
import static io.restassured.RestAssured.given;

public class PhotoRequestSteps extends BaseRequestSteps {
    @Step
    public List<Photo> getOnePageOfCuriosityPhotosByEarthDate(String earth_date, int pageNumber) {
        return this.getCuriosityPhotos(null, earth_date, pageNumber);
    }

    @Step
    public List<Photo> getOnePageOfCuriosityPhotosBySol(int sol, int pageNumber) {
        return this.getCuriosityPhotos(sol, null, pageNumber);
    }

    @Step
    public List<Photo> getAllPagesOfCuriosityPhotosBySol(int sol) {
        return this.getCuriosityPhotos(sol, null, null);
    }

    private List<Photo> getCuriosityPhotos(Integer sol, String earth_date, Integer pageNumber) {
        String dateType;
        Object date;

        if (sol != null) {
            dateType = "sol";
            date = sol;
        } else {
            dateType = "earth_date";
            date = earth_date;
        }

        return given()
                .log().uri()
                .param(dateType, date)
                .param("api_key", key)
                .param("page", pageNumber)
                .when()
                .get(Endpoints.GET_CURIOSITY_PHOTOS.getUrl())
                .then()
                .log().status()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("photos", Photo.class);
    }

    @Step
    public BufferedImage getPhotoAsBufferedImage(String url) {
        BufferedImage image = null;
        InputStream inputStream = given()
                .log().uri()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .extract()
                .asInputStream();
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            LOGGER.error(String.format("Not able to download/read an image by url: %s", url));
        }
        LOGGER.info(String.format("The image downloaded from url: %s was read", url));
        return image;
    }
}
