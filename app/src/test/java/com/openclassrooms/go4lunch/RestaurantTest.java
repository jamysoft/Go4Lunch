package com.openclassrooms.go4lunch;
import static junit.framework.Assert.assertEquals;
import com.openclassrooms.go4lunch.models.Restaurant;
import org.junit.Before;
import org.junit.Test;

public class RestaurantTest {
    private Restaurant restaurantToTest;
    static String idRestaurantToTest="1237";
    static String nameRestaurantToTest="my restaurant";
    static String phoneRestaurantToTest="06 33 22 22 44";
    static Integer ratingRestaurantToTest=3;
    static String typeRestaurantToTest="MAR";
    static String urlPictureRestaurantToTest="urlRestaurant";
    static String webSiteRestaurantToTest="www.restaurant.fr";
    static String adressRestaurantToTest="adr";
    static String hourClosedRestaurantToTest="01h00";

    static String idRestaurantToTest2="4567";
    static String nameRestaurantToTest2="my restaurant2";
    static String phoneRestaurantToTest2="06 89 22 22 78";
    static Integer ratingRestaurantToTest2=4;
    static String typeRestaurantToTest2="FR";
    static String urlPictureRestaurantToTest2="urlRestaurant2";
    static String webSiteRestaurantToTest2="www.restaurant2.fr";
    static String adressRestaurantToTest2="adr2";
    static String hourClosedRestaurantToTest2="02h00";

    @Before
    public void setUp() {
        restaurantToTest = new Restaurant(idRestaurantToTest,nameRestaurantToTest,phoneRestaurantToTest,
                ratingRestaurantToTest,typeRestaurantToTest,urlPictureRestaurantToTest,webSiteRestaurantToTest,adressRestaurantToTest,
                hourClosedRestaurantToTest);
    }
    @Test
    public void getIdRestaurant() {
        assertEquals(idRestaurantToTest, restaurantToTest.getIdR());
    }
    @Test
    public void getNameRestaurant() {
        assertEquals(nameRestaurantToTest, restaurantToTest.getName());
    }
    @Test
    public void getPhoneRestaurant() {
        assertEquals(phoneRestaurantToTest, restaurantToTest.getPhone());
    }
    @Test
    public void getRatingRestaurant() {
        assertEquals(ratingRestaurantToTest, restaurantToTest.getRating());
    }
    @Test
    public void getAdressRestaurant() {
        assertEquals(adressRestaurantToTest, restaurantToTest.getAdresse());
    }
    @Test
    public void getTypeRestaurant() {
        assertEquals(typeRestaurantToTest, restaurantToTest.getType());
    }
    @Test
    public void getWebSiteRestaurant() {
        assertEquals(webSiteRestaurantToTest, restaurantToTest.getWebSite());
    }
    @Test
    public void getHourClosedRestaurant() {
        assertEquals(hourClosedRestaurantToTest, restaurantToTest.getHourClosed());
    }
    @Test
    public void getUrlPictureRestaurant() {
        assertEquals(urlPictureRestaurantToTest, restaurantToTest.getUrlPicture());
    }
    @Test
    public void setIdRestaurant() {
        restaurantToTest.setIdR(idRestaurantToTest2);
        assertEquals(idRestaurantToTest2, restaurantToTest.getIdR());
    }
    @Test
    public void setNameRestaurant() {
        restaurantToTest.setName(nameRestaurantToTest2);
        assertEquals(nameRestaurantToTest2, restaurantToTest.getName());
    }
    @Test
    public void setPhoneRestaurant() {
        restaurantToTest.setPhone(phoneRestaurantToTest2);
        assertEquals(phoneRestaurantToTest2, restaurantToTest.getPhone());
    }
    @Test
    public void setRatingRestaurant() {
        restaurantToTest.setRating(ratingRestaurantToTest2);
        assertEquals(ratingRestaurantToTest2, restaurantToTest.getRating());
    }
    @Test
    public void setAdressRestaurant() {
        restaurantToTest.setAdresse(adressRestaurantToTest2);
        assertEquals(adressRestaurantToTest2, restaurantToTest.getAdresse());
    }
    @Test
    public void setTypeRestaurant() {
        restaurantToTest.setType(typeRestaurantToTest2);
        assertEquals(typeRestaurantToTest2, restaurantToTest.getType());
    }
    @Test
    public void setWebSiteRestaurant() {
        restaurantToTest.setWebSite(webSiteRestaurantToTest2);
        assertEquals(webSiteRestaurantToTest2, restaurantToTest.getWebSite());
    }
    @Test
    public void setHourClosedRestaurant() {
        restaurantToTest.setHourClosed(hourClosedRestaurantToTest2);
        assertEquals(hourClosedRestaurantToTest2, restaurantToTest.getHourClosed());
    }
    @Test
    public void setUrlPictureRestaurant() {
        restaurantToTest.setUrlPicture(urlPictureRestaurantToTest2);
        assertEquals(urlPictureRestaurantToTest2, restaurantToTest.getUrlPicture());
    }


}
